package com.yzhk.smartsh.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.yzhk.smartsh.R;
import com.yzhk.smartsh.global.GlobalConstant;

public class NewsContent extends Activity {

	private ImageButton ib_newscontent_back;
	private ImageButton ib_newscontent_textsize;
	private ImageButton ib_newscontent_share;
	private TextView tv_newscontent_title;
	private ProgressBar pb_newscontent;
	private WebSettings settings;
	private FrameLayout fl_newscontent;
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newscontent);

		initUi();
		initWebView();
		initActionBar();
	}

	private void initActionBar() {
		ib_newscontent_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		ib_newscontent_textsize.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showTextSizeChoose();
			}
		});
		
		ib_newscontent_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showShare();
				
			}
		});
		
	}

	private int mTempWhich;
	private int mComfirmWhich = 2;
	protected void showTextSizeChoose() {
		AlertDialog.Builder builder = new AlertDialog.Builder(NewsContent.this);
		builder.setTitle("选择字号");
		String[] items = new String[]{"超小号字体","小号字体","正常字体","大号字体","超大号字体"};
		builder.setSingleChoiceItems(items, mComfirmWhich, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mTempWhich = which;
			}
		});
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mComfirmWhich = mTempWhich;
				switch (mComfirmWhich) {
				case 0:
					settings.setTextSize(TextSize.SMALLEST);
					break;
				case 1:
					settings.setTextSize(TextSize.SMALLER);
					break;
				case 2:
					settings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					settings.setTextSize(TextSize.LARGER);
					break;
				case 4:
					settings.setTextSize(TextSize.LARGEST);
					break;

				default:
					break;
				}
				dialog.dismiss();
			}
		});
		builder.show();
	}

	private void initUi() {
		ib_newscontent_back = (ImageButton) findViewById(R.id.ib_newscontent_back);
		ib_newscontent_textsize = (ImageButton) findViewById(R.id.ib_newscontent_textsize);
		ib_newscontent_share = (ImageButton) findViewById(R.id.ib_newscontent_share);
		tv_newscontent_title = (TextView) findViewById(R.id.tv_newscontent_title);
		pb_newscontent = (ProgressBar) findViewById(R.id.pb_newscontent);
		fl_newscontent = (FrameLayout) findViewById(R.id.fl_newscontent);
	}

	private void initWebView() {
		mWebView = new WebView(this);
		String mUrl = GlobalConstant.SERVER+getIntent().getStringExtra("url");
		
		mWebView.loadUrl(mUrl);
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				pb_newscontent.setVisibility(View.VISIBLE);
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				pb_newscontent.setVisibility(View.GONE);
			}
		});
		settings = mWebView.getSettings();
		
		
		fl_newscontent.addView(mWebView);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		settings.setJavaScriptEnabled(true);
	}
	@Override
	protected void onStop() {
		super.onStop();
		settings.setJavaScriptEnabled(false);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mWebView!=null){
			mWebView.clearHistory();
			mWebView.destroy();
			mWebView = null;
		}
	}
	
	private void showShare() {
	     OnekeyShare oks = new OnekeyShare();
	     //关闭sso授权
	     oks.disableSSOWhenAuthorize(); 

	    // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
	     //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
	     // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
	     oks.setTitle("分享是一种美德");
	     // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
	     oks.setTitleUrl("http://sharesdk.cn");
	     // text是分享文本，所有平台都需要这个字段
	     oks.setText("我是分享文本");
	     // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//	     oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
	     // url仅在微信（包括好友和朋友圈）中使用
	     oks.setUrl("http://sharesdk.cn");
	     // comment是我对这条分享的评论，仅在人人网和QQ空间使用
	     oks.setComment("我是测试评论文本");
	     // site是分享此内容的网站名称，仅在QQ空间使用
	     oks.setSite(getString(R.string.app_name));
	     // siteUrl是分享此内容的网站地址，仅在QQ空间使用
	     oks.setSiteUrl("http://sharesdk.cn");

	    // 启动分享GUI
	    oks.show(this);
	}
}









