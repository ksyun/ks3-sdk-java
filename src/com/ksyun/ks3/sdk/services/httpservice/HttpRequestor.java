/**
 * @author: yangji
 * @data:   Apr 3, 2013
 */
package com.ksyun.ks3.sdk.services.httpservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import com.ksyun.ks3.sdk.dto.internal.Request;
import com.ksyun.ks3.sdk.dto.internal.Response;
import com.ksyun.ks3.sdk.services.httpservice.HttpFactory;

public class HttpRequestor {	
	private HttpClient httpClient;
	private HttpFactory httpFactory = new HttpFactory();
	
	public HttpRequestor(){
		httpClient = httpFactory.getHttpClient();
		
	}
	
	public Response sendRequest(Request request) throws Exception{
		
		HttpRequestBase httpRequest = httpFactory.generateHttpRequest(request);
		HttpResponse httpResponse = httpClient.execute(httpRequest);	
		Response response = httpFactory.generateFromHttpResponse(httpResponse);		

		return response;		
	}
}
