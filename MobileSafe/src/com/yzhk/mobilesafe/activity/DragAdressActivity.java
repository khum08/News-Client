package com.yzhk.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzhk.mobilesafe.R;

public class DragAdressActivity extends Activity {

	private ImageView iv_drag;
	private SharedPreferences sp;
	private WindowManager mWM;
	private int winWidth;
	private int winHeigth;
	private TextView tv_top;
	private TextView tv_bottom;
	private long[] mHit = new long[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		int left = sp.getInt("left", 0);
		int top = sp.getInt("top", 0);

		iv_drag = (ImageView) findViewById(R.id.iv_drag);
		tv_top = (TextView) findViewById(R.id.tv_top);
		tv_bottom = (TextView) findViewById(R.id.tv_bottom);

		mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
		Point point = new Point();
		mWM.getDefaultDisplay().getSize(point);
		winWidth = point.x;
		winHeigth = point.y;

		if (top > winHeigth / 2) {
			tv_bottom.setVisibility(View.INVISIBLE);
			tv_top.setVisibility(View.VISIBLE);
		} else {
			tv_bottom.setVisibility(View.VISIBLE);
			tv_top.setVisibility(View.INVISIBLE);
		}
		if (top < 30) {
			top = 30;
		}
		if (left < 5) {
			left = 5;
		}

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_drag
				.getLayoutParams();
		params.leftMargin = left;
		params.topMargin = top;
		iv_drag.setLayoutParams(params);

		iv_drag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.arraycopy(mHit, 1, mHit, 0, mHit.length - 1);
				mHit[mHit.length - 1] = SystemClock.uptimeMillis();
				if (mHit[mHit.length - 1] - mHit[0] < 500) {
					iv_drag.layout((winWidth - iv_drag.getWidth()) / 2,
							iv_drag.getTop(),
							(winWidth + iv_drag.getWidth()) / 2,
							iv_drag.getBottom());
				}

			}
		});

		iv_drag.setOnTouchListener(new OnTouchListener() {

			private int startX;
			private int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					break;

				case MotionEvent.ACTION_MOVE:

					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();

					int dx = endX - startX;
					int dy = endY - startY;

					int t = iv_drag.getTop() + dy;
					int b = iv_drag.getBottom() + dy;
					int l = iv_drag.getLeft() + dx;
					int r = iv_drag.getRight() + dx;

					if (l < 0 || r > winWidth || t < 0 || b > winHeigth - 20) {
						break;
					}

					if (t > winHeigth / 2) {
						tv_bottom.setVisibility(View.INVISIBLE);
						tv_top.setVisibility(View.VISIBLE);
					} else {
						tv_bottom.setVisibility(View.VISIBLE);
						tv_top.setVisibility(View.INVISIBLE);
					}
					iv_drag.layout(l, t, r, b);

					startX = endX;
					startY = endY;
					break;
				case MotionEvent.ACTION_UP:

					int left = iv_drag.getLeft();
					int top = iv_drag.getTop();
					sp.edit().putInt("left", left).commit();
					sp.edit().putInt("top", top).commit();

					break;
				default:
					break;
				}

				return false;
			}
		});

	}
}
