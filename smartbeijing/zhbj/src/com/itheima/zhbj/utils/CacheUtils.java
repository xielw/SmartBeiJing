package com.itheima.zhbj.utils;

import android.content.Context;

public class CacheUtils {

	public static void setCache(Context context,String url,String json){
		
		PrefUtils.setString(url, context, json);
	}
	
	
	public static String getCache(Context context,String url){
		
		return PrefUtils.getString(url, context, null);
	}
}
