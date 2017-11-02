package com.yzhk.smartsh.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class LocalCacheUtils {

	public static final String LOCAL_CACHE_PATH = Environment
			.getExternalStorageDirectory() + "/smartsh_cache";

	
	/**
	 * 设置本地缓存
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void setBitmapCache(String url, Bitmap bitmap) {
		File file = new File(LocalCacheUtils.LOCAL_CACHE_PATH);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
		String encode = MD5Util.encode(url);
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File(file, encode));
			bitmap.compress(CompressFormat.JPEG, 100, os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * 获取本地缓存的图片
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapCache(String url) {
		Bitmap bitmap = null;

		String encode = MD5Util.encode(url);
		File file = new File(LocalCacheUtils.LOCAL_CACHE_PATH, encode);
		if (file.exists()) {
			InputStream is = null;
			try {
				is = new FileInputStream(file);
				bitmap = BitmapFactory.decodeStream(is);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

		return bitmap;
	}

}
