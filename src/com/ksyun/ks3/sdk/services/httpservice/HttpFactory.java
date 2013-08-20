/**
 * @author: yangji
 * @data:   Apr 2, 2013
 */
package com.ksyun.ks3.sdk.services.httpservice;

import com.ksyun.ks3.sdk.configs.ConnectioinConfig;
import com.ksyun.ks3.sdk.dto.internal.HttpMethod;
import com.ksyun.ks3.sdk.dto.internal.Request;
import com.ksyun.ks3.sdk.dto.internal.Response;
import com.ksyun.ks3.sdk.services.authservice.Signature;
import com.ksyun.ks3.sdk.tools.EncodingUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SimpleTimeZone;

public class HttpFactory {

	private HttpRequestBase getRequestBase(Request request) {

		HttpRequestBase httpRequestBase = null;
		request = Signature.signRequest(request);

		String method = request.getMethod();
		InputStream content = request.getBody();
		String url = request.getUrl();
		
//		System.out.println(url);

		String bucket = request.getBucekt();
		if (bucket != null && bucket.trim().length() != 0)
			url = bucket + "." + url;
		

		String object = request.getObject();
		if (object != null && object.trim().length() != 0){
			url = url + "/" + EncodingUtils.getUrlEncode(object);			
		}			

		url  = "http://"+ url;

		Map<String, String> params = request.getParams();
		if (params != null && params.size() > 0) {
			
			url += "?";
			
			for(Entry<String, String> entry :params.entrySet()){
				String key = entry.getKey();
				String value = entry.getValue();
				if(key!=null&&!key.trim().equals("")){
					if(value==null)
						url+=key+"&";
					else
						url+=key+"="+value+"&";
				}				
			}
			
			
//			for (Entry<String, String> entry : params.entrySet()) {
//				String kv = entry.getKey() + "=" + entry.getValue() + "&";
//				url += kv;
//			}
			url = url.substring(0, url.length() - 1);
		}
		
//		System.out.println(url);

		if (method.equals(HttpMethod.GET))
			httpRequestBase = new HttpGet(url);
		else if (method.equals(HttpMethod.DELETE))
			httpRequestBase = new HttpDelete(url);
		else if (method.equals(HttpMethod.HEAD))
			httpRequestBase = new HttpHead(url);
		else if(method.equals(HttpMethod.POST))
			httpRequestBase = new HttpPost(url);		
		else if (method.equals(HttpMethod.PUT)) {
			HttpPut putMethod = new HttpPut(url);
			if (content != null) {			
				HttpEntity entity = new InputStreamEntity(content,Long.valueOf(request.getHeaders().get("content-length")));				
				putMethod.setEntity(entity);
			}
			httpRequestBase = putMethod;
		} 
		else
			throw new IllegalArgumentException("Unsupported HTTP method:"
					+ request.getMethod());
		
		return httpRequestBase;
	}
	
    @SuppressWarnings("deprecation")
	public HttpClient getHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setUseExpectContinue(params, false);
        HttpProtocolParams.setUserAgent(params, ConnectioinConfig.userAgent);
        ConnManagerParams.setMaxTotalConnections(params, ConnectioinConfig.maxConnections);
        ConnManagerParams.setMaxConnectionsPerRoute(params,
                new ConnPerRoute() {
                    @Override
                    public int getMaxForRoute(HttpRoute httproute) {
                        return 32;
                    }
                });

        HttpClientParams.setRedirecting(params, true);

        params.setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, ConnectioinConfig.connectionTimeout);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT,
                ConnectioinConfig.socketTimeout);
        params.setParameter(
                CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024);

        params.setParameter(ClientPNames.HANDLE_REDIRECTS,
                Boolean.FALSE);
        params.setParameter(
                CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8"); // 默认为ISO-8859-1
        params.setParameter(
                CoreProtocolPNames.HTTP_ELEMENT_CHARSET, "UTF-8"); // 默认为US-ASCII
        params.removeParameter(ConnRouteParams.DEFAULT_PROXY);

        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        schReg.register(new Scheme("https", SSLSocketFactory
                .getSocketFactory(), 443));
        ThreadSafeClientConnManager conMgr = new ThreadSafeClientConnManager(
                params, schReg);
        return new DefaultHttpClient(conMgr, params);
    }

	public HttpClient generateHttpClient() {

		HttpParams httpParams = new BasicHttpParams();
		HttpProtocolParams
				.setUserAgent(httpParams, ConnectioinConfig.userAgent);
		HttpConnectionParams.setConnectionTimeout(httpParams,
				ConnectioinConfig.connectionTimeout);
		HttpConnectionParams.setSoTimeout(httpParams,
				ConnectioinConfig.socketTimeout);
		HttpConnectionParams.setStaleCheckingEnabled(httpParams, true);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));  
        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));  
		ThreadSafeClientConnManager connMgr = new ThreadSafeClientConnManager(httpParams, schReg);
//		connMgr.setDefaultMaxPerRoute(ConnectioinConfig.maxConnections);
//		connMgr.setMaxTotal(ConnectioinConfig.maxConnections);

		DefaultHttpClient httpClient = new DefaultHttpClient(connMgr,
				httpParams);
//		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
		return httpClient;
	}

	public HttpRequestBase generateHttpRequest(Request request) {

		if (request.getHeaders().get("date") == null
				|| request.getHeaders().get("date") == "") {
			SimpleDateFormat sdf = new SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
			sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
			request.addHeader("date", sdf.format(new Date()));
		}

		if (request.getHeaders().get("content-type") == null
				|| request.getHeaders().get("content-type") == "") {

			request.addHeader("content-type", "text/plain; charset=UTF-8");
		}

		HttpRequestBase httpRequest = getRequestBase(request);
		for (Entry<String, String> entry : request.getHeaders().entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (!key.equalsIgnoreCase("Host")&&!key.equalsIgnoreCase("content-length"))
				httpRequest.addHeader(key, value);
		}

		return httpRequest;
	}

	public Response generateFromHttpResponse(HttpResponse httpResponse) {

		Response response = new Response();
		response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		try {
			response.setBody(httpResponse.getEntity().getContent());
		} catch (Exception e) {
			response.setBody(new ByteArrayInputStream("".getBytes()));
		}

		Header headers[] = httpResponse.getAllHeaders();
		Map<String, String> resultHeaders = new HashMap<String, String>();
		for (int i = 0; i < headers.length; i++) {
			Header h = headers[i];
			resultHeaders.put(h.getName(), h.getValue());
		}

		response.setHeaders(resultHeaders);

		return response;
	}
}
