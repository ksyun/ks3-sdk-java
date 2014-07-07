/**
 * @author: yangji
 * @data:   Aug 19, 2013
 */
package com.ksyun.ks3.sdk.services.ks3service;

import com.ksyun.ks3.sdk.dto.Credential;

public class RequestFactory {
	
	private Credential credential;
	public RequestFactory(Credential credential){
		this.credential = credential;
	}
	
	public RequestBuilder getBuilder() throws Exception{
		return new RequestBuilder(credential);		
	}

}
