package com.yzhk.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.yzhk.mobilesafe.R;
import com.yzhk.mobilesafe.service.LockCleanService;

public class ProcessSettingActivity extends Activity {

	private CheckBox cb_prosetting_show;
	private SharedPreferences sp;
	private CheckBox cb_prosetting_lockclean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_setting);
		
		initShowSetting();
		initClean();
	}

	private void initClean() {
		cb_prosetting_lockclean = (CheckBox) findViewById(R.id.cb_prosetting_lockclean);
		
		cb_prosetting_lockclean.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					startService(new Intent(ProcessSettingActivity.this, LockCleanService.class));
				}else{
					stopService(new Intent(ProcessSettingActivity.this,LockCleanService.class));
				}
			}
		});
		
	}
	
	//初始化系统进程显示框
	private void initShowSetting() {
		cb_prosetting_show = (CheckBox) findViewById(R.id.cb_prosetting_show);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean isChecked = sp.getBoolean("showSystemProcess", false);
		cb_prosetting_show.setChecked(isChecked);
		
		cb_prosetting_show.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					sp.edit().putBoolean("showSystemProcess", true).commit();
				}else{
					sp.edit().putBoolean("showSystemProcess", false).commit();
				}
				
			}
		});
	
		
	}
}













