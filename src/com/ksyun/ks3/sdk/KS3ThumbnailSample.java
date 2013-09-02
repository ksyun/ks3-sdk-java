/**
 * @author: yangji
 * @data:   Aug 31, 2013
 */
package com.ksyun.ks3.sdk;

import java.io.File;

import com.ksyun.ks3.sdk.KS3Client;
import com.ksyun.ks3.sdk.dto.AccessControlList;
import com.ksyun.ks3.sdk.dto.AccessControlPolicy;
import com.ksyun.ks3.sdk.dto.Credential;

/**
 * 该Sample展示了生成缩略图链接的相关功能。
 * 对于非公开可读的Object生成的缩略图，无法使用CDN进行加速，条件允许下推荐将图片权限设置为公开可读以获得CDN的支持。
 */
public class KS3ThumbnailSample {

	private static final String ACCESS_KEY_ID = "<INSERT YOUR ACCESS_KEY_ID ID HERE>";
	private static final String ACCESS_KEY_SECRET = "<INSERT YOUR KSS ACCESS_KEY_SECRET HERE>";

	public static void main(String[] args) throws Exception {
		
		//type your ACCESS_KEY_ID and ACCESS_KEY_SECRET, and remove the next line
		System.exit(0);
		
		Credential credential = new Credential(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		KS3Client client = new KS3Client(credential);
		
		String bucketName = "bucket-"+ACCESS_KEY_ID.toLowerCase();
		String privatePic = "privatePic";
		String publicPic  = "publicPic";
		
		File file = new File("/tmp/test.png");//Make sure this file exists.
		
		//创建一个Bucket
		client.createBucket(bucketName);
		
		//上传不同访问权限的图片(MIME与图片格式相对应)
		client.putObject(bucketName, privatePic, file, "image/png", AccessControlList.PRIVATE);	
		client.putObject(bucketName, publicPic, file, "image/png", AccessControlList.READONLY);
		
		//设置高度和宽度
		int height = 200;
		int width = 200;
		
		//取得外链
		String privateUrl = getThumbnailUrl(client, bucketName, privatePic, width, height);
		String publicUrl = getThumbnailUrl(client, bucketName, publicPic, width, height);
		
		//打印
		System.out.println("privateUrl:\n"+privateUrl);	
		System.out.println("publicUrl:\n"+publicUrl);
		
		System.out.println("请在测试完url之后按任意键执行后续清理语句.");
		System.in.read();
		
		//删除 object
		client.deleteObject(bucketName, privatePic);
		client.deleteObject(bucketName, publicPic);		
		
		//删除 bucket
		client.deleteBucket(bucketName);
	
	}
	
	public static String getThumbnailUrl(KS3Client client,String bucketName,String objectKey,int width,int height) throws Exception{
		
		//查看object的访问属性
		AccessControlPolicy acl = client.getObjectACL(bucketName, objectKey);
		
		//对于私有的图片，需要使用含有签名的链接来访问缩略图
		if(acl.equals(AccessControlList.PRIVATE)){
			int expires = 30*24*60*60;//私有图片通过签名访问，必须设置缩略图过期时间
			return client.getThumbnailUrl(bucketName, objectKey, width, height, true, expires);
		}
		//对于公开可读或者公开读写到图片，可以生成不含有签名到链接，速度较快，推荐将图片设置成公开可读
		else if(acl.equals(AccessControlList.PUBLIC)||acl.equals(AccessControlList.READONLY)){
			return client.getThumbnailUrl(bucketName, objectKey, width, height, false, 0);
		}
		
		return null;		
	}

}
