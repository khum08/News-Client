package com.yzhk.smartsh.fragment;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yzhk.smartsh.R;
import com.yzhk.smartsh.activity.HomeActivity;
import com.yzhk.smartsh.domain.CategroiesData.SubData;
import com.yzhk.smartsh.pagers.NewsPager;

public class LeftMenuFragment extends BaseFragment {

	private View view;
	private ListView lv_leftmenu;
	public ArrayList<SubData> serverData;
	private MyAdpater mAdpater;
	private int mCurrentPosition = 0;
	
	@Override
	public View initView() {
		view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		return view;
	}

	@Override
	public void initData() {
		lv_leftmenu = (ListView) view.findViewById(R.id.lv_leftmenu);
	}
	
	class MyAdpater extends BaseAdapter{

		@Override
		public int getCount() {
			return serverData.size();
		}

		@Override
		public SubData getItem(int position) {
			return serverData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.listview_leftmenu, null);
			TextView tv_leftmenu_item = (TextView) view.findViewById(R.id.tv_leftmenu_item);
			tv_leftmenu_item.setText(getItem(position).title);
			
			if(mCurrentPosition==position){
				tv_leftmenu_item.setEnabled(true);
			}else{
				tv_leftmenu_item.setEnabled(false);
			}
			
			return view;
		}
		
	}
	
	public void setListViewData(ArrayList<SubData> data){
		mCurrentPosition = 0;
		serverData = data;
		
		mAdpater = new MyAdpater();
		lv_leftmenu.setAdapter(mAdpater);
		
		lv_leftmenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCurrentPosition = position;
				
				toggle();
				mAdpater.notifyDataSetChanged();
				loadingNewsData(position);
			}
		});
	}

	/**
	 * 加载主页数据
	 */
	protected void loadingNewsData(int position) {
		HomeActivity MainUI = (HomeActivity) mActivity;
		MainFragment mainFragment = MainUI.getMainFragment();
		NewsPager pager = (NewsPager) mainFragment.viewPagerList.get(1);
		pager.setNewsData(position);
	}

	/**
	 * 点击后收起侧边栏
	 */
	protected void toggle() {
		HomeActivity MainUI = (HomeActivity) mActivity;
		SlidingMenu slidingMenu = MainUI.getSlidingMenu();
		slidingMenu.toggle();
	}
}
