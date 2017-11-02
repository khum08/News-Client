package com.yzhk.smartsh.pagers;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yzhk.smartsh.R;
import com.yzhk.smartsh.activity.HomeActivity;

public abstract class BasePager {
	
	public Context context;
	public TextView tv_viewpager_title;
	public FrameLayout fl_view_content;
	public LinearLayout ll_viewpager_root;
	public ImageButton ib_title_btn;
	public View mView;
	public ImageButton ib_photos;
	
	
	public BasePager(Context context){
		this.context = context;
		mView = initView();
	}
	public View initView(){
		View view = View.inflate(context, R.layout.viewpager_base, null);
		tv_viewpager_title = (TextView) view.findViewById(R.id.tv_viewpager_title);
		fl_view_content = (FrameLayout)view.findViewById(R.id.fl_view_content);
		ll_viewpager_root = (LinearLayout) view.findViewById(R.id.ll_viewpager_root);
		ib_photos = (ImageButton)view.findViewById(R.id.ib_photos);
		
		ib_title_btn = (ImageButton)view.findViewById(R.id.ib_title_btn);
		ib_title_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggle();
			}
		});
		
		return view;
	}
	public abstract void initViewData();

	private void toggle(){
		HomeActivity activity = (HomeActivity)context;
		SlidingMenu slidingMenu = activity.getSlidingMenu();
		slidingMenu.toggle();
	}
}











