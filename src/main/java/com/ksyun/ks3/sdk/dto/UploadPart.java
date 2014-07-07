/**
 * @author: yangji
 * @data:   Aug 20, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;

import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * A basic element of a part of object.
 */
public class UploadPart implements Serializable{
		
	private static final long serialVersionUID = 2702283681523326541L;
	private int partNumber;
	private String eTag;
		
	/**
	 * Construct a UploadPart by default.
	 */
	public UploadPart() {}
	
	/**
	 * Construct a UploadPart instance.
	 * @param partNumber the number of a part of the object.
	 * @param eTag the eTag of a part of object.
	 */
	public UploadPart(int partNumber, String eTag) {
		this.partNumber = partNumber;
		this.eTag = eTag;
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
