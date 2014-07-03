/**
 * @author: yangji
 * @data:   Apr 11, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;

import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * An abjectEtag contains the MD5 of you object.
 */
public class ObjectEtag implements Serializable{
	
	private static final long serialVersionUID = 5148234442747150732L;
	private String eTagValue;
	
	/**
	 * Default constructor.
	 */
	public ObjectEtag(){}
	
	
	/**
	 * Construct a eTag instance by eTag value.
	 * @param eTagValue The eTag value.
	 */
	public ObjectEtag(String eTagValue) {
		super();
		this.eTagValue = eTagValue;
	}
	

	/**
	 * Get the string express of the instance.
	 */
	public String toString() {
		return eTagValue;
	}

	/**
	 * Get eTag value.
	 * @return The eTag value.
	 */
	public String geteTagValue() {
		return eTagValue;
	}
	
	/**
	 * Set the eTag value.
	 * @param eTagValue The eTag value.
	 */
	public void seteTagValue(String eTagValue) {
		this.eTagValue = eTagValue;
	}
	
	/**
	 * Get JSon of the instance.
	 */
	public String toJson(){
		return JsonUtils.getJson(this);		
	}
	

}
