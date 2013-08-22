/**
 * @author: yangji
 * @data:   Apr 8, 2013
 */
package com.ksyun.ks3.sdk.services.ks3service;


import java.util.HashMap;
import java.util.Map;

import com.ksyun.ks3.sdk.dto.AccessControlList;
import com.ksyun.ks3.sdk.dto.AccessControlPolicy;
import com.ksyun.ks3.sdk.dto.Credential;
import com.ksyun.ks3.sdk.dto.ObjectList;
import com.ksyun.ks3.sdk.dto.ObjectListOptions;
import com.ksyun.ks3.sdk.dto.internal.HttpMethod;
import com.ksyun.ks3.sdk.dto.internal.Request;
import com.ksyun.ks3.sdk.dto.internal.Response;
import com.ksyun.ks3.sdk.services.httpservice.HttpRequestor;
import com.ksyun.ks3.sdk.services.ks3service.BucketOperation;
import com.ksyun.ks3.sdk.services.ks3service.KS3Operation;


public class BucketOperation extends KS3Operation {

	public BucketOperation(HttpRequestor requestor, Credential credential) throws Exception {
		super(requestor, credential);
	}

	public void createBucket(String bucketName) throws Exception {
		RequestBuilder requestBuilder = requestFactory.getBuilder();	
		Request request = requestBuilder.setMethod(HttpMethod.PUT).setBucket(bucketName).build();
		sendMessage(request);
	}

	public void deleteBucket(String bucketName) throws Exception {
		RequestBuilder requestBuilder = requestFactory.getBuilder();	
		Request request = requestBuilder.setMethod(HttpMethod.DELETE).setBucket(bucketName).build();
		sendMessage(request);
	}

	public AccessControlPolicy getBucketACL(String bucketName) throws Exception {

		RequestBuilder requestBuilder = requestFactory.getBuilder();
		Map<String,String> params = new HashMap<String,String>();
		params.put("acl", null);
		Request request = requestBuilder.setMethod(HttpMethod.GET).setBucket(bucketName).addPamams(params).build();
		Response response = sendMessageAndKeepAlive(request);	
		
		return resultParse.getAccessControlPolicy(response.getBody());
	}

	public void setBucketACL(String bucketName, AccessControlList acl) throws Exception {
		RequestBuilder requestBuilder = requestFactory.getBuilder();
		Map<String,String> params = new HashMap<String,String>();
		params.put("acl", null);
		Request request = requestBuilder.setMethod(HttpMethod.PUT).setBucket(bucketName).addHeader("x-kss-acl",acl.toString()).addPamams(params).build();		
		sendMessage(request);
	}
	
	public ObjectList getObjectList(String bucketName) throws Exception{
		RequestBuilder requestBuilder = requestFactory.getBuilder();	
		Request request = requestBuilder.setMethod(HttpMethod.GET).setBucket(bucketName).build();
		Response response = sendMessageAndKeepAlive(request);	
		
		return resultParse.getObjectList(response.getBody());
	}
	
	public ObjectList getObjectListByOptions(ObjectListOptions options) throws Exception{
		
		String bucketName = options.getBucketName();
		String prefix = options.getPrefix();
		String marker = options.getMarker();
		Integer maxKeys = options.getMaxKeys();
		char delimiter = options.getDelimiter();
		
		RequestBuilder requestBuilder = requestFactory.getBuilder();	
		
		requestBuilder = requestBuilder.setMethod(HttpMethod.GET).setBucket(bucketName);
		requestBuilder = prefix == null ? requestBuilder:requestBuilder.addParam("prefix", prefix);
		requestBuilder = marker == null ? requestBuilder:requestBuilder.addParam("marker", marker);
		requestBuilder = maxKeys == null ? requestBuilder:requestBuilder.addParam("max-keys", String.valueOf(maxKeys));
		requestBuilder = delimiter == 0 ? requestBuilder:requestBuilder.addParam("delimiter", String.valueOf(delimiter));
		
		Request request = requestBuilder.build();
		Response response = sendMessageAndKeepAlive(request);
		
		return resultParse.getObjectList(response.getBody());
	}
}
