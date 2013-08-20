/**
 * @author: yangji
 * @data:   Aug 19, 2013
 */
package com.ksyun.ks3.sdk.services.ks3service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.ksyun.ks3.sdk.dto.Credential;
import com.ksyun.ks3.sdk.dto.internal.HttpMethod;
import com.ksyun.ks3.sdk.dto.internal.Request;
import com.ksyun.ks3.sdk.dto.internal.Response;
import com.ksyun.ks3.sdk.services.httpservice.HttpRequestor;
import com.ksyun.ks3.sdk.tools.CodeUtils;

public class MultipartUploadOperation extends KS3Operation {

	protected MultipartUploadOperation(HttpRequestor requestor,
			Credential credential) throws Exception {
		super(requestor, credential);
	}

	public void initiateMultipartUpload(String bucketName, String objectKey)
			throws Exception {

		CodeUtils.checkStringParams("bucketName", bucketName);
		CodeUtils.checkStringParams("objectKey", objectKey);

		RequestBuilder requestBuilder = requestFactory.getBuilder();
		Map<String,String> params = new HashMap<String,String>();
		params.put("uploads", null);
		Request request = requestBuilder.setMethod(HttpMethod.POST).setBucket(bucketName).setObjectKey(objectKey).addPamams(params).build();
		Response response = sendMessageAndKeepAlive(request);
		
		forTest(response.getBody());
		

	}
	
	public static void forTest(InputStream is){
				
		StringBuilder sb = new StringBuilder();
        String readline = "";
        try
        {            
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while (br.ready())
            {
                readline = br.readLine();
                sb.append(readline);
            }
            br.close();
            is.close();
        } catch (IOException ie)
        {
            System.out.println("converts failed.");
        }
        System.out.println(sb.toString());
	}

	public static void main(String[] args) throws Exception {

		String ACCESS_KEY_ID = "AUIJP4LZQQPVHV2JVJ2A";
		String ACCESS_KEY_SECRET = "nzW1E6W4dTRSAGZJi/EINuO/1eeGXumBfmfcx42C";
		Credential credential = new Credential(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		
		MultipartUploadOperation muo = new MultipartUploadOperation(new HttpRequestor(), credential);
		muo.initiateMultipartUpload("bucket", "test.rar");
	}

}
