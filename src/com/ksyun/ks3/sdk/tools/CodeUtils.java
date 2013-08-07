/**
 * @author: yangji
 * @data:   Apr 8, 2013
 */
package com.ksyun.ks3.sdk.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.ksyun.ks3.sdk.dto.internal.Response;
import com.ksyun.ks3.sdk.exceptions.ResponseException;

public class CodeUtils {
	
	public static void checkRespsonse(Response response) throws Exception{
		if(response.getStatusCode()/100<3)
			return;
		
		InputStream is = response.getBody();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
	    StringBuffer buffer = new StringBuffer();
	    String line = "";
	    try {
			while ((line = in.readLine()) != null){
			  buffer.append(line);
			}
		} catch (Exception e) {
			throw new Exception("Parse request error failed.");
		}
	    throw new ResponseException(response.getStatusCode(),buffer.toString());
		
	}
	
	public static void checkStringParams(String paramKey,String paramValue) throws Exception{
		if(paramValue==null||paramValue.trim().equals(""))
			throw new IllegalArgumentException("null parameter:"+paramKey);		
	}
	
	public static void checkMapParams(String paramKey,Map paramValue) throws Exception{
		if(paramValue==null||paramKey.length()==0)
			throw new IllegalArgumentException("null parameter:"+paramKey);		
	}
	
	public static void checkObjectParams(String paramKey,Object paramValue) throws Exception{
		if(paramValue==null)
			throw new IllegalArgumentException("null parameter:"+paramKey);		
	}
	
}
