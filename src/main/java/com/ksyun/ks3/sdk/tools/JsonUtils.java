/**
 * @author: yangji
 * @data:   Jul 21, 2013
 */
package com.ksyun.ks3.sdk.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonUtils {
	
	public static String getJson(Object data){
		return JSONObject.fromObject(data,JsonUtils.getFixedJsonConfig()).toString();
	}
	
	//解决Date类型输出到Json时格式错乱的问题
	public static JsonConfig getFixedJsonConfig(){
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("EEE, dd MMM yyyy HH:mm:ss z"));
		return jsonConfig;
	}

}


class DateJsonValueProcessor implements JsonValueProcessor {
	private String format = "EEE, dd MMM yyyy HH:mm:ss z";

	public DateJsonValueProcessor() {		
	}

	public DateJsonValueProcessor(String format) {
		this.format = format;
	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		String[] obj = {};
		if(value!=null)
		{
			if (value instanceof Date[]) {
				SimpleDateFormat sf = new SimpleDateFormat(format);
				Date[] dates = (Date[]) value;
				obj = new String[dates.length];
				for (int i = 0; i < dates.length; i++) {
					obj[i] = sf.format(dates[i]);
				}
			}
		}
		return obj;
	}

	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		if(value!=null)
		{
			if (value instanceof Date) {
				SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
				String str = sdf.format((Date) value);
				return str;
			}
		}
		return value;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}