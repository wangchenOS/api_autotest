package com.sen.api.utils;

import org.apache.http.HttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;

public class ClientWrapper {

	private CloseableHttpClient httpClient;
	private String responseBody;
	private HttpEntity entity;
	private int responseCode;
	
	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}
	public void setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}
	public String getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
	public HttpEntity getEntity() {
		return entity;
	}
	public void setEntity(HttpEntity entity) {
		this.entity = entity;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	
	
	
}
