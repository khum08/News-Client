package com.yzhk.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.yzhk.mobilesafe.R;
import com.yzhk.mobilesafe.view.SettingItemView;

public class Guide2Activity extends BaseGuideActivity {
	

	private SettingItemView siv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide2);
		
		siv = (SettingItemView)findViewById(R.id.siv_sim);
		String simNum = sp.getString("sim", null);
		if(!TextUtils.isEmpty(simNum)){
			siv.setCheck(true);
		}else{
			siv.setCheck(false);
		}
		
		siv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(siv.isChecked()){
					siv.setCheck(false);
					sp.edit().remove("sim").commit();
				}else{
					siv.setCheck(true);
					
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = tm.getSimSerialNumber();
					sp.edit().putString("sim", simSerialNumber).commit();
					
				}
			}
		});
		
	}
	
	@Override
	public void showNextPage() {
		String simNum = sp.getString("sim", null);
		if(simNum!=null){
			startActivity(new Intent(this,Guide3Activity.class));
			finish();
			overridePendingTransition(R.anim.enteranim_next,R.anim.exitanim_next);
		}else{
			Toast.makeText(this, "请绑定SIM卡哦", Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this,Guide1Activity.class));
		finish();
		overridePendingTransition(R.anim.enteranim_previous, R.anim.exitanim_previous);
	}
	
}
