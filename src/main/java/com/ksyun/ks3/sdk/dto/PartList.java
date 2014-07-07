/**
 * @author: yangji
 * @data:   Aug 20, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ksyun.ks3.sdk.tools.CodeUtils;
import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * A PartList instance contains the result for querying a list of part of a object.
 */
public class PartList implements Serializable{
	
	private static final long serialVersionUID = 2610015072570084666L;
	private String bucketName;
	private String objectKey;
	private String uploadId;
	private Integer maxParts;
	private Integer partNumberMarker;
	private Owner owner;
	private Owner initiator;
	private String storageClass;
	private boolean isTruncated;
	private Integer nextPartNumberMarker;
	private List<PartInfo> parts;
	
	/**
	 * Construct a PartList instance.
	 * @param bucketName the bucket name
	 * @param objectKey the key of object
	 * @param uploadId the id of a upload session
	 * @param maxParts the max parts of a part list.
	 * @param partNumberMarker the part number after which listing begins.
	 * @param owner the owner of the object.
	 * @param initiator the initiator of the object.
	 * @param storageClass the class of storage used to store the uploaded object.
	 * @param isTruncated is the query result truncated.
	 * @param nextPartNumberMarker the next part marker of the part list.
	 */
	public PartList(String bucketName, String objectKey, String uploadId,
			Integer maxParts, Integer partNumberMarker, Owner owner,
			Owner initiator, String storageClass, boolean isTruncated,
			Integer nextPartNumberMarker) {
		this.bucketName = bucketName;
		this.objectKey = objectKey;
		this.uploadId = uploadId;
		this.maxParts = maxParts;
		this.partNumberMarker = partNumberMarker;
		this.owner = owner;
		this.initiator = initiator;
		this.storageClass = storageClass;
		this.isTruncated = isTruncated;
		this.nextPartNumberMarker = nextPartNumberMarker;
		this.parts = new ArrayList<PartInfo>();
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
	public void setMaxParts(Integer maxParts) {
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
	public void setPartNumberMarker(Integer partNumberMarker) {
		this.partNumberMarker = partNumberMarker;
	}

	/**
	 * Get the owner of the object.
	 * @return The owner of the object.
	 */
	public Owner getOwner() {
		return owner;
	}

	/**
	 * Set the owner of the object.
	 * @param owner The owner of the object.
	 */
	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	/**
	 * Get the initiator of the object.
	 * @return the initiator of the object.
	 */
	public Owner getInitiator() {
		return initiator;
	}

	/**
	 * Set the initiator of the object.
	 * @param initiator The initiator of the object.
	 */
	public void setInitiator(Owner initiator) {
		this.initiator = initiator;
	}

	/**
	 * Get the class of storage used to store the uploaded object.
	 * @return The class of storage used to store the uploaded object.
	 */
	public String getStorageClass() {
		return storageClass;
	}

	/**
	 * Set the class of storage used to store the uploaded object.
	 * @param storageClass The class of storage used to store the uploaded object.
	 */
	public void setStorageClass(String storageClass) {
		this.storageClass = storageClass;
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
	 * Get the next part marker of the part list.
	 * @return The next part marker of the part list.
	 */
	public Integer getNextPartNumberMarker() {
		return nextPartNumberMarker;
	}

	/**
	 * Set the next part marker of the part list.
	 * @param nextPartNumberMarker The next part marker of the part list.
	 */
	public void setNextPartNumberMarker(Integer nextPartNumberMarker) {
		this.nextPartNumberMarker = nextPartNumberMarker;
	}

	/**
	 * Get the the list of PartInfo.
	 * @return The the list of PartInfo.
	 */
	public List<PartInfo> getParts() {
		return parts;
	}

	/**
	 * Add a PartInfo instance to the list.
	 * @param part
	 */
	public void addPart(PartInfo part) {
		this.parts.add(part);
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
