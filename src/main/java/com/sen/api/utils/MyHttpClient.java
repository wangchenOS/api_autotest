package com.sen.api.utils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public abstract class MyHttpClient {
	protected static final String DEFALUT_ENCODE = "UTF-8";

	protected  CloseableHttpClient httpClient = null;
	protected  HttpClientContext context = null;
	protected  CookieStore cookieStore = null;
	protected  RequestConfig requestConfig = null;

	
	protected void init() {
		context = HttpClientContext.create();
		cookieStore = new BasicCookieStore();
		requestConfig = RequestConfig.custom().setConnectTimeout(3000).setSocketTimeout(3000)
				.setConnectionRequestTimeout(3000).build();
		// 设置默认跳转以及存储cookie
		httpClient = HttpClientBuilder.create().setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
				.setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)
				.setDefaultCookieStore(cookieStore).build();
		/*
		 * Cookie cookie = new Cookie() {
		 * 
		 * @Override public boolean isSecure() { // TODO Auto-generated method stub
		 * return false; }
		 * 
		 * @Override public boolean isPersistent() { // TODO Auto-generated method stub
		 * return false; }
		 * 
		 * @Override public boolean isExpired(Date date) { // TODO Auto-generated method
		 * stub return false; }
		 * 
		 * @Override public int getVersion() { // TODO Auto-generated method stub return
		 * 0; }
		 * 
		 * @Override public String getValue() { // TODO Auto-generated method stub
		 * return "8753db3c-7c6f-4f13-b78b-492a847e5511"; }
		 * 
		 * @Override public int[] getPorts() { // TODO Auto-generated method stub return
		 * null; }
		 * 
		 * @Override public String getPath() { // TODO Auto-generated method stub return
		 * "/"; }
		 * 
		 * @Override public String getName() { // TODO Auto-generated method stub return
		 * "dspuid"; }
		 * 
		 * @Override public Date getExpiryDate() { // TODO Auto-generated method stub
		 * return null; }
		 * 
		 * @Override public String getDomain() { // TODO Auto-generated method stub
		 * return "qa-dsp2.suanshubang.com"; }
		 * 
		 * @Override public String getCommentURL() { // TODO Auto-generated method stub
		 * return null; }
		 * 
		 * @Override public String getComment() { // TODO Auto-generated method stub
		 * return null; } }; cookieStore.addCookie(cookie);
		 */

	}

	public abstract ClientWrapper sendHttpGet(String url) throws Exception;

	public abstract ClientWrapper sendHttpPost(String url, String body) throws Exception;
	
	public abstract ClientWrapper sendHttpPost(String url, List<NameValuePair> pairs) throws Exception;


	public void resetClient() throws IOException {
		ReportUtil.log("MyHttpClient reset");
		httpClient.close();
		httpClient = null;
		cookieStore = null;
		requestConfig = null;
		init();
	}
	
	
}
