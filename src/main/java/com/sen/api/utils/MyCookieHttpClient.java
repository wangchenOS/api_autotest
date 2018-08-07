package com.sen.api.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**
 * 保持会话的第一种方法：如果用的是同一个HttpClient且没去手动连接放掉client.getConnectionManager().shutdown();
 * 都不用去设置cookie的ClientPNames.COOKIE_POLICY。httpclient都是会保留cookie的
 * 
 */

public class MyCookieHttpClient extends MyHttpClient{
	private Header[] headers;
	
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
	}
	
	
	public boolean login(String url, Map<String, String> map)
			throws Exception {
		if (map == null || map.isEmpty())
			return false;
		String loginJson = "{";
		for (Map.Entry<String, String> entry : map.entrySet()) { 
			  System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
			  loginJson += entry.getKey() + ":" +  "\"" + entry.getValue() + "\"," ;
			}
		loginJson = loginJson.substring(0, loginJson.length() - 1);
		loginJson += "}";

		ReportUtil.log("loginJson:" + loginJson);
	
		ClientWrapper client = null;
		client = sendHttpPostJsonBody(url, loginJson, false);

		try {
			JSONObject jsonStr = JSONObject.parseObject(client.getResponseBody());
			if (jsonStr != null && jsonStr.getInteger("status") == 0)
				return true;
			return false;

		}catch (Exception e) {
			ReportUtil.log("Response Error:" + client.getResponseBody());
			throw new Exception("Parse login response error!");
		}

	}
	
	public ClientWrapper sendHttpGetBinary(String url,boolean isNeedClose) throws Exception {
		
		//httpClient = HttpClients.createDefault();
		/*RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)   //设置连接超时时间
                .setConnectionRequestTimeout(3000) // 设置请求超时时间
                .setSocketTimeout(3000)
                .setRedirectsEnabled(true)//默认允许自动重定向
                .build();*/
		if (httpClient == null)
			init();

	    HttpGet httpGet = new HttpGet(url);
		
	    CloseableHttpResponse response = httpClient.execute(httpGet);
	    
		HttpEntity entity = response.getEntity();

		ClientWrapper client = new ClientWrapper();
		client.setHttpClient(httpClient);
		client.setEntity(entity);
		return client;
	}
	
	
	
	private ClientWrapper sendHttpGet(String url,boolean isNeedClose) throws Exception {
		
		if (httpClient == null)
			init();

	    HttpGet httpGet = new HttpGet(url);

		
	    CloseableHttpResponse response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		String responseContent = EntityUtils.toString(entity, "UTF-8");
	    //response.close();
		
		
		ClientWrapper client = new ClientWrapper();
		client.setHttpClient(httpClient);
		client.setEntity(entity);
		client.setResponseBody(responseContent);
		return client;
	}
	
	public ClientWrapper sendHttpGet(String url) throws Exception {
		return sendHttpGet(url, false);
	}
	
	
	@Override
	public ClientWrapper sendHttpPost(String url, String body)
			throws Exception {
		// TODO Auto-generated method stub
		
		return sendHttpPostJsonBody(url, body, false);
		
	}

	public ClientWrapper sendHttpPost(String url, List<NameValuePair> pairs)
			throws Exception {
		// TODO Auto-generated method stub
		return sendHttpPostFormBody(url, pairs, false);
	}
	
	
	

	private ClientWrapper sendHttpPostJsonBody(String url, String body,boolean isNeedClose) throws Exception {
		if (httpClient == null)
			init();
		HttpPost httpPost = new HttpPost(url);
		if (headers != null)
			httpPost.setHeaders(headers);
		httpPost.setEntity(new StringEntity(body));

		CloseableHttpResponse response = httpClient.execute(httpPost);
		
		int responseCode = response.getStatusLine().getStatusCode();
		HttpEntity entity = response.getEntity();
		
		String responseContent = EntityUtils.toString(entity, DEFALUT_ENCODE);

		//response.close();
		if (isNeedClose)
			httpClient.close();
		
		ClientWrapper client = new ClientWrapper();
		client.setHttpClient(httpClient);
		client.setEntity(entity);
		client.setResponseCode(responseCode);
		client.setResponseBody(responseContent);
		return client;
	}

	private ClientWrapper sendHttpPostFormBody(String url, List<NameValuePair> pairs, boolean isNeedClose) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);

		UrlEncodedFormEntity formEntity = null;

		formEntity = new UrlEncodedFormEntity(pairs, DEFALUT_ENCODE);
		httpPost.setEntity(formEntity);
		// 获取响应信息
		CloseableHttpResponse response = httpClient.execute(httpPost);

		HttpEntity entity = response.getEntity();
		String responseContent = EntityUtils.toString(entity, "UTF-8");

		response.close();
		
		if (isNeedClose) {
			httpClient.close();
		}
		
		ClientWrapper client = new ClientWrapper();
		client.setHttpClient(httpClient);
		client.setResponseBody(responseContent);
		return client;
	}


	public static Map<String, String> getLoginCookies(String loginURL, List<NameValuePair> pairs,
			CloseableHttpClient httpClient) {
		int statusCode = 0;
		String retStr = null;
		Map<String, String> loginMap = new HashMap<String, String>();
		// 采用post方法
		HttpPost post = new HttpPost(loginURL);
		// 设置body
		UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(pairs, "UTF-8");
			post.setEntity(entity);
			// 获取响应信息
			CloseableHttpResponse response = httpClient.execute(post);
			statusCode = response.getStatusLine().getStatusCode();
			retStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			// 将获取的值对放入map中返回给调用方
			loginMap.put(loginURL, retStr);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return loginMap;
	}

	
	public ClientWrapper sendHttpRequest(String url,String method,String body) throws Exception {
		String methodStr = method.toLowerCase();
		switch (methodStr) {
		case "get":
			return sendHttpGet(url);
		case "put":
			break;
		case "post":
			return sendHttpPost(url, body);
		case "delete":
			break;
		default:
			break;
		}
		
		return null;
	}
	public Header[] getHeaders() {
		return headers;
	}

	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}

	
}