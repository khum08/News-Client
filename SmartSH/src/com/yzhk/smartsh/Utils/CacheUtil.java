package com.yzhk.smartsh.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtil {
	
	/**
	 * 以url的md5值为key，target为value，作为缓存存入sharedpreferences
	 * @param context
	 * @param url
	 * @param target
	 */
	public static void putCache(Context context,String url,String target){
		
		SharedPreferences mPref = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		String key = MD5Util.encode(url);
		mPref.edit().putString(key, target).commit();
	}
	
	/**
	 * 以url的md5值为key，获取存入sharedpreferences的缓存，不存在时返回“”；
	 * @param context
	 * @param url
	 * @return
	 */
	public static String getCache(Context context,String url){
		SharedPreferences mPref = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		String key = MD5Util.encode(url);
		String cache = mPref.getString(key,null);
		return cache;
	}
}
