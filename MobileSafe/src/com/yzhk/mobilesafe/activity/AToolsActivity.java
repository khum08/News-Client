package com.yzhk.mobilesafe.activity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yzhk.mobilesafe.R;
import com.yzhk.mobilesafe.db.dao.Addressdao;
import com.yzhk.mobilesafe.engine.SmsBackUp;
import com.yzhk.mobilesafe.engine.SmsBackUp.CallBack;
import com.yzhk.mobilesafe.service.RocketService;

public class AToolsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);

		initCommomNum();
		initRocket();
		initSmsBackUp();
		initProcessLock();

	}
	
	private void initProcessLock() {
		TextView tv_app_lock = (TextView) findViewById(R.id.tv_app_lock);
		tv_app_lock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AToolsActivity.this, AToolsAppLockActivity.class));
			}
		});
		
		
	}

	private void initCommomNum() {
		TextView tv_atool_commonnum = (TextView) findViewById(R.id.tv_atool_commonnum);
		tv_atool_commonnum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AToolsActivity.this, AToolsCallActivity.class));
			}
		});
		
	}

	//初始化短信备份界面
	private void initSmsBackUp() {

		TextView tv_sms_backup = (TextView) findViewById(R.id.tv_sms_backup);
		tv_sms_backup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				confirmBackupDialog();
			}
		});

	}
	
	//短信备份对话框
	private void confirmBackupDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("确认备份")
				.setMessage("短信将备份至sd卡的'smss.xml'文件中")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				final ProgressDialog pd = new ProgressDialog(
						AToolsActivity.this);
				pd.setIcon(R.drawable.ic_launcher);
				pd.setTitle("备份短信(刻意放慢)");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.show();

				new Thread() {
					public void run() {

						String path = Environment.getExternalStorageDirectory()
								.getAbsolutePath()
								+ File.separator
								+ "Smss.xml";
						SmsBackUp.backUp(getApplicationContext(), path, new CallBack() {
							
							@Override
							public void setProgress(int position) {
								pd.setProgress(position);
							}
							
							@Override
							public void setMax(int max) {
								pd.setMax(max);
							}
						});

						pd.dismiss();
					};
				}.start();

				
			}
		}).setNegativeButton("取消", null);
		builder.show();
	}

	// 初始化小火箭
	private void initRocket() {
		TextView iv_rocket = (TextView) findViewById(R.id.tv_rocket);
		iv_rocket.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showRocketDialog();
			}
		});
	}

	// 开启小火箭对话框
	protected void showRocketDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("小火箭服务");

		builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				stopService(new Intent(getApplicationContext(),
						RocketService.class));
			}
		});

		builder.setPositiveButton("开启", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				startService(new Intent(getApplicationContext(),
						RocketService.class));
			}
		});

		builder.show();

	}

	public void phoneSearch(View view) {
		showdialog();
	}

	// 归属地查询对话框
	private void showdialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog dialog = builder.create();

		View v = View.inflate(this, R.layout.view_dialog_phonesearch, null);
		dialog.setView(v);
		dialog.show();

		final EditText et_phoneNum = (EditText) v
				.findViewById(R.id.et_phonenumb);
		Button bt_search = (Button) v.findViewById(R.id.bt_search);
		final TextView tv_showPlace = (TextView) v
				.findViewById(R.id.tv_showplace);

		bt_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phoneNum = et_phoneNum.getText().toString().trim();
				if (!TextUtils.isEmpty(phoneNum)) {
					String address = Addressdao.searchAddress(phoneNum);
					tv_showPlace.setText(address);
				} else {
					Toast.makeText(AToolsActivity.this, "号码不能为空哦",
							Toast.LENGTH_SHORT).show();
					ScaleAnimation scale = new ScaleAnimation(1.0f, 0f, 1.0f,
							0f, et_phoneNum.getWidth() / 2, et_phoneNum
									.getHeight() / 2);
					scale.setDuration(1000);
					et_phoneNum.startAnimation(scale);
					showVibrator();
				}
			}
		});
		et_phoneNum.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s.toString())) {
					String address = Addressdao.searchAddress(s.toString());
					tv_showPlace.setText(address);
				} else {
					Toast.makeText(AToolsActivity.this, "号码不能为空哦",
							Toast.LENGTH_SHORT).show();

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	protected void showVibrator() {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(new long[] { 1000, 1000, 1000, 2000 }, -1);
	}

}
