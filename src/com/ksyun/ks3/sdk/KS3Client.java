/**
 * @author: yangji
 * @data:   Apr 8, 2013
 */
package com.ksyun.ks3.sdk;

import java.io.File;
import java.util.List;

import com.ksyun.ks3.sdk.dto.AccessControlList;
import com.ksyun.ks3.sdk.dto.AccessControlPolicy;
import com.ksyun.ks3.sdk.dto.Bucket;
import com.ksyun.ks3.sdk.dto.Credential;
import com.ksyun.ks3.sdk.dto.ObjectEntity;
import com.ksyun.ks3.sdk.dto.ObjectEtag;
import com.ksyun.ks3.sdk.dto.ObjectGetOptions;
import com.ksyun.ks3.sdk.dto.ObjectList;
import com.ksyun.ks3.sdk.dto.ObjectListOptions;
import com.ksyun.ks3.sdk.dto.PresignedUrlOptions;
import com.ksyun.ks3.sdk.services.httpservice.HttpRequestor;
import com.ksyun.ks3.sdk.services.ks3service.BucketOperation;
import com.ksyun.ks3.sdk.services.ks3service.ObjectOperation;
import com.ksyun.ks3.sdk.services.ks3service.ServiceOperation;

/**
 * a KS3Client provides the methods to operate the KSS interfaces.
 */
public class KS3Client {
	
	private static HttpRequestor requestor = new HttpRequestor();
	private ServiceOperation serviceOperation;
	private BucketOperation bucketOperation;
	private ObjectOperation objectOperation;

	/**
	 * Construct a KS3Client instance by your credential.
	 * @param credential A instance of Credential, which is constructed by your access key id and your access key secret.
	 * @throws Exception
	 */
	public KS3Client(Credential credential) throws Exception {
		this.serviceOperation = new ServiceOperation(requestor,credential);
		this.bucketOperation = new BucketOperation(requestor,credential);
		this.objectOperation = new ObjectOperation(requestor,credential);
	}	
	
	/**
	 * Get the bucket list.
	 * @return Bucket list.
	 * @throws Exception
	 */
	public List<Bucket> getBucketList() throws Exception{
		return serviceOperation.getBucketList();
	}
	
	/**
	 * Create a bucket.
	 * @param bucketName Bucket name.
	 * @throws Exception
	 */
	public void createBucket(String bucketName) throws Exception {
		bucketOperation.createBucket(bucketName);	
	}
	
	/**
	 * Delete a bucket.
	 * @param bucketName Bucket name.
	 * @throws Exception
	 */
	public void deleteBucket(String bucketName) throws Exception {
		bucketOperation.deleteBucket(bucketName);
	}
	
	/**
	 * Get the access control policy of the specified bucket.
	 * @param bucketName Bucket name.
	 * @return a AccessControlPolicy instance that describes your policy.
	 * @throws Exception
	 */
	public AccessControlPolicy getBucketACL(String bucketName) throws Exception {
		return bucketOperation.getBucketACL(bucketName);		
	}
	
	/**
	 * Set the access control policy to a bucket.
	 * @param bucketName Bucket name.
	 * @param acl The AccessControlPolicy instance that describes your policy.
	 * @throws Exception
	 */
	public void setBucketACL(String bucketName, AccessControlList acl) throws Exception {
		bucketOperation.setBucketACL(bucketName, acl);
	}
	
	/**
	 * Get the object list of a bucket.
	 * @param bucketName Bucket name.
	 * @return object list of a bucket.
	 * @throws Exception
	 */
	public ObjectList getObjectList(String bucketName) throws Exception{
		return bucketOperation.getObjectList(bucketName);
	}
	
	/**
	 * Get the object list of a bucket by options.
	 * @param options An ObjectListOptions instance that describes the advanced options related with getting object list such as marker,prefix and so on.
	 * @return object list of a bucket.
	 * @throws Exception
	 */
	public ObjectList getObjectListByOptions(ObjectListOptions options) throws Exception{
		return bucketOperation.getObjectListByOptions(options);
	}
	
	/**
	 * Upload a file into a object.
	 * @param bucketName Bucket name.
	 * @param objectKey The key of a object.
	 * @param file The file to upload.
	 * @param mimeType Mime type according to the type of the file.
	 * @return The eTag of the object.
	 * @throws Exception
	 */
	public ObjectEtag putObject(String bucketName, String objectKey, File file, String mimeType) throws Exception{
		return objectOperation.putObject(bucketName, objectKey, file, mimeType);
	}
	
	/**
	 * Put text into a object.
	 * @param bucketName Bucket name.
	 * @param objectKey The key of a object.
	 * @param content The text of the object content.
	 * @return The eTag of the object.
	 * @throws Exception
	 */
	public ObjectEtag putStringObject(String bucketName, String objectKey,String content) throws Exception{	
		return objectOperation.putStringObject(bucketName, objectKey, content);
	}
	
	/**
	 * Get a object.
	 * @param bucketName Bucket name.
	 * @param objectKey The key of a object.
	 * @return A object entity that contains the content of the object and the bucket it owns and the object key.
	 * @throws Exception
	 */
	public ObjectEntity getObject(String bucketName, String objectKey) throws Exception{
		return objectOperation.getObject(bucketName, objectKey);
	}
	
	/**
	 * Get a object by options.
	 * @param options An ObjectGetOptions instance that contains some advanced usages for get an object.
	 * @return A object entity that contains the content of the object and the bucket it owns and the object key.
	 * @throws Exception
	 */
	public ObjectEntity getObejctByOptions(ObjectGetOptions options) throws Exception{
		return objectOperation.getObejctByOptions(options);		
	}
	
	/**
	 * Delete a object.
	 * @param bucketName Bucket name.
	 * @param objectKey The key of a object.
	 * @throws Exception
	 */
	public void deleteObject(String bucketName, String objectKey) throws Exception{
		objectOperation.deleteObject(bucketName, objectKey);
	}		
	
	/**
	 * Generate a pre-signed url.
	 * @param options An PresignedUrlOptions instance that contains the conditions to generate a pre-signed url.
	 * @return url
	 * @throws Exception
	 */
	public String getPresignedUrl(PresignedUrlOptions options) throws Exception{
		return objectOperation.getPresignedUrl(options);
	}
}
