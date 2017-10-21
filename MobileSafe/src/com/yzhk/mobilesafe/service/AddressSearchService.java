package com.yzhk.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yzhk.mobilesafe.R;
import com.yzhk.mobilesafe.db.dao.Addressdao;

public class AddressSearchService extends Service {

	private TelephonyManager tm;
	private MyListener myListener;
	private CallOutReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		
		//来电监听用telephonymanager
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		myListener = new MyListener();
		tm.listen(myListener, PhoneStateListener.LISTEN_CALL_STATE);

		//去电监听动态注册
		receiver = new CallOutReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);
	}

	@Override
	public void onDestroy() {
		tm.listen(myListener, PhoneStateListener.LISTEN_NONE);
		unregisterReceiver(receiver);
	}
	
	class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				String address = Addressdao.searchAddress(incomingNumber);
//				Toast.makeText(AddressSearchService.this, address,
//						Toast.LENGTH_LONG).show();
				showToast(address);
				break;

			default:
				break;
			}
		}
	}
	
	class CallOutReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String number = getResultData();
			String address = Addressdao.searchAddress(number);
			showToast(address);
		}
		
	}
	
	private void showToast(String text) {
		WindowManager mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		params.gravity = Gravity.LEFT + Gravity.TOP;
		
		params.setTitle("Toast");
		
		View view = View.inflate(this, R.layout.view_toast_address, null);
		view.setBackgroundResource(R.drawable.call_locate_green);
		TextView tv_toast = (TextView) view.findViewById(R.id.tv_toast);
		tv_toast.setText(text);
		tv_toast.setTextColor(Color.RED);
		
		mWM.addView(view, params);

	}
}
