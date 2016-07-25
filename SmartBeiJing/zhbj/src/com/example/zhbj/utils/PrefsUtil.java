package com.example.zhbj.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefsUtil {
      
	public static boolean getBoolean(Context ctx,String key,Boolean defValue){
		SharedPreferences sp = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
	    return sp.getBoolean(key, defValue);
	}
	
	public static void  putBoolean(Context ctx,String key,Boolean value){
		SharedPreferences sp = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
	    sp.edit().putBoolean(key, value).commit();
	}

	public static void putString(Context ctx, String key,String value) {
		SharedPreferences sp = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
	    sp.edit().putString(key, value).commit();
		
	}
	
	public static String getString(Context ctx,String key,String defValue){
		SharedPreferences sp = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
	    return sp.getString(key, defValue);
	}
}
