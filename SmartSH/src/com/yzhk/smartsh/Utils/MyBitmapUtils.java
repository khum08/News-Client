package com.yzhk.smartsh.Utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class MyBitmapUtils {

	private NetCacheUtils netCacheUtils;
	private LocalCacheUtils localCacheUtils;
	private MemoryCacheUtils memoryCacheUtils;
	
	public MyBitmapUtils(){
		localCacheUtils = new LocalCacheUtils();
		memoryCacheUtils = new MemoryCacheUtils();
		netCacheUtils = new NetCacheUtils(localCacheUtils,memoryCacheUtils);
	}

	public void display(ImageView iv_photos_listview, String uri) {
		

		Bitmap bitmapCache = memoryCacheUtils.getBitmapCache(uri);
		if (bitmapCache != null) {
			iv_photos_listview.setImageBitmap(bitmapCache);
			System.out.println("照片从内存缓存中获取。。。。。。。。。。。");
			return;
		}

		bitmapCache = localCacheUtils.getBitmapCache(uri);
		if (bitmapCache != null) {
			iv_photos_listview.setImageBitmap(bitmapCache);
			System.out.println("照片从本地缓存中获取。。。");
			return;
		}
		
		System.out.println("照片从网络缓存中获取");
		netCacheUtils.display(iv_photos_listview, uri);

	}

}
