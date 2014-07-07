/**
 * @author: yangji
 * @data:   Apr 16, 2013
 */
package com.ksyun.ks3.sdk.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;

import com.ksyun.ks3.sdk.tools.CodeUtils;
import com.ksyun.ks3.sdk.tools.JsonUtils;

/**
 * An ObjectGetOptions instance contains the advanced options of getting an object.
 */
public class ObjectGetOptions implements Serializable{
	
	private static final long serialVersionUID = 591590580620033028L;
	private String bucketName;
	private String objectKey;
	private long range[];
	private String matchingETagConstraints;
	private String nonmatchingEtagConstraints;
	private Date unmodifiedSinceConstraint;
	private Date modifiedSinceConstraint;
	private Map<String,String> params;
	
	/**
	 * Construct a ObjectGetOptions instance by bucket name and object key.
	 * @param bucketName Bucket name.
	 * @param objectKey Object key.
	 */
	public ObjectGetOptions(String bucketName, String objectKey) throws Exception {	
		setBucketName(bucketName);
		setObjectKey(objectKey);
		params = new HashMap<String,String>();
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
	 * Get the range of the object you are getting.
	 * @return A 2-length array. range[0] is start, range[1] is end.
	 */
	public long[] getRange(){
		return this.range;
	}
	
	/**
	 * Set the range of the object you are getting.
	 * @param start The start of the range of the object.
	 * @param end The end of the range of the object.
	 */
	public void setRange(long start, long end)
	{
		if(start<0||start>end){
			throw new IllegalArgumentException("wrong range params");
		}
		range = (new long[] {
			start, end
		});
	}
	
	/**
	 * Get the value of "If-Match" header.
	 * @return The value of "If-Match" header. 
	 */
	public String getMatchingETagConstraints() {
		return matchingETagConstraints;
	}
	
	/**
	 * Set the value of "If-Match" header.
	 * @param matchingETagConstraints The value of "If-Match" header.
	 * @throws Exception
	 */
	public void setMatchingETagConstraints(String matchingETagConstraints) throws Exception {
		CodeUtils.checkStringParams("matchingETagConstraints", matchingETagConstraints);
		this.matchingETagConstraints = matchingETagConstraints;
	}
	
	/**
	 * Get the value of "If-None-Match" header.
	 * @return The value of "If-None-Match" header.
	 */
	public String getNonmatchingEtagConstraints() {
		return nonmatchingEtagConstraints;
	}
	
	
	/**
	 * Set the value of "If-None-Match" header.
	 * @param nonmatchingEtagConstraints The value of "If-None-Match" header.
	 * @throws Exception
	 */
	public void setNonmatchingEtagConstraints(String nonmatchingEtagConstraints) throws Exception {
		CodeUtils.checkStringParams("nonmatchingEtagConstraints", nonmatchingEtagConstraints);
		this.nonmatchingEtagConstraints = nonmatchingEtagConstraints;
	}
	
	/**
	 * Get the value of "If-Unmodified-Since" header.
	 * @return The value of "If-Unmodified-Since" header.
	 */
	public Date getUnmodifiedSinceConstraint() {
		return unmodifiedSinceConstraint;
	}
	
	/**
	 * Set the value of "If-Unmodified-Since" header.
	 * @param unmodifiedSinceConstraint The value of "If-Unmodified-Since" header.
	 * @throws Exception
	 */
	public void setUnmodifiedSinceConstraint(Date unmodifiedSinceConstraint) throws Exception {
		CodeUtils.checkObjectParams("unmodifiedSinceConstraint", unmodifiedSinceConstraint);
		this.unmodifiedSinceConstraint = unmodifiedSinceConstraint;
	}
	
	/**
	 * Get the value of "If-Modified-Since" header.
	 * @return The value of "If-Modified-Since" header.
	 */
	public Date getModifiedSinceConstraint() {
		return modifiedSinceConstraint;
	}	
	
	/**
	 * Set the value of "If-Modified-Since" header.
	 * @param modifiedSinceConstraint The value of "If-Modified-Since" header.
	 * @throws Exception
	 */
	public void setModifiedSinceConstraint(Date modifiedSinceConstraint) {
		this.modifiedSinceConstraint = modifiedSinceConstraint;
	}
	
	/**
	 * Get parameters for getting an object.
	 * @return The parameters for getting an object.
	 */
	public Map<String, String> getParams() {
		return params;
	}
	
	/**
	 * Set the parameters for getting an object. 
	 * @param params The parameters for getting an object. 
	 * @throws Exception
	 */
	public void setParams(Map<String, String> params) throws Exception {
		CodeUtils.checkMapParams("params", params);
		this.params = params;
	}
	
	/**
	 * Add a pair of key-value parameter.
	 * @param paramKey The key of parameter.
	 * @param paramValue the value of parameter.
	 * @throws Exception
	 */
	public void addParams(String paramKey,String paramValue) throws Exception{
		CodeUtils.checkStringParams("paramKey", paramKey);
		CodeUtils.checkStringParams("paramValue", paramValue);
		this.params.put(paramKey, paramValue);
	}	
	
	/**
	 * Get the value of "If-Unmodified-Since" header in string format.
	 * @return The value of "If-Unmodified-Since" header in string format.
	 */
	public String getUnmodifiedSinceString(){
		return data2String(this.unmodifiedSinceConstraint);
	}
	
	/**
	 * Get the value of "If-Modified-Since" header in string format.
	 * @return The value of "If-Modified-Since" header in string format.
	 */
	public String getModifiedSinceString(){
		return data2String(this.modifiedSinceConstraint);
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
	
	private String data2String(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
		sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
		return sdf.format(date);
	}

}
