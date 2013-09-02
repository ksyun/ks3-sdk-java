/**
 * @author: yangji
 * @data:   Apr 3, 2013
 */
package com.ksyun.ks3.sdk.services.ks3service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


import com.ksyun.ks3.sdk.dto.Credential;
import com.ksyun.ks3.sdk.dto.internal.Request;
import com.ksyun.ks3.sdk.services.ks3service.RequestBuilder;
import com.ksyun.ks3.sdk.tools.CodeUtils;


public class RequestBuilder {
	
	
	private String host;
	private String method;
	private String bucket;
	private String objectKey;
	private InputStream objectValue;
	private Map<String, String> headers = new HashMap<String, String>();
	private Map<String, String> params = new HashMap<String, String>();
	private Credential credential;
	private final String DEFAULT_HOST = "kss.ksyun.com";
	private final String IMG_HOST = "img.ks3.ksyun.com";

	public RequestBuilder(Credential credential) throws Exception {
		CodeUtils.checkObjectParams("credential", credential);
		this.host = DEFAULT_HOST;
		this.credential = credential;
	}

	public RequestBuilder setBucket(String bucketName) throws Exception {
		CodeUtils.checkStringParams("bucketName", bucketName);
		this.bucket = bucketName;
		return this;
	}

	public RequestBuilder setObjectKey(String objectKey) throws Exception {
		CodeUtils.checkStringParams("objectKey", objectKey);
		this.objectKey = objectKey;
		return this;
	}

	public RequestBuilder setObjectValue(InputStream objectValue) throws Exception {
		CodeUtils.checkObjectParams("objectValue", objectValue);
		this.objectValue = objectValue;
		return this;
	}

	public RequestBuilder setMethod(String method) throws Exception {
		CodeUtils.checkStringParams("method", method);
		this.method = method.toUpperCase();
		return this;
	}

	public RequestBuilder addHeader(String headerKey, String headerValue)
			throws Exception {
		CodeUtils.checkStringParams("headerKey", headerKey);
		CodeUtils.checkStringParams("headerValue", headerValue);
		this.headers.put(headerKey, headerValue);
		return this;
	}

	public RequestBuilder addParam(String paramKey, String paramValue)
			throws Exception {
		CodeUtils.checkObjectParams("paramKey", paramKey);
		CodeUtils.checkObjectParams("paramValue", paramValue);
		params.put(paramKey, paramValue);
		return this;
	}
	
	public RequestBuilder addPamams(Map<String,String> params) throws Exception{		
		CodeUtils.checkObjectParams("params", params);
		this.params.putAll(params);
		return this;		
	}
	
	public RequestBuilder addHeaders(Map<String,String> headers) throws Exception{
		CodeUtils.checkObjectParams("headers", headers);
		this.headers.putAll(headers);
		return this;
	}
	
	public RequestBuilder setThumbnailHost() throws Exception{
		this.host = this.IMG_HOST;
		return this;
	}
	
	private void clear(){
		this.method=null;
		this.bucket=null;
		this.objectKey=null;
		this.objectValue=null;		
		this.headers=new HashMap<String, String>();
		this.params=new HashMap<String, String>();
	}
	

	public Request build() {

		Request request = new Request();
		request.setCredential(this.credential);
		String url = this.host;

		// when bucket is not null
		if (bucket != null) {
			request.setBucekt(bucket);

			// when objectKey is not null
			if (objectKey != null) {
				request.setObject(objectKey);

				// when objectValue is not null
				if (objectValue != null)
					request.setBody(objectValue);
				
			}
		}

		request.setUrl(url);
		request.setMethod(method);
		request.setHeaders(headers);
		request.setParams(params);
		
		clear();

		return request;
	}	
}
