package com.yzhk.mobilesafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.yzhk.mobilesafe.R;
import com.yzhk.mobilesafe.activity.RocketRunActivity;

public class RocketService extends Service {

	private WindowManager mWm;
	private LayoutParams mParams;
	private View view;
	private int winWidth;
	private int winHeigth;

	private int startX;
	private int startY;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			mParams.y =(Integer)msg.obj;
			mWm.updateViewLayout(view, mParams);
		};
	};
	@Override
	public void onCreate() {
		super.onCreate();

		mWm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		
		Point point = new Point();
		mWm.getDefaultDisplay().getSize(point);
		winWidth = point.x;
		winHeigth = point.y;
		
		mParams = new WindowManager.LayoutParams();

		mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		mParams.format = PixelFormat.TRANSLUCENT;
		mParams.gravity = Gravity.TOP + Gravity.LEFT; 
		
		view = View.inflate(this, R.layout.view_rocket, null);
		ImageView iv_rocket = (ImageView) view.findViewById(R.id.iv_rocket);
		AnimationDrawable ad = (AnimationDrawable) iv_rocket.getBackground();
		ad.start();
		
		mWm.addView(view, mParams);
		
		iv_rocket.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					
					startX = (int)event.getRawX();
					startY = (int)event.getRawY();
					
					break;
					
				case MotionEvent.ACTION_MOVE:
					
					int endX = (int)event.getRawX();
					int endY = (int)event.getRawY();
					
					int dx = endX - startX;
					int dy = endY -startY;
					
					int l =view.getLeft()+dx;
					int r =view.getRight()+dx;
					int t =view.getTop()+dy;
					int b =view.getBottom();
					
					if(l<0 || t<0 || r> winWidth || b>winHeigth){
						break;
					}
					
					mParams.x = mParams.x +dx;
					mParams.y = mParams.y +dy;
					//view.layout(l, t, r, b);
					mWm.updateViewLayout(view, mParams);
					
					startX = endX;
					startY = endY;
					break;
					
				case MotionEvent.ACTION_UP:
					
					int left = winWidth/2-80;
					int right = winWidth/2+80;
					if(startX>left && startX<right && startY>winHeigth-40){
						UpToSky();
						
						Intent intent = new Intent(RocketService.this,RocketRunActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startService(intent);
					}
					
					break;
				default:
					break;
				}
				
				return true;
			}
		});

	}

	protected void UpToSky() {
		
		new Thread(){
			
			public void run() {
				
				for(int i=11;i>0;i--){
					startY = startY /10 *i;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				Message msg = Message.obtain();
				msg.obj = startY;
				mHandler.sendMessage(msg);
				
			};
		}.start();
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mWm!=null && view!=null){
			mWm.removeView(view);
		}
	}

}
