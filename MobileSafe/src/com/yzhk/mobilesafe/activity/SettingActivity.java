package com.yzhk.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.yzhk.mobilesafe.R;
import com.yzhk.mobilesafe.service.AddressSearchService;
import com.yzhk.mobilesafe.service.BlackNumbService;
import com.yzhk.mobilesafe.utils.ServiceUtils;
import com.yzhk.mobilesafe.view.SettingChooseView;
import com.yzhk.mobilesafe.view.SettingItemView;

public class SettingActivity extends Activity {

	private SettingItemView sivUpdate;
	private SettingItemView sivAddressSearch;
	private SharedPreferences sp;
	private SettingChooseView scv_addresscolor;
	private SettingChooseView scv_address_drag;
	private SettingItemView siv_blacknumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		sp = getSharedPreferences("config", MODE_PRIVATE);

		// 初始化界面
		initUpdate();
		initAddressSearch();
		initAddressColor();
		initAddressDrag();
		initBlackNumb();
	}

	private void initBlackNumb() {
		siv_blacknumber = (SettingItemView)findViewById(R.id.siv_blacknumber);

		boolean isRunning = ServiceUtils.isServiceRunning(this, "com.yzhk.mobilesafe.service.BlackNumbService");
		siv_blacknumber.setCheck(isRunning);
		
		siv_blacknumber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(siv_blacknumber.isChecked()){
					siv_blacknumber.setCheck(false);
					stopService(new Intent(getApplicationContext(),BlackNumbService.class));
				}else{
					siv_blacknumber.setCheck(true);
					startService(new Intent(getApplicationContext(),BlackNumbService.class));
				}
				
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	
	}
	
	private void initAddressDrag() {
		scv_address_drag = (SettingChooseView)findViewById(R.id.scv_address_drag);
		scv_address_drag.setTitle("归属地提示框显示位置");
		scv_address_drag.setDesc("设置归属地提示框的显示位置");
		
		scv_address_drag.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingActivity.this,DragAdressActivity.class));
				
			}
		});
		
		
	}

	//初始化归属地提示框背景
	private void initAddressColor() {

		final String[] items = { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
		scv_addresscolor = (SettingChooseView) findViewById(R.id.scv_addresscolor);
		scv_addresscolor.setTitle("归属地提示框风格");

		int style = sp.getInt("style", 0);
		
		scv_addresscolor.setDesc(items[style]);
		

		scv_addresscolor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SettingActivity.this);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle("归属地提示框风格");
				int style = sp.getInt("style", 0);
				builder.setSingleChoiceItems(items, style,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								scv_addresscolor.setDesc(items[which]);
								sp.edit().putInt("style", which).commit();
								dialog.dismiss();
							}
						});

				builder.setNegativeButton("取消", null);
				builder.show();
			}
		});

	}

	// 初始化来电归属地显示
	private void initAddressSearch() {
		sivAddressSearch = (SettingItemView) findViewById(R.id.siv_addresssearch);
		boolean isRunning = ServiceUtils.isServiceRunning(this,
				"com.yzhk.mobilesafe.service.AddressSearchService");
		sivAddressSearch.setCheck(isRunning);

		sivAddressSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sivAddressSearch.isChecked()) {
					sivAddressSearch.setCheck(false);
					stopService(new Intent(SettingActivity.this,
							AddressSearchService.class));

				} else {
					sivAddressSearch.setCheck(true);
					startService(new Intent(SettingActivity.this,
							AddressSearchService.class));
				}
			}
		});
	}

	// 初始化是否更新设置
	private void initUpdate() {
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		boolean autoUpdate = sp.getBoolean("auto_update", true);
		if (autoUpdate) {
			sivUpdate.setCheck(true);
		} else {
			sivUpdate.setCheck(false);
		}

		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (sivUpdate.isChecked()) {
					sivUpdate.setCheck(false);
					sp.edit().putBoolean("auto_update", false).commit();
				} else {
					sivUpdate.setCheck(true);
					sp.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}
}
