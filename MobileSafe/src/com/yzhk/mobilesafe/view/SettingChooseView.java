package com.yzhk.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzhk.mobilesafe.R;

public class SettingChooseView extends RelativeLayout {

	private TextView tvTitle;
	private TextView tvDesc;

	public SettingChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public SettingChooseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	private void initView(){
		View.inflate(getContext(), R.layout.view_setting_choose, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
	}
	
	public void setTitle(String str){
		tvTitle.setText(str);
	}
	public void setDesc(String str){
		tvDesc.setText(str);
	}
	

}
