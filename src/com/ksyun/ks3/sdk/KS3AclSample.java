/**
 * @author: yangji
 * @data:   Sep 2, 2013
 */
package com.ksyun.ks3.sdk;

import com.ksyun.ks3.sdk.KS3Client;
import com.ksyun.ks3.sdk.dto.AccessControlList;
import com.ksyun.ks3.sdk.dto.AccessControlPolicy;
import com.ksyun.ks3.sdk.dto.Credential;

/**
 * 该Sample展示了Bucket和Object的ACL的相关功能。
 */
public class KS3AclSample {
	
	private static final String ACCESS_KEY_ID = "<INSERT YOUR ACCESS_KEY_ID ID HERE>";
	private static final String ACCESS_KEY_SECRET = "<INSERT YOUR KSS ACCESS_KEY_SECRET HERE>";

	public static void main(String[] args) throws Exception {
		
		//type your ACCESS_KEY_ID and ACCESS_KEY_SECRET, and remove the next line
		System.exit(0);
		
		Credential credential = new Credential(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		KS3Client client = new KS3Client(credential);
		
		String bucketName = "bucket-"+ACCESS_KEY_ID.toLowerCase();
		
		//创建一个Bucket
		client.createBucket(bucketName);
		
		//测试bucket的acl
		testSetBucketAcl(client, bucketName);
		
		//测试带有acl的上传功能
		testPutObjectWithACL(client,bucketName);
		
		//测试修改Object的acl的功能
		testSetObjectACL(client,bucketName);
		
		//删除 bucket
		client.deleteBucket(bucketName);

	}
	
	//测试bucket的acl
	public static void testSetBucketAcl(KS3Client client,String bucketName) throws Exception{
		
		//查看acl
		AccessControlPolicy acl = client.getBucketACL(bucketName);
		System.out.println("Default ACL of "+bucketName+":"+acl.getGrant());
		
		//修改acl并查看
		client.setBucketACL(bucketName, AccessControlList.READONLY);
		acl = client.getBucketACL(bucketName);
		System.out.println("New ACL of "+bucketName+":"+acl.getGrant());
		
		//为了数据的安全，还原默认设置
		client.setBucketACL(bucketName, AccessControlList.PRIVATE);		
	}
	
	//对于新上传的Object，可以直接在上传时设置ACL属性
	public static void testPutObjectWithACL(KS3Client client,String bucketName) throws Exception{
		
		//定义三个objectKey
		String object1 = "object1";
		String object2 = "object2";
		String object3 = "object3";
		
		//object的value
		String testContent = "testContent";
		
		//使用带权限的上传接口上传Object到S3服务器
		client.putStringObject(bucketName, object1, testContent, AccessControlList.PRIVATE);
		client.putStringObject(bucketName, object2, testContent, AccessControlList.READONLY);
		client.putStringObject(bucketName, object3, testContent, AccessControlList.PUBLIC);
		
		//查看三个Object的Object
		AccessControlPolicy acl1 = client.getObjectACL(bucketName, object1);
		AccessControlPolicy acl2 = client.getObjectACL(bucketName, object2);
		AccessControlPolicy acl3 = client.getObjectACL(bucketName, object3);
		
		//输出acl
		System.out.println("ACL List:");
		System.out.println("  "+object1+":"+acl1.getGrant());
		System.out.println("  "+object1+":"+acl2.getGrant());
		System.out.println("  "+object1+":"+acl3.getGrant());
		
		//删除Object
		client.deleteObject(bucketName, object1);
		client.deleteObject(bucketName, object2);
		client.deleteObject(bucketName, object3);		
	}
	
	//对于已经上传过的Object，可以重新设置该Object的属性
	public static void testSetObjectACL(KS3Client client,String bucketName) throws Exception{
		
		//定义三个objectKey
		String object1 = "object1";
		String object2 = "object2";
		String object3 = "object3";
		
		//object的value
		String testContent = "testContent";
		
		//上传Object
		client.putStringObject(bucketName, object1, testContent);
		client.putStringObject(bucketName, object2, testContent);
		client.putStringObject(bucketName, object3, testContent);
		
		//查看三个Object的Object
		AccessControlPolicy acl1 = client.getObjectACL(bucketName, object1);
		AccessControlPolicy acl2 = client.getObjectACL(bucketName, object2);
		AccessControlPolicy acl3 = client.getObjectACL(bucketName, object3);
		
		//输出acl
		System.out.println("Default ACL List:");
		System.out.println("  "+object1+":"+acl1.getGrant());
		System.out.println("  "+object1+":"+acl2.getGrant());
		System.out.println("  "+object1+":"+acl3.getGrant());
		
		//修改acl属性
		client.setObjectACL(bucketName, object1, AccessControlList.PRIVATE);
		client.setObjectACL(bucketName, object2, AccessControlList.READONLY);
		client.setObjectACL(bucketName, object3, AccessControlList.PUBLIC);
		
		//重新查看三个Object的Object
		acl1 = client.getObjectACL(bucketName, object1);
		acl2 = client.getObjectACL(bucketName, object2);
		acl3 = client.getObjectACL(bucketName, object3);
		
		//重新输出acl
		System.out.println("New ACL List:");
		System.out.println("  "+object1+":"+acl1.getGrant());
		System.out.println("  "+object1+":"+acl2.getGrant());
		System.out.println("  "+object1+":"+acl3.getGrant());
		
		//删除Object
		client.deleteObject(bucketName, object1);
		client.deleteObject(bucketName, object2);
		client.deleteObject(bucketName, object3);	
		
	}

}
