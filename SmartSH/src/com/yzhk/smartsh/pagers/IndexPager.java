package com.yzhk.smartsh.pagers;

import android.app.Activity;
import android.view.View;

import com.yzhk.smartsh.R;


public class IndexPager extends BasePager {

	public IndexPager(Activity context) {
		super(context);
		
	}

	@Override
	public void initViewData() {
		tv_viewpager_title.setText("智慧上海");
		View view = View.inflate(context, R.layout.view_index, null);
		fl_view_content.addView(view);
	}
	
	

	
	
}
