package com.yzhk.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.yzhk.mobilesafe.R;

public class RocketRunActivity extends Activity {
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			finish();
		};	
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rocket_bg);
		
		AlphaAnimation aa = new AlphaAnimation(0f, 1f);
		aa.setDuration(500);
		ImageView iv_bg1 = (ImageView)findViewById(R.id.iv_bg1);
		ImageView iv_bg2 = (ImageView)findViewById(R.id.iv_bg2);
		
		iv_bg1.startAnimation(aa);
		iv_bg2.startAnimation(aa);
		
		mHandler.sendEmptyMessageDelayed(0, 1000);
	
	}

}
