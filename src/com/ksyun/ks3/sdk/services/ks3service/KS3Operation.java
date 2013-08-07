/**
 * @author: yangji
 * @data:   Apr 8, 2013
 */
package com.ksyun.ks3.sdk.services.ks3service;

import com.ksyun.ks3.sdk.dto.Credential;
import com.ksyun.ks3.sdk.dto.internal.Request;
import com.ksyun.ks3.sdk.dto.internal.Response;
import com.ksyun.ks3.sdk.services.httpservice.HttpRequestor;
import com.ksyun.ks3.sdk.services.ks3service.RequestBuilder;
import com.ksyun.ks3.sdk.services.ks3service.ResultParse;
import com.ksyun.ks3.sdk.tools.CodeUtils;

public class KS3Operation {
	
	private HttpRequestor requestor;		
	protected RequestBuilder requestBuilder;
	protected ResultParse resultParse;
	
	protected KS3Operation(HttpRequestor requestor,Credential credential) throws Exception{
		this.requestor = requestor;			
		this.requestBuilder = new RequestBuilder(credential);
		this.resultParse = new ResultParse();
	}	
	protected Response sendMessage(Request request) throws Exception{
		Response response = this.requestor.sendRequest(request);
		CodeUtils.checkRespsonse(response);
		return response;
	}

}
