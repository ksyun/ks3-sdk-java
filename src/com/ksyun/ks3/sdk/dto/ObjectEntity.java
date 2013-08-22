/**
 * @author: yangji
 * @data:   Apr 11, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.InputStream;
import java.io.Serializable;

import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * A object entity that contains the content of the object.
 */
public class ObjectEntity implements Serializable{

	private static final long serialVersionUID = -7664972008408662875L;
	private String bucketName;
	private String objectKey;
	private InputStream objectValue;
	
	/**
	 * Construct a object entity by bucket name and object key.
	 * @param bucketName Bucket name.
	 * @param objectKey Object key.
	 */
	public ObjectEntity(String bucketName, String objectKey) {
		this.bucketName = bucketName;
		this.objectKey = objectKey;
	}	
	
	/**
	 * Construct a object entity by bucket name,object key and the object value.
	 * @param bucketName Bucket name.
	 * @param objectKey Object key.
	 * @param objectValue Object value.
	 */
	public ObjectEntity(String bucketName, String objectKey,InputStream objectValue) {
		this.bucketName = bucketName;
		this.objectKey = objectKey;
		this.objectValue = objectValue;
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
	public void setBucketName(String bucketName) {
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
	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}
	
	/**
	 * Get the object value.
	 * @return The object value.
	 */
	public InputStream getObjectValue() {
		return objectValue;
	}
	/**
	 * Set the object value.
	 * @param objectValue The object value.
	 */
	public void setObjectValue(InputStream objectValue) {
		this.objectValue = objectValue;
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
