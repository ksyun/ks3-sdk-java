/**
 * @author: yangji
 * @data:   Aug 20, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;
import java.util.List;

import com.ksyun.ks3.sdk.tools.CodeUtils;
import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * An CompleteMultipartUploadOptions instance contains the advanced options to complete the multipart upload.
 */
public class CompleteMultipartUploadOptions implements Serializable{

	private static final long serialVersionUID = 8569557445899542729L;
	private String bucketName;
	private String objectKey;
	private String uploadId;
	private List<UploadPart> uploadList;
	
	/**
	 * Construct a CompleteMultipartUploadOptions instance.
	 * @param bucketName the bucket name
	 * @param objectKey the key of object
	 * @param uploadId the id of a upload session
	 * @param uploadList the list of upload sessions.
	 * @throws Exception
	 */
	public CompleteMultipartUploadOptions(String bucketName, String objectKey,
			String uploadId, List<UploadPart> uploadList) throws Exception {
		setBucketName(bucketName);
		setObjectKey(objectKey);
		setUploadId(uploadId);
		setUploadList(uploadList);
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
		CodeUtils.checkStringParams("uploadId", uploadId);
		this.uploadId = uploadId;
	}
	
	/**
	 * Get the list of upload sessions.
	 * @return The list of upload sessions.
	 */
	public List<UploadPart> getUploadList() {
		return uploadList;
	}
	
	/**
	 * Set the list of upload sessions.
	 * @param uploadList The list of upload sessions.
	 * @throws Exception
	 */
	public void setUploadList(List<UploadPart> uploadList) throws Exception {
		CodeUtils.checkObjectParams("uploadList", uploadList);
		this.uploadList = uploadList;
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
