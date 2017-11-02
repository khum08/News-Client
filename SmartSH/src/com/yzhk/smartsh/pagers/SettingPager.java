package com.yzhk.smartsh.pagers;

import android.app.Activity;
import android.view.View;

import com.yzhk.smartsh.R;


public class SettingPager extends BasePager {

	public SettingPager(Activity context) {
		super(context);
		
	}

	@Override
	public void initViewData() {
		tv_viewpager_title.setText("设置");
		View view = View.inflate(context, R.layout.view_setting, null);
		fl_view_content.addView(view);
	}
	
	

	
	
}
