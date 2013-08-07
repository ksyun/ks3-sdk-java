/**
 * @author: yangji
 * @data:   Jul 23, 2013
 */
package com.ksyun.ks3.sdk.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EncodingUtils {
	
	public static String getUrlEncode(String content){
		String result=null;
		try {
			result = URLEncoder.encode(content,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			//这里是100%不会抛异常的，因为"UTF-8"不是当参数传过来的，是咱们自己写死的
		}
		result = result.replace("+", "%20");
		result = result.replace("*", "%2A");
		return result;		
	}
	
	public static void main(String[] args) {
		System.out.println(EncodingUtils.getUrlEncode(""));
	}

}
