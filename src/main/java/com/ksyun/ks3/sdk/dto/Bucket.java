/**
 * @author: yangji
 * @data:   Apr 8, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;
import java.util.Date;

import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * This is just a model of a bucket.
 */
public class Bucket implements Serializable{

	private static final long serialVersionUID = -2229418034330183866L;
	private String name;
	private Date creationDate;
	
	/**
	 * Default constructor.
	 */
	public Bucket(){}	
	
	/**
	 * Construct a bucket by bucket name and the creation date.
	 * @param name bucket name.
	 * @param creationDate creation date.
	 */
	public Bucket(String name, Date creationDate) {
		
		this.name = name;
		this.creationDate = creationDate;
	}
	
	/**
	 * Get the bucket name.
	 * @return The bucket name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the bucket name.
	 * @param name The bucket name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the creation date.
	 * @return The creation date.
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	
	/**
	 * Set the creation date.
	 * @param creationDate The creation date.
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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
