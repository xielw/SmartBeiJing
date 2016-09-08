package com.itheima.zhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

	public static boolean getBoolean(String key,Context context,boolean defValue){
		
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}
	
	public static void setBoolean(String key,Context context,boolean value){
		
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
		
	}
	
public static int getInt(String key,Context context,int defValue){
		
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}
	
	public static void setInt(String key,Context context,int value){
		
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
		
	}
	
public static String getString(String key,Context context,String defValue){
		
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}
	
	public static void setString(String key,Context context,String value){
		
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
		
	}
}
