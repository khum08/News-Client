package com.yzhk.smartsh.activity;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.yzhk.smartsh.R;
import com.yzhk.smartsh.fragment.LeftMenuFragment;
import com.yzhk.smartsh.fragment.MainFragment;

public class HomeActivity extends SlidingFragmentActivity {

	private static final String TAG_MAIN_CONTENT = "TAG_MAIN_CONTENT";
	private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
	private FragmentManager mFm;
	private SharedPreferences mPref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		initSlidingMenu();
		initFragment();
	}

	private void initFragment() {
		mFm = getSupportFragmentManager();
		FragmentTransaction transaction = mFm.beginTransaction();
		transaction.replace(R.id.fl_home_root, new MainFragment(), TAG_MAIN_CONTENT);
		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),
				TAG_LEFT_MENU);
		
		transaction.commit();
		
	}

	private void initSlidingMenu() {
		setBehindContentView(R.layout.menu_silding);
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		//获取屏幕大小,并存入sharedPreference中
		Display display = getWindowManager().getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		int width = point.x;
		int height = point.y;
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		mPref.edit().putInt("display_width", width).commit();
		mPref.edit().putInt("display_height", height).commit();
		
		slidingMenu.setBehindOffset((int) (width*0.688));
	}
	
	public LeftMenuFragment getLeftMenuFragment(){
		LeftMenuFragment fragment = (LeftMenuFragment) mFm.findFragmentByTag(TAG_LEFT_MENU);
		return fragment;
	}
	
	public MainFragment getMainFragment(){
		FragmentManager fragmentManager = getSupportFragmentManager();
		MainFragment fragment = (MainFragment) fragmentManager.findFragmentByTag(TAG_MAIN_CONTENT);
		return fragment;
	}
	
}



















