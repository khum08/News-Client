package com.yzhk.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yzhk.mobilesafe.R;
import com.yzhk.mobilesafe.db.domain.ProcessInfo;
import com.yzhk.mobilesafe.engine.ProcessInfoPro;

public class ProcessManageActivity extends Activity {

	private MyAdapter mAdapter;
	private List<ProcessInfo> processInfolist;
	private List<ProcessInfo> userProcessInfolist;
	private List<ProcessInfo> sysProcessInfolist;

	private ListView lv_process;
	private ProgressBar pb_process;
	private TextView tv_process_hover;
	private SharedPreferences sp;
	private int processCount;
	private long availMemory;
	private long releaseMem=0;
	private TextView tv_process_acount;
	private TextView tv_process_memory;
	private long totalMemory;
	private String totalSize;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			pb_process.setVisibility(View.GONE);
			tv_process_hover.setVisibility(View.VISIBLE);

			mAdapter = new MyAdapter();
			lv_process.setAdapter(mAdapter);

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_processmanager);

		initData();
		initListview();
		initButton();
	}

	// 初始化可点击的TextView
	private void initButton() {
		TextView tv_process_selectall = (TextView) findViewById(R.id.tv_process_selectall);
		TextView tv_process_selectother = (TextView) findViewById(R.id.tv_process_selectother);
		TextView tv_process_clean = (TextView) findViewById(R.id.tv_process_clean);
		TextView tv_process_setting = (TextView) findViewById(R.id.tv_process_setting);

		tv_process_selectall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (userProcessInfolist != null) {
					for (ProcessInfo pro : userProcessInfolist) {
						if (pro.getProcessPackage().equals(getPackageName())) {
							continue;
						}
						pro.setIschecked(true);

					}
				}
				if (sysProcessInfolist != null) {
					for (ProcessInfo pro : sysProcessInfolist) {
						pro.setIschecked(true);
					}
				}

				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
			}
		});
		tv_process_selectother.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (userProcessInfolist != null) {
					for (ProcessInfo pro : userProcessInfolist) {
						if (pro.getProcessPackage().equals(getPackageName())) {
							continue;
						}
						pro.setIschecked(!pro.isIschecked());

					}
				}
				if (sysProcessInfolist != null) {
					for (ProcessInfo pro : sysProcessInfolist) {
						pro.setIschecked(!pro.isIschecked());
					}
				}

				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}

			}
		});
		tv_process_clean.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//刷新界面
				ArrayList<ProcessInfo> tempList = new ArrayList<ProcessInfo>();
				
				if(sysProcessInfolist!=null){
					for (ProcessInfo ProInfo : sysProcessInfolist) {
						if(ProInfo.isIschecked()==true){
							tempList.add(ProInfo);
						}
					}
				}
				if(userProcessInfolist!=null){
					for (ProcessInfo ProInfo2 : userProcessInfolist) {
						if(ProInfo2.isIschecked()==true){
							tempList.add(ProInfo2);
						}
					}
				}
				
				for (ProcessInfo processInfo : tempList) {
					if(sysProcessInfolist.contains(processInfo)){
						sysProcessInfolist.remove(processInfo);
					}
					if(userProcessInfolist.contains(processInfo)){
						userProcessInfolist.remove(processInfo);
					}
					long memoryUsed = processInfo.getMemoryUsed();
					releaseMem += memoryUsed;
					availMemory += memoryUsed;
					
					//后台杀进程
					ProcessInfoPro.killPro(ProcessManageActivity.this, processInfo);
				}
				
				if(tempList!=null){
					processCount -= tempList.size();
					tv_process_acount.setText("进程" + processCount + "个");
					String size = Formatter.formatShortFileSize(ProcessManageActivity.this, availMemory);
					String releaseSize = Formatter.formatShortFileSize(ProcessManageActivity.this, releaseMem);
					tv_process_memory.setText("剩余/总内存：" + size + "/" + totalSize);
					
					Toast.makeText(ProcessManageActivity.this,
							"杀死了"+tempList.size()+"个进程，释放了"+releaseSize+"内存",
							Toast.LENGTH_SHORT).show();
				}
				if(mAdapter!=null){
					mAdapter.notifyDataSetChanged();
				}
				
			}
		});
		tv_process_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(ProcessManageActivity.this,
						ProcessSettingActivity.class), 0);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}

	// 初始化listview
	private void initListview() {
		lv_process = (ListView) findViewById(R.id.lv_process);
		sp = getSharedPreferences("config", MODE_PRIVATE);

		new Thread() {
			public void run() {
				processInfolist = ProcessInfoPro
						.getProcessInfo(ProcessManageActivity.this);
				userProcessInfolist = new ArrayList<ProcessInfo>();
				sysProcessInfolist = new ArrayList<ProcessInfo>();
				for (ProcessInfo proInfo : processInfolist) {
					if (proInfo.isSystemProcess()) {
						sysProcessInfolist.add(proInfo);
					} else {
						userProcessInfolist.add(proInfo);
					}
				}

				mHandler.sendEmptyMessage(0);

			};
		}.start();

		pb_process = (ProgressBar) findViewById(R.id.pb_process);
		tv_process_hover = (TextView) findViewById(R.id.tv_process_hover);

		// listview滑动监听
		lv_process.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				if (userProcessInfolist != null && sysProcessInfolist != null) {
					if (firstVisibleItem < userProcessInfolist.size() + 1) {
						tv_process_hover.setText("用户进程："
								+ userProcessInfolist.size());
					} else {
						tv_process_hover.setText("系统进程："
								+ sysProcessInfolist.size());
					}
				}
			}
		});
		// listview item点击监听
		lv_process.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CheckBox cb_process = (CheckBox) view
						.findViewById(R.id.cb_process);
				if (position == 0 || position == userProcessInfolist.size() + 1) {
					return;
				} else {
					if (position < userProcessInfolist.size() + 1) {
						ProcessInfo proInfo = userProcessInfolist.get(position - 1);

						if (proInfo != null) {
							if (proInfo.getProcessPackage().equals(getPackageName())) {
								Toast.makeText(ProcessManageActivity.this, "你难道想让我自杀吗？", Toast.LENGTH_SHORT).show();
								return;
							}
							proInfo.setIschecked(!proInfo.isIschecked());
							cb_process.setChecked(proInfo.isIschecked());

						}
					} else {
						ProcessInfo proInfo2 = sysProcessInfolist.get(position
								- 2 - userProcessInfolist.size());
						if (proInfo2 != null) {
							proInfo2.setIschecked(!proInfo2.isIschecked());
							cb_process.setChecked(proInfo2.isIschecked());
						}
					}
				}
			}
		});

	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getItemViewType(int position) {
			if (position == 0 || position == userProcessInfolist.size() + 1) {
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getCount() {
			boolean showSysPro = sp.getBoolean("showSystemProcess", false);
			if (showSysPro) {

				return userProcessInfolist.size() + 1;
			} else {
				return processInfolist.size() + 2;
			}
		}

		@Override
		public ProcessInfo getItem(int position) {
			if (position == 0 || position == userProcessInfolist.size() + 1) {
				return null;
			} else {
				if (position > userProcessInfolist.size() + 1) {
					return sysProcessInfolist.get(position
							- userProcessInfolist.size() - 2);
				} else {
					return userProcessInfolist.get(position - 1);
				}
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int type = getItemViewType(position);
			if (type == 0) {
				// 进程数量条目
				if (position == 0) {
					if (convertView == null) {
						convertView = View.inflate(ProcessManageActivity.this,
								R.layout.view_listview_processcount, null);
					}
					TextView tv_process_count = (TextView) convertView
							.findViewById(R.id.tv_process_count);
					tv_process_count.setText("用户进程："
							+ userProcessInfolist.size());

					return convertView;
				} else {
					if (convertView == null) {
						convertView = View.inflate(ProcessManageActivity.this,
								R.layout.view_listview_processcount, null);
					}
					TextView tv_process_count = (TextView) convertView
							.findViewById(R.id.tv_process_count);
					tv_process_count.setText("系统进程："
							+ sysProcessInfolist.size());

					return convertView;
				}

			} else {
				// 进程条目
				ViewHolder mViewHolder = null;
				if (convertView == null) {
					mViewHolder = new ViewHolder();
					convertView = View.inflate(getApplicationContext(),
							R.layout.view_listview_process, null);
					mViewHolder.iv_process_icon = (ImageView) convertView
							.findViewById(R.id.iv_process_icon);
					mViewHolder.tv_process_name = (TextView) convertView
							.findViewById(R.id.tv_process_name);
					mViewHolder.tv_process_size = (TextView) convertView
							.findViewById(R.id.tv_process_size);
					mViewHolder.cb_process = (CheckBox) convertView
							.findViewById(R.id.cb_process);

					convertView.setTag(mViewHolder);
				} else {
					mViewHolder = (ViewHolder) convertView.getTag();
				}
				mViewHolder.iv_process_icon.setImageDrawable(getItem(position)
						.getIcon());
				mViewHolder.tv_process_name.setText(getItem(position)
						.getProcessName());
				String size = Formatter.formatFileSize(
						ProcessManageActivity.this, getItem(position)
								.getMemoryUsed());
				mViewHolder.tv_process_size.setText("内存占用：" + size);
				mViewHolder.cb_process.setChecked(getItem(position)
						.isIschecked());

				return convertView;
			}

		}

	}

	static class ViewHolder {
		ImageView iv_process_icon;
		TextView tv_process_name;
		TextView tv_process_size;
		CheckBox cb_process;
	}

	// 初始化进程数和内存占用
	private void initData() {
		tv_process_acount = (TextView) findViewById(R.id.tv_process_acount);
		tv_process_memory = (TextView) findViewById(R.id.tv_process_memory);

		processCount = ProcessInfoPro.getProcessCount(this);
		tv_process_acount.setText("进程" + processCount + "个");

		totalMemory = ProcessInfoPro.getTotalMemory(this);
		totalSize = Formatter.formatFileSize(this, totalMemory);
		availMemory = ProcessInfoPro.getAvailMemory(this);
		String availSize = Formatter.formatFileSize(this, availMemory);

		tv_process_memory.setText("剩余/总内存：" + availSize + "/" + totalSize);
	}
}
