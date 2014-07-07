/**
 * @author: yangji
 * @data:   Aug 20, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;

import com.ksyun.ks3.sdk.tools.CodeUtils;
import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * An PartListOptions instance contains the advanced options of getting a list of part of a object.
 */
public class PartListOptions implements Serializable{

	private static final long serialVersionUID = -1670291461256069494L;
	private String bucketName;
	private String objectKey;
	private String uploadId;
	private Integer maxParts;
	private Integer partNumberMarker;	
	
	/**
	 * Construct a PartListOptions instance.
	 * @param bucketName the bucket name
	 * @param objectKey the key of object
	 * @param uploadId the id of a upload session
	 * @throws Exception
	 */
	public PartListOptions(String bucketName, String objectKey, String uploadId) throws Exception {
		setBucketName(bucketName);
		setObjectKey(objectKey);
		setUploadId(uploadId);
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
	 * Get the max parts of a part list.
	 * @return The max parts of a part list.
	 */
	public Integer getMaxParts() {
		return maxParts;
	}

	/**
	 * Set the max parts of a part list.
	 * @param maxParts The max parts of a part list.
	 */	
	public void setMaxParts(Integer maxParts) throws Exception {
		CodeUtils.checkObjectParams("maxParts", maxParts);
		if(maxParts<1)
			throw new IllegalArgumentException("MaxParts out of range.");
		this.maxParts = maxParts;
	}
	
	/**
	 * Set the part number after which listing begins.
	 * @return The part number after which listing begins.
	 */
	public Integer getPartNumberMarker() {
		return partNumberMarker;
	}

	/**
	 * Set the part number after which listing begins.
	 * @param partNumberMarker The part number after which listing begins.
	 */
	public void setPartNumberMarker(Integer partNumberMarker) throws Exception {
		CodeUtils.checkObjectParams("partNumberMarker", partNumberMarker);
		if(partNumberMarker<1||partNumberMarker>1000)
			throw new IllegalArgumentException("PartNumberMarker out of range.");		
		this.partNumberMarker = partNumberMarker;
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
