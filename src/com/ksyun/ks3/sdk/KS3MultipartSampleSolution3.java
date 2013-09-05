/**
 * @author: yangji
 * @data:   Aug 21, 2013
 */
package com.ksyun.ks3.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.ksyun.ks3.sdk.dto.AbortMultipartUploadOptions;
import com.ksyun.ks3.sdk.dto.CompleteMultipartUploadOptions;
import com.ksyun.ks3.sdk.dto.Credential;
import com.ksyun.ks3.sdk.dto.InitiateMultipartUploadResult;
import com.ksyun.ks3.sdk.dto.UploadPart;
import com.ksyun.ks3.sdk.dto.UploadPartOptions;
import com.ksyun.ks3.sdk.dto.UploadPartResult;
import com.ksyun.ks3.sdk.tools.IOUtils;

/**
 *分块上传解决方案（三）：
 *
 *执行方式：
 *	多句柄对文件同时读同时上传，性能最好。
 *
 *适用条件：
 *	只能传输文件。
 *  只能传输完整文件，不能从某一部分开始传输，灵活性不高。
 *
 *注意：
 *	只能传输文件，不能以流的形式传入。
 */
public class KS3MultipartSampleSolution3 {

	private static final String ACCESS_KEY_ID = "<INSERT YOUR ACCESS_KEY_ID ID HERE>";
	private static final String ACCESS_KEY_SECRET = "<INSERT YOUR KSS ACCESS_KEY_SECRET HERE>";

	private static final int PART_SIZE = 5 * 1024 * 1024; // 多线程同时上传时，每块的大小
	private static final int CONCURRENCIES = 10; // 并发数

	public static void main(String[] args) throws Exception {

		// type your ACCESS_KEY_ID and ACCESS_KEY_SECRET, and remove the next line
		System.exit(0);

		Credential credential = new Credential(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		KS3Client client = new KS3Client(credential);
		
		// 创建一个Bucket
		String bucketName = "bucket-"+ACCESS_KEY_ID.toLowerCase();
		client.createBucket(bucketName);
		
		// 定义Object,并指向一个文件
		String objectKey = "test.zip";
		String filePath = "/tmp/" + objectKey;
		String mimeType = "application/octet-stream";
		File fileToUpload = new File(filePath);
		
		// 分块上传
		multipartUploadObejctByMultithreading(client, fileToUpload, bucketName, objectKey,mimeType);
		
		// 删除Object
		client.deleteObject(bucketName, objectKey);
		
		// 删除Bucket
		client.deleteBucket(bucketName);		

	}

	public static void multipartUploadObejctByMultithreading(KS3Client client,
			File file, String bucketName,String objectKey,String mimeType) throws Exception {

		// 要上传到文件至少要一个Part的大小
		if (file.length() < PART_SIZE)
			throw new IllegalArgumentException(
					"The object you are trying to upload is not large as a PART_SIZE.");		

		// 计算上传的块的数量
		int partCount = (int) (file.length() / PART_SIZE);
		if (file.length() % PART_SIZE != 0)
			partCount++;

		// 初始化一个指定数量线程的线程池，并构造一个线程安全到list承载上传列表的相关信息
		ExecutorService threadPool = Executors.newFixedThreadPool(CONCURRENCIES);
		List<UploadPart> partList = Collections.synchronizedList(new ArrayList<UploadPart>());

		// 初始化一个上传会话
		InitiateMultipartUploadResult imur = client.initiateMultipartUpload(bucketName, objectKey,mimeType);
		String uploadId = imur.getUploadId();		
		
		
		System.out.println("[Upload start.]");

		//依次启动在线程池内启动线程
		for (int i = 0; i < partCount; i++) {
			
			long start = PART_SIZE * i;
			long currentPartSize = PART_SIZE < file.length() - start ? PART_SIZE : file.length() - start;

			InputStream inputstream = new FileInputStream(file);
			
			threadPool.execute(new UploadPartThread(start, bucketName,
					objectKey, uploadId, i+1, currentPartSize, inputstream,client, partList));
		}		
		
		//等待全部线程执行结束
		threadPool.shutdown();
		while (!threadPool.isTerminated()) {
			threadPool.awaitTermination(5, TimeUnit.SECONDS);
        }
		
		//上传失败
		if (partList.size() != partCount){
			AbortMultipartUploadOptions abortMultipartUploadOptions = new AbortMultipartUploadOptions(bucketName, objectKey, uploadId);
			client.abortMultipartUpload(abortMultipartUploadOptions);
			throw new Exception("[Upload failed.]");
			
		}
		//上传成功
		else{		
			
			//把partNum排序
	        Collections.sort(partList, new Comparator<UploadPart>(){
	            public int compare(UploadPart arg0, UploadPart arg1) {
	            	UploadPart part1= arg0;
	            	UploadPart part2= arg1;

	                return part1.getPartNumber() - part2.getPartNumber();
	            }  
	        });
			
			CompleteMultipartUploadOptions completeMultipartUploadOptions = new CompleteMultipartUploadOptions(bucketName, objectKey, uploadId, partList);
			client.completeMultipartUpload(completeMultipartUploadOptions);
			System.out.println("[Upload Suceess.]");		
		}		
	}

	//上传线程
	private static class UploadPartThread implements Runnable {

		private long start;
		private String bucketName;
		private String objectKey;
		private String uploadId;
		private int partNumber;
		private long partSize;
		private InputStream inputStream;		
		private KS3Client client;
		private List<UploadPart> partList;

		public UploadPartThread(long start, String bucketName,
				String objectKey, String uploadId, int partNumber,
				long partSize, InputStream inputStream, KS3Client client,
				List<UploadPart> partList) {
			this.start = start;
			this.bucketName = bucketName;
			this.objectKey = objectKey;
			this.uploadId = uploadId;
			this.partNumber = partNumber;
			this.partSize = partSize;
			this.inputStream = inputStream;
			this.client = client;
			this.partList = partList;
		}

		@Override
		public void run() {

			try {						
				
				// 跳过已经传输过的部分
				inputStream.skip(start);

				// 上传
				UploadPartOptions options = new UploadPartOptions(bucketName,
						objectKey, uploadId, partNumber, partSize, inputStream);
				
				System.out.println("[Part "+partNumber+" uploading……]");
				UploadPartResult result = client.uploadPart(options);
				System.out.println("[Part "+partNumber+" uploaded.]");
				

				// 添加上传成功到UploadPart
				partList.add(result.getUploadPart());

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.safelyCloseInputStream(inputStream);
			}
		}
	}
}
