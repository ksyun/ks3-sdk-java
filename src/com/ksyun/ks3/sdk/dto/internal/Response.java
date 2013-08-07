/**
 * @author: yangji
 * @data:   Apr 1, 2013
 */
package com.ksyun.ks3.sdk.dto.internal;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Response {
	
	private Integer statusCode;
	private InputStream body;
	private Map<String,String> headers;
	
	public Response(){
		headers = new HashMap<String,String>();
	}
	
	public void addHeader(String key,String value){
		headers.put(key, value);
	}
		
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	
	public InputStream getBody() {
		return body;
	}

	public void setBody(InputStream body) {
		this.body = body;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	

}
