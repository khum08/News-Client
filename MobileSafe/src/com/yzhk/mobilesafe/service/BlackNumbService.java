package com.yzhk.mobilesafe.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.yzhk.mobilesafe.db.dao.BlackNumbDao;

public class BlackNumbService extends Service {

	private BlackNumbDao mDao;
	private SmsReceiver mReceiver;
	private MyPhoneStateListener mListener;
	private TelephonyManager mTm;
	private MyContentObserver mObserver;

	@Override
	public void onCreate() {
		super.onCreate();

		interceptSms();
		interceptPhone();
	}

	// 拦截来电
	private void interceptPhone() {
		mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mListener = new MyPhoneStateListener();
		mTm.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	// 电话状态监听器
	class MyPhoneStateListener extends PhoneStateListener {

		

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:

				break;
			case TelephonyManager.CALL_STATE_RINGING:
				int mode = mDao.getMode(incomingNumber);
				if (mode == 2 || mode == 3) {
					// 拦截电话
					// ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
					try {
						// 反射调用
						Class<?> clazz = Class.forName("ServiceManager");
						Method method = clazz.getMethod("getService",
								String.class);
						IBinder iBinder = (IBinder) method.invoke(null,
								Context.TELEPHONY_SERVICE);
						ITelephony iTelephony = ITelephony.Stub
								.asInterface(iBinder);
						iTelephony.endCall();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					//删除通话记录
					mObserver = new MyContentObserver(new Handler(), incomingNumber);
					getContentResolver().registerContentObserver(Uri.parse("content://call_log/calls"), true, mObserver);

				}

				break;

			default:
				break;
			}

		}
	}
	
	//	ContentObserver检测数据库变化
	class MyContentObserver extends ContentObserver{

		private String phone;

		public MyContentObserver(Handler handler,String phone) {
			super(handler);
			this.phone = phone;
		}
		
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			getContentResolver().delete(Uri.parse("content://call_log/calls"), "number=?", new String[]{phone});
		}
		
	}
	
	// 拦截短信
	private void interceptSms() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		mReceiver = new SmsReceiver();
		registerReceiver(mReceiver, filter);
	}

	// 短信广播
	class SmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] objects = (Object[]) bundle.get("pdus");
				for (Object object : objects) {
					SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
					String phone = sms.getOriginatingAddress();

					if (mDao == null) {
						mDao = BlackNumbDao
								.getInstanse(getApplicationContext());
					}
					int mode = mDao.getMode(phone);
					if (mode == 1 || mode == 3) {
						abortBroadcast();
					}
				}
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mReceiver!=null){
			unregisterReceiver(mReceiver);
		}
		mTm.listen(mListener, PhoneStateListener.LISTEN_NONE);
		if(mObserver!=null){
			getContentResolver().unregisterContentObserver(mObserver);
		}
	}

}
