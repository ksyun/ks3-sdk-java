/**
 * @author: yangji
 * @data:   Sep 4, 2013
 */
package com.ksyun.ks3.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ksyun.ks3.sdk.KS3Client;
import com.ksyun.ks3.sdk.dto.Credential;
import com.ksyun.ks3.sdk.dto.ObjectEntity;
import com.ksyun.ks3.sdk.dto.ObjectGetOptions;
import com.ksyun.ks3.sdk.dto.ObjectMetadata;

/**
 * KS3 Java SDK提供分块下载和断点续传基础支持。
 * KS3服务的断电续传遵循HTTP标准协议：
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
 * 使用SDK可以设置请求下载Object时的range参数，从而每次下载部分数据。
 * 当连接发生中断时，只需重新下载剩余区块即可，无需重复从头开始下载。
 * 
 */
public class KS3MutipartDownloadSample {
	
	private static final String ACCESS_KEY_ID = "<INSERT YOUR ACCESS_KEY_ID ID HERE>";
	private static final String ACCESS_KEY_SECRET = "<INSERT YOUR KSS ACCESS_KEY_SECRET HERE>";
	
	private static Integer PART_SIZE = 5*1024*1024;//下载的单块大小

	public static void main(String[] args) throws Exception {
		
		//type your ACCESS_KEY_ID and ACCESS_KEY_SECRET, and remove the next line
		System.exit(0);
		
		Credential credential = new Credential(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		KS3Client client = new KS3Client(credential);
		
		String bucketName = "bucket-"+ACCESS_KEY_ID.toLowerCase();
		String objectKey = "test.zip";
		
		//创建一个Bucket
		client.createBucket(bucketName);
		
		// 上传文件（建议至少大于一个PART_SIZE的大小）
		File objectToUpload = new File("/tmp/"+objectKey);
		int offset = 0;
		Long fileSize  = objectToUpload.length();
		System.out.println("正在上传……");
		client.putObjectByInputStream(bucketName, objectKey, new FileInputStream(objectToUpload), offset, fileSize.intValue(), null);
		
		
		// 取得总大小
		ObjectMetadata metadate = client.headObject(bucketName, objectKey);
		Integer length = Integer.valueOf(metadate.getMetadata().get("Content-Length"));
		String requestId = metadate.getMetadata().get("x-kss-request-id");
		
		// 计算上传的块的数量
		int partCount = (int) (length / PART_SIZE);
		if (length % PART_SIZE != 0)
			partCount++;
		
		//下载保存的问题
		File target = new File("/tmp/"+"save_"+objectKey);
		if(!target.exists())
			target.createNewFile();
		else
			System.out.println("文件已经存在");
		
		//逐块下载文件
		for (int i = 0; i < partCount; i++) {
			
			long start = PART_SIZE * i;
			long currentPartSize = PART_SIZE < length - start ? PART_SIZE : length - start;			
			
			ObjectGetOptions options = new ObjectGetOptions(bucketName, objectKey);
			options.setRange(start, start + currentPartSize-1);
			
			//下载块
			System.out.println("第"+(i+1)+"块正在下载……[RequestId:"+requestId+"]");
			ObjectEntity entity = client.getObejctByOptions(options);
			System.out.println("第"+(i+1)+"块下载完成……[RequestId:"+requestId+"]");
			
			
			//此处需要对已经下载的块(或Range)和RequestId进行持久化，以保证下载终止后可以继续下载还未开始的区块。
			
			
			//每下载一部分后，写入outputstream,使用追加模式
			writeStream(entity.getObjectValue(), new FileOutputStream(target,true));
			
		}
		
		System.out.println("文件下载成功");
			
		//删除 object
		client.deleteObject(bucketName, objectKey);
		
		//删除 bucket
		client.deleteBucket(bucketName);

	}
	
	//读取inputstream，并写入outputstream
	public static void writeStream(InputStream inputStream, OutputStream outputStream){
		
		try {            
            byte[] byteArr = new byte[1024];
            //读取的字节数
            int readCount = inputStream.read(byteArr);
            //如果已到达文件末尾，则返回-1
            while (readCount != -1) {
            	outputStream.write(byteArr, 0, readCount);
                readCount = inputStream.read(byteArr);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
		}catch(IOException e){
			e.printStackTrace();
		}		
	}
}
