/**
 * @author: yangji
 * @data:   Apr 8, 2013
 */
package com.ksyun.ks3.sdk.services.ks3service;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ksyun.ks3.sdk.dto.AbortMultipartUploadOptions;
import com.ksyun.ks3.sdk.dto.CompleteMultipartUploadOptions;
import com.ksyun.ks3.sdk.dto.CompleteMultipartUploadResult;
import com.ksyun.ks3.sdk.dto.Credential;
import com.ksyun.ks3.sdk.dto.InitiateMultipartUploadResult;
import com.ksyun.ks3.sdk.dto.ObjectEntity;
import com.ksyun.ks3.sdk.dto.ObjectEtag;
import com.ksyun.ks3.sdk.dto.ObjectGetOptions;
import com.ksyun.ks3.sdk.dto.PartList;
import com.ksyun.ks3.sdk.dto.PartListOptions;
import com.ksyun.ks3.sdk.dto.PresignedUrlOptions;
import com.ksyun.ks3.sdk.dto.UploadPart;
import com.ksyun.ks3.sdk.dto.UploadPartOptions;
import com.ksyun.ks3.sdk.dto.UploadPartResult;
import com.ksyun.ks3.sdk.dto.internal.HttpMethod;
import com.ksyun.ks3.sdk.dto.internal.Request;
import com.ksyun.ks3.sdk.dto.internal.Response;
import com.ksyun.ks3.sdk.services.authservice.Signature;
import com.ksyun.ks3.sdk.services.httpservice.HttpRequestor;
import com.ksyun.ks3.sdk.services.ks3service.KS3Operation;
import com.ksyun.ks3.sdk.services.ks3service.ObjectOperation;
import com.ksyun.ks3.sdk.tools.CodeUtils;
import com.ksyun.ks3.sdk.tools.EncodingUtils;

public class ObjectOperation extends KS3Operation {

	public ObjectOperation(HttpRequestor requestor, Credential credential)
			throws Exception {
		super(requestor, credential);
	}

	public ObjectEtag putObject(String bucketName, String objectKey, File file, String mimeType)
			throws Exception {
		
		if(mimeType==null||mimeType.trim().equals(""))
			mimeType = "application/octet-stream";		

		RequestBuilder requestBuilder = requestFactory.getBuilder();	
		Request request = requestBuilder.setMethod(HttpMethod.PUT)
				.setBucket(bucketName).setObjectKey(objectKey).setObjectValue(new FileInputStream(file))
				.addHeader("content-type", mimeType)
				.addHeader("content-length", String.valueOf(file.length()))
				.build();
		
		Response response = sendMessage(request);
		
		String eTag = response.getHeaders().get("ETag");
		return new ObjectEtag(eTag);
	}
	
	public ObjectEtag putObjectByInputStream(String bucketName, String objectKey, InputStream inputStream, 
			int offset, int length, String mimeType) throws Exception {
		
		// 起始位置不能在length之后
		if (offset < 0 || offset > length)
			throw new IllegalArgumentException("Illegal arguments");
		
		//得到实际的length
		length = length - offset;
		
		//处理偏移量
		try {
			inputStream.skip(offset);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Skip data failed.");
		}
			
		
		if(mimeType==null||mimeType.trim().equals(""))
			mimeType = "application/octet-stream";		

		RequestBuilder requestBuilder = requestFactory.getBuilder();	
		Request request = requestBuilder.setMethod(HttpMethod.PUT)
				.setBucket(bucketName).setObjectKey(objectKey).setObjectValue(inputStream)
				.addHeader("content-type", mimeType)
				.addHeader("content-length", String.valueOf(length))
				.build();
		
		Response response = sendMessage(request);
		
		String eTag = response.getHeaders().get("ETag");
		return new ObjectEtag(eTag);
	}
	
	public ObjectEtag putStringObject(String bucketName, String objectKey,String content) throws Exception{	
		
		RequestBuilder requestBuilder = requestFactory.getBuilder();		
		Request request = requestBuilder.setMethod(HttpMethod.PUT)
				.setBucket(bucketName).setObjectKey(objectKey).setObjectValue(new ByteArrayInputStream(content.getBytes()))
				.addHeader("content-type", "text/plain; charset=UTF-8")
				.addHeader("content-length", String.valueOf(content.length()))
				.build();
		
		Response response = sendMessage(request);
		
		String eTag = response.getHeaders().get("ETag");		
		return new ObjectEtag(eTag);
		
	}
	
