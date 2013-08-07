/**
 * @author: yangji
 * @data:   Jul 4, 2013
 */
package com.ksyun.ks3.sdk.exceptions;

public class ResponseException extends Exception {
	
	private Integer statusCode;	
	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public ResponseException(Integer statusCode ,String message) {		
		super(message);
		this.statusCode = statusCode;
	}


}
