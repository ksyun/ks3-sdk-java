/**
 * @author: yangji
 * @data:   Apr 8, 2013
 */
package com.ksyun.ks3.sdk;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.ksyun.ks3.sdk.dto.AbortMultipartUploadOptions;
import com.ksyun.ks3.sdk.dto.AccessControlList;
import com.ksyun.ks3.sdk.dto.AccessControlPolicy;
import com.ksyun.ks3.sdk.dto.Bucket;
import com.ksyun.ks3.sdk.dto.CompleteMultipartUploadOptions;
import com.ksyun.ks3.sdk.dto.CompleteMultipartUploadResult;
import com.ksyun.ks3.sdk.dto.Credential;
import com.ksyun.ks3.sdk.dto.InitiateMultipartUploadResult;
import com.ksyun.ks3.sdk.dto.ObjectEntity;
import com.ksyun.ks3.sdk.dto.ObjectEtag;
import com.ksyun.ks3.sdk.dto.ObjectGetOptions;
import com.ksyun.ks3.sdk.dto.ObjectList;
import com.ksyun.ks3.sdk.dto.ObjectListOptions;
import com.ksyun.ks3.sdk.dto.PartList;
import com.ksyun.ks3.sdk.dto.PartListOptions;
import com.ksyun.ks3.sdk.dto.PresignedUrlOptions;
import com.ksyun.ks3.sdk.dto.UploadPartOptions;
import com.ksyun.ks3.sdk.dto.UploadPartResult;
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
	 * Upload a file into a object.
	 * @param bucketName Bucket name.
	 * @param objectKey The key of a object.
	 * @param file The file to upload.
	 * @param mimeType Mime type according to the type of the file.
	 * @param acl The access policy list of the object.
	 * @return The eTag of the object.
	 * @throws Exception
	 */
	public ObjectEtag putObject(String bucketName, String objectKey, File file, String mimeType,AccessControlList acl) throws Exception{
		return objectOperation.putObject(bucketName, objectKey, file, mimeType, acl);
	}
	
	/**
	 * Upload data by input stream.
	 * @param bucketName Bucket name.
	 * @param objectKey The key of a object.
	 * @param inputStream The input stream of the data.
	 * @param offset The offset of the input stream.
	 * @param length The length of the input stream.
	 * @param mimeType Mime type according to the type of the file.
	 * @return The eTag of the object.
	 * @throws Exception
	 */
	public ObjectEtag putObjectByInputStream(String bucketName, String objectKey, InputStream inputStream, 
			int offset, int length, String mimeType) throws Exception{
		return objectOperation.putObjectByInputStream(bucketName, objectKey, inputStream, offset, length, mimeType);
	}
	
	/**
	 * Upload data by input stream.
	 * @param bucketName Bucket name.
	 * @param objectKey The key of a object.
	 * @param inputStream The input stream of the data.
	 * @param offset The offset of the input stream.
	 * @param length The length of the input stream.
	 * @param mimeType Mime type according to the type of the file.
	 * @param acl The access policy list of the object.
	 * @return The eTag of the object.
	 * @throws Exception
	 */
	public ObjectEtag putObjectByInputStream(String bucketName, String objectKey, InputStream inputStream, 
			int offset, int length, String mimeType, AccessControlList acl) throws Exception{
		return objectOperation.putObjectByInputStream(bucketName, objectKey, inputStream, offset, length, mimeType,acl);
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
	 * Put text into a object.
	 * @param bucketName Bucket name.
	 * @param objectKey The key of a object.
	 * @param content The text of the object content.
	 * @param acl The access policy list of the object.
	 * @return The eTag of the object.
	 * @throws Exception
	 */
	public ObjectEtag putStringObject(String bucketName, String objectKey,String content, AccessControlList acl) throws Exception{	
		return objectOperation.putStringObject(bucketName, objectKey, content, acl);
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
	
	/**
	 * Initiate a multipart upload session.
	 * @param bucketName Bucket name.
	 * @param objectKey The key of a object.
	 * @return InitiateMultipartUploadResult instance.
	 * @throws Exception
	 */
	public InitiateMultipartUploadResult initiateMultipartUpload(String bucketName, String objectKey) throws Exception{
		return objectOperation.initiateMultipartUpload(bucketName, objectKey);
	}
	
	/**
	 * Upload a part of the object by options.
	 * @param uploadPartOptions UploadPartOptions instance.
	 * @return UploadPartResult instance.
	 * @throws Exception
	 */
	public UploadPartResult uploadPart(UploadPartOptions uploadPartOptions) throws Exception{
		return objectOperation.uploadPart(uploadPartOptions);
	}
	
	/**
	 * Complete a multipart upload session.
	 * @param completeMultipartUploadOptions CompleteMultipartUploadOptions instance.
	 * @return CompleteMultipartUploadResult CompleteMultipartUploadResult instacne.
	 * @throws Exception
	 */
	public CompleteMultipartUploadResult completeMultipartUpload(CompleteMultipartUploadOptions completeMultipartUploadOptions) throws Exception{
		return objectOperation.completeMultipartUpload(completeMultipartUploadOptions);
	}
	
	/**
	 * Abort a multipart upload session.This will disable the uploadId of current session.
	 * @param abortMultipartUploadOptions AbortMultipartUploadOptions instance
	 * @throws Exception
	 */
	public void abortMultipartUpload(AbortMultipartUploadOptions abortMultipartUploadOptions) throws Exception{
		objectOperation.abortMultipartUpload(abortMultipartUploadOptions);
	}
	
	/**
	 * Get a list of parts of a object of a multipart upload session.
	 * @param partListOptions partListOptions instance.
	 * @return PartList PartList instance
	 * @throws Exception
	 */
	public PartList getPartList(PartListOptions partListOptions) throws Exception{
		return objectOperation.getPartList(partListOptions);
	}	
	
	/**
	 * Get the url of thumbnail of a picture
	 * @param bucketName Bucket name.
	 * @param objectKey The key of a object.
	 * @param width The width of thumbnail.
	 * @param height The height of thumbnail.
	 * @param signed Whether with signature
	 * @param expires If you get a signed url,expires is expires.
	 * @return The url of thumbnail.
	 * @throws Exception
	 */
	public String getThumbnailUrl(String bucketName, String objectKey,int width,int height,boolean signed,int expires) throws Exception{
		return objectOperation.getThumbnailUrl(bucketName, objectKey, width, height, signed, expires);
	}
	
	/**
	 * Set the access control policy to a object.
	 * @param bucketName Bucket name.
	 * @param objectKey The key of object.
	 * @param acl The AccessControlPolicy instance that describes your policy.
	 * @throws Exception
	 */	
	public void setObjectACL(String bucketName, String objectKey,AccessControlList acl) throws Exception{
		objectOperation.setObjectACL(bucketName, objectKey, acl);
	}
	
	/**
	 * Get the access control policy of the specified object.
	 * @param bucketName Bucket name.
	 * @param objectKey The key of object.
	 * @return a AccessControlPolicy instance that describes your policy.
	 * @throws Exception
	 */
	public AccessControlPolicy getObjectACL(String bucketName, String objectKey) throws Exception{
		return objectOperation.getObjectACL(bucketName, objectKey);
	}
	
}
