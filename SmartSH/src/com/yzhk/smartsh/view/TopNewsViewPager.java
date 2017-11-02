package com.yzhk.smartsh.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager {

	private int startX;
	private int startY;

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TopNewsViewPager(Context context) {
		super(context);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			startX = (int) ev.getX();
			startY = (int) ev.getY();
			
			break;
		case MotionEvent.ACTION_MOVE:
			
			int endX = (int) ev.getX();
			int endY = (int) ev.getY();
			int dx = endX - startX;
			int dy = endY - startY;
			
			
			if (Math.abs(dy) < Math.abs(dx)) {
				int currentItem = getCurrentItem();
				if (dx > 0) {
					if (currentItem == 0) {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {
					int count = getAdapter().getCount();// item总数
					if (currentItem == count - 1) {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}

			} else {
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	
}
