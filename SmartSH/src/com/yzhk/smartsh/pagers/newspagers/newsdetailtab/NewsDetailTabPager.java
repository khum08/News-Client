package com.yzhk.smartsh.pagers.newspagers.newsdetailtab;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.viewpagerindicator.CirclePageIndicator;
import com.yzhk.smartsh.R;
import com.yzhk.smartsh.Utils.CacheUtil;
import com.yzhk.smartsh.activity.NewsContent;
import com.yzhk.smartsh.domain.CategroiesData.SubChildren;
import com.yzhk.smartsh.domain.NewsTabData;
import com.yzhk.smartsh.domain.NewsTabData.News;
import com.yzhk.smartsh.global.GlobalConstant;
import com.yzhk.smartsh.pagers.newspagers.BaseNewsDetailPager;
import com.yzhk.smartsh.view.PullToRefreshListView;
import com.yzhk.smartsh.view.PullToRefreshListView.OnRefreshListener;

public class NewsDetailTabPager extends BaseNewsDetailPager {

	public SubChildren subCildren;
	public View mRootView;
	private ViewPager vp_news_detail_tab;
	private MyAdapter mAdapter;
	public NewsTabData newsTabData;
	private TextView tv_newstab_imagename;
	private CirclePageIndicator mIndicator;
	private ArrayList<News> newsList;
	private PullToRefreshListView lv_tabnews;
	private MyListViewAdapter mListViewAdapter;
	private View listViewHeader;
	private ArrayList<News> moreNewsList;
	private String moreUrl;
	private String moreDataUrl;
	private SharedPreferences mPref;
	private Handler mHandler;


	public NewsDetailTabPager(Context context, SubChildren subCildren) {
		super(context);
		this.subCildren = subCildren;
	}

	@Override
	public View initView() {
		// mView = new TextView(context);
		// mView.setGravity(Gravity.CENTER);

		mRootView = View.inflate(context, R.layout.view_news_tabs, null);
		lv_tabnews = (PullToRefreshListView)mRootView.findViewById(R.id.lv_tabnews);
		listViewHeader = View.inflate(context, R.layout.view_listview_header, null);
		lv_tabnews.addHeaderView(listViewHeader);
		return mRootView;
	}
	
	/**
	 * 初始化数据，注意此方法的调用时机（易空指针）
	 */
	@Override
	public void initData() {
		
		vp_news_detail_tab = (ViewPager) listViewHeader.findViewById(R.id.vp_news_detail_tab);
		tv_newstab_imagename = (TextView)listViewHeader.findViewById(R.id.tv_newstab_imagename);
		
		String cache = CacheUtil.getCache(context, GlobalConstant.SERVER+subCildren.url);
		if(!TextUtils.isEmpty(cache)){
			parseJson(cache,false);
		}
		getDataFromServer(GlobalConstant.SERVER+subCildren.url);
		
		mPref = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}
	
