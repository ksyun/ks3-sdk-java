/**
 * @author: yangji
 * @data:   Apr 11, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;

import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * AccessControlPolicy describes the basic access polices about a bucket.
 */
public class AccessControlPolicy implements Serializable{

	private static final long serialVersionUID = -5019350711193143100L;
	private String ownerDisplayName;
	private String ownerID;
	private String grant;

	/**
	 * Construct a AccessControlPolicy
	 * @param ownerDisplayName The display name of the owner.
	 * @param ownerID The ID of the owner.
	 * @param grant The grant.
	 */
	public AccessControlPolicy(String ownerDisplayName, String ownerID,
			String grant) {
		super();
		this.ownerDisplayName = ownerDisplayName;
		this.ownerID = ownerID;
		this.grant = grant;
	}

	/**
	 * Get the string express of the instance.
	 */
	public String toString() {
		return "AccessControlPolicy :" + this.grant;
	}

	/**
	 * Get the the display name of the owner.
	 * @return The the display name of the owner.
	 */
	public String getOwnerDisplayName() {
		return ownerDisplayName;
	}
	
	/**
	 * Set the the display name of the owner.
	 * @param ownerDisplayName The the display name of the owner.
	 */
	public void setOwnerDisplayName(String ownerDisplayName) {
		this.ownerDisplayName = ownerDisplayName;
	}

	/**
	 * Get the the ID of the owner.
	 * @return The the ID of the owner.
	 */
	public String getOwnerID() {
		return ownerID;
	}

	/**
	 * Set the the ID of the owner.
	 * @param ownerID The the ID of the owner.
	 */
	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	/**
	 * Get the grant.
	 * @return The grant.
	 */
	public String getGrant() {
		return grant;
	}

	/**
	 * Set the grant.
	 * @param grant The grant.
	 */
	public void setGrant(String grant) {
		this.grant = grant;
	}
	
	/**
	 * Get JSon of the instance.
	 */
	public String toJson(){
		return JsonUtils.getJson(this);		
	}
}
