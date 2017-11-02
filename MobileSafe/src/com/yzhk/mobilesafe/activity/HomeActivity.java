package com.yzhk.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yzhk.mobilesafe.R;
import com.yzhk.mobilesafe.utils.MD5Utils;

public class HomeActivity extends Activity {

	private Button btnOk;
	private Button btnCancel;
	private EditText etPassword;
	private EditText etPasswordConfirm;
	private SharedPreferences sp;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		GridView gv = (GridView) findViewById(R.id.gv_function);
		gv.setAdapter(new HomeAdapter());
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				switch (position) {
				case 8:
					startActivity(new Intent(HomeActivity.this,SettingActivity.class));
					break;
				case 7:
					startActivity(new Intent(HomeActivity.this,AToolsActivity.class));
					break;
				case 6:
					startActivity(new Intent(HomeActivity.this,BaseCacheActivity.class));
					break;
				case 5:
					startActivity(new Intent(HomeActivity.this,AnitVirusActivity.class));
					break;
				case 4:
					startActivity(new Intent(HomeActivity.this,TrafficManager.class));
					break;
				case 3:
					startActivity(new Intent(HomeActivity.this,ProcessManageActivity.class));
					break;
				case 2:
					startActivity(new Intent(HomeActivity.this,AppManagerActivity.class));
					break;
				case 1:
					startActivity(new Intent(HomeActivity.this,CommunSafeActivity.class));
					break;
				case 0:
					showMDialog();
					break;

				default:
					break;
				}
			}
		});
		
		
	}

	//进入手机防盗的密码
	protected void showMDialog() {
		String passwordSaved = sp.getString("password", null);
		if(!TextUtils.isEmpty(passwordSaved)){
			showInputPasswordDialog();
		}else{
			showSetPasswordDialog();
		}
		
		
	}
	//0.手机防盗 输入密码框
	private void showInputPasswordDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		
		View view = View.inflate(this, R.layout.view_dialog_inputpass, null);
		dialog.setView(view);
		
		btnOk = (Button) view.findViewById(R.id.btn_ok);
		btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		etPassword = (EditText) view.findViewById(R.id.et_password);
		
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String password = etPassword.getText().toString();
				String passwordSaved = sp.getString("password", null);
				if(!TextUtils.isEmpty(password)){
					if(passwordSaved.equals(MD5Utils.encode(password))){
						startActivity(new Intent(HomeActivity.this,LostFoundActivity.class));
						dialog.dismiss();
					}else{
						etPassword.setText("");
						Toast.makeText(HomeActivity.this, "密码错误!", Toast.LENGTH_SHORT).show();
					}
					
				}else{
					Toast.makeText(HomeActivity.this, "密码不能为空!", Toast.LENGTH_SHORT).show();
				}
			
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	//0.手机防盗 设置密码框
	private void showSetPasswordDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		
		View view = View.inflate(this, R.layout.view_dialog_setpass, null);
		dialog.setView(view);
		dialog.show();
		
		btnOk = (Button) view.findViewById(R.id.btn_ok);
		btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		etPassword = (EditText) view.findViewById(R.id.et_password);
		etPasswordConfirm = (EditText) view.findViewById(R.id.et_password_confirm);
		
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String password = etPassword.getText().toString();
				String passwordCon = etPasswordConfirm.getText().toString();
				if(!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordCon)){
					if(password.equals(passwordCon)){
						sp.edit().putString("password", MD5Utils.encode(password)).commit();
						startActivity(new Intent(HomeActivity.this,LostFoundActivity.class));
						dialog.dismiss();
						Toast.makeText(HomeActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
					}else{
						etPasswordConfirm.setText("");
						Toast.makeText(HomeActivity.this, "两次密码不一致!", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(HomeActivity.this, "输入框不能为空!", Toast.LENGTH_SHORT).show();
				}
			
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		
	}
	
	
	//gridview's adapter
	class HomeAdapter extends BaseAdapter {

		String[] tv_items = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒",
				"缓存清理", "高级工具", "设置中心"};

		int[] iv_items = { R.drawable.home_safe, R.drawable.home_callmsgsafe,
				R.drawable.home_apps, R.drawable.home_taskmanager,
				R.drawable.home_netmanager, R.drawable.home_trojan,
				R.drawable.home_sysoptimize, R.drawable.home_tools,
				R.drawable.home_settings };

		@Override
		public int getCount() {
			return tv_items.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			View view = View.inflate(HomeActivity.this,
					R.layout.home_list_item, null);

			ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
			TextView tv_item = (TextView) view.findViewById(R.id.tv_item);

			iv_item.setImageResource(iv_items[position]);
			tv_item.setText(tv_items[position]);

			return view;
		}

	}
}
