/**
 * @author: yangji
 * @data:   Apr 15, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;

import com.ksyun.ks3.sdk.tools.CodeUtils;
import com.ksyun.ks3.sdk.tools.EncodingUtils;
import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * An ObjectListOptions instance contains the advanced options of getting the object list of a bucket.
 */
public class ObjectListOptions implements Serializable{
	
	private String bucketName;
	private String prefix;
	private String marker;
	private Integer maxKeys;
	private char delimiter;	
	
	/**
	 * Construct a ObjectListOptions instance by bucket name.
	 * @param bucketName The bucket name.
	 * @throws Exception
	 */
	public ObjectListOptions(String bucketName) throws Exception {		
		setBucketName(bucketName);
		this.prefix = null;
		this.marker = null;
		this.maxKeys = null;
		this.delimiter = 0;
	}	
	
	/**
	 * Construct a ObjectListOptions instance by the specified parameters.
	 * @param bucketName The bucket name.
	 * @param prefix The prefix.
	 * @param marker The marker.
	 * @param maxKeys The max keys.
	 * @param delimiter The delimiter.
	 * @throws Exception
	 */
	public ObjectListOptions(String bucketName, String prefix, String marker,
			Integer maxKeys, char delimiter) throws Exception {
		setBucketName(bucketName);
		setPrefix(prefix);
		setMarker(marker);
		setMaxKeys(maxKeys);
		setDelimiter(delimiter);
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
	 * Get the prefix.
	 * @return The prefix.
	 */
	public String getPrefix() {
		return prefix;
	}
	
	/**
	 * Set the prefix.
	 * @param prefix the prefix.
	 * @throws Exception
	 */
	public void setPrefix(String prefix) throws Exception {
		CodeUtils.checkObjectParams("prefix", prefix);
//		this.prefix = prefix;
		this.prefix = EncodingUtils.getUrlEncode(prefix);
	}
	
	/**
	 * Get the marker.
	 * @return The marker.
	 */
	public String getMarker() {
		return marker;
	}
	
	/**
	 * Set the marker.
	 * @param marker The marker.
	 * @throws Exception
	 */
	public void setMarker(String marker) throws Exception {
		CodeUtils.checkObjectParams("marker", marker);
		this.marker = EncodingUtils.getUrlEncode(marker);
	}
	
	/**
	 * Get the max keys.
	 * @return The max keys.
	 */
	public Integer getMaxKeys() {
		return maxKeys;
	}
	
	/**
	 * Set the max keys.
	 * @param maxKeys The max keys.
	 * @throws Exception
	 */
	public void setMaxKeys(Integer maxKeys) throws Exception {
		CodeUtils.checkObjectParams("maxKeys", maxKeys);
		if (maxKeys.intValue() < 0 || maxKeys.intValue() > 1000)
		{
			throw new IllegalArgumentException(("MaxKeys out of range."));
		} else
		{
			this.maxKeys = maxKeys;
			return;
		}
	}
	
	/**
	 * Get the delimiter.
	 * @return The delimiter.
	 */
	public char getDelimiter() {
		return delimiter;
	}
	
	/**
	 * Set the delimiter.
	 * @param delimiter The delimiter.
	 * @throws Exception
	 */
	public void setDelimiter(char delimiter) throws Exception {
		CodeUtils.checkObjectParams("delimiter", delimiter);		
		this.delimiter = delimiter;
	}
	
	/**
	 * Get Json of the instance.
	 */
	public String toJson(){
		return JsonUtils.getJson(this);		
	}
	
	

}
