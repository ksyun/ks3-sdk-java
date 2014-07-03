/**
 * @author: yangji
 * @data:   Aug 20, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;

import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * The onwer of a bucket in ks3.
 */
public class Owner  implements Serializable{


	private static final long serialVersionUID = -4455422312315649682L;
	private String displayName;
	private String id;
	
	/**
	 * Construct a Owner instance by default.
	 */
	public Owner() {}
	
	/**
	 * Construct a Owner instance.
	 * @param displayName The name of the owner.
	 * @param id The id of the owner.
	 */
	public Owner(String displayName, String id) {
		this.displayName = displayName;
		this.id = id;
	}
	
	/**
	 * Get the name of the owner.
	 * @return The name of the owner.
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Set the name of the owner.
	 * @param displayName The name of the owner.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * Get the id of the owner.
	 * @return The id of the owner.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Set the id of the owner.
	 * @param id The id of the owner.
	 */
	public void setId(String id) {
		this.id = id;
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
