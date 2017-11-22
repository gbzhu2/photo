package com.gbzhu.edu.elp.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

import com.gbzhu.edu.elp.common.exception.ELPSysException;

/**
 * http请求帮助类
 *
 * @author yywang5
 */
public class HttpClientUtils {

    /**
     * http get请求
     *
     * @param url 请求地址
     * @return http响应字符串
     */
    public static String httpGetString(String url) {
        Assert.notNull(url, "url is required!");
        return baseHttpGetString(url, null);
    }

    /**
     * http post 请求
     *
     * @param url    请求地址
     * @param params 参数
     * @return http响应字符串
     */
    public static String httpPostString(String url, Map<String, String> params) {
        Assert.notNull(url, "url is required!");

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = getHttpPost(url, params);
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            return EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            throw new ELPSysException("http请求错误", e);
        }
    }
    
    /**
     * http post 请求
     *
     * @param url    请求地址
     * @param params 参数
     * @param outTime 超时时间(ms)
     * @return http响应字符串
     */
    public static String httpPostString(String url, Map<String, String> params, int outTime) {
        Assert.notNull(url, "url is required!");
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, outTime);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, outTime);
        HttpPost httpPost = getHttpPost(url, params);
        HttpResponse response = null;
        try { 
            response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            return EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            throw new ELPSysException("http请求错误", e);
        }
    }

    /**
     * 获取httppost
     *
     * @param url    the url
     * @param params 参数
     * @return the httpost
     */
    private static HttpPost getHttpPost(String url, Map<String, String> params) {

        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }

        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            throw new ELPSysException("http请求错误", e);
        }

        return httpost;
    }

    /**
     * httpGet请求
     *
     * @param url    发送请求地址
     * @param params 参数列表
     * @return http响应字符串
     */
    public static String httpGetString(String url, Map<String, String> params) {
        Assert.notNull(url, "url is required!");
        Assert.notNull(params, "params is required!");
        return baseHttpGetString(url, params);
    }

    /**
     * http get 请求
     *
     * @param url 请求地址
     * @return 响应流
     */
    public static InputStream httpGetStream(String url) {
        Assert.notNull(url, "url is required!");
        return baseHttpGetStream(url, null);
    }

    /**
     * http get 请求
     *
     * @param url    请求地址
     * @param params 参数
     * @return 响应流
     */
    public static InputStream httpGetStream(String url,
                                            Map<String, String> params) {
        Assert.notNull(url, "url is required!");
        Assert.notNull(params, "params is required!");
        return baseHttpGetStream(url, params);
    }

    /**
     * httpGet请求
     *
     * @param url    发送请求地址
     * @param params 参数列表
     * @return http响应字符串
     */
    private static String baseHttpGetString(String url,
                                            Map<String, String> params) {
        InputStream inputStream = baseHttpGetStream(url, params);
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        int data = -1;
        try {
            //跳过bom头
            byte[] bytes = new byte[3];
            for (int index = 0; index < bytes.length; index++) {
                data = inputStream.read();
                if (data != -1) {
                    bytes[index] = (byte) data;
                } else {
                    break;
                }
            }
            if (!(bytes.length == 3 && (bytes[0] & 0xFF) == 0xEF && (bytes[1] & 0xFF) == 0xBB
                    && (bytes[2] & 0xFF) == 0xBF)) {
                for (int index = 0; index < bytes.length; index++) {
                    byteArray.write(bytes[index]);
                }
            }
            while ((data = inputStream.read()) != -1) {
                byteArray.write(data);
            }
            return byteArray.toString();
        } catch (IOException e) {
            throw new ELPSysException("http请求错误", e);
        }

    }

    /**
     * http get获取响应流
     *
     * @param url    get请求地址
     * @param params 参数
     * @return httpget响应流
     */
    private static InputStream baseHttpGetStream(String url,
                                                 Map<String, String> params) {
        String requestUrl = "";
        if (params != null) {
            requestUrl = buildRequestUrl(url, params);
        } else {
            requestUrl = url;
        }
        HttpGet httpGet = new HttpGet(requestUrl);
        HttpClient httpClient =new DefaultHttpClient();
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();
            return inputStream;
        } catch (ClientProtocolException e) {
            throw new ELPSysException("http请求错误", e);
        } catch (IOException e) {
            throw new ELPSysException("http请求错误", e);
        }
    }

    /**
     * 构建url
     *
     * @param url    url地址
     * @param params 参数
     * @return 构建完成的url
     */
    private static String buildRequestUrl(String url, Map<String, String> params) {
        String requestUrl = url;
        if (params.size() > 0) {
            requestUrl += "?";
        }
        Iterator<String> iterator = params.keySet().iterator();
        boolean isFirst = true;
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = params.get(key);
            if (!isFirst) {
                requestUrl += "&";
            }
            requestUrl = requestUrl + key + "=" + value;
            isFirst = false;
        }
        return requestUrl;
    }
    
    /**
     * 获取全路径
     * @param url 原路径
     * @param baseUrl 基础路径
     * @return 全路径
     */
    public static String getFullUrl(String url, String baseUrl) {
    	if(url.startsWith("http://") || url.startsWith("https://")) {
    		return url;
    	} else {
    		String preUrl = baseUrl;
    		if(preUrl.endsWith("/")) {
    			preUrl = preUrl.substring(0, preUrl.length() - 1);
    		}
    		if(!url.startsWith("/")) {
    			preUrl = preUrl + "/";
    		}
    		String fullUrl = preUrl + url;
    		if(fullUrl.endsWith("/")) {
    			fullUrl = fullUrl.substring(0, fullUrl.length() - 1);
    		}
    		return fullUrl;
    	}
    }

    /**
     * 获取全路径
     * @param url 原路径
     * @param request request
     * @return 全路径
     */
    public static String getFullUrl(String url, HttpServletRequest request) {
    	if("80".equals(request.getServerPort())) {
    		return getFullUrl(url, request.getScheme() + "://" + request.getServerName());
    	} else {
    		return getFullUrl(url, request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());
    	}
    }
}
