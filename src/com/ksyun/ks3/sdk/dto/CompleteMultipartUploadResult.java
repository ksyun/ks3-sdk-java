/**
 * @author: yangji
 * @data:   Aug 20, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;

import com.ksyun.ks3.sdk.tools.CodeUtils;
import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * A CompleteMultipartUploadResult instance contains the information about completing a multipart upload session.
 */
public class CompleteMultipartUploadResult implements Serializable {

	private static final long serialVersionUID = 4403520852343206776L;
	private String bucketName;
	private String objectKey;
	private String eTag;
	private String location;
	
	/**
	 * Construct a CompleteMultipartUploadResult instance by default.
	 */
	public CompleteMultipartUploadResult(){}	
	/**
	 * Construct a CompleteMultipartUploadResult instance.
	 * @param bucketName the bucket name
	 * @param objectKey the key of object
	 * @param eTag the eTag of a part of object.
	 * @param location the location of a part of object.
	 * @throws Exception
	 */
	public CompleteMultipartUploadResult(String bucketName, String objectKey,
			String eTag, String location) throws Exception {
		this.bucketName = bucketName;
		this.setObjectKey(objectKey);
		this.eTag = eTag;
		this.location = location;
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
	 * Get the eTag of a part of object.
	 * @return the eTag of a part of object.
	 */
	public String geteTag() {
		return eTag;
	}
	
	/**
	 * Set the eTag of a part of object.
	 * @param eTag The eTag of a part of object.
	 */
	public void seteTag(String eTag) {
		this.eTag = eTag;
	}
	
	/**
	 * Get the location of a part of object.
	 * @return The location of a part of object.
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * Set the location of a part of object.
	 * @param location The location of a part of object.
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	 * Get JSon of the instance.
	 */
	public String toJson(){
		return JsonUtils.getJson(this);		
	}
	
	/**
	 * Get String of the instance.
	 */
	public String toString(){
		return this.toJson();
	}
	
	
}
