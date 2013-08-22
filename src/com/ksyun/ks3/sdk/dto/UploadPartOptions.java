/**
 * @author: yangji
 * @data:   Aug 20, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.InputStream;
import java.io.Serializable;

import com.ksyun.ks3.sdk.tools.CodeUtils;
import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * An UploadPartOptions instance contains the advanced options of uploading a part of a object.
 */
public class UploadPartOptions implements Serializable{

	private static final long serialVersionUID = -1061802337618086069L;
	private String bucketName;
	private String objectKey;
	private String uploadId;
	private int partNumber;
	private long partSize;
	private String md5;
	private InputStream inputStream;

	/**
	 * Construct a UploadPartOptions with the specific parameters.
	 * @param bucketName the bucket name.
	 * @param objectKey the key of object.
	 * @param uploadId the id of a upload session.
	 * @param partNumber the number of a part of the object.
	 * @param partSize the size of a part.
	 * @param inputStream the input stream of a part of a object.
	 * @throws Exception
	 */
	public UploadPartOptions(String bucketName, String objectKey,
			String uploadId, int partNumber, long partSize,
			InputStream inputStream) throws Exception {
		setBucketName(bucketName);
		setObjectKey(objectKey);
		setUploadId(uploadId);
		setPartNumber(partNumber);
		setPartSize(partSize);
		setInputStream(inputStream);		
	}
	
	/**
	 * Construct a UploadPartOptions with the specific parameters.
	 * @param bucketName the bucket name.
	 * @param objectKey the key of object.
	 * @param uploadId the id of a upload session.
	 * @param partNumber the number of a part of the object.
	 * @param partSize the size of a part.
	 * @param md5 the md5 of a part of a object.
	 * @param inputStream
	 * @throws Exception
	 */
	public UploadPartOptions(String bucketName, String objectKey,
			String uploadId, int partNumber, long partSize, String md5,
			InputStream inputStream) throws Exception {
		setBucketName(bucketName);
		setObjectKey(objectKey);
		setUploadId(uploadId);
		setPartNumber(partNumber);
		setPartSize(partSize);
		setMd5(md5);
		setInputStream(inputStream);		
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
	 * The number of a part of the object.
	 * @return The number of a part of the object.
	 */
	public int getPartNumber() {
		return partNumber;
	}
	
	/**
	 * Set the number of a part of the object.
	 * @param partNumber The number of a part of the object.
	 */	
	public void setPartNumber(int partNumber) throws Exception{
		if(partNumber<1||partNumber>1000)
			throw new IllegalArgumentException("PartNumber out of range");
		this.partNumber = partNumber;
	}
	
	/**
	 * Get the size of a part.
	 * @return The size of a part.
	 */
	public long getPartSize() {
		return partSize;
	}
	
	/**
	 * Set the size of a part.
	 * @param partSize The size of a part.
	 * @throws Exception
	 */
	public void setPartSize(long partSize) throws Exception {
		if(partSize<0L||partSize>0x140000000L)//0B-5GB
			throw new IllegalArgumentException("partSize out of range");
		this.partSize = partSize;
	}
	
	/**
	 * The md5 of a part of a object.
	 * @return The md5 of a part of a object.
	 */
	public String getMd5() {
		return md5;
	}
	/**
	 * Set the md5 of a part of a object.
	 * @param md5 The md5 of a part of a object.
	 */
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	/**
	 * The input stream of a part of a object.
	 * @return The input stream of a part of a object.
	 */
	public InputStream getInputStream() {
		return inputStream;
	}
	
	/**
	 * Set the input stream of a part of a object.
	 * @param inputStream The input stream of a part of a object.
	 * @throws Exception
	 */
	public void setInputStream(InputStream inputStream) throws Exception {
		CodeUtils.checkObjectParams("InputStream", inputStream);
		this.inputStream = inputStream;
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
