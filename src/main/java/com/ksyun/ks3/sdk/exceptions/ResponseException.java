/**
 * @author: yangji
 * @data:   Jul 4, 2013
 */
package com.ksyun.ks3.sdk.exceptions;

/**
 * The ResponseException instance contains the statusCode and the ErrorCode of KS3.
 */
public class ResponseException extends Exception {	
	
	private static final long serialVersionUID = 6686994262255914114L;
	private Integer statusCode;	
	
	/**
	 * Construct a ResponseException.
	 * @param statusCode The status Code of HTTP Request.
	 * @param message The the ErrorCode of KS3.
	 */
	public ResponseException(Integer statusCode ,String message) {		
		super(message);
		this.statusCode = statusCode;
	}

	/**
	 * Set the status Code of HTTP Request. 
	 * @param statusCode The status Code of HTTP Request. 
	 */
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	
	/**
	 * Get the status Code of HTTP Request. 
	 * @return The status Code of HTTP Request. 
	 */
	public Integer getStatusCode() {
		return statusCode;
	}

}
