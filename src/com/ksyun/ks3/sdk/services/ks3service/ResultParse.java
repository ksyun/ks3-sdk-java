/**
 * @author: yangji
 * @data:   Apr 8, 2013
 */
package com.ksyun.ks3.sdk.services.ks3service;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import com.ksyun.ks3.sdk.dto.AccessControlPolicy;
import com.ksyun.ks3.sdk.dto.Bucket;
import com.ksyun.ks3.sdk.dto.ObjectInfo;
import com.ksyun.ks3.sdk.dto.ObjectList;
import com.ksyun.ks3.sdk.tools.IOUtils;

public class ResultParse {
	
	private SimpleDateFormat sdf;	
	
	public ResultParse(){
		sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
		sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
	}
	
	private Element getRootElement(InputStream is) throws Exception{
		
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc;
		try {
			doc = db.parse(is);
		} catch (Exception e) {
			
			throw new Exception("respones builder error");
		}
		finally{
			IOUtils.safelyCloseInputStream(is);
		}
		
		return doc.getDocumentElement();
	}
	
	public AccessControlPolicy getAccessControlPolicy(InputStream is) throws Exception{
		Element root;
		try {
			root = getRootElement(is);		
		} catch (Exception e) {
			throw e;
		}

		NodeList ownerList = root.getElementsByTagName("Owner");
		Element owner = (Element)ownerList.item(0);
		
		Node nameNode = owner.getElementsByTagName("DisplayName").item(0);
		String displayName = nameNode.getTextContent();
		
		Node idNode = owner.getElementsByTagName("ID").item(0);
		String id = idNode.getTextContent();
		
		NodeList accessControlList = root.getElementsByTagName("AccessControlList");
		Element accessControl = (Element)accessControlList.item(0);
		Node grantNode = accessControl.getElementsByTagName("Grant").item(0);
		String grant = grantNode.getTextContent();
		
		IOUtils.safelyCloseInputStream(is);
		
		return new AccessControlPolicy(displayName, id, grant);
		
	}
	
	public List<Bucket> getBucketList(InputStream is) throws Exception{					
		List<Bucket> bucketlist = new ArrayList<Bucket>();	
		Element root;
		try {
			root = getRootElement(is);		
		} catch (Exception e) {
			throw e;
		}
		
		NodeList bucketsList = root.getElementsByTagName("Buckets");
		NodeList bucketList = bucketsList.item(0).getChildNodes();
		
		int len = bucketList.getLength();
		
		try {
			
			for (int i=0; i<len; i++)
			{
				Element node = (Element)bucketList.item(i);
				
				Node nodeBucketName = node.getElementsByTagName("Name").item(0);
				String bucketName = nodeBucketName.getTextContent();
				
				Node nodeCreationDate = node.getElementsByTagName("CreationDate").item(0);
				Date creationDate = sdf.parse(nodeCreationDate.getTextContent());
				
				bucketlist.add(new Bucket(bucketName, creationDate));	
			}
			
			
		} catch (ParseException e) {
			throw new Exception("response parse error");
		}	
		finally{			
			IOUtils.safelyCloseInputStream(is);
		}
		
		return bucketlist;
	}
	
	public ObjectList getObjectList(InputStream is) throws Exception{	
		
		ObjectList objectList;
		
		Element root;
		try {
			root = getRootElement(is);		
		} catch (Exception e) {
			throw e;
		}
		
		String bucketName = root.getElementsByTagName("Name").item(0).getTextContent();
		String maxKeys = root.getElementsByTagName("MaxKeys").item(0).getTextContent();
		String prefix = root.getElementsByTagName("Prefix").item(0).getTextContent();
		String marker = root.getElementsByTagName("Marker").item(0).getTextContent();
		Boolean isTruncated = Boolean.valueOf(root.getElementsByTagName("IsTruncated").item(0).getTextContent());
		
		objectList = new ObjectList(bucketName, maxKeys, prefix, marker, isTruncated);

		NodeList contentsList = root.getElementsByTagName("Contents");
		int len = contentsList.getLength();
		
		try {
			for(int i=0; i<len; i++)
			{
				Element node = (Element)contentsList.item(i);
				
				Date lastModified = sdf.parse(node.getElementsByTagName("LastModified").item(0).getTextContent());
				String eTag = node.getElementsByTagName("ETag").item(0).getTextContent();
				String key = node.getElementsByTagName("Key").item(0).getTextContent();
				String sSize = node.getElementsByTagName("Size").item(0).getTextContent();
				int size = Integer.valueOf(sSize);
				
				Element ownerNode = (Element)node.getElementsByTagName("Owner").item(0);
				String displayName = ownerNode.getElementsByTagName("DisplayName").item(0).getTextContent();
				String id = ownerNode.getElementsByTagName("ID").item(0).getTextContent();
				
				objectList.addObjectInfo(new ObjectInfo(displayName, id, lastModified, eTag, key, size));
			}
			
		} catch (ParseException e) {
			throw new Exception("response parse error");
		}	
		finally{			
			IOUtils.safelyCloseInputStream(is);
		}
		
		return objectList;
	}

}
