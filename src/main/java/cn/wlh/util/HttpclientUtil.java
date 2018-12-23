package cn.wlh.util;

import cn.wlh.exception.AppCheckException;
import cn.wlh.exception.AppException;
import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpclientUtil {

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
    
    /***闲置检查间隔时间，单位毫秒***/
    public static final int VALIDATE_AFTER_INACTIVITY = 4000;
    
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
                    		new SSLConnectionSocketFactory(sslContext,new MyHostnameVerifier()))
                    .build();
            
            connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
            connectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
            connectionManager.setValidateAfterInactivity(VALIDATE_AFTER_INACTIVITY);
        } catch (Exception e) {
        	throw new AppException("1000","init httpclient connectionManager error",e);
        }
    }
    
    public static CloseableHttpClient createInstance(int connectTimeout, int socketTimeout) {
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
               // .setStaleConnectionCheckEnabled(true)
                .setCookieSpec("easy")
                .build();
        
        return HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfig)
            .setRedirectStrategy(redirectStrategy)
            .build();
    }
    /********** post form start **********/
    public static String formPost(String url,String requestBody ) throws AppCheckException {
    	if(!StringUtil.isUrl(url)) {
    		throw new AppCheckException("1002","it is not a url:"+url);
    	}
    	 CloseableHttpClient httpclient = null;
         HttpPost httppost = new HttpPost(url);
         try {
             httpclient = createInstance(0, 0);
             if(null != requestBody) {
            	// 创建参数队列
                 HttpEntity uefEntity = new ByteArrayEntity(requestBody.getBytes());
                 httppost.setEntity(uefEntity);
             }
             HttpResponse response = null;
             response = httpclient.execute(httppost);
             
             if(null == response) {
             	return "";
             }
             HttpEntity entity = response.getEntity();
             if(entity != null) {
             	return EntityUtils.toString(entity, "UTF-8");
             }
         } catch (Exception e) {
        	 throw new AppCheckException("1001","formPost error",e);
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
    public static String post(String url, Map<String, String> params, int connectTimeout, 
    		int readTimeout,Header[] headers) throws Exception {
    	if(!StringUtil.isUrl(url)) {
    		throw new AppCheckException("1002","it is not a url:"+url);
    	}
        CloseableHttpClient httpclient = null;
        HttpPost httppost = new HttpPost(url);
        if(null != headers) {
        	httppost.setHeaders(headers);
        }
        
        try {
            httpclient = createInstance(connectTimeout, readTimeout);
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
    /********** post form end **********/
    
    /********** post json start **********/
    public static String postJsonObj(String url, Object data)
			throws AppCheckException {
    	return postJson(url,JSON.toJSONString(data),0,0,null);
    }
    
    public static String postJson(String url, String data)
			throws AppCheckException {
    	return postJson(url,data,0,0,null);
    }
    public static String postJson(String url, String data, int connectTimeout, int readTimeout,Header[] headers)
    		throws AppCheckException
    {
    	return doPostJson(url,data,0,0,headers);
    }
    
    public static String postJson(String url, Object data,Header[] headers)
			throws AppCheckException {
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
			throws AppCheckException {
		if(!StringUtil.isUrl(url)) {
    		throw new AppCheckException("1002","it is not a url:"+url);
    	}
		CloseableHttpClient httpclient = null;
		HttpPost httppost = null;
		try {
			httpclient = createInstance(connectTimeout, readTimeout);
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
			return EntityUtils.toString(result.getEntity());
		} catch (Exception e) {
			 throw new AppCheckException("1001","doPostJson error",e);
		} finally {
			if (httppost != null) {
				httppost.releaseConnection();
			}
		}
	} /********** post json end **********/
    
    
    
    /********** get form start **********/
    public static String get(String url, Map<String, String> params) throws Exception {
    	return doGet(url,params,0,0,null);
    }
    
    public static String get(String url, Map<String, String> params,Header[] headers) throws Exception {
    	return doGet(url,params,0,0,headers);
    }
    
    public static String doGet(String url, Map<String, String> params, int connectTimeout, 
    		int readTimeout,Header[] headers) throws Exception {
    	if(!StringUtil.isUrl(url)) {
    		throw new AppCheckException("1002","it is not a url:"+url);
    	}
        CloseableHttpClient httpclient = null;
        HttpGet httpget = null;
        try {
            httpclient = createInstance(connectTimeout, readTimeout);
            String urlPramStr = null;
            if(params == null || params.size() == 0) {
            }else {
            	// 创建参数队列
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                for (String key : params.keySet()) {
                    formparams.add(new BasicNameValuePair(key, params.get(key)));
                }
                urlPramStr = URLEncodedUtils.format(formparams, "utf-8");
                if(-1==url.indexOf("?")) {
                	url = url+"?"+urlPramStr;
                }else {
                	url = url+"&"+urlPramStr;
                }
                
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
        	throw new AppCheckException("1001","doGet error",e);
        } finally {
            if(httpget != null) {
                httpget.releaseConnection();
            }
        }
        return "";
    } /********** get form end **********/
    
    
    /********** post file start **********/
    public static String postFile(String url,Map<String, String> dataMap,Map<String,InputStream> inputs,Header[] headers) throws AppCheckException {
    	return doPostFile(url,dataMap,inputs,0,0,headers);
    }
    
    		
    private static String doPostFile(String url,Map<String, String> dataMap,Map<String,InputStream> inputs, int connectTimeout, int readTimeout,Header[] headers)
			throws AppCheckException {
		if(!StringUtil.isUrl(url)) {
    		throw new AppCheckException("1002","it is not a url:"+url);
    	}
		CloseableHttpClient httpclient = null;
		HttpPost httppost = null;
		try {
			httpclient = createInstance(connectTimeout, readTimeout);
			httppost = new HttpPost(url);
			if(null != headers) {
	        	httppost.setHeaders(headers);
	        }
			
			MultipartEntityBuilder  multipartEntityBuilder = MultipartEntityBuilder.create();
            if(null != inputs && !inputs.isEmpty()) {
            	for(Map.Entry<String, InputStream> entityIn : inputs.entrySet()) {
            		multipartEntityBuilder.addBinaryBody(entityIn.getKey(), entityIn.getValue(),ContentType.create("image/jpeg"),"ocrImage.jpg");
            	}
            	
			}
            
            if(null != dataMap && !dataMap.isEmpty()) {
            	for(Map.Entry<String, String> entityStr : dataMap.entrySet()) {
            		multipartEntityBuilder.addTextBody(entityStr.getKey(), entityStr.getValue());
            	}
            	
			}
            httppost.setEntity(multipartEntityBuilder.build());
			
			HttpResponse result = httpclient.execute(httppost);
			if (result == null) {
				return "";
			}
			// 请求结束，返回结果
			return EntityUtils.toString(result.getEntity());
		} catch (Exception e) {
			 throw new AppCheckException("1010","doPostFile error",e);
		} finally {
			if (httppost != null) {
				httppost.releaseConnection();
			}
		}
	}
    
    /********** post file end **********/
    
   
    
   
	
    
}
