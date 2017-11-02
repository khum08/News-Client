package com.yzhk.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.yzhk.mobilesafe.R;

public class Guide4Activity extends BaseGuideActivity {
	
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide4);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		final CheckBox cb_protect = (CheckBox) findViewById(R.id.cb_protect);
		
		boolean protect = sp.getBoolean("protect", true);
		if(protect){
			cb_protect.setChecked(true);
			cb_protect.setText("防盗保护已经开启");
		}else{
			cb_protect.setChecked(false);
			cb_protect.setText("防盗保护已经关闭");
		}
		
		cb_protect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					cb_protect.setText("防盗保护已经开启");
					sp.edit().putBoolean("protect", true).commit();
				}else{
					cb_protect.setText("防盗保护已经关闭");
					sp.edit().putBoolean("protect", false).commit();
				}
			}
		});
		
	}
	
	@Override
	public void showNextPage() {
		sp.edit().putBoolean("configed",true).commit();
		startActivity(new Intent(this,LostFoundActivity.class));
		finish();
		overridePendingTransition(R.anim.enteranim_next,R.anim.exitanim_next);
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this,Guide3Activity.class));
		finish();
		overridePendingTransition(R.anim.enteranim_previous, R.anim.exitanim_previous);
	}
}
