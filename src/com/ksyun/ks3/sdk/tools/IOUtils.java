/**
 * @author: yangji
 * @data:   Apr 8, 2013
 */
package com.ksyun.ks3.sdk.tools;

import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
	public static void safelyCloseInputStream(InputStream is){	
		try {
			if(is!=null){				
				is.close();
				is = null;			
			}
			
		} catch (IOException e) {}
	}

}
