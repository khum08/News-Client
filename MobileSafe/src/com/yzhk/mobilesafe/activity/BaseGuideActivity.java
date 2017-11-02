package com.yzhk.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public abstract class BaseGuideActivity extends Activity {

	private GestureDetector mDetector;
	public SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		mDetector = new GestureDetector(this, new SimpleOnGestureListener() {
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				if (Math.abs(velocityX) < 100) {
					Toast.makeText(BaseGuideActivity.this, "滑动太TM慢了",
							Toast.LENGTH_SHORT).show();
					return true;
				}
				if (Math.abs(e1.getRawY() - e2.getRawY()) > 300) {
					Toast.makeText(BaseGuideActivity.this, "不能这样滑哦，亲！",
							Toast.LENGTH_SHORT).show();
					return true;
				}

				if (e1.getRawX() - e2.getRawX() > 100) {
					showNextPage();
					return true;
				}
				if (e2.getRawX() - e1.getRawX() > 100) {
					showPreviousPage();
					return true;

				}

				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}

	public abstract void showNextPage();

	public abstract void showPreviousPage();

	public void next(View v) {
		showNextPage();
	}

	public void previous(View v) {
		showPreviousPage();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
