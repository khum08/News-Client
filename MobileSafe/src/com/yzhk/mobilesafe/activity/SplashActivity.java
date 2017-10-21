package com.yzhk.mobilesafe.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yzhk.mobilesafe.R;
import com.yzhk.mobilesafe.utils.StreamUtil;

public class SplashActivity extends Activity {
	
	protected static final int CODE_UPDATE_DIALOG = 0;
	protected static final int CODE_ERROR_URL = 1;
	protected static final int CODE_ERROR_IO = 2;
	protected static final int CODE_ERROR_JSON = 3;
	protected static final int CODE_ENTERHOME = 4;
	private String mVersionName;
	private int mVersionCode;
	private String mDescription;
	private String mDownloadUrl;
	
	private int versionCode;
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			case CODE_ERROR_URL:
				Toast.makeText(SplashActivity.this, "URL网址错误", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_ERROR_IO:
				Toast.makeText(SplashActivity.this, "服务器已关闭", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_ERROR_JSON:
				Toast.makeText(SplashActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_ENTERHOME:
				enterHome();
				break;	
			}
		};
	};
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		String version = getVersion();
		
		RelativeLayout rlSplash = (RelativeLayout) findViewById(R.id.rl_splash);
		
		TextView tv_version = (TextView) findViewById(R.id.tv_version);
		tv_version.setText("版本号："+version);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		copyDb("address.db");
		copyDb("commonnum.db");
		
		
		boolean autoUpdate = sp.getBoolean("auto_update", true);
		if(autoUpdate){
			checkVersion();
		}else{
			handler.sendEmptyMessageDelayed(CODE_ENTERHOME, 2000);
		}

		//渐变页面 属性动画
		AlphaAnimation aa = new AlphaAnimation(0.1f, 1f);
		aa.setDuration(1500);
		rlSplash.startAnimation(aa);
		
	}
	
	private void checkVersion() {
		new Thread(){
			
			private HttpURLConnection conn;

			@Override
			public void run() {
				Message msg = Message.obtain();
				long startTime = System.currentTimeMillis();
				
				try {
					System.out.println("11111111");
					URL url = new URL("http://192.168.0.109:8080/checkversion.json");
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					conn.connect();
					System.out.println("2222222222222222");
					if(conn.getResponseCode()==200){
						System.out.println("链接成功");
						InputStream inputStream = conn.getInputStream();
						String mjson = StreamUtil.getStringFromStream(inputStream);
						
						JSONObject jo =new JSONObject(mjson);
						mVersionName = jo.getString("versionName");
						mVersionCode = jo.getInt("versionCode");
						mDescription = jo.getString("description");
						mDownloadUrl = jo.getString("downloadUrl");
						System.out.println(mDescription);
						if(mVersionCode>versionCode){
							msg.what = CODE_UPDATE_DIALOG;
						}else{
							enterHome();
						}
						
					}
				} catch (MalformedURLException e) {
					msg.what = CODE_ERROR_URL;
					e.printStackTrace();
				} catch (IOException e) {
					msg.what = CODE_ERROR_IO;
					e.printStackTrace();
				} catch (JSONException e) {
					msg.what = CODE_ERROR_JSON;
					e.printStackTrace();
				}finally{
					long endTime = System.currentTimeMillis();
					long timeUsed = endTime - startTime;
					if(timeUsed<2000){
						try {
							Thread.sleep(2000-timeUsed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					handler.sendMessage(msg);
					if(conn!=null){
						conn.disconnect();
					}
				}
				
			}

		}.start();
	}

	//提示升级对话框
	public void showUpdateDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("更新提示");
		builder.setMessage("最新版本："+mVersionName+"\n"+"版本描述："+mDescription);
		builder.setPositiveButton("立即下载", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				download();
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});
		builder.show();
		
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface arg0) {
				enterHome();
			}
		});
		
	}
	
	//下载更新安装包
	protected void download() {
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			HttpUtils http =new HttpUtils();
			String target = Environment.getExternalStorageDirectory()+"/mobilesafe.apk";
			http.download(mDownloadUrl, target, new RequestCallBack<File>() {
				
				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					ProgressBar pb_download = (ProgressBar) findViewById(R.id.pb_download);
					pb_download.setVisibility(View.VISIBLE);
					pb_download.setMax((int) total);
					pb_download.setProgress((int) current);
				}
				
				@Override
				public void onSuccess(ResponseInfo<File> resp) {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
			        intent.addCategory("android.intent.category.DEFAULT");
			        intent.setDataAndType(Uri.fromFile(resp.result), "application/vnd.android.package-archive");
			        startActivityForResult(intent, 0);  
					
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
				}
				
				
			});
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==0){
			enterHome();
		}
	}

	//检查版本是否为最新
	public String getVersion(){
		
		String versionName = null;
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}
	public int getVersionCode(){
		versionCode = -1;
		try {
			versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
	
	//进入主界面
	public void enterHome(){
		Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	//init database;
	private void copyDb(String database){
		
		File desFile = new File(getFilesDir(),database);
		if(desFile.exists()){
			return;
		}
		
		InputStream in = null;
		OutputStream out = null;
		
		try {
			out = new FileOutputStream(desFile);
			in = getAssets().open(database);
			
			byte[] buffer = new byte[1024];
			int len;
			while((len=in.read(buffer))!=-1){
				out.write(buffer,0,len);
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
