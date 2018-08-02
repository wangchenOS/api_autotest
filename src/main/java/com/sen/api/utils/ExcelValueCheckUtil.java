package com.sen.api.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.Assert;

public class ExcelValueCheckUtil {
	private static final String DEFAULT_HEAD_PATTERN = "^\\$\\.head(\\.\\w+(\\[\\d+\\])?)+";
	private static final String DEFAULT_BODY_PATTERN = "^\\$\\.body(\\.\\w+(\\[\\d+\\])?)+";
	private static final String DEFAULT_VALUE_PATTERN = "^\\w+";
	private static List<String> headPath= new ArrayList<>();
	private static List<String> bodyPath= new ArrayList<>();
	
	public static boolean checkVerifyString(List<String> verifyStrList ) {
		if (verifyStrList == null || verifyStrList.size() == 0)
			return false;
		
		for(String str : verifyStrList) {
			int index = str.indexOf("=");
		    if (index == -1) {
		    	return false;
		    }
		    str = str.substring(0,index);
		    if (str.contains("head")) {
		    	Pattern pattern = Pattern.compile(DEFAULT_HEAD_PATTERN);
		    	Matcher m = pattern.matcher(str);
		    	if(!m.matches()) {
		    		return false;
		    	}
		    	/*headPath = getPath(str.substring(0,index));
		    	headPath.add("=");
		    	headPath.add(value);*/
		    }else {
		    	Pattern pattern = Pattern.compile(DEFAULT_BODY_PATTERN);
		    	Matcher m = pattern.matcher(str);
		    	if(!m.matches()) {
		    		return false;
		    	}
		    /*	bodyPath = getPath(str.substring(0,index));
		    	bodyPath.add("=");
		    	bodyPath.add(value);*/
		    }
		}
		
	    return true;
	}
	
	
	public static boolean checkMethodString(String method) {
		if (method == null || method.equals("")) {
			return false;
		}
		String methodStr = method.toLowerCase();
		switch (methodStr) {
			case "get":
			case "post":
			case "put":
			case "delete":	
				return true;
			default:
				return false;
		}
		
		
	}
	
	private static List<String> getPath(String str){
		if (str == null || str.length() == 0)
			return null;
		String[] strArray = str.split("\\.");
		List<String> result = new ArrayList<>();
		for (int i = 2; i< strArray.length; i++) {
			result.add(strArray[i]);
		}
		
		return result;
	}

	public static List<String> getHeadPath() {
		return headPath;
	}

	public static List<String> getBodyPath() {
		return bodyPath;
	}
	
	
}
