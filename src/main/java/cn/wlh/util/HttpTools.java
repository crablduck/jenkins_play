package cn.wlh.util;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


/**
 * @author chenchong@rong360.com
 * @date [2015-5-27]
 */
@SuppressWarnings("deprecation")
public class HttpTools {
    
    /***** 超时时间 *****/
    public static final int TIMEOUT = 10000;
    
    /***** HTTP最大连接数 *****/
    private static final int MAX_TOTAL_CONNECTIONS = 400;
    
    /***** HTTP最大路由数 *****/
    private static final int MAX_ROUTE_CONNECTIONS = 100;
    
    /***** HTTP连接超时时间 *****/
    public static final int CONNECT_TIMEOUT = 10000;
    
    /***** HTTP套接字SOCKET超时时间 *****/
    public static final int SOCKET_TIMEOUT = 20000;
    
    /***** 连接池管理对象 *****/
    private static PoolingHttpClientConnectionManager connectionManager = null;
    
    /***** 初始化连接池 *****/
    static {
        try {
			
        	 SSLContext sslContext = null ;
        	    X509TrustManager tm = new X509TrustManager() {
                    
                    public void checkClientTrusted(X509Certificate[] chain,
                            String authType) throws CertificateException {
                    }
                   
                    public void checkServerTrusted(X509Certificate[] chain,
                            String authType) throws CertificateException {
                    }
                   
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
            };
     			sslContext = SSLContext.getInstance("TLS");
     		sslContext.init(null, new TrustManager[]{tm}, null);
			Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", new MyPlainSocketFactory())
                    .register("https",
                       // new MySSLSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER))
                    		new SSLConnectionSocketFactory(sslContext,new MyHostnameVerifier()))
                    .build();
            
            connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
            connectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
            connectionManager.setValidateAfterInactivity(4000);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
  /*  public static CloseableHttpClient createInstance(int connectTimeout, int socketTimeout) {
        if(connectTimeout == 0) {
            connectTimeout = HttpTools.CONNECT_TIMEOUT;
        }
        if(socketTimeout == 0) {
            socketTimeout = HttpTools.SOCKET_TIMEOUT;
        }
        
        boolean redirectAll = false;
        DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        if(redirectAll) {
            redirectStrategy = new LaxRedirectStrategy();
        }
        
		RequestConfig requestConfig =
            RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .setConnectionRequestTimeout(connectTimeout)
                .setRedirectsEnabled(true)
                .setStaleConnectionCheckEnabled(true)
                .setCookieSpec("easy")
                .build();
        
        return HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfig)
            .setRedirectStrategy(redirectStrategy)
            .build();
    }
    
    public static String formPost(String url,String requestBody ) throws Exception{
    	 CloseableHttpClient httpclient = null;
         HttpPost httppost = new HttpPost(url);
         try {
             httpclient = HttpTools.createInstance(0, 0);
             if(requestBody == null) {
                 return "";
             }
             
             // 创建参数队列
             HttpEntity uefEntity = new ByteArrayEntity(requestBody.getBytes());
            //  UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
             httppost.setEntity(uefEntity);
             
             HttpResponse response = null;
             response = httpclient.execute(httppost);
             
             if(response == null) {
             	return "";
             }
             HttpEntity entity = response.getEntity();
             if(entity != null) {
             	return EntityUtils.toString(entity, "UTF-8");
             }
         } catch (Exception e) {
             throw e;
         } finally {
             if(httppost != null) {
                 httppost.releaseConnection();
             }
         }
         return "";
    }
    public static String post(String url, Map<String, String> params) throws Exception {
    	return post(url,params,0,0,null);
    }
    
    public static String post(String url, Map<String, String> params,Header[] headers) throws Exception {
    	return post(url,params,0,0,headers);
    }
    
    public static String get(String url, Map<String, String> params) throws Exception {
    	return get(url,params,0,0,null);
    }
    
    public static String get(String url, Map<String, String> params,Header[] headers) throws Exception {
    	return get(url,params,0,0,headers);
    }
    
    public static String get(String url, Map<String, String> params, int connectTimeout, 
    		int readTimeout,Header[] headers) throws Exception {
        CloseableHttpClient httpclient = null;
        HttpGet httpget = null;
        try {
            httpclient = HttpTools.createInstance(connectTimeout, readTimeout);
            String urlPramStr = null;
            if(params == null || params.size() == 0) {
            }else {
            	// 创建参数队列
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                for (String key : params.keySet()) {
                    formparams.add(new BasicNameValuePair(key, params.get(key)));
                }
                urlPramStr = URLEncodedUtils.format(formparams, "utf-8");
                url = url+"?"+urlPramStr;
            }
            
            
           httpget = new HttpGet(url);
           if(null != headers) {
        	   httpget.setHeaders(headers);
           }
            HttpResponse response = null;
            response = httpclient.execute(httpget);
            
            if(response == null) {
            	return "";
            }
            HttpEntity entity = response.getEntity();
            if(entity != null) {
            	return EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if(httpget != null) {
                httpget.releaseConnection();
            }
        }
        return "";
    }
    
    public static String post(String url, Map<String, String> params, int connectTimeout, 
    		int readTimeout,Header[] headers) throws Exception {
        CloseableHttpClient httpclient = null;
        HttpPost httppost = new HttpPost(url);
        if(null != headers) {
        	httppost.setHeaders(headers);
        }
        
        try {
            httpclient = HttpTools.createInstance(connectTimeout, readTimeout);
            if(params == null || params.size() == 0) {
                return "";
            }
            
            // 创建参数队列
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                formparams.add(new BasicNameValuePair(key, params.get(key)));
            }
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            
            HttpResponse response = null;
            response = httpclient.execute(httppost);
            
            if(response == null) {
            	return "";
            }
            HttpEntity entity = response.getEntity();
            if(entity != null) {
            	return EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if(httppost != null) {
                httppost.releaseConnection();
            }
        }
        return "";
    }
    
    public static String postJsonObj(String url, Object data)
			throws ClientProtocolException, IOException {
    	return postJson(url,JSON.toJSONString(data),0,0,null);
    }
    
    public static String postJson(String url, String data)
			throws ClientProtocolException, IOException {
    	return postJson(url,data,0,0,null);
    }
    public static String postJson(String url, String data, int connectTimeout, int readTimeout,Header[] headers)
    		throws ClientProtocolException, IOException  
    {
    	return doPostJson(url,data,0,0,headers);
    }
    
    public static String postJson(String url, Object data,Header[] headers)
			throws ClientProtocolException, IOException {
    	String json = null;
    	if(null != data) {
    		if(data instanceof String) {
    			json = String.valueOf(data);
    		}else {
    			json = JSON.toJSONString(data);
    		}
    	}
    	return doPostJson(url,json,0,0,headers);
    }
    
	private static String doPostJson(String url, String data, int connectTimeout, int readTimeout,Header[] headers)
			throws ClientProtocolException, IOException {

		CloseableHttpClient httpclient = null;
		HttpPost httppost = null;
		if(null == url) {
			return null;
		}
		try {
			httpclient = HttpTools.createInstance(connectTimeout, readTimeout);
			httppost = new HttpPost(url);
			if(null != headers) {
	        	httppost.setHeaders(headers);
	        }
			StringEntity entity = new StringEntity(data, "utf-8");// 解决中文乱码问题
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			httppost.setEntity(entity);

			HttpResponse result = httpclient.execute(httppost);
			if (result == null) {
				return "";
			}
			// 请求结束，返回结果
			String resData = EntityUtils.toString(result.getEntity());
			return resData;
		} catch (Exception e) {
			throw e;
		} finally {
			if (httppost != null) {
				httppost.releaseConnection();
			}
		}
	}*/
    
}
