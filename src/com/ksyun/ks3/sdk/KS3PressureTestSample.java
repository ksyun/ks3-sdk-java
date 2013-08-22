/**
 * @author: yangji
 * @data:   Aug 21, 2013
 */
package com.ksyun.ks3.sdk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.ksyun.ks3.sdk.dto.Credential;

/**
 * 连续&并发上传文件压力测试
 * 
 * 1.创建Bucket
 * 2.多线程持续上传文件
 * 3.连续删除文件
 * 4.删除Bucket
 * 
 */
public class KS3PressureTestSample {
	
	private static final String ACCESS_KEY_ID = "<INSERT YOUR ACCESS_KEY_ID ID HERE>";
	private static final String ACCESS_KEY_SECRET = "<INSERT YOUR KSS ACCESS_KEY_SECRET HERE>";
	
	private static final Integer FILE_NUMBER = 5000;//5000个文件
	private static final Integer CONCURRENCIES = 40;//40个线程并发。并发数建议小于连接池默认连接数（50）
	
	public static void main(String[] args) throws Exception {
		
		// type your ACCESS_KEY_ID and ACCESS_KEY_SECRET, and remove the next line
		System.exit(0);

		Credential credential = new Credential(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		KS3Client client = new KS3Client(credential);	
		
		System.out.println("[任务开始]");
		
		// 创建一个Bucket
		String bucketName = "bucket-"+ACCESS_KEY_ID.toLowerCase();
		client.createBucket(bucketName);
		System.out.println("[创建Bucket:"+bucketName+"]");
		
		//定义线程池
		ExecutorService threadPool = Executors.newFixedThreadPool(CONCURRENCIES);
		
		//fileList 记录下有哪些文件被上传了，一会方便删除
		List<String> fileList = Collections.synchronizedList(new ArrayList<String>());
		
		//40个连续并发上传共5000个文件
		System.out.println("[开始上传Object......]");
		for(int i=0;i<FILE_NUMBER;i++){
			threadPool.execute(new UploadFileThread(bucketName, client,fileList));
		}
		
		//等待全部线程执行结束
		threadPool.shutdown();
		while (!threadPool.isTerminated()) {
			threadPool.awaitTermination(5, TimeUnit.SECONDS);
        }
		
		System.out.println("[上传完毕]");
		
		System.out.println("\n[删除Object（可能会花费一些时间）]");		
		for(int i=0;i<fileList.size();i++){
			String randomKey = fileList.get(i);
			try {
				client.deleteObject(bucketName, randomKey);	
				System.out.println("[删除成功:"+randomKey+"]");
			} catch (Exception e) {
				//如果有极个别删除失败的情况，说明随机生成的ObjectKey有重复，之前已经被删过一次了，再删就会提示无该Object
				//这种情况几乎很少发生，但存在理论上的可能性
				System.out.println("[删除失败:"+randomKey+"]");
			}				
		}		
		
		System.out.println("[正在删除Bucket......]");
		
		try {
			client.deleteBucket(bucketName);
		} catch (Exception e) {
			System.out.println("[删除Bucket失败][请检查Bucket中是否残留有文件]");
		}
		
		
		System.out.println("[任务结束]");
		
		float successPercent = ((float)fileList.size()/FILE_NUMBER)*100;
		
		System.out.println("[上传成功率:"+successPercent + "%]");
		
	}
	
	private static class UploadFileThread implements Runnable{

		private String bucketName;
		private KS3Client client;	
		private List<String> fileList;
			
		
		public UploadFileThread(String bucketName,KS3Client client,List<String> fileList) {
			this.bucketName = bucketName;
			this.client = client;
			this.fileList = fileList;
		}

		@Override
		public void run() {
			
			//随机文件名&随机文本内容
			String randomObjectKey = randomString(20);
			String randomContent = randomString(50);
			fileList.add(randomObjectKey);			
			
			try {
				System.out.println("[正在上传:" + randomObjectKey + "......]");
				client.putStringObject(bucketName, randomObjectKey, randomContent);
				System.out.println("[上传成功:" + randomObjectKey + "]");
				
				System.out.println("[当前上传成功个数:" + fileList.size() + "]");
			} catch (Exception e) {				
				e.printStackTrace();
			}			
			
		}		
	}
	
	public static String randomString(int length){
    
		 String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		 StringBuffer sb = new StringBuffer();
		 Random random = new Random();
		 
		 for (int i = 0; i < length; i++){
			 sb.append(allChar.charAt(random.nextInt(allChar.length())));
		 }
	
		 return sb.toString();
    }

}
