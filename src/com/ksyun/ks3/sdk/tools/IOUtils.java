/**
 * @author: yangji
 * @data:   Apr 8, 2013
 */
package com.ksyun.ks3.sdk.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {
	
	public static void safelyCloseInputStream(InputStream is){	
		try {
			if(is!=null){				
				is.close();
				is = null;			
			}
			
		} catch (IOException e) {}
	}
	
	public static String inputStream2String(InputStream inputstream){
		BufferedReader in = new BufferedReader(new InputStreamReader(inputstream));
	    StringBuffer buffer = new StringBuffer();
	    String line = "";
	    try {
	    	while ((line = in.readLine()) != null){
	  	      buffer.append(line);
	  	    }
		} catch (Exception e) {
			return null;
		}finally{
			safelyCloseInputStream(inputstream);
		}
	    
	    return buffer.toString();
	}

}
