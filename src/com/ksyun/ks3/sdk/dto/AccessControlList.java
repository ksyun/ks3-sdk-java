/**
 * @author: yangji
 * @data:   Apr 11, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;

import com.ksyun.ks3.sdk.dto.AccessControlList;
import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * The AccessControlList defines the access control policy.Three policies are supported:private,readonly and public.
 */
public final class AccessControlList implements Serializable{	
	
	
	private static final long serialVersionUID = -3659262771000245871L;
	/**
	 * That means only you have access to the bucket/object.
	 */
	public static final AccessControlList PRIVATE;
	/**
	 * That means everyone can have access to the bucket/object but couldn't modify it.
	 */
	public static final AccessControlList READONLY;
	/**
	 * That means the bucket/object is fully public.
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
	 * Get JSon of the instance.
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
