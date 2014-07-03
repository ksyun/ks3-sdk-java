/**
 * @author: yangji
 * @data:   Aug 20, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;

import com.ksyun.ks3.sdk.tools.CodeUtils;
import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * A UploadPartResult instance contains the information about finishing a upload session.
 */
public class UploadPartResult implements Serializable{	
	
	private static final long serialVersionUID = 1943843850998534236L;
	private String uploadId;
	private int partNumber;
	private String eTag;	
	
	/**
	 * Construct a UploadPartResult instance by default.
	 */
	public UploadPartResult() {}
	
	/**
	 * Construct a UploadPartResult instance.
	 * @param uploadId the id of a upload session.
	 * @param partNumber the number of a part of the object.
	 * @param eTag the eTag of a part of object.
	 */
	public UploadPartResult(String uploadId, int partNumber, String eTag) {
		this.uploadId = uploadId;
		this.partNumber = partNumber;
		this.eTag = eTag;
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
	 * Get a UploadPart instance.
	 * @return The UploadPart instance.
	 */
	public UploadPart getUploadPart(){
		return new UploadPart(partNumber, eTag);
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
