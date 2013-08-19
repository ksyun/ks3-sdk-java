/**
 * @author: yangji
 * @data:   Apr 8, 2013
 */
package com.ksyun.ks3.sdk.services.ks3service;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import com.ksyun.ks3.sdk.dto.Credential;
import com.ksyun.ks3.sdk.dto.ObjectEntity;
import com.ksyun.ks3.sdk.dto.ObjectEtag;
import com.ksyun.ks3.sdk.dto.ObjectGetOptions;
import com.ksyun.ks3.sdk.dto.PresignedUrlOptions;
import com.ksyun.ks3.sdk.dto.internal.HttpMethod;
import com.ksyun.ks3.sdk.dto.internal.Request;
import com.ksyun.ks3.sdk.dto.internal.Response;
import com.ksyun.ks3.sdk.services.authservice.Signature;
import com.ksyun.ks3.sdk.services.httpservice.HttpRequestor;
import com.ksyun.ks3.sdk.services.ks3service.KS3Operation;
import com.ksyun.ks3.sdk.services.ks3service.ObjectOperation;
import com.ksyun.ks3.sdk.tools.EncodingUtils;

public class ObjectOperation extends KS3Operation {

	public ObjectOperation(HttpRequestor requestor, Credential credential)
			throws Exception {
		super(requestor, credential);
	}

	public ObjectEtag putObject(String bucketName, String objectKey, File file, String mimeType)
			throws Exception {
		
		if(mimeType==null||mimeType.trim().equals(""))
			mimeType = "application/octet-stream";		

		RequestBuilder requestBuilder = requestFactory.getBuilder();	
		Request request = requestBuilder.setMethod(HttpMethod.PUT)
				.setBucket(bucketName).setObjectKey(objectKey).setObjectValue(new FileInputStream(file))
				.addHeader("content-type", mimeType)
				.addHeader("content-length", String.valueOf(file.length()))
				.build();
		
		Response response = sendMessage(request);
		
		String eTag = response.getHeaders().get("ETag");
		return new ObjectEtag(eTag);
	}
	
	public ObjectEtag putStringObject(String bucketName, String objectKey,String content) throws Exception{	
		
		RequestBuilder requestBuilder = requestFactory.getBuilder();		
		Request request = requestBuilder.setMethod(HttpMethod.PUT)
				.setBucket(bucketName).setObjectKey(objectKey).setObjectValue(new ByteArrayInputStream(content.getBytes()))
				.addHeader("content-type", "text/plain; charset=UTF-8")
				.addHeader("content-length", String.valueOf(content.length()))
				.build();
		
		Response response = sendMessage(request);
		
		String eTag = response.getHeaders().get("ETag");		
		return new ObjectEtag(eTag);
		
	}
	
	public ObjectEntity getObject(String bucketName, String objectKey) throws Exception{
		
		RequestBuilder requestBuilder = requestFactory.getBuilder();	
		Request request = requestBuilder.setMethod(HttpMethod.GET).setBucket(bucketName).setObjectKey(objectKey).build();
		Response response = sendMessageAndKeepAlive(request);
		
		return new ObjectEntity(bucketName, objectKey, response.getBody());
	}
	
	public ObjectEntity getObejctByOptions(ObjectGetOptions options) throws Exception{
		
		String bucketName = options.getBucketName();
		String objectKey = options.getObjectKey();
		
		RequestBuilder requestBuilder = requestFactory.getBuilder();	
		requestBuilder.setMethod("GET").setBucket(bucketName).setObjectKey(objectKey);
		if(options.getRange()!=null){
			requestBuilder = requestBuilder.addHeader("Range", "bytes="+options.getRange()[0]+"-"+options.getRange()[1]);
		}
		if(options.getMatchingETagConstraints()!=null){
			requestBuilder = requestBuilder.addHeader("If-Match", options.getMatchingETagConstraints());
		}
		if(options.getNonmatchingEtagConstraints()!=null){
			requestBuilder = requestBuilder.addHeader("If-None-Match", options.getNonmatchingEtagConstraints());
		}
		if(options.getModifiedSinceConstraint()!=null){
			requestBuilder = requestBuilder.addHeader("If-Modified-Since", options.getModifiedSinceString());
		}
		if(options.getUnmodifiedSinceConstraint()!=null){
			requestBuilder = requestBuilder.addHeader("If-Unmodified-Since", options.getUnmodifiedSinceString());
		}		
		if(options.getParams()!=null){
			for(Entry<String,String> entry:options.getParams().entrySet())
				requestBuilder = requestBuilder.addParam(entry.getKey(), entry.getValue());
		}
		
		Request request = requestBuilder.build();
		Response response = sendMessageAndKeepAlive(request);
		
		return new ObjectEntity(bucketName, objectKey, response.getBody());
	}
	
	public void deleteObject(String bucketName, String objectKey) throws Exception{
		
		RequestBuilder requestBuilder = requestFactory.getBuilder();	
		Request request = requestBuilder.setMethod(HttpMethod.DELETE).setBucket(bucketName).setObjectKey(objectKey).build();
		sendMessage(request);		
	}
	
	public String getPresignedUrl(PresignedUrlOptions options) throws Exception{
		
		String bucketName = options.getBucketName();
		String objectKey = options.getObjectKey();
		Integer expires = options.getExpires();
		HashMap<String, String> paramsHeaders = options.getResponseHeaders();
		
		String expTime = String.valueOf(new Date().getTime()/1000+expires);
		RequestBuilder requestBuilder = requestFactory.getBuilder();	
		requestBuilder = requestBuilder.setMethod("GET").setBucket(bucketName).setObjectKey(objectKey).addHeader("Date",expTime);
		
		for(Entry<String, String> entry:paramsHeaders.entrySet())
			requestBuilder = requestBuilder.addParam(entry.getKey(), entry.getValue());
		
		Request request = requestBuilder.build();
		
		String signature = Signature.getSignatureByRequest(request);
		signature = URLEncoder.encode(signature,"UTF-8");
		String accessKeyId = request.getCredential().getAccessKeyId();
		
		objectKey = EncodingUtils.getUrlEncode(objectKey);
		
		String url = "http://kss.ksyun.com/"+bucketName+"/"+objectKey+"?Expires="+expTime;
		url = url + "&AccessKeyId="+accessKeyId+"&Signature="+signature;
		if(paramsHeaders==null||paramsHeaders.size()<1)
			return url;
		
		for(Entry<String,String> entry:paramsHeaders.entrySet()){
			url=url+"&"+URLEncoder.encode(entry.getKey(),"UTF-8")+"="+URLEncoder.encode(entry.getValue(),"UTF-8");
		}
			
		return url;
	}
	
}
