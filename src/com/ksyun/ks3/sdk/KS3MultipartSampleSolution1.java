/**
 * @author: yangji
 * @data:   Aug 21, 2013
 */
package com.ksyun.ks3.sdk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
 *分块上传解决方案（一）：
 *
 *执行方式：
 *	对于已知大小的数据源(或已知期望传输大小)，一次性将数据加载至内存，然后多线程同时上传。
 *
 *适用条件：
 *	该策略可以保证足够多的线程同时进行上传,可以缩短上传时间（从I/O完成开始计算时间）；
 *  该策略可以通过设置offset不从流的起点开始读取,只读取源数据的一部分；
 *	当且仅当单个连接的连接速度为瓶颈时，建议使用此方法。
 *
 *注意：
 *	内存较小且单个数据源较大时慎用,经测试，当JVM可用内存较小时，读取较大文件，可能会引发java.lang.OutOfMemoryError;
 *	当PART_SIZE较大时，开启的线程数请不要超过连接数，否则可能引起连接池没有可用空闲连接而导致性能降低；
 *	该方法需要已知数据源的大小。
 */
public class KS3MultipartSampleSolution1 {

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
		
		// 定义Object,并指向一个文件，进而生成一个“流”
		String objectKey = "test.zip";
		String filePath = "/tmp/" + objectKey;
		String mimeType = "application/octet-stream";
		File fileToUpload = new File(filePath);
		

		//得到一个“流”
		InputStream inputstream = new FileInputStream(fileToUpload);	
		
		//偏移量
		int offset = 0;
		
		// 分块上传
		multipartUploadObejctByMultithreading(client, inputstream, offset, fileToUpload.length(), bucketName, objectKey,mimeType);
		
		// 删除Object
		client.deleteObject(bucketName, objectKey);
		
		// 删除Bucket
		client.deleteBucket(bucketName);		

	}

	public static void multipartUploadObejctByMultithreading(KS3Client client,
			InputStream inputstream, long offset, long length, String bucketName,
			String objectKey,String mimeType) throws Exception {

		// 要上传到文件至少要一个Part的大小
		if (length < PART_SIZE)
			throw new IllegalArgumentException(
					"The object you are trying to upload is not large as a PART_SIZE.");

		// 起始位置不能在length之后
		if (offset < 0 || offset > length)
			throw new IllegalArgumentException("Illegal arguments");
		
		//得到实际的length
		length = length - offset;
		
		//处理偏移量
		try {
			inputstream.skip(offset);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Skip data failed.");
		}

		// 计算上传的块的数量
		int partCount = (int) (length / PART_SIZE);
		if (length % PART_SIZE != 0)
			partCount++;

		// 初始化一个指定数量线程的线程池，并构造一个线程安全到list承载上传列表的相关信息
		ExecutorService threadPool = Executors.newFixedThreadPool(CONCURRENCIES);
		List<UploadPart> partList = Collections.synchronizedList(new ArrayList<UploadPart>());

		// 初始化一个上传会话
		InitiateMultipartUploadResult imur = client.initiateMultipartUpload(bucketName, objectKey,mimeType);
		String uploadId = imur.getUploadId();
		
		// 一次性读到内存，然后多线程同时上传
		CopyInputStream copy = new CopyInputStream(inputstream);
		
		System.out.println("[Upload start.]");

		//依次启动在线程池内启动线程
		for (int i = 0; i < partCount; i++) {
			
			long start = PART_SIZE * i;
			long currentPartSize = PART_SIZE < length - start ? PART_SIZE : length - start;

			InputStream inputstreamCopy = copy.getCopy();
			
			threadPool.execute(new UploadPartThread(start, bucketName,
					objectKey, uploadId, i+1, currentPartSize, inputstreamCopy,client, partList));
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

class CopyInputStream{
	
    private InputStream _is;
    private ByteArrayOutputStream _copy = new ByteArrayOutputStream();

    public CopyInputStream(InputStream is){       
    	_is = is;
        try{
            copy();
        }
        catch(IOException ex){
            // do nothing
        }
    }

    private int copy() throws IOException{
        int read = 0;
        int chunk = 0;
        byte[] data = new byte[256];

        while(-1 != (chunk = _is.read(data))){
            read += data.length;
            _copy.write(data, 0, chunk);
        }
        return read;
    }

    public InputStream getCopy(){
        return (InputStream)new ByteArrayInputStream(_copy.toByteArray());
    }
}
