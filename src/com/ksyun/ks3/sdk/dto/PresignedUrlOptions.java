/**
 * @author: yangji
 * @data:   Apr 17, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import com.ksyun.ks3.sdk.tools.CodeUtils;
import com.ksyun.ks3.sdk.tools.JsonUtils;


/**
 * An PresignedUrlOptions instance contains the advanced options of making a pre-signed url.
 */
public class PresignedUrlOptions implements Serializable{
	
	private String bucketName;
	private String objectKey;
	private Integer expires;
	private HashMap<String,String> responseHeaders;
	private HashSet<String> headerSet;
	
	/**
	 * Construct a PresignedUrlOptions instance by the specified parameters.
	 * @param bucketName
	 * @param objectKey
	 * @param expires
	 * @throws Exception
	 */
	public PresignedUrlOptions(String bucketName, String objectKey,Integer expires) throws Exception {
		setBucketName(bucketName);
		setObjectKey(objectKey);
		setExpires(expires);
		responseHeaders =  new HashMap<String, String>();
		initHeaderSet();
	}
	
	/**
	 * Get the bucket name.
	 * @return The bucket name.
	 */
	public String getBucketName() {
		return bucketName;
	}
	
	/**
	 * Set the bucket name.
	 * @param bucketName The bucket name.
	 */
	public void setBucketName(String bucketName) throws Exception {
		CodeUtils.checkStringParams("bucketName", bucketName);
		this.bucketName = bucketName;
	}
	
	/**
	 * Get the object key.
	 * @return The object key.
	 */
	public String getObjectKey() {
		return objectKey;
	}
	
	/**
	 * Set the object key.
	 * @param objectKey The object key.
	 */
	public void setObjectKey(String objectKey) throws Exception {
		CodeUtils.checkStringParams("objectKey", objectKey);
		this.objectKey = objectKey;
	}

	/**
	 * Get the expires date.
	 * @return The expires date.
	 */
	public Integer getExpires() {
		return expires;
	}

	/**
	 * Set the expires date.
	 * @param expires The expires date.
	 * @throws Exception
	 */
	public void setExpires(Integer expires) throws Exception {
		CodeUtils.checkObjectParams("expires", expires);
		this.expires = expires;
	}
	
	/**
	 * Add an overrided response header.
	 * @param headerKey The key of the header.
	 * @param headerValue The value of the header.
	 * @throws Exception
	 */
	public void addOverridedResponseHeader(String headerKey,String headerValue) throws Exception{
		CodeUtils.checkObjectParams("headerKey", headerKey);
		CodeUtils.checkObjectParams("headerValue", headerValue);
		if(!headerSet.contains(headerKey.toLowerCase()))
			throw new IllegalArgumentException("Unsupported response header:"+headerKey);

		responseHeaders.put(headerKey, headerValue);
	}
	
	/**
	 * Get the map of overrided response headers you've set before.
	 * @return The map of overrided response headers you've set before.
	 */
	public HashMap<String,String> getResponseHeaders(){
		return this.responseHeaders;
	}	
	
	/**
	 * Get Json of the instance.
	 */
	public String toJson(){
		return JsonUtils.getJson(this);		
	}
	
	private void initHeaderSet(){
		headerSet = new HashSet<String>();
		headerSet.add("response-content-type");
		headerSet.add("response-content-language");
		headerSet.add("response-expires");
		headerSet.add("response-cache-control");
		headerSet.add("response-content-disposition");
		headerSet.add("response-content-encoding");
	}
}
