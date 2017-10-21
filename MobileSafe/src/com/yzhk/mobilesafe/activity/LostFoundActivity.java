package com.yzhk.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzhk.mobilesafe.R;

public class LostFoundActivity extends Activity {
	
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean configed = sp.getBoolean("configed", false);
		if(configed){
			setContentView(R.layout.activity_safe);
		}else{
			startActivity(new Intent(this,Guide1Activity.class));
			finish();
		}
		
		String phone_safe = sp.getString("phone_safe", null);
		TextView tv_phone_safe = (TextView) findViewById(R.id.tv_phone_safe);
		tv_phone_safe.setText(phone_safe);
		
		boolean protect = sp.getBoolean("protect", false);
		ImageView iv_lock = (ImageView) findViewById(R.id.iv_lock);
		if(protect){
			iv_lock.setImageResource(R.drawable.lock);
		}else{
			iv_lock.setImageResource(R.drawable.unlock);
		}
		
	}
	
	public void reset(View v){
		startActivity(new Intent(this,Guide1Activity.class));
		overridePendingTransition(R.anim.enteranim_next,R.anim.exitanim_next);
	}
	
}
