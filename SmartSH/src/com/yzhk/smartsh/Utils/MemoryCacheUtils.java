package com.yzhk.smartsh.Utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryCacheUtils {
	
//	private static HashMap<String, WeakReference<Bitmap>> map = new HashMap<String, WeakReference<Bitmap>>();
	private LruCache<String, Bitmap> lruCache;
	
	public MemoryCacheUtils(){
		long maxMemory = Runtime.getRuntime().maxMemory();
		lruCache = new LruCache<String, Bitmap>((int) (maxMemory/8));
	}
	/**
	 * 将图片缓存至内存中
	 * @param url
	 * @param bitmap
	 */
	public void setBitmapCache(String url,Bitmap bitmap){
//		WeakReference<Bitmap> soft = new WeakReference<Bitmap>(bitmap);
//		map.put(url, soft);
		lruCache.put(url, bitmap);
	}
	
	/**
	 * 将缓存在内存中的图片取出
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapCache(String url){
//		Bitmap bitmap = null;
//		WeakReference<Bitmap> soft = map.get(url);
//		if(soft!=null){
//			bitmap = soft.get();
//		}
		System.out.println("lruCache的方法被调用了"+lruCache.get(url));
		
		return lruCache.get(url);
	}

}
