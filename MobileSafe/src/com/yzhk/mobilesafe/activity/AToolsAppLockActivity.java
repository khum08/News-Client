package com.yzhk.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yzhk.mobilesafe.R;
import com.yzhk.mobilesafe.db.dao.AppLockedDao;
import com.yzhk.mobilesafe.db.domain.AppInfoBean;
import com.yzhk.mobilesafe.engine.AppInfoManager;

public class AToolsAppLockActivity extends Activity {

	private Button bt_applock_unlock;
	private Button bt_applock_lock;
	private ProgressBar pb_applock;
	private LinearLayout ll_applock_unlock;
	private LinearLayout ll_applock_lock;
	private TextView tv_applock_unlockapp_count;
	private ListView lv_applock_unlock;
	private TextView tv_applock_lockapp_count;
	private ListView lv_applock_lock;
	private AppLockedDao dao;
	private List<String> mDaoAppList;
	private List<AppInfoBean> mAppInfoList;
	private List<AppInfoBean> mLockedAppList;
	private List<AppInfoBean> mUnLockedAppList;
	private MyAdapter mUnlockAdapter;
	protected MyAdapter mLockedAdapter;
	
	private Handler mHandler = new Handler(){
		

		public void handleMessage(android.os.Message msg) {
			
			pb_applock.setVisibility(View.GONE);
			ll_applock_unlock.setVisibility(View.VISIBLE);
			mUnlockAdapter = new MyAdapter(true);//未加锁
			mLockedAdapter = new MyAdapter(false);//已加锁
			lv_applock_unlock.setAdapter(mUnlockAdapter);
			lv_applock_lock.setAdapter(mLockedAdapter);
			
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applock);
		
		initData();
		initUI();
		
	}

	/**
	 * 获取数据和系统应用信息
	 */
	private void initData() {
		
		bt_applock_unlock = (Button) findViewById(R.id.bt_applock_unlock);
		bt_applock_lock = (Button) findViewById(R.id.bt_applock_lock);
		pb_applock = (ProgressBar)findViewById(R.id.pb_applock);
		ll_applock_unlock = (LinearLayout)findViewById(R.id.ll_applock_unlock);
		ll_applock_lock = (LinearLayout)findViewById(R.id.ll_applock_lock);
	
		tv_applock_unlockapp_count = (TextView) findViewById(R.id.tv_applock_unlockapp_count);
		lv_applock_unlock = (ListView) findViewById(R.id.lv_applock_unlock);
		
		tv_applock_lockapp_count = (TextView) findViewById(R.id.tv_applock_lockapp_count);
		lv_applock_lock = (ListView) findViewById(R.id.lv_applock_lock);
		
		
		
		new Thread(){
			public void run() {
				dao = AppLockedDao.getInstance(AToolsAppLockActivity.this);
				mDaoAppList = dao.findAll();
				
				mLockedAppList = new ArrayList<AppInfoBean>();
				mUnLockedAppList = new ArrayList<AppInfoBean>();
				
				mAppInfoList = AppInfoManager.getAppInfo(AToolsAppLockActivity.this);
				for (AppInfoBean appInfo : mAppInfoList) {
					if(mDaoAppList.contains(appInfo.getPackageName())){
						mLockedAppList.add(appInfo);
					}else{
						mUnLockedAppList.add(appInfo);
					}
				}
				
				int size = mUnLockedAppList.size();
				int size2 = mLockedAppList.size();
				tv_applock_unlockapp_count.setText("未加锁应用("+size+")");
				tv_applock_lockapp_count.setText("已加锁应用("+size2+")");
				
				mHandler.sendEmptyMessage(0);
				
			};
		}.start();
		
	}
	
	/**
	 * listview的适配器
	 * @author 大傻春
	 *
	 */
	class MyAdapter extends BaseAdapter{
		
		private boolean flag;

		public MyAdapter(boolean flag){
			this.flag = flag;
		}

		@Override
		public int getCount() {
			int count;
			if(flag){
				count = mUnLockedAppList.size();
			}else{
				count = mLockedAppList.size();
			}
			return count;
		}

		@Override
		public AppInfoBean getItem(int position) {
			AppInfoBean appInfo;
			if(flag){
				appInfo = mUnLockedAppList.get(position);
			}else{
				appInfo = mLockedAppList.get(position);
			}
			return appInfo;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder mViewHolder = null;
			if(convertView==null){
				mViewHolder = new ViewHolder();
				convertView = View.inflate(AToolsAppLockActivity.this, R.layout.view_listview_applock, null);
				mViewHolder.iv_applock = (ImageView) convertView.findViewById(R.id.iv_applock);
				mViewHolder.tv_applock_package = (TextView) convertView.findViewById(R.id.tv_applock_package);
				mViewHolder.tv_applock_lock = (ImageView) convertView.findViewById(R.id.tv_applock_lock);
				
				convertView.setTag(mViewHolder);
			}else{
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			mViewHolder.iv_applock.setImageDrawable(getItem(position).getIcon());
			mViewHolder.tv_applock_package.setText(getItem(position).getPackageName());
			if(flag){
				mViewHolder.tv_applock_lock.setBackgroundResource(R.drawable.unlock);
			}else{
				mViewHolder.tv_applock_lock.setBackgroundResource(R.drawable.lock);
			}
			
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		ImageView iv_applock;
		TextView tv_applock_package;
		ImageView tv_applock_lock;
	}
	/**
	 * 初始化界面 注册点击事件
	 */
	private void initUI() {
		
		bt_applock_unlock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bt_applock_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
				bt_applock_lock.setBackgroundResource(R.drawable.tab_right_default);
				ll_applock_unlock.setVisibility(View.VISIBLE);
				ll_applock_lock.setVisibility(View.GONE);
			}
		});
		
		bt_applock_lock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bt_applock_unlock.setBackgroundResource(R.drawable.tab_left_default);
				bt_applock_lock.setBackgroundResource(R.drawable.tab_right_pressed);
				ll_applock_unlock.setVisibility(View.GONE);
				ll_applock_lock.setVisibility(View.VISIBLE);
			}
		});
	}
}







