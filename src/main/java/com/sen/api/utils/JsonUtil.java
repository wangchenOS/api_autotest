package com.sen.api.utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonUtil {
	public static String getJsonObject(String jsonStr,List<String> jsonPath) {
		JSONObject jsonObj = JSON.parseObject(jsonStr);
		JSONObject pathObj = jsonObj;
		JSONArray pathArray = null;
		String result = "";
		for(int i = 0; i< jsonPath.size(); i++) {
			String path = jsonPath.get(i);
			if(path.contains("[")){
				int beforeIndex = path.indexOf("[");
				int afterIndex = path.indexOf("]");
				int indexValue = Integer.valueOf(path.substring(beforeIndex+1,afterIndex));
				if(i == jsonPath.size() -1) {
					path =  path.substring(0,beforeIndex);
					pathArray = pathObj.getJSONArray(path);
					result = pathArray.getString(indexValue);
				}else {
					path =  path.substring(0,beforeIndex);
					pathArray = pathObj.getJSONArray(path);
					pathObj = pathArray.getJSONObject(indexValue);
				}
			}
			else 
			{
				if(i == jsonPath.size() -1) {
					result = pathObj.getString(path);
				}else {
					pathObj = pathObj.getJSONObject(path);
				}
			}
		}
		
		return result;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String jsonStr = "{\"data\":{\"customerStatus\":1,\"agentId\":0,\"customerType\":3,\"role\":[\"6\"],\"permissions\":{\"report\":[\"view\"],\"campaign\":[\"*\"],\"adunit\":[\"*\"],\"user\":[\"view\"],\"creative\":[\"*\"],\"customer\":[\"*\"]},\"name\":\"test\",\"customerId\":1000,\"id\":102,\"type\":2,\"email\":\"419370626@qq.com\",\"customerName\":\"北京三块科技在线有限公司\"},\"status\":0}";
		List<String> path = new ArrayList<>();
		path.add("data");
		path.add("permissions");
		path.add("report[0]");
		getJsonObject(jsonStr,path);
	}

}
