/**
 * @author: yangji
 * @data:   Apr 2, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;

import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * A container that contains your access key id and your access key secret.
 */
public class Credential implements Serializable{
	
	private String accessKeyId="";
	private String accessKeySecret="";	

	/**
	 * Construct a credential instance by access key id and access key secret.
	 * @param accessKeyId Access key id
	 * @param accessKeySecret Access key secret
	 */
	public Credential(String accessKeyId,String accessKeySecret){
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
	}
	
	/**
	 * Get the access key id.
	 * @return The access key id.
	 */
	public String getAccessKeyId() {
		return accessKeyId;
	}
	/**
	 * Set the access key id.
	 * @param accessKeyId The access key id.
	 */
	public void setAccessKeyId(String accessKeyId) {
		if(accessKeyId!=null)
			this.accessKeyId = accessKeyId;
	}
	/**
	 * Get the access key secret.
	 * @return The access key secret.
	 */
	public String getAccessKeySecret() {
		return accessKeySecret;
	}
	/**
	 * Set The access key secret.
	 * @param accessKeySecret The access key secret.
	 */
	public void setAccessKeySecret(String accessKeySecret) {
		if(accessKeyId!=null)
			this.accessKeySecret = accessKeySecret;
	}
	
	/**
	 * Get Json of the instance.
	 */
	public String toJson(){
		return JsonUtils.getJson(this);		
	}
}
