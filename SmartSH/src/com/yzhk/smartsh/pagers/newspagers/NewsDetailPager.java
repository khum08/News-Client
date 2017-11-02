package com.yzhk.smartsh.pagers.newspagers;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;
import com.yzhk.smartsh.R;
import com.yzhk.smartsh.activity.HomeActivity;
import com.yzhk.smartsh.domain.CategroiesData.SubData;
import com.yzhk.smartsh.pagers.newspagers.newsdetailtab.NewsDetailTabPager;

public class NewsDetailPager extends BaseNewsDetailPager implements OnPageChangeListener{

	public ViewPager vp_news_detail;
	public SubData subData;
	private MyAdapter mAdapter;
	private View view;
	private ArrayList<NewsDetailTabPager> tabPagerList;

	public NewsDetailPager(Context context, SubData subData) {
		super(context);
		this.subData = subData;
	}

	@Override
	public View initView() {
		view = View.inflate(context, R.layout.view_news_detail, null);
		vp_news_detail = (ViewPager) view.findViewById(R.id.vp_news_detail);
		return view;
	}

	@Override
	public void initData() {
		tabPagerList = new ArrayList<NewsDetailTabPager>();
		for (int i = 0; i < subData.children.size(); i++) {
			NewsDetailTabPager tabPager = new NewsDetailTabPager(context,subData.children.get(i));
			tabPagerList.add(tabPager);
		}
		
		mAdapter = new MyAdapter();
		vp_news_detail.setAdapter(mAdapter);

		TabPageIndicator indicator = (TabPageIndicator) view
				.findViewById(R.id.mindicator);
		indicator.setViewPager(vp_news_detail);
		indicator.setOnPageChangeListener(this);
	}

	class MyAdapter extends PagerAdapter {
		
		@Override
		public CharSequence getPageTitle(int position) {
			return subData.children.get(position).title;
		}
		
		@Override
		public int getCount() {
			return tabPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			
			NewsDetailTabPager tabPager = tabPagerList.get(position);
			tabPager.initData();
			container.addView(tabPager.mRootView);
			return tabPager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	@Override
	public void onPageScrollStateChanged(int position) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int position) {
		if(position==0){
			setSildingMenuEnable(true);
		}else{
			setSildingMenuEnable(false);
		}
	}
	
	public void setSildingMenuEnable(boolean enable){
		HomeActivity mainUI = (HomeActivity) context;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		if(enable){
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}else{
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
}