	/**
	 * 向服务器请求数据
	 * @param url
	 */
	private void getDataFromServer(final String url) {
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(context, arg1, Toast.LENGTH_SHORT).show();
				arg0.printStackTrace();
				lv_tabnews.refreshComplete(false);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				CacheUtil.putCache(context, url, result);
				parseJson(result, false);
				lv_tabnews.refreshComplete(true);
			}
		});
	}
	//加载更多数据
	protected void getMoreDataFromServer() {
		HttpUtils http = new HttpUtils();

		http.send(HttpMethod.GET, moreDataUrl, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				arg0.printStackTrace();
				lv_tabnews.refreshComplete(false);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				parseJson(result, true);
				lv_tabnews.refreshComplete(true);
			}
		});
		
		
	}
	
	/**
	 * 出入json数据，解析并填充视图
	 * @param result
	 */
	public void parseJson(String result ,boolean isMore) {
		Gson gson = new Gson();
		newsTabData = gson.fromJson(result, NewsTabData.class);
		moreUrl = newsTabData.data.more;
		
		if(!TextUtils.isEmpty(moreUrl)){
			moreDataUrl = GlobalConstant.SERVER+moreUrl;
		}else{
			moreDataUrl = null;
		}
		if(!isMore){
			if(newsTabData!=null){
				initTabViewPager();
				initListView();
			}
		}else{
			
			if(newsTabData!=null){
				moreNewsList = newsTabData.data.news;
				newsList.addAll(moreNewsList);
				mListViewAdapter.notifyDataSetChanged();
			}
		}
		
	}

	/**
	 * 初始化listview视图
	 */
	private void initListView() {
		newsList = newsTabData.data.news;
		
		mListViewAdapter = new MyListViewAdapter();
		lv_tabnews.setAdapter(mListViewAdapter);
		
		lv_tabnews.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void listen() {
				new Thread(){
					public void run() {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						getDataFromServer(GlobalConstant.SERVER + subCildren.url);
					};
				}.start();
			}

			@Override
			public void onLoadMore() {
				if(moreDataUrl==null){
					Toast.makeText(context, "没有更多数据了", Toast.LENGTH_SHORT).show();
					lv_tabnews.refreshComplete(true);
				}else{
					getMoreDataFromServer();
				}
			}
		});
		
		//点击侦听，记录已读功能
		lv_tabnews.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int headerViewsCount = lv_tabnews.getHeaderViewsCount();
				position = position - headerViewsCount;
				int newsId = newsList.get(position).id;
				String read_ids = mPref.getString("read_ids", "");
				if(!read_ids.contains(newsId+"")){
					read_ids = read_ids + newsId+",";
					mPref.edit().putString("read_ids", read_ids);
				}
				
				TextView tv_listview_title = (TextView) view.findViewById(R.id.tv_listview_title);
				tv_listview_title.setTextColor(Color.GRAY);
				
				Intent intent = new Intent(context, NewsContent.class);
				intent.putExtra("url", newsList.get(position).url);
				context.startActivity(intent);
			}
		});
		
		
	}
	
	

	/**
	 * 初始化页签的viewpage视图
	 */
	public void initTabViewPager() {
		mAdapter = new MyAdapter();
		vp_news_detail_tab.setAdapter(mAdapter);
		
		mIndicator = (CirclePageIndicator)listViewHeader.findViewById(R.id.indicator);
		mIndicator.setViewPager(vp_news_detail_tab);
		
		mIndicator.setCurrentItem(0);
		String title = newsTabData.data.topnews.get(0).title;
		tv_newstab_imagename.setText(title);
		
		mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				String title = newsTabData.data.topnews.get(position).title;
				tv_newstab_imagename.setText(title);
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		
		//广告图轮播
		if(mHandler == null){
			mHandler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					int currentItem = vp_news_detail_tab.getCurrentItem();
					currentItem++;
					if(currentItem > vp_news_detail_tab.getAdapter().getCount()-1){
						currentItem = 0 ;
					}
					vp_news_detail_tab.setCurrentItem(currentItem);
					
					mHandler.sendEmptyMessageDelayed(0, 2000);
				}
			};
			mHandler.sendEmptyMessageDelayed(0, 2000);
		}
		//手指拖动广告图逻辑
		vp_news_detail_tab.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mHandler.removeCallbacksAndMessages(null);
					break;
				case MotionEvent.ACTION_UP:
					mHandler.sendEmptyMessageDelayed(0, 2000);
					break;
				case MotionEvent.ACTION_CANCEL:
					mHandler.sendEmptyMessageDelayed(0, 2000);
					break;

				default:
					break;
				}
				return false;
			}
		});
		
	}
	
	/**
	 * listview 适配器
	 * @author 大傻春
	 *
	 */
	class MyListViewAdapter extends BaseAdapter{
		
		private BitmapUtils bitmap;

		public MyListViewAdapter(){
			bitmap = new BitmapUtils(context);
			bitmap.configDefaultLoadFailedImage(R.drawable.news_pic_default);
			bitmap.configDefaultLoadingImage(R.drawable.news_pic_default);
		}
		@Override
		public int getCount() {
			return newsList.size();
		}

		@Override
		public News getItem(int position) {
			return newsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			MyViewHolder mViewHolder;
			if(convertView==null){
				mViewHolder = new MyViewHolder();
				convertView = View.inflate(context, R.layout.listview_newstab, null);
				mViewHolder.iv_listview_newstab = (ImageView) convertView.findViewById(R.id.iv_listview_newstab);
				mViewHolder.tv_listview_title = (TextView) convertView.findViewById(R.id.tv_listview_title);
				mViewHolder.tv_listview_date = (TextView) convertView.findViewById(R.id.tv_listview_date);
				convertView.setTag(mViewHolder);
			}else{
				mViewHolder = (MyViewHolder) convertView.getTag();
			}
			bitmap.display(mViewHolder.iv_listview_newstab, GlobalConstant.SERVER+getItem(position).listimage);
			mViewHolder.tv_listview_title.setText(getItem(position).title);
			mViewHolder.tv_listview_date.setText(getItem(position).pubdate);
			
			String read_ids = mPref.getString("read_ids", "");
			if(read_ids.contains(getItem(position).id+"")){
				mViewHolder.tv_listview_title.setTextColor(Color.GRAY);
			}
			
			return convertView;
		}
		
	}
	static class MyViewHolder{
		public ImageView iv_listview_newstab;
		public TextView tv_listview_title;
		public TextView tv_listview_date;
	}
	
	/**
	 * viewpager适配器
	 * @author 大傻春
	 *
	 */
	class MyAdapter extends PagerAdapter{
		
		private BitmapUtils bitmap;
		public MyAdapter(){
			bitmap = new BitmapUtils(context);
			bitmap.configDefaultLoadingImage(R.drawable.topnews_item_default);
			bitmap.configDefaultLoadFailedImage(R.drawable.topnews_item_default);
		}
		@Override
		public int getCount() {
			return newsTabData.data.topnews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView iv = new ImageView(context);
			iv.setScaleType(ScaleType.FIT_XY);
			
			bitmap.display(iv, GlobalConstant.SERVER+newsTabData.data.topnews.get(position).topimage);
			container.addView(iv);
			return iv;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
	}

}

















