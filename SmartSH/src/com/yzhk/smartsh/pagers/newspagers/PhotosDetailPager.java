package com.yzhk.smartsh.pagers.newspagers;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yzhk.smartsh.R;
import com.yzhk.smartsh.Utils.CacheUtil;
import com.yzhk.smartsh.Utils.MyBitmapUtils;
import com.yzhk.smartsh.domain.PhotosData;
import com.yzhk.smartsh.domain.PhotosData.News;
import com.yzhk.smartsh.global.GlobalConstant;

public class PhotosDetailPager extends BaseNewsDetailPager {

	private ListView lv_photos;
	private GridView gv_photos;
	private MyAdapter mAdapter;
	private ArrayList<News> news;
	private ImageButton ib_photos;
	private boolean isGrid;
	
	public PhotosDetailPager(Context context,ImageButton ib_photos) {
		super(context);
		this.ib_photos = ib_photos;
	}

	@Override
	public View initView() {
		
		View view = View.inflate(context, R.layout.view_photos, null);
		lv_photos = (ListView) view.findViewById(R.id.lv_photos);
		gv_photos = (GridView) view.findViewById(R.id.gv_photos);
		return view;
	}

	@Override
	public void initData() {
		
		ib_photos.setVisibility(View.VISIBLE);
		getDataFromServer();
		ib_photos.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isGrid){
					isGrid = false;
					ib_photos.setBackgroundResource(R.drawable.icon_pic_grid_type);
					gv_photos.setVisibility(View.INVISIBLE);
					lv_photos.setVisibility(View.VISIBLE);
				}else{
					isGrid = true;
					ib_photos.setBackgroundResource(R.drawable.icon_pic_list_type);
					gv_photos.setVisibility(View.VISIBLE);
					lv_photos.setVisibility(View.INVISIBLE);
				}
				
			}
		});
	}
	
	private void getDataFromServer() {
		HttpUtils http = new HttpUtils();
		final String url = GlobalConstant.PHOTOS_URL;
		http.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(context, arg1, Toast.LENGTH_SHORT).show();
				arg0.printStackTrace();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				CacheUtil.putCache(context, url, result);
				parseJson(result, false);
			}
		});
	}
	
	protected void parseJson(String result, boolean isMore) {
		Gson gson = new Gson();
		PhotosData photosData = gson.fromJson(result, PhotosData.class);
		news = photosData.data.news;
		
		mAdapter = new MyAdapter();
		lv_photos.setAdapter(mAdapter);
		gv_photos.setAdapter(mAdapter);
	}

	class MyAdapter extends BaseAdapter{

//		private BitmapUtils bitmap;
//
//		public MyAdapter(){
//			bitmap = new BitmapUtils(context);
//			bitmap.configDefaultLoadFailedImage(R.drawable.pic_item_list_default);
//			bitmap.configDefaultLoadingImage(R.drawable.pic_item_list_default);
//		}
		private MyBitmapUtils mBitmapUtils;
		public MyAdapter(){
			mBitmapUtils = new MyBitmapUtils();
			
		}
		@Override
		public int getCount() {
			return news.size();
		}

		@Override
		public News getItem(int position) {
			return news.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder mViewHolder;
			if(convertView==null){
				mViewHolder = new ViewHolder();
				convertView = View.inflate(context, R.layout.adapter_photos, null);
				mViewHolder.iv_photos_listview = (ImageView) convertView.findViewById(R.id.iv_photos_listview);
				mViewHolder.tv_photos_title = (TextView) convertView.findViewById(R.id.tv_photos_title);
				convertView.setTag(mViewHolder);
				
			}else{
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			
			String uri = GlobalConstant.SERVER +getItem(position).listimage;
//			bitmap.display(mViewHolder.iv_photos_listview, uri);
			mBitmapUtils.display(mViewHolder.iv_photos_listview, uri);
			
			mViewHolder.tv_photos_title.setText(getItem(position).title);
			
			return convertView;
		}
		
	}
	static class ViewHolder{
		public ImageView iv_photos_listview;
		public TextView tv_photos_title;
	}
	
}










