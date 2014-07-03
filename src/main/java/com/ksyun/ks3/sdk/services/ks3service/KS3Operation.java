/**
 * @author: yangji
 * @data:   Apr 8, 2013
 */
package com.ksyun.ks3.sdk.services.ks3service;

import com.ksyun.ks3.sdk.dto.Credential;
import com.ksyun.ks3.sdk.dto.internal.Request;
import com.ksyun.ks3.sdk.dto.internal.Response;
import com.ksyun.ks3.sdk.services.httpservice.HttpRequestor;
import com.ksyun.ks3.sdk.services.ks3service.ResultParse;
import com.ksyun.ks3.sdk.tools.CodeUtils;

public class KS3Operation {
	
	private HttpRequestor requestor;		
	protected ResultParse resultParse;
	protected RequestFactory requestFactory;
	
	protected KS3Operation(HttpRequestor requestor,Credential credential) throws Exception{
		this.requestor = requestor;			
		this.requestFactory = new RequestFactory(credential);
		this.resultParse = new ResultParse();
	}	
	protected Response sendMessage(Request request) throws Exception{
		Response response = this.requestor.sendRequest(request);
		CodeUtils.checkRespsonse(response,false);
		return response;
	}
	
	protected Response sendMessageAndKeepAlive(Request request) throws Exception{
		Response response = this.requestor.sendRequest(request);
		CodeUtils.checkRespsonse(response,true);
		
		return response;
	}

}
