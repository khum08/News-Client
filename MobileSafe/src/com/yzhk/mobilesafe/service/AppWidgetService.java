package com.yzhk.mobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import com.yzhk.mobilesafe.R;
import com.yzhk.mobilesafe.engine.ProcessInfoPro;
import com.yzhk.mobilesafe.receiver.MyAppWidgetProvider;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

public class AppWidgetService extends Service {

	protected static final String tag = "AppWidgetService";
	private Timer timer;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		
		startTimer();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		InnerReceiver mReceiver = new InnerReceiver();
		registerReceiver(mReceiver, filter);
	
	}
	
	//监听锁屏和解锁广播
	class InnerReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(Intent.ACTION_SCREEN_OFF)){
				if(timer!=null){
					timer.cancel();
				}
			}else{
				startTimer();
			}
		}
		
	}
	
	private void startTimer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {				
				updateWidget();
				Log.i(tag, "5miao yici de renwu zhengzai yunxing");
				
			}
		}, 0, 5000);
		
	}
	
	
	protected void updateWidget() {
		
		AppWidgetManager awm = AppWidgetManager.getInstance(this);
		
		RemoteViews views = new RemoteViews(getPackageName(), R.layout.view_appwidget);
		long availMemory = ProcessInfoPro.getAvailMemory(this);
		String fileSize = Formatter.formatFileSize(this, availMemory);
		int processCount = ProcessInfoPro.getProcessCount(this);
		views.setTextViewText(R.id.tv_appwidget_process_count, "进程总数："+processCount);
		views.setTextViewText(R.id.tv_appwidget_process_memory, "内存占用："+fileSize);
		
		//一键清理
		Intent intent2 = new Intent();
		intent2.setAction("android.intent.action.KILL_ALLPROCEEE");
		PendingIntent pendingIntent2 =PendingIntent.getService(this, 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
		views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent2);
		
		//进入主界面
		Intent intent = new Intent();
		intent.setAction("android.intent.action.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		views.setOnClickPendingIntent(R.id.ll_appwidget, pendingIntent);
		
		ComponentName componentName = new ComponentName(this, MyAppWidgetProvider.class);
		awm.updateAppWidget(componentName, views);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(timer!=null){
			timer.cancel();
		}
	}
	

}
