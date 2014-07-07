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
 *分块上传解决方案（二）：
 *
 *执行方式：
 *	对于已知大小或未知大小的数据源，逐块（每次读取BufferSize大小）读入内存。当某块被读入内存后，立刻激活某个线程进行上传。
 *
 *适用条件：
 *	更优化的内存使用，可以改善方案(一)中一次性读入内存的可能引起内存不足的问题；
 *  该策略可以通过设置offset不从流的起点开始读取;
 *	可以处理未知大小的数据源。
 *
 *注意：
 *	该策略不能立即激活全部可用线程，线程会随着数据的逐块加载逐一激活上传任务，池内线程全部激活的时间取决于前一块Buffer的IO时间；
 *	当Buffer较大时，开启的线程数请不要超过连接数，否则可能引起连接池没有可用空闲连接而导致性能降低。
 */
public class KS3MultipartSampleSolution2 {

	private static final String ACCESS_KEY_ID = "<INSERT YOUR ACCESS_KEY_ID ID HERE>";
	private static final String ACCESS_KEY_SECRET = "<INSERT YOUR KSS ACCESS_KEY_SECRET HERE>";

	private static final int BUFFER_SIZE = 5 * 1024 * 1024; // Buffer大小
	private static final int CONCURRENCIES = 6; // 并发数

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
		multipartUploadObejctByMultithreading(client, inputstream, offset,bucketName, objectKey,mimeType);
		
		// 删除Object
		client.deleteObject(bucketName, objectKey);
		
		// 删除Bucket
		client.deleteBucket(bucketName);		

	}

	public static void multipartUploadObejctByMultithreading(KS3Client client,InputStream inputstream, 
			int offset,String bucketName,String objectKey,String mimeType) throws Exception{
		
		// 偏移量不能为负数
		if (offset < 0)
			throw new IllegalArgumentException("Illegal offset.");
		
		// 处理偏移量
		try {
			inputstream.skip(offset);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Skip data failed.");
		}

		// 初始化一个指定数量线程的线程池，并构造一个线程安全到list承载上传列表的相关信息
		ExecutorService threadPool = Executors.newFixedThreadPool(CONCURRENCIES);
		List<UploadPart> partList = Collections.synchronizedList(new ArrayList<UploadPart>());

		// 初始化一个上传会话
		InitiateMultipartUploadResult imur = client.initiateMultipartUpload(bucketName, objectKey,mimeType);
		String uploadId = imur.getUploadId();	
		
		AbortMultipartUploadOptions abortMultipartUploadOptions = new AbortMultipartUploadOptions(bucketName, objectKey, uploadId);

		// 定义变量
		int partCount = 0;
		int currentSize = 0;
		boolean isLastPart = false;
		
		// 一直读，知道读到数据源结尾为止
		while(true){
			
			InputStream inputstreamPart = null;
			currentSize = BUFFER_SIZE;
			
			try {
				inputstreamPart = getInputStreamPart(inputstream);
				
			} catch (EndOfStreamException e) {
				
				int dataLength = e.getBufferSize();
				if(dataLength==0)
					break;// 说明上一块正好读完，这块是空的,直接break
				else{
					isLastPart = true;//
					currentSize = dataLength;
					inputstreamPart = e.getLastInputStream();	
				}												
				
			} catch (IOException e) {
				client.abortMultipartUpload(abortMultipartUploadOptions);
				throw new Exception("[Upload failed.]");
			}
			
			partCount++;
			threadPool.execute(new UploadPartThread(bucketName,objectKey, uploadId, partCount, currentSize, inputstreamPart,client, partList));
			
			if(isLastPart)
				break;
		}
		
		
		// 等待全部线程执行结束
		threadPool.shutdown();
		while (!threadPool.isTerminated()) {
			threadPool.awaitTermination(5, TimeUnit.SECONDS);
        }
		
		// 上传失败
		if (partList.size() != partCount){
			client.abortMultipartUpload(abortMultipartUploadOptions);
			throw new Exception("[Upload failed.]");
			
		}
		// 上传成功
		else{		
			
			// 把partNum排序
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

	// 上传线程
	private static class UploadPartThread implements Runnable {

		private String bucketName;
		private String objectKey;
		private String uploadId;
		private int partNumber;
		private long partSize;
		private InputStream inputStream;		
		private KS3Client client;
		private List<UploadPart> partList;

		public UploadPartThread(String bucketName,String objectKey, String uploadId, int partNumber,
				long partSize, InputStream inputStream, KS3Client client,List<UploadPart> partList) {
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
				
				// 上传
				UploadPartOptions options = new UploadPartOptions(bucketName,
						objectKey, uploadId, partNumber, partSize, inputStream);
				
				System.out.println("[Uploading part "+partNumber+" ......]");
				UploadPartResult result = client.uploadPart(options);
				System.out.println("[Part "+partNumber+" uploaded]");
				

				// 添加上传成功到UploadPart
				partList.add(result.getUploadPart());

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.safelyCloseInputStream(inputStream);
			}
		}
	}
	
	private static InputStream getInputStreamPart(InputStream inStream)throws IOException,EndOfStreamException{  
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
        byte[] buff = new byte[BUFFER_SIZE];  
        int rc = 0;  

        rc = inStream.read(buff, 0, BUFFER_SIZE);
        
        // 上一块正好读完
        if(rc<=0)
        	throw new EndOfStreamException(0, null);
        
        swapStream.write(buff, 0, rc);        
        byte[] in2b = swapStream.toByteArray();         
        InputStream inputStream = (InputStream)new ByteArrayInputStream(in2b);
        
        // 这一块是最后一块
        if(rc<BUFFER_SIZE)
        	throw new EndOfStreamException(rc, inputStream);
        
        return inputStream;
    }
}

class EndOfStreamException extends Exception{

	private static final long serialVersionUID = 5739396849577521997L;
	private int bufferSize;
	private InputStream inputStream;
	public EndOfStreamException(int bufferSize,InputStream inputStream){
		super();
		this.bufferSize = bufferSize;
		this.inputStream = inputStream;
	}
	public int getBufferSize(){
		return this.bufferSize;
	}
	public InputStream getLastInputStream(){
		return this.inputStream;
	}
}



