/**
 * @author: yangji
 * @data:   Aug 20, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;
import java.util.Date;

import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * A partInfo describes the information of a part of a object.
 */
public class PartInfo implements Serializable{

	private static final long serialVersionUID = -670614822498037526L;
	private int partNumber;
	private Date lastModified;
	private String eTag;
	private long size;
	
	/**
	 * Construct a PartInfo instance by default.
	 */
	public PartInfo() {}
	
	/**
	 * Construct a PartInfo instance.
	 * @param partNumber the number of a part of the object.
	 * @param lastModified the last modified date of the part of object.
	 * @param eTag the eTag of a part of object.
	 * @param size the size of a part.
	 */
	public PartInfo(int partNumber, Date lastModified, String eTag, long size) {
		super();
		this.partNumber = partNumber;
		this.lastModified = lastModified;
		this.eTag = eTag;
		this.size = size;
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
	public void setPartNumber(int partNumber) {
		this.partNumber = partNumber;
	}
	/**
	 * Get the last modified date of the part of object.
	 * @return The last modified date of the part of object.
	 */
	public Date getLastModified() {
		return lastModified;
	}
	
	/**
	 * Set the last modified date of the part of object.
	 * @param lastModified The last modified date of the part of object.
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
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
	 * Get the size of a part.
	 * @return The size of a part.
	 */
	public long getSize() {
		return size;
	}
	
	/**
	 * Set the size of a part.
	 * @param size The size of a part.
	 */
	public void setSize(long size) {
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
