package com.yzhk.smartsh.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.event.OnScrollStateChanged;
import com.yzhk.smartsh.R;

public class PullToRefreshListView extends ListView {
	
	private static final int STATE_PULL_TO_REFRESH = 1;
	private static final int STATE_RELEASE_TO_REFRESH = 2;
	private static final int STATE_REFRESHING = 3;
	
	private int mCurrentState = STATE_PULL_TO_REFRESH;
	
	private View mHeaderView;
	private ImageView iv_refresh;
	private ProgressBar pb_refresh;
	private TextView tv_refresh_title;
	private TextView tv_refresh_time;
	private int startY = -1;
	private int measuredHeight;
	private RotateAnimation rotateDown;
	private RotateAnimation rotateUp;
	private int dy;

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public PullToRefreshListView(Context context) {
		super(context);
		initView();
	}
	
	public boolean isLoadingMore;
	public void initView(){
		mHeaderView = View.inflate(getContext(), R.layout.view_pulltorefresh_header, null);
		this.addHeaderView(mHeaderView);
		
		iv_refresh = (ImageView) mHeaderView.findViewById(R.id.iv_refresh);
		pb_refresh = (ProgressBar) mHeaderView.findViewById(R.id.pb_refresh);
		tv_refresh_title = (TextView)mHeaderView.findViewById(R.id.tv_refresh_title);
		tv_refresh_time = (TextView)mHeaderView.findViewById(R.id.tv_refresh_time);
		initAnim();
		
		//隐藏头布局
		mHeaderView.measure(0, 0);
		measuredHeight = mHeaderView.getMeasuredHeight();
		mHeaderView.setPadding(0, -measuredHeight, 0, 0);
		
		mFooterView = View.inflate(getContext(), R.layout.listview_footerview_tab, null);
		this.addFooterView(mFooterView);
		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
		
		this.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case SCROLL_STATE_IDLE:
					int lastVisiblePosition = getLastVisiblePosition();
					int count = getCount();
					if(lastVisiblePosition==count-1 && !isLoadingMore){
						isLoadingMore = true;
						mFooterView.setPadding(0, 0, 0, 0);
						
						setSelection(count-1);
						mListen.onLoadMore();
						
					}
					
					break;

				default:
					break;
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if(startY==-1){
				startY = (int) ev.getY();
			}
			int endY = (int) ev.getY();
			dy = endY - startY;
			
			// 下拉
			int firstVisiblePosition = getFirstVisiblePosition();
			if (firstVisiblePosition == 0 && dy >0) {
				if (dy > measuredHeight && mCurrentState != STATE_RELEASE_TO_REFRESH) {
					mCurrentState = STATE_RELEASE_TO_REFRESH;
					refreshState();
				} else if(dy<= measuredHeight && mCurrentState != STATE_PULL_TO_REFRESH){
					mCurrentState = STATE_PULL_TO_REFRESH;
					refreshState();
				}
				
				if(-measuredHeight+dy>150){
					mHeaderView.setPadding(0, 150, 0, 0);
				}else{
					mHeaderView.setPadding(0, -measuredHeight+dy, 0, 0);
				}
				return true;
			}
			
			
			break;
		case MotionEvent.ACTION_UP:
			startY = -1;
			if(mCurrentState == STATE_RELEASE_TO_REFRESH){
				mCurrentState = STATE_REFRESHING;
				refreshState();
				mHeaderView.setPadding(0, 0, 0, 0);
				if(mListen!=null){
					mListen.listen();
				}
			}else if(mCurrentState == STATE_PULL_TO_REFRESH){
				
				refreshState();
				mHeaderView.setPadding(0, -measuredHeight, 0, 0);
			}
			
			break;

		default:
			break;
		}
		
		
		
		return super.onTouchEvent(ev);
	}
	
	
	public void initAnim(){
		rotateDown = new RotateAnimation(0, 180, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateDown.setDuration(300);
		rotateDown.setFillAfter(true);

		rotateUp = new RotateAnimation(0, -180, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateUp.setDuration(300);
		rotateUp.setFillAfter(true);
	}
	
	public void refreshState(){
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			tv_refresh_title.setText("下拉刷新");
			pb_refresh.setVisibility(View.INVISIBLE);
			iv_refresh.setVisibility(View.VISIBLE);
			break;
		case STATE_RELEASE_TO_REFRESH:
			tv_refresh_title.setText("释放刷新");
			pb_refresh.setVisibility(View.INVISIBLE);
			iv_refresh.setVisibility(View.VISIBLE);
			iv_refresh.startAnimation(rotateUp);
			break;
		case STATE_REFRESHING:
			tv_refresh_title.setText("正在刷新");
			iv_refresh.clearAnimation();
			iv_refresh.setVisibility(View.INVISIBLE);
			pb_refresh.setVisibility(View.VISIBLE);
			
			break;

		default:
			break;
		}
	}
	
	public void refreshComplete(boolean success){
		if(!isLoadingMore){
			mCurrentState = STATE_PULL_TO_REFRESH;
			refreshState();
			mHeaderView.setPadding(0, -measuredHeight, 0, 0);
			
			if(success){
				SimpleDateFormat format = new SimpleDateFormat("MM-dd hh:mm");
				tv_refresh_time.setText("上次刷新时间："+format.format(new Date()));
			}
			
		}else{
//			mHeaderView.setPadding(0, -measuredHeight, 0, 0);
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
			isLoadingMore = false;
		}
		
		
	}
	
	private OnRefreshListener mListen;
	private View mFooterView;
	private int mFooterViewHeight;

	public void setOnRefreshListener(OnRefreshListener listen){
		mListen = listen;
	}
	
	public interface OnRefreshListener{
		public void listen();
		public void onLoadMore();
	}
	
	
}



















