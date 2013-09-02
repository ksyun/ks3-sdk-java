/**
 * @author: yangji
 * @data:   Apr 12, 2013
 */
package com.ksyun.ks3.sdk;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.ksyun.ks3.sdk.dto.Bucket;
import com.ksyun.ks3.sdk.dto.Credential;
import com.ksyun.ks3.sdk.dto.ObjectEntity;
import com.ksyun.ks3.sdk.dto.ObjectEtag;
import com.ksyun.ks3.sdk.dto.ObjectInfo;
import com.ksyun.ks3.sdk.dto.ObjectList;

/**
 * 该Sample展示了用户常用的基本功能。
 */
public class KS3SimpleSamples {
	
	private static final String ACCESS_KEY_ID = "<INSERT YOUR ACCESS_KEY_ID ID HERE>";
	private static final String ACCESS_KEY_SECRET = "<INSERT YOUR KSS ACCESS_KEY_SECRET HERE>";

	public static void main(String[] args) throws Exception {
		
		//type your ACCESS_KEY_ID and ACCESS_KEY_SECRET, and remove the next line
		System.exit(0);
		
		Credential credential = new Credential(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		KS3Client client = new KS3Client(credential);
		
		String bucketName = "bucket-"+ACCESS_KEY_ID.toLowerCase();
		
		String objectKey1 = "string_test";
		String objectValue = "the string to upload";
		
		String objectKey2 = "file_test";
		String filePath = "/tmp/test.xml";//Make sure this file exists.
		String fileSavePath = "/tmp/test_dl.xml";
		
		String objectKey3 = "file_test3";
		InputStream inputstream = new ByteArrayInputStream(new byte[1024]);
		int offset = 0;
		int length = 1024;
		String fileSavePathFromStream = "/tmp/test_dl_stream";
		
		//创建一个Bucket
		client.createBucket(bucketName);
		
		//展示Buckets
		List<Bucket> bucketList = client.getBucketList();
		showBucketList(bucketList);
		
		//上传一个文本文件
		ObjectEtag eTag1 = client.putStringObject(bucketName, objectKey1, objectValue);
		showObjectEtag(eTag1);
		
		//上传一个文件
		ObjectEtag eTag2 = client.putObject(bucketName, objectKey2, new File(filePath), "text/xml");
		showObjectEtag(eTag2);		
		
		//以流的形式上传一个文件
		ObjectEtag eTag3 = client.putObjectByInputStream(bucketName, objectKey3, inputstream, offset, length, "application/octet-stream");
		showObjectEtag(eTag3);
		
		//展示objects
		ObjectList objectList = client.getObjectList(bucketName);
		showObjectList(objectList);
		
		//下载文本文件
		ObjectEntity objEntity1 = client.getObject(bucketName, objectKey1);
		showFileContent(objEntity1);
		
		//下载文件
		ObjectEntity objEntity2 = client.getObject(bucketName, objectKey2);
		downloadFile(objEntity2,fileSavePath);
		
		//下载以流的形式上传的object
		ObjectEntity objEntity3 = client.getObject(bucketName, objectKey3);
		downloadFile(objEntity3,fileSavePathFromStream);
		
		//删除 object1
		client.deleteObject(bucketName, objectKey1);
		
		//删除 object2
		client.deleteObject(bucketName, objectKey2);
		
		//删除 object3
		client.deleteObject(bucketName, objectKey3);
		
		//删除 bucket
		client.deleteBucket(bucketName);
	}
	
	public static void showBucketList(List<Bucket> bucketList){
		System.out.println("[Bucket list]");
		for(Bucket bucket:bucketList){			
			System.out.println(bucket.getName()+":"+bucket.getCreationDate());
		}
		System.out.println();
	}
	
	public static void showObjectEtag(ObjectEtag eTag){
		System.out.println("[Object eTag]");
		System.out.println(eTag.toString());
		System.out.println();
	}
	
	public static void showObjectList(ObjectList objectList){
		System.out.println("[Object list]");
		System.out.println("bucketName:"+objectList.getBucketName());
		System.out.println("maxKeys:"+objectList.getMaxKeys());
		System.out.println("marker:"+objectList.getMarker());
		System.out.println("prefix:"+objectList.getPrefix());
		System.out.println("isTruncated:"+objectList.getIsTruncated());
		
		List<ObjectInfo> objectInfoList = objectList.getObjectInfoList();
		for(ObjectInfo objectInfo:objectInfoList){
			System.out.println("  objectKey:"+objectInfo.getKey());	
			System.out.println("    ownerDisplayName:"+objectInfo.getOwnerDisplayName());	
			System.out.println("    ownerID:"+objectInfo.getOwnerID());	
			System.out.println("    eTag:"+objectInfo.geteTag());	
			System.out.println("    size:"+objectInfo.getSize());
			System.out.println("    lastModified:"+objectInfo.getLastModified());			
		}		
	}
	
	public static void showFileContent(ObjectEntity objEntity){
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(objEntity.getObjectValue()));		
			StringBuffer buffer = new StringBuffer();
			String line = "";
			
				while ((line = in.readLine()) != null) {
					buffer.append(line);
				}
				
			System.out.println("[Object content]");
			System.out.println(buffer.toString());
		} catch (IOException e) {
			System.out.println("Object value can't be parsed to string with UTF-8 encoding.");
		}	
		System.out.println();
	}
	
	public static void downloadFile(ObjectEntity objEntity,String fileSavePath){
		
		FileOutputStream fos;
		InputStream is = objEntity.getObjectValue();
		System.out.println("[Download object]");
		try
		{
			fos = new FileOutputStream(fileSavePath);  
			int ch = 0;  
			while((ch=is.read()) != -1){  
	            fos.write(ch);  
	        }
			fos.close();  
	        is.close();
	        System.out.println("Download succeeds:"+fileSavePath);
		}
		catch(Exception e){			
			System.out.println("Download failed.");
		}
		System.out.println();		
	}

}
