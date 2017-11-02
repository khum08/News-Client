package com.yzhk.smartsh.pagers.newspagers;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class TopicDetailPager extends BaseNewsDetailPager {

	public TopicDetailPager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		TextView textView = new TextView(context);
		textView.setText("专题详情页");
		textView.setTextColor(Color.RED);
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(20);
		
		return textView;
	}

	@Override
	public void initData() {

	}

}
