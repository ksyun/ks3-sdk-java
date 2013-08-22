/**
 * @author: yangji
 * @data:   Apr 1, 2013
 */
package com.ksyun.ks3.sdk.dto.internal;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.ksyun.ks3.sdk.dto.Credential;

public class Request {
	
	private String method;
	private String url;
	private InputStream body;
	private Map<String,String> headers;	
	private Map<String,String> params;
	private Credential credential;
	private String bucekt;
	private String object;

	public Request(){
		headers = new HashMap<String,String>();
	}	
		
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
		this.headers=headers;
	}
	public void addHeader(String key,String value){
		headers.put(key, value);
	}
	public Credential getCredential() {
		return credential;
	}
	public void setCredential(Credential credential) {
		this.credential = credential;
	}	
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public String getBucekt() {
		return bucekt;
	}
	public void setBucekt(String bucekt) {
		this.bucekt = bucekt;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}	
}
