package com.yzhk.smartsh.fragment;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yzhk.smartsh.R;
import com.yzhk.smartsh.activity.HomeActivity;
import com.yzhk.smartsh.pagers.BasePager;
import com.yzhk.smartsh.pagers.GovPager;
import com.yzhk.smartsh.pagers.IndexPager;
import com.yzhk.smartsh.pagers.NewsPager;
import com.yzhk.smartsh.pagers.ServicePager;
import com.yzhk.smartsh.pagers.SettingPager;
import com.yzhk.smartsh.view.NoScrollViewPager;

public class MainFragment extends BaseFragment {

	private View view;
	private NoScrollViewPager vp_right_content;
	private MyAdapter mAdapter;
	public ArrayList<BasePager> viewPagerList;
	private RadioGroup rg_main_bottom;

	@Override
	public View initView() {
		view = View.inflate(mActivity, R.layout.fragment_right_content, null);
		vp_right_content = (NoScrollViewPager) view.findViewById(R.id.vp_right_content);
		return view;
	}

	@Override
	public void initData() {
		initVPData();
		mAdapter = new MyAdapter();
		vp_right_content.setAdapter(mAdapter);
		
		rg_main_bottom = (RadioGroup) view.findViewById(R.id.rg_main_bottom);
		rg_main_bottom.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:
					vp_right_content.setCurrentItem(0, false);
					break;
				case R.id.rb_news:
					vp_right_content.setCurrentItem(1, false);
					break;
				case R.id.rb_service:
					vp_right_content.setCurrentItem(2, false);
					break;
				case R.id.rb_gov:
					vp_right_content.setCurrentItem(3, false);
					break;
				case R.id.rb_setting:
					vp_right_content.setCurrentItem(4, false);
					break;
				default:
					break;
				}
			}
		});
		
		vp_right_content.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				BasePager pager = viewPagerList.get(position);
				//初始化当页数据
				pager.initViewData();
				if(position==0 || position ==4){
					slidingMenuEnable(false);
				}else{
					slidingMenuEnable(true);
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		BasePager pager = viewPagerList.get(0);
		//初始化当页数据
		pager.initViewData();
		
		vp_right_content.setCurrentItem(0, false);
		slidingMenuEnable(false);
	}
	
	public void slidingMenuEnable(boolean enable){
		SlidingMenu slidingMenu = ((HomeActivity) mActivity).getSlidingMenu();
		if(enable){
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}else{
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
	
	//初始化viewpager的view数据
	public void initVPData(){
		
		viewPagerList = new ArrayList<BasePager>();
		viewPagerList.add(new IndexPager(mActivity));
		viewPagerList.add(new NewsPager(mActivity));
		viewPagerList.add(new GovPager(mActivity));
		viewPagerList.add(new ServicePager(mActivity));
		viewPagerList.add(new SettingPager(mActivity));
	}
	class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return viewPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = viewPagerList.get(position);
			View view = pager.mView;
			container.addView(view);
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
	}
	
}
