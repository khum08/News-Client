package com.yzhk.smartsh.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.yzhk.smartsh.R;

public class SplashActivity extends Activity {

	private RelativeLayout rl_splash_root;
	private AnimationSet set;
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		initUI();
	}

	private void initUI() {
		rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
		initAnimation();
		rl_splash_root.startAnimation(set);
		
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		set.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			public void onAnimationEnd(Animation animation) {
				boolean isFirstEnter = mPref.getBoolean("isFirstEnter", true);
				Intent intent = new Intent();
				if(isFirstEnter){
					intent.setClass(SplashActivity.this, GuideActivity.class);
				}else{
					intent.setClass(SplashActivity.this, HomeActivity.class);
				}
				startActivity(intent);
				finish();
			}
		});
		
	}

	//初始化动画
	private void initAnimation() {
		RotateAnimation rotate = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF,	0.5f);
		rotate.setDuration(500);
		rotate.setFillAfter(true);
		
		ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		scale.setDuration(500);
		scale.setFillAfter(true);
		
		AlphaAnimation alpha = new AlphaAnimation(0.5f, 1);
		alpha.setDuration(2000);
		alpha.setFillAfter(true);
		
		set = new AnimationSet(true);
		set.addAnimation(rotate);
		set.addAnimation(scale);
		set.addAnimation(alpha);
	}

}


















