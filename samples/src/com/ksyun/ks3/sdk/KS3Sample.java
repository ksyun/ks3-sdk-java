package com.ksyun.ks3.sdk;

import java.io.BufferedReader;
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
	


public class KS3Sample {
	
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
		
		//Create a bucket
		client.createBucket(bucketName);
		
		//Show buckets
		List<Bucket> bucketList = client.getBucketList();
		showBucketList(bucketList);
		
		//Put a object by string
		ObjectEtag eTag1 = client.putStringObject(bucketName, objectKey1, objectValue);
		showObjectEtag(eTag1);
		
		//Put a object by file
		ObjectEtag eTag2 = client.putObject(bucketName, objectKey2, new File(filePath), "text/xml");
		showObjectEtag(eTag2);		
		
		//Show objects
		ObjectList objectList = client.getObjectList(bucketName);
		showObjectList(objectList);
		
		//Get the string object
		ObjectEntity objEntity1 = client.getObject(bucketName, objectKey1);
		showFileContent(objEntity1);
		
		//Download the file object
		ObjectEntity objEntity2 = client.getObject(bucketName, objectKey2);
		downloadFile(objEntity2,fileSavePath);
		
		//Delete object1
		client.deleteObject(bucketName, objectKey1);
		
		//Delete object2
		client.deleteObject(bucketName, objectKey2);
		
		//Delete a bucket
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
