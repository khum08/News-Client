package com.yzhk.mobilesafe.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.yzhk.mobilesafe.R;
import com.yzhk.mobilesafe.db.dao.BlackNumbDao;
import com.yzhk.mobilesafe.db.domain.BlackNumber;

public class CommunSafeActivity extends Activity {

	private ImageView iv_addblacknumber;
	private AlertDialog dialog;
	private List<BlackNumber> list;
	private ListView lv_blacknumber;
	private EditText et_blacknumb;
	private RadioGroup rg_mode;
	private int mode;
	private MyAdapter mAdapter;
	private BlackNumbDao blackNumbDao;
	public boolean isLoading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blacknumb);

		lv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);
		// getBlackNumb2list();
		list = initData(0);
		mAdapter = new MyAdapter();
		lv_blacknumber.setAdapter(mAdapter);

		// 监听黑名单添加按钮
		iv_addblacknumber = (ImageView) findViewById(R.id.iv_addblacknumber);
		iv_addblacknumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showAddDialog();
			}
		});

		// 监听滑动，刷新数据
		lv_blacknumber.setOnScrollListener(new MyOnScrollListener());

	}

	class MyOnScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			switch (scrollState) {
			case SCROLL_STATE_FLING:
				
				break;
			case SCROLL_STATE_IDLE:
				if(lv_blacknumber.getLastVisiblePosition()>=list.size()-1
						&& !isLoading ){
					isLoading = true;//防止重复加载的锁
					
					int count = blackNumbDao.getCount();
					if(count>list.size()){
						List<BlackNumber> moreData = initData(list.size());
						list.addAll(moreData);
						if(mAdapter!=null){
							mAdapter.notifyDataSetChanged();
						}
					}else{
						Toast.makeText(getApplicationContext(), "没有更多了哦", Toast.LENGTH_SHORT).show();
					}
					
					isLoading = false;
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

	}

	// listview的适配器
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {

			ViewHolder vHolder = null;
			if (convertView == null) {
				convertView = View.inflate(CommunSafeActivity.this,
						R.layout.view_list_blacknumb, null);
				vHolder = new ViewHolder();
				vHolder.tv_black_phone = (TextView) convertView
						.findViewById(R.id.tv_black_phone);
				vHolder.tv_black_mode = (TextView) convertView
						.findViewById(R.id.tv_black_mode);
				vHolder.iv_black_delete = (ImageView) convertView
						.findViewById(R.id.iv_black_delete);

				convertView.setTag(vHolder);
			} else {
				vHolder = (ViewHolder) convertView.getTag();
			}

			// 监听删除按钮
			vHolder.iv_black_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					blackNumbDao.delete(list.get(position).getPhone());

					list.remove(position);
					if (mAdapter != null) {
						mAdapter.notifyDataSetChanged();
					}
				}
			});

			vHolder.tv_black_phone.setText(list.get(position).getPhone());
			switch (Integer.parseInt(list.get(position).getMode())) {
			case 1:
				vHolder.tv_black_mode.setText("拦截短信");
				break;
			case 2:
				vHolder.tv_black_mode.setText("拦截电话");
				break;
			case 3:
				vHolder.tv_black_mode.setText("拦截短信&电话");
				break;
			default:
				break;
			}

			return convertView;
		}

	}

	static class ViewHolder {
		TextView tv_black_phone;
		TextView tv_black_mode;
		ImageView iv_black_delete;
	}

	private List<BlackNumber> initData(int startIndex) {
		blackNumbDao = BlackNumbDao.getInstanse(this);
		
		List<BlackNumber> list = blackNumbDao.findTwenty(startIndex);
		return list;
	}

	private void getBlackNumb2list() {

		blackNumbDao = BlackNumbDao.getInstanse(this);
		list = blackNumbDao.queryAll();
	}

	// 添加黑名单对话框
	protected void showAddDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		dialog = builder.create();
		View view = View.inflate(this, R.layout.view_dialog_addblack, null);

		et_blacknumb = (EditText) view.findViewById(R.id.et_blacknumb);
		rg_mode = (RadioGroup) view.findViewById(R.id.rg_mode);
		Button bt_blackn_ok = (Button) view.findViewById(R.id.bt_blackn_ok);
		Button bt_blackn_cancel = (Button) view
				.findViewById(R.id.bt_blackn_cancel);

		rg_mode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_1:
					mode = 1;
					break;
				case R.id.rb_2:
					mode = 2;
					break;
				case R.id.rb_3:
					mode = 3;
					break;

				default:
					mode = 1;
					break;
				}
			}
		});

		bt_blackn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String phone = et_blacknumb.getText().toString();

				if (!TextUtils.isEmpty(phone)) {
					blackNumbDao.insert(phone, mode + "");

					// 刷新ui
					// getBlackNumb2list();
					// lv_blacknumber.setAdapter(new MyAdapter());
					BlackNumber bn = new BlackNumber(phone, mode + "");
					list.add(0, bn);
					if (mAdapter != null) {
						mAdapter.notifyDataSetChanged();
					}

				} else {
					Toast.makeText(getApplicationContext(), "号码不能为空哦",
							Toast.LENGTH_SHORT).show();
				}

				dialog.dismiss();
			}
		});
		bt_blackn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}
}
