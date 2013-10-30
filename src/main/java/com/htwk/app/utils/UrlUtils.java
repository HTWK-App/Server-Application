package com.htwk.app.utils;

public class UrlUtils {

	public static String getHtwkUrl(String url){
		return getValidUrl(url, "http://www.htwk-leipzig.de/");
	}
	
	private static String getValidUrl(String url, String host){
		if (!url.startsWith("http://")){
			return host+url;
		}
		return url;
	}
}
