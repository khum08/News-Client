package com.yzhk.smartsh.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.yzhk.smartsh.R;
import com.yzhk.smartsh.Utils.DensityUtils;

public class GuideActivity extends Activity {

	private ViewPager vp_guide;
	private LinearLayout ll_guide_add;
	private MyAdapter mAdapter;
	private ImageView iv_guide_redpoint;
	private Button btn_guide_start;
	
	private int[] ids = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
	private ArrayList<ImageView> list;
	private int dis;
	private SharedPreferences mPref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
		initUI();
	}

	private void initUI() {
		vp_guide = (ViewPager) findViewById(R.id.vp_guide);
		ll_guide_add = (LinearLayout) findViewById(R.id.ll_guide_add);
		iv_guide_redpoint = (ImageView) findViewById(R.id.iv_guide_redpoint);
		btn_guide_start = (Button) findViewById(R.id.btn_guide_start);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		
		initData();
		mAdapter = new MyAdapter();
		vp_guide.setAdapter(mAdapter);
		
		//设置viewpager的滑动监听
		vp_guide.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if(position==2){
					btn_guide_start.setVisibility(View.VISIBLE);
				}else{
					btn_guide_start.setVisibility(View.INVISIBLE);
				}
			}
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				int disOffset = (int) (position * dis + positionOffset* dis);
				
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
									LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.leftMargin = disOffset;
				iv_guide_redpoint.setLayoutParams(params);
				
			}
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		
		btn_guide_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPref.edit().putBoolean("isFirstEnter", true).commit();
				Intent intent = new Intent(GuideActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	//初始化Viewpager的图片
	private void initData() {
		
		list = new ArrayList<ImageView>();
		for (int i = 0; i < ids.length; i++) {
			ImageView view = new ImageView(this);
			view.setBackgroundResource(ids[i]);
			list.add(view);
			
			//创建小圆点
			ImageView ivPoint = new ImageView(this);
			if(i!=0){
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.leftMargin = DensityUtils.dp2px(30, this);
				ivPoint.setLayoutParams(params);
			}
			ivPoint.setImageResource(R.drawable.shape_point_gray);
			ll_guide_add.addView(ivPoint);
		}
		
		//监听控件绘制
		iv_guide_redpoint.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				iv_guide_redpoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				dis = ll_guide_add.getChildAt(1).getLeft() - ll_guide_add.getChildAt(0).getLeft();
			}
		});
		
		
	}
	
	
	class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return ids.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = list.get(position);
			container.addView(view);
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
		
	}
}
