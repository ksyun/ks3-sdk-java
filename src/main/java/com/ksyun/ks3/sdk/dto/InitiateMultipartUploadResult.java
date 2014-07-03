/**
 * @author: yangji
 * @data:   Aug 20, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;

import com.ksyun.ks3.sdk.tools.CodeUtils;
import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * A InitiateMultipartUploadResult instance contains the information about initiating a multipart upload session.
 */
public class InitiateMultipartUploadResult implements Serializable{
	

	private static final long serialVersionUID = -2996464071225484857L;
	private String uploadId;
	private String bucketName;
	private String objectKey;
	
	/**
	 * Construct a InitiateMultipartUploadResult instance by default.
	 */
	public InitiateMultipartUploadResult(){}
		
	/**
	 * Construct a InitiateMultipartUploadResult instance.
	 * @param uploadId the id of a upload session.
	 * @param bucketName the bucket name
	 * @param objectKey the key of object
	 */
	public InitiateMultipartUploadResult(String uploadId, String bucketName,
			String objectKey) {
		this.uploadId = uploadId;
		this.bucketName = bucketName;
		this.objectKey = objectKey;
	}
	
	/**
	 * Get the id of a upload session.
	 * @return The id of a upload session.
	 */
	public String getUploadId() {
		return uploadId;
	}
	
	/**
	 * Set the id of a upload session.
	 * @param uploadId The id of a upload session.
	 * @throws Exception
	 */
	public void setUploadId(String uploadId) throws Exception {
		CodeUtils.checkStringParams("uploadId", bucketName);
		this.uploadId = uploadId;
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