	public ObjectEntity getObject(String bucketName, String objectKey) throws Exception{
		
		RequestBuilder requestBuilder = requestFactory.getBuilder();	
		Request request = requestBuilder.setMethod(HttpMethod.GET).setBucket(bucketName).setObjectKey(objectKey).build();
		Response response = sendMessageAndKeepAlive(request);
		
		return new ObjectEntity(bucketName, objectKey, response.getBody());
	}
	
	public ObjectEntity getObejctByOptions(ObjectGetOptions options) throws Exception{
		
		String bucketName = options.getBucketName();
		String objectKey = options.getObjectKey();
		
		RequestBuilder requestBuilder = requestFactory.getBuilder();	
		requestBuilder.setMethod("GET").setBucket(bucketName).setObjectKey(objectKey);
		if(options.getRange()!=null){
			requestBuilder = requestBuilder.addHeader("Range", "bytes="+options.getRange()[0]+"-"+options.getRange()[1]);
		}
		if(options.getMatchingETagConstraints()!=null){
			requestBuilder = requestBuilder.addHeader("If-Match", options.getMatchingETagConstraints());
		}
		if(options.getNonmatchingEtagConstraints()!=null){
			requestBuilder = requestBuilder.addHeader("If-None-Match", options.getNonmatchingEtagConstraints());
		}
		if(options.getModifiedSinceConstraint()!=null){
			requestBuilder = requestBuilder.addHeader("If-Modified-Since", options.getModifiedSinceString());
		}
		if(options.getUnmodifiedSinceConstraint()!=null){
			requestBuilder = requestBuilder.addHeader("If-Unmodified-Since", options.getUnmodifiedSinceString());
		}		
		if(options.getParams()!=null){
			for(Entry<String,String> entry:options.getParams().entrySet())
				requestBuilder = requestBuilder.addParam(entry.getKey(), entry.getValue());
		}
		
		Request request = requestBuilder.build();
		Response response = sendMessageAndKeepAlive(request);
		
		return new ObjectEntity(bucketName, objectKey, response.getBody());
	}
	
	public void deleteObject(String bucketName, String objectKey) throws Exception{
		
		RequestBuilder requestBuilder = requestFactory.getBuilder();	
		Request request = requestBuilder.setMethod(HttpMethod.DELETE).setBucket(bucketName).setObjectKey(objectKey).build();
		sendMessage(request);		
	}
	
	public String getPresignedUrl(PresignedUrlOptions options) throws Exception{
		
		String bucketName = options.getBucketName();
		String objectKey = options.getObjectKey();
		Integer expires = options.getExpires();
		HashMap<String, String> paramsHeaders = options.getResponseHeaders();
		
		String expTime = String.valueOf(new Date().getTime()/1000+expires);
		RequestBuilder requestBuilder = requestFactory.getBuilder();	
		requestBuilder = requestBuilder.setMethod("GET").setBucket(bucketName).setObjectKey(objectKey).addHeader("Date",expTime);
		
		for(Entry<String, String> entry:paramsHeaders.entrySet())
			requestBuilder = requestBuilder.addParam(entry.getKey(), entry.getValue());
		
		Request request = requestBuilder.build();
		
		String signature = Signature.getSignatureByRequest(request);
		signature = URLEncoder.encode(signature,"UTF-8");
		String accessKeyId = request.getCredential().getAccessKeyId();
		
		objectKey = EncodingUtils.getUrlEncode(objectKey);
		
		String url = "http://kss.ksyun.com/"+bucketName+"/"+objectKey+"?Expires="+expTime;
		url = url + "&AccessKeyId="+accessKeyId+"&Signature="+signature;
		if(paramsHeaders==null||paramsHeaders.size()<1)
			return url;
		
		for(Entry<String,String> entry:paramsHeaders.entrySet()){
			url=url+"&"+URLEncoder.encode(entry.getKey(),"UTF-8")+"="+URLEncoder.encode(entry.getValue(),"UTF-8");
		}
			
		return url;
	}
	
	public InitiateMultipartUploadResult initiateMultipartUpload(String bucketName, String objectKey)
			throws Exception {

		CodeUtils.checkStringParams("bucketName", bucketName);
		CodeUtils.checkStringParams("objectKey", objectKey);

		RequestBuilder requestBuilder = requestFactory.getBuilder();
		Map<String,String> params = new HashMap<String,String>();
		params.put("uploads", null);
		Request request = requestBuilder.setMethod(HttpMethod.POST).setBucket(bucketName).setObjectKey(objectKey).addPamams(params).build();
		Response response = sendMessageAndKeepAlive(request);
		
		return resultParse.getInitiateMultipartUploadResult(response.getBody());
	}
	
