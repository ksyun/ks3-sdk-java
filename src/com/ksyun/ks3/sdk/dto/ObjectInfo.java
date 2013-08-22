/**
 * @author: yangji
 * @data:   Apr 11, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;
import java.util.Date;

import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * ObjectInfo describes the basic informations of an object.
 */
public class ObjectInfo implements Serializable{
	
	private static final long serialVersionUID = 3972250451975939718L;
	private String ownerDisplayName;
	private String ownerID;
	private Date lastModified;
	private String eTag;
	private String key;
	private Integer size;	
	
	/**
	 * Construct an ObjectInfo instance by specified parameters.
	 * @param ownerDisplayName The display name of the owner.
	 * @param ownerID The ID of the owner.
	 * @param lastModified The last modified date of the object.
	 * @param eTag The eTag of the object.
	 * @param key The object key.
	 * @param size The object size.
	 */
	public ObjectInfo(String ownerDisplayName, String ownerID,
			Date lastModified, String eTag, String key, Integer size) {	
		this.ownerDisplayName = ownerDisplayName;
		this.ownerID = ownerID;
		this.lastModified = lastModified;
		this.eTag = eTag;
		this.key = key;
		this.size = size;
	}
	
	/**
	 * Get the display name of the owner.
	 * @return The display name of the owner.
	 */
	public String getOwnerDisplayName() {
		return ownerDisplayName;
	}
	
	/**
	 * Set the display name of the owner.
	 * @param ownerDisplayName The display name of the owner.
	 */
	public void setOwnerDisplayName(String ownerDisplayName) {
		this.ownerDisplayName = ownerDisplayName;
	}
	
	/**
	 * Get the ID of the owner.
	 * @return The ID of the owner.
	 */
	public String getOwnerID() {
		return ownerID;
	}
	
	/**
	 * Set the ID of the owner.
	 * @param ownerID The ID of the owner.
	 */
	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}
	
	/**
	 * Get the last modified date of the object.
	 * @return The last modified date of the object.
	 */
	public Date getLastModified() {
		return lastModified;
	}
	
	/**
	 * Set the last modified date of the object.
	 * @param lastModified The last modified date of the object.
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	/**
	 * Get the eTag of the object.
	 * @return The eTag of the object.
	 */
	public String geteTag() {
		return eTag;
	}
	
	/**
	 * Set the eTag of the object.
	 * @param eTag The eTag of the object.
	 */
	public void seteTag(String eTag) {
		this.eTag = eTag;
	}
	
	/**
	 * Get the object key. 
	 * @return G=The object key.
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Set the object key.
	 * @param key The object key.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Get the object size.
	 * @return The object size.
	 */
	public Integer getSize() {
		return size;
	}
	
	/**
	 * Set the object size.
	 * @param size The object size.
	 */
	public void setSize(Integer size) {
		this.size = size;
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
