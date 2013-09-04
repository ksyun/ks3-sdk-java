/**
 * @author: yangji
 * @data:   Sep 4, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * Metadata of your object.
 */
public class ObjectMetadata implements Serializable {
	
	
	private static final long serialVersionUID = -1744940447357277626L;
	private Map<String,String> metadata;
	
	/**
	 * Default constructor.
	 * @param metadata The metadata of your object.
	 */
	public ObjectMetadata(Map<String,String> metadata){
		this.metadata = metadata;
	}
	
	/**
	 * Get read-only metadata of your object. 
	 * @return The metadata of your object.It's a map.
	 */
	public Map<String,String> getMetadata(){
		return Collections.unmodifiableMap(metadata);
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
