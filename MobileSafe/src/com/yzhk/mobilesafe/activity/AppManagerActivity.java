package com.yzhk.mobilesafe.activity;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yzhk.mobilesafe.R;
import com.yzhk.mobilesafe.db.domain.AppInfoBean;
import com.yzhk.mobilesafe.engine.AppInfoManager;

public class AppManagerActivity extends Activity implements OnClickListener{

	private List<AppInfoBean> appInfolist;
	private List<AppInfoBean> sysAppInfolist;
	private List<AppInfoBean> userAppInfolist;
	private ListView lv_app_status;
	private ProgressBar pb_apploading;
	private TextView tv_appinfo_hover;
	
	
	private Handler mHandler = new Handler(){
		
		private LvAdapter mLvAdapter;

		public void handleMessage(android.os.Message msg) {
			
			pb_apploading.setVisibility(View.GONE);
			
			
			mLvAdapter = new LvAdapter();
			lv_app_status.setAdapter(mLvAdapter);
		};
	};
	private PopupWindow mPopupWindow;
	private  AppInfoBean infoBean;
	
	//listview适配器
	class LvAdapter extends BaseAdapter{
		
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount()+1;
		}
		@Override
		public int getItemViewType(int position) {
			if(position==0 || position ==userAppInfolist.size()+1){
				return 0;
			}else{
				return 1;
			}
		}
		@Override
		public int getCount() {
			return appInfolist.size()+2;
		}

		@Override
		public AppInfoBean getItem(int position) {
			if(position == 0 || position == userAppInfolist.size()+1){
				return null;
			}else{
				if(position<userAppInfolist.size()+1){
					return userAppInfolist.get(position-1);
				}else{
					return sysAppInfolist.get(position-userAppInfolist.size()-2);
				}
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			int type = getItemViewType(position);
			if(type==0){
				
				ViewHolder2 mViewHolder2 = null;
				if(convertView==null){
					mViewHolder2 = new ViewHolder2();
					convertView = View.inflate(AppManagerActivity.this, R.layout.view_listview_appinfocount, null);
					mViewHolder2.tv_app_count = (TextView) convertView.findViewById(R.id.tv_appinfo_count);
					convertView.setTag(mViewHolder2);
				}else{
					mViewHolder2 = (ViewHolder2) convertView.getTag();
				}
				if(position==0){
					mViewHolder2.tv_app_count.setText("用户应用("+userAppInfolist.size()+")");
				}else{
					mViewHolder2.tv_app_count.setText("系统应用("+sysAppInfolist.size()+")");
				}
				
				return convertView;
			}else{
				ViewHolder mViewHolder = null;
				if(convertView ==null){
					convertView = View.inflate(AppManagerActivity.this, R.layout.view_listview_appinfo, null);
					mViewHolder = new ViewHolder();
					
					mViewHolder.tv_appname = (TextView)convertView.findViewById(R.id.tv_appname);
					mViewHolder.tv_appgrade = (TextView)convertView.findViewById(R.id.tv_appgrade);
					mViewHolder.iv_appimage = (ImageView)convertView.findViewById(R.id.iv_appimage);
					
					convertView.setTag(mViewHolder);
				
				}else{
					mViewHolder = (ViewHolder) convertView.getTag();
				}
				
				mViewHolder.tv_appname.setText(getItem(position).getName());
				if(getItem(position).isSdApp()){
					mViewHolder.tv_appgrade.setText("SD卡应用");
				}else{
					mViewHolder.tv_appgrade.setText("内存应用");
				}
				mViewHolder.iv_appimage.setImageDrawable(getItem(position).getIcon());
				
				return convertView;
			}
			
		}
		
	}
	static class ViewHolder {
		TextView tv_appname;
		TextView tv_appgrade;
		ImageView iv_appimage;
	}
	static class ViewHolder2{
		TextView tv_app_count;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appmanager);
		