	public UploadPartResult uploadPart(UploadPartOptions uploadPartOptions) throws Exception{
		
		String bucketName = uploadPartOptions.getBucketName();
		String objectKey = uploadPartOptions.getObjectKey();
		
		int partNumber = uploadPartOptions.getPartNumber();
		String uploadId = uploadPartOptions.getUploadId();

		long partSize = uploadPartOptions.getPartSize();
		String md5 = uploadPartOptions.getMd5();
		
		InputStream input = uploadPartOptions.getInputStream();
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("partNumber", String.valueOf(partNumber));
		params.put("uploadId", uploadId);
		
		Map<String,String> headers = new HashMap<String, String>();
		headers.put("content-length", String.valueOf(partSize));
		if(md5!=null&&md5.length()>0)
			headers.put("content-md5", md5);		
		
		RequestBuilder requestBuilder = requestFactory.getBuilder();
		Request request = requestBuilder.setMethod(HttpMethod.PUT).setBucket(bucketName).setObjectKey(objectKey).addPamams(params).addHeaders(headers).setObjectValue(input).build();
		
		Response response = sendMessage(request);
		
		String eTag = response.getHeaders().get("ETag");
				
		return new UploadPartResult(uploadId, partNumber, eTag);	
	}
	
	public CompleteMultipartUploadResult completeMultipartUpload(CompleteMultipartUploadOptions completeMultipartUploadOptions) throws Exception{		
		
		String bucketName = completeMultipartUploadOptions.getBucketName();
		String objectKey = completeMultipartUploadOptions.getObjectKey();		
		String uploadId = completeMultipartUploadOptions.getUploadId();
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("uploadId", uploadId);
		
		String uploadListXml = buildMultipartRequestXml(completeMultipartUploadOptions.getUploadList());
		
		byte inputBytes[] = null;
		try{
			inputBytes = uploadListXml.getBytes("utf-8");
		}catch (UnsupportedEncodingException e){}//Never throws exception
		
		ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
		
		Map<String,String> headers = new HashMap<String, String>();
		headers.put("content-length", String.valueOf(uploadListXml.length()));
		
		RequestBuilder requestBuilder = requestFactory.getBuilder();
		Request request = requestBuilder.setMethod(HttpMethod.POST).setBucket(bucketName).setObjectKey(objectKey).addPamams(params).addHeaders(headers).setObjectValue(input).build();
		
		Response response = sendMessageAndKeepAlive(request);
		
		return 	resultParse.getCompleteMultipartUploadResult(response.getBody());
		
	}
	
	public void abortMultipartUpload(AbortMultipartUploadOptions abortMultipartUploadOptions) throws Exception{
		
		String bucketName = abortMultipartUploadOptions.getBucketName();
		String objectKey = abortMultipartUploadOptions.getObjectKey();		
		String uploadId = abortMultipartUploadOptions.getUploadId();	
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("uploadId", uploadId);
		
		RequestBuilder requestBuilder = requestFactory.getBuilder();
		Request request = requestBuilder.setMethod(HttpMethod.DELETE).setBucket(bucketName).setObjectKey(objectKey).addPamams(params).build();
		
		sendMessage(request);		
	}
	
	public PartList getPartList(PartListOptions partListOptions) throws Exception{
		
		String bucketName = partListOptions.getBucketName();
		String objectKey = partListOptions.getObjectKey();		
		String uploadId = partListOptions.getUploadId();
		
		Integer maxParts = partListOptions.getMaxParts();
		Integer partNumberMarker = partListOptions.getPartNumberMarker();
		
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("uploadId", uploadId);		
		if(maxParts!=null) 
			params.put("max-parts", String.valueOf(maxParts));
		if(partNumberMarker!=null) 
			params.put("part-number-marker", String.valueOf(partNumberMarker));
		
		RequestBuilder requestBuilder = requestFactory.getBuilder();
		Request request = requestBuilder.setMethod(HttpMethod.GET).setBucket(bucketName).setObjectKey(objectKey).addPamams(params).build();
		
		Response response = sendMessageAndKeepAlive(request);	
		
		
		return resultParse.getPartList(response.getBody());
		
	}	
	
	private String buildMultipartRequestXml(List<UploadPart> uploadList){
		StringBuffer xml = new StringBuffer();
		xml.append("<CompleteMultipartUpload>");
		for(UploadPart part:uploadList){
			int partNumber = part.getPartNumber();
			String eTag = part.geteTag();
			xml.append("<Part>");
			xml.append("<PartNumber>"+partNumber+"</PartNumber>");
			xml.append("<ETag>"+eTag+"</ETag>");
			xml.append("</Part>");
		}
		xml.append("</CompleteMultipartUpload>");
		
		return xml.toString();	
	}	
}
