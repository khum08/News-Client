package com.yzhk.smartsh.Utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.yzhk.smartsh.R;

public class NetCacheUtils {

	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtil;

	public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
		mLocalCacheUtils = localCacheUtils;
		mMemoryCacheUtil= memoryCacheUtils;
	}

	public void display(ImageView iv_photos_listview, String uri) {
		iv_photos_listview.setImageResource(R.drawable.pic_item_list_default);

		new MyAsnycTask().execute(iv_photos_listview, uri);
	}

	class MyAsnycTask extends AsyncTask<Object, Void, Bitmap> {

		private ImageView iv_photos_listview;
		private String uri;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Bitmap doInBackground(Object... object) {
			iv_photos_listview = (ImageView) object[0];
			uri = (String) object[1];
			iv_photos_listview.setTag(uri);

			Bitmap bitmap = download(uri);

			return bitmap;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			mLocalCacheUtils.setBitmapCache(uri, result);
			mMemoryCacheUtil.setBitmapCache(uri, result);
			
			String tag = (String) iv_photos_listview.getTag();
			if (uri.equals(tag)) {
				iv_photos_listview.setImageBitmap(result);
			}

		}
	}

	/**
	 * 用HttpURLConnecttion下载图片
	 * 
	 * @param uri
	 * @return
	 */
	public Bitmap download(String uri) {
		URL url;
		HttpURLConnection conn = null;
		Bitmap bitmap = null;
		try {
			url = new URL(uri);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.connect();

			if (conn.getResponseCode() == 200) {
				InputStream inputStream = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(inputStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return bitmap;
	}

}
