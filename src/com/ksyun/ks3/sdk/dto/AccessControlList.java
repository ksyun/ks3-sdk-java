/**
 * @author: yangji
 * @data:   Apr 11, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;

import com.ksyun.ks3.sdk.dto.AccessControlList;
import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * The AccessControlList defines the access control policy.Three policies are supported:private,readonly and public. * 
 */
public final class AccessControlList implements Serializable{	
	
	/**
	 * That means only you have access to the bucket.
	 */
	public static final AccessControlList PRIVATE;
	/**
	 * That means everyone can have access to the bucket but couldn't modify it.
	 */
	public static final AccessControlList READONLY;
	/**
	 * That means the bucket is fully public.
	 */
	public static final AccessControlList PUBLIC;
	
	private String acl;
	
	private AccessControlList(String acl){
		this.acl = acl;
	}	
	
	/**
	 * Get the string express of the instance.
	 */
	public String toString() {
		return this.acl;
	}
	
	/**
	 * Get Json of the instance.
	 */
	public String toJson(){
		return JsonUtils.getJson(this);		
	}

	static {
		PRIVATE = new AccessControlList("private");
		READONLY = new AccessControlList("public-read");
		PUBLIC = new AccessControlList("public-read-write");
	}

}
