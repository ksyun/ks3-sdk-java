/**
 * @author: yangji
 * @data:   Apr 2, 2013
 */
package com.ksyun.ks3.sdk.services.authservice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


import com.ksyun.ks3.sdk.dto.internal.Request;
import com.ksyun.ks3.sdk.tools.Base64;
import com.ksyun.ks3.sdk.tools.EncodingUtils;

public class Signature {
	
	private static Set<String> responseHeaders = new HashSet<String>();
	
	static {		
		responseHeaders.add("response-content-type");
		responseHeaders.add("response-content-language");
		responseHeaders.add("response-expires");
		responseHeaders.add("response-cache-control");
		responseHeaders.add("response-content-disposition");
		responseHeaders.add("response-content-encoding");	
	}
	
	private static String base64encoding(byte[] b){		
		return Base64.encodeBytes(b);
	}
		
	private static String sign(String key,String strToSign){		
		
		try {
            
            byte[] keyBytes = key.getBytes();           
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
            
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            
            byte[] rawHmac = mac.doFinal(strToSign.getBytes());
            
            String hexBytes = base64encoding(rawHmac);
            return hexBytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
	
	public  static String getSignatureByRequest(Request request){
		
		String bucket = request.getBucekt();
		String object = request.getObject();
		
		
		if(object!=null)
			object = EncodingUtils.getUrlEncode(object);
		if(bucket!=null)
			bucket = EncodingUtils.getUrlEncode(bucket);
		
		
		List<String> list=new ArrayList<String>();
		list.add(request.getMethod());
		
		Map<String,String> headers = request.getHeaders();
		Map<String,String> interestedHeaders = new TreeMap<String,String>();
		for(Entry<String,String> entry:headers.entrySet()){
			String lowerKey=entry.getKey().toLowerCase();
			if (lowerKey.equals("content-type") || lowerKey.equals("content-md5") || lowerKey.equals("date") || lowerKey.startsWith("x-kss-")){
				interestedHeaders.put(lowerKey, entry.getValue());
			}
		}
		
		if(!interestedHeaders.keySet().contains("content-type"))
			interestedHeaders.put("content-type", "");
		
		if(!interestedHeaders.keySet().contains("content-md5"))
			interestedHeaders.put("content-md5", "");
		
		if(!interestedHeaders.keySet().contains("date"))
			interestedHeaders.put("date", "");
		
		for(Entry<String, String> entry :interestedHeaders.entrySet()){
			if(entry.getKey().startsWith("x-kss-"))
				list.add(entry.getKey()+":"+entry.getValue());
			else
				list.add(entry.getValue());
		}

		String resUrl = "/";		
		if(bucket!=null && bucket.trim().length() != 0){
			
			resUrl=resUrl+bucket+"/";

			if(object!=null && bucket.trim().length() != 0)
				resUrl=resUrl+object;
		}	
		
		if(request.getUrl().endsWith("?acl"))
			resUrl+="?acl";
		
		Map<String, String> params = request.getParams();
		Map<String, String> interestedParams = new TreeMap<String, String>();
		if(params!=null&&params.size()>0){
			for(Entry<String,String> entry:params.entrySet()){
				String lowerKey=entry.getKey().toLowerCase();
				if(responseHeaders.contains(lowerKey))
					interestedParams.put(lowerKey, entry.getValue());	
			}
			
			String paramsStr = "";
			for(Entry<String, String> entry :interestedParams.entrySet()){
				paramsStr+=entry.getKey()+"="+entry.getValue()+"&";
			}
			
			if(paramsStr.length()>0){
				paramsStr=paramsStr.substring(0, paramsStr.length()-1);
				resUrl+="?"+paramsStr;				
			}
			
			
		}
		
		list.add(resUrl);
		
		String strToSign="";
		for(String item:list){
			strToSign+=item+"\n";
		}
		strToSign=strToSign.substring(0, strToSign.length()-1);
			
		String signedStr = sign(request.getCredential().getAccessKeySecret(),strToSign);
		return signedStr;
	}
	
	public static Request signRequest(Request request){
		
		String signedStr = getSignatureByRequest(request);
		request.addHeader("authorization", "KSS "+request.getCredential().getAccessKeyId()+":"+signedStr);
		
		return request;
	}
}
