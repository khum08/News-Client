package com.yzhk.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzhk.mobilesafe.R;

public class SettingItemView extends RelativeLayout {

	private static final String NAME = "http://schemas.android.com/apk/res/com.yzhk.mobilesafe";
	private TextView tvTitle;
	private TextView tvDesc;
	private CheckBox cbStatus;
	private String mTitle;
	private String mDescOn;
	private String mDescOff;

	public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mTitle = attrs.getAttributeValue(NAME, "title");
		mDescOn = attrs.getAttributeValue(NAME, "desc_on");
		mDescOff = attrs.getAttributeValue(NAME, "desc_off");
		initView();
	}

	public SettingItemView(Context context) {
		super(context);
		initView();
	}
	
	private void initView(){
		View.inflate(getContext(), R.layout.view_setting_item, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
		cbStatus = (CheckBox) findViewById(R.id.cb_status);
		
		setTitle(mTitle);
		
	}
	
	public void setTitle(String str){
		tvTitle.setText(str);
	}
	public void setDesc(String str){
		tvDesc.setText(str);
	}
	public void setCheck(boolean flag){
		cbStatus.setChecked(flag);
		
		if(isChecked()){
			tvDesc.setText(mDescOn);
		}else{
			tvDesc.setText(mDescOff);
		}
	}
	public boolean isChecked(){
		return cbStatus.isChecked();
	}

}
