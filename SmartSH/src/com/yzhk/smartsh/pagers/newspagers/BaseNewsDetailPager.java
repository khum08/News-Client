package com.yzhk.smartsh.pagers.newspagers;

import android.content.Context;
import android.view.View;

public abstract class BaseNewsDetailPager {
	
	public Context context;
	public View mRootView;

	public BaseNewsDetailPager(Context context) {
		this.context = context;
		mRootView = initView();
	}
	
	public abstract View initView();
	
	public abstract void initData();

}