		initAvailableSpace();
		initListView();
	}

	//初始化listview
	private void initListView() {
		
		pb_apploading = (ProgressBar)findViewById(R.id.pb_apploading);
		lv_app_status = (ListView)findViewById(R.id.lv_app_status);
		
		new Thread(){
			public void run() {
				appInfolist = AppInfoManager.getAppInfo(AppManagerActivity.this);
				sysAppInfolist = new ArrayList<AppInfoBean>();
				userAppInfolist = new ArrayList<AppInfoBean>();
				
				for (AppInfoBean appinfo : appInfolist) {
					if(appinfo.isSystem()){
						sysAppInfolist.add(appinfo);
					}else{
						userAppInfolist.add(appinfo);
					}
				}
				
				mHandler.sendEmptyMessage(0);
			};
		}.start();
		
		lv_app_status.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(sysAppInfolist!=null && userAppInfolist!=null){
					tv_appinfo_hover = (TextView)findViewById(R.id.tv_appinfo_hover);
					tv_appinfo_hover.setVisibility(View.VISIBLE);
					if(firstVisibleItem>userAppInfolist.size()){
						tv_appinfo_hover.setText("系统应用("+sysAppInfolist.size()+")");
					}else{
						tv_appinfo_hover.setText("用户应用("+userAppInfolist.size()+")");
					}
				}
			}
		});
		
		lv_app_status.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position==0 || position==userAppInfolist.size()+1){
					return;
				}else{
					if(position>userAppInfolist.size()+1){
						infoBean = sysAppInfolist.get(position-userAppInfolist.size()-2);
					}else{
						infoBean = userAppInfolist.get(position-1);
					}
					showPopupWindow(view);
				}
			}
		});
	}
	
	protected void showPopupWindow(View v) {
		
		//popupwindow弹出动画
		AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
		alphaAnimation.setDuration(1000);
		alphaAnimation.setFillAfter(true);
		
		ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f,
				0f, 1f, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(1000);
		scaleAnimation.setFillAfter(true);
		
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(scaleAnimation);
		
		View view = View.inflate(AppManagerActivity.this, R.layout.view_popupwindow_app, null);
		mPopupWindow = new PopupWindow(view, 
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT,
				true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable());
		mPopupWindow.showAsDropDown(v, v.getWidth()/2, -v.getHeight());
		view.startAnimation(animationSet);
		
		//设置item监听
		TextView tv_app_launch = (TextView) view.findViewById(R.id.tv_app_launch);
		TextView tv_app_unintall = (TextView) view.findViewById(R.id.tv_app_unintall);
		TextView tv_app_share = (TextView) view.findViewById(R.id.tv_app_share);
		tv_app_unintall.setOnClickListener(this);
		tv_app_launch.setOnClickListener(this);
		tv_app_share.setOnClickListener(this);
		
	}

	//初始化磁盘大小
	private void initAvailableSpace() {
		
		TextView tv_disk = (TextView)findViewById(R.id.tv_disk);
		
		String diskPath = Environment.getDataDirectory().getAbsolutePath();
		
		long diskAvilableSpace = getAvilableSpace(diskPath);
		String diskSize = Formatter.formatFileSize(this, diskAvilableSpace);
		
		tv_disk.setText("可用空间："+diskSize);
	}

	//获取指定路径的可用空间大小
	private long getAvilableSpace(String diskPath) {
		StatFs diskSf = new StatFs(diskPath);
		long count = diskSf.getAvailableBlocks();
		long size = diskSf.getBlockSize();
		return count*size;
	}

	
	//监听item点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//启动应用
		case R.id.tv_app_launch:
			PackageManager pm = getPackageManager();
			Intent launchIntentForPackage = pm.getLaunchIntentForPackage(infoBean.getPackageName());
			if(launchIntentForPackage != null){
				startActivity(launchIntentForPackage);
			}else{
				Toast.makeText(this, "这玩意儿启动不了", Toast.LENGTH_SHORT).show();
			}
			
			break;
		//卸载应用
		case R.id.tv_app_unintall:
			try {
				Intent intent = new Intent(Intent.ACTION_DELETE);
				intent.setData(Uri.parse("package:"+infoBean.getPackageName()));
				startActivity(intent);
			} catch (Exception e) {
				Toast.makeText(this, "这玩意儿卸不了", Toast.LENGTH_SHORT).show();
			}
			
			break;
		//分享应用	
		case R.id.tv_app_share:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_TEXT, "分享你一个不错的应用哦，应用名叫"+infoBean.getName());
			intent.setType("text/plain");
			startActivity(intent);
			break;
		}
		if(mPopupWindow!=null){
			mPopupWindow.dismiss();
		}
	}
}








