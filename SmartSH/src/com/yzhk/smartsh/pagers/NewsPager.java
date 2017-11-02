package com.yzhk.smartsh.pagers;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yzhk.smartsh.Utils.CacheUtil;
import com.yzhk.smartsh.activity.HomeActivity;
import com.yzhk.smartsh.domain.CategroiesData;
import com.yzhk.smartsh.fragment.LeftMenuFragment;
import com.yzhk.smartsh.global.GlobalConstant;
import com.yzhk.smartsh.pagers.newspagers.BaseNewsDetailPager;
import com.yzhk.smartsh.pagers.newspagers.NewsDetailPager;
import com.yzhk.smartsh.pagers.newspagers.PhotosDetailPager;
import com.yzhk.smartsh.pagers.newspagers.TopicDetailPager;
import com.yzhk.smartsh.pagers.newspagers.interactDetailPager;

public class NewsPager extends BasePager {

	private String TAG = "smartsh";
	public CategroiesData gsonResult;
	public ArrayList<BaseNewsDetailPager> detailPagerList = new ArrayList<BaseNewsDetailPager>();

	public NewsPager(Activity context) {
		super(context);
	}

	@Override
	public void initViewData() {
		tv_viewpager_title.setText("新闻");
		ib_title_btn.setVisibility(View.VISIBLE);

		// 处理缓存
		String cache = CacheUtil.getCache(context, GlobalConstant.CATEGORY_URL);
		if (!TextUtils.isEmpty(cache)) {
			Log.e(TAG, "发现缓存了。。。。");
			parseDataAndSetSilding(cache);
		}
		getDataFromServer();
		
	}

	public void getDataFromServer() {

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, GlobalConstant.CATEGORY_URL,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						arg0.printStackTrace();
						Toast.makeText(context, "服务器未打开，Sorry！",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String jsonResult = responseInfo.result;
						CacheUtil.putCache(context,
								GlobalConstant.CATEGORY_URL, jsonResult);
						parseDataAndSetSilding(jsonResult);
					}
				});
	}

	public void parseDataAndSetSilding(String result) {
		Gson gson = new Gson();
		gsonResult = gson.fromJson(result, CategroiesData.class);
		HomeActivity mainUI = (HomeActivity) context;
		LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
		leftMenuFragment.setListViewData(gsonResult.data);
		
		detailPagerList.add(new NewsDetailPager(context,gsonResult.data.get(0)));
		detailPagerList.add(new TopicDetailPager(context));
		detailPagerList.add(new PhotosDetailPager(context,ib_photos));
		detailPagerList.add(new interactDetailPager(context));
		
		setNewsData(0);
	}

	/**
	 * 侧边栏点击后调用此方法，初始话主页面数据
	 */
	public void setNewsData(int position) {

		setCurrentPosition(position);
		if(position==2){
			ib_photos.setVisibility(View.VISIBLE);
		}else{
			ib_photos.setVisibility(View.INVISIBLE);
		}
	}

	private void setCurrentPosition(int position) {
		BaseNewsDetailPager pager = detailPagerList.get(position);
		View mRootView = pager.mRootView;
		
		tv_viewpager_title.setText(gsonResult.data.get(position).title);
		fl_view_content.removeAllViews();
		fl_view_content.addView(mRootView);
		pager.initData();
	}

	
}
