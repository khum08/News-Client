package com.yzhk.mobilesafe.service;

import com.yzhk.mobilesafe.engine.ProcessInfoPro;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class LockCleanService extends Service {

	private MyReceiver mReceiver;

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		mReceiver = new MyReceiver();
		registerReceiver(mReceiver, filter);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		if(mReceiver!=null){
			unregisterReceiver(mReceiver);
		}
		super.onDestroy();
	}
	
	class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			ProcessInfoPro.killAllPro(context);
		}
	}

}
