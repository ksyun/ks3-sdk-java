/**
 * @author: yangji
 * @data:   Apr 11, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ksyun.ks3.sdk.dto.ObjectInfo;
import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * ObjectList describes the object information list and the query information.
 */
public class ObjectList implements Serializable{
	

	private static final long serialVersionUID = 8017243791817876379L;
	private String bucketName;
	private String maxKeys;
	private String prefix;
	private String marker;
	private Boolean isTruncated;
	private List<ObjectInfo> objectInfoList;
	
	/**
	 * Default constructor.
	 */
	public ObjectList(){}	
	
	/**
	 * Construct a ObjectList instance by specified parameters. 
	 * @param bucketName The bucket name.
	 * @param maxKeys The max keys of the query.
	 * @param prefix The prefix of the query.
	 * @param marker The marker of the query.
	 * @param isTruncated Is the query result truncated.
	 */
	public ObjectList(String bucketName, String maxKeys, String prefix, String marker, Boolean isTruncated) {
		this.bucketName = bucketName;
		this.maxKeys = maxKeys;
		this.prefix = prefix;
		this.marker = marker;
		this.isTruncated = isTruncated;
		this.objectInfoList = new ArrayList<ObjectInfo>();
	}
	
	/**
	 * Get the bucket name.
	 * @return the bucket name.
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
	 * Get the max keys of the query.
	 * @return The max keys of the query.
	 */
	public String getMaxKeys() {
		return maxKeys;
	}
	
	/**
	 * Set the max keys of the query.
	 * @param maxKeys The max keys of the query.
	 */
	public void setMaxKeys(String maxKeys) {
		this.maxKeys = maxKeys;
	}
	
	/**
	 * Get the prefix of the query.
	 * @return The prefix of the query.
	 */
	public String getPrefix() {
		return prefix;
	}
	
	/**
	 * Set the prefix of the query.
	 * @param prefix The prefix of the query.
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	/**
	 * Get the marker of the query.
	 * @return The marker of the query.
	 */
	public String getMarker() {
		return marker;
	}
	
	/**
	 * Set the marker of the query. 
	 * @param marker The marker of the query.
	 */	
	public void setMarker(String marker) {
		this.marker = marker;
	}
	
	/**
	 * Get is the query result truncated.
	 * @return Is the query result truncated.
	 */
	public Boolean getIsTruncated() {
		return isTruncated;
	}
	
	/**
	 * Set is the query result truncated.
	 * @param isTruncated Is the query result truncated.
	 */
	public void setIsTruncated(Boolean isTruncated) {
		this.isTruncated = isTruncated;
	}
	
	/**
	 * Add an objectInfo instance.
	 * @param objInfo An objectInfo instance.
	 */
	public void addObjectInfo(ObjectInfo objInfo){		
		this.objectInfoList.add(objInfo);
	}
	
	/**
	 * Get the objectInfo list.
	 * @return The objectInfo list.
	 */
	public List<ObjectInfo> getObjectInfoList(){
		return this.objectInfoList;
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
