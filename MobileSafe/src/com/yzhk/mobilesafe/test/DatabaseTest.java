package com.yzhk.mobilesafe.test;

import java.util.Random;

import com.yzhk.mobilesafe.activity.AppStopActivity;
import com.yzhk.mobilesafe.db.dao.BlackNumbDao;

import android.content.Intent;
import android.sax.StartElementListener;
import android.test.AndroidTestCase;

public class DatabaseTest extends AndroidTestCase {

	public void insert(){
		BlackNumbDao dao = BlackNumbDao.getInstanse(getContext());
		dao.insert("13412345678", "1");
		for (int i = 0; i < 100	; i++) {
			if(i<10){
				dao.insert("1517921211"+i, new Random().nextInt(3)+1+"");
			}else{
				dao.insert("151792121"+i, new Random().nextInt(3)+1+"");
			}
		}
		
	}
	
	public void enterlock(){
		Intent intent = new Intent();
		intent.setClass(getContext(), AppStopActivity.class);
		intent.putExtra("pkgName", getContext().getPackageName());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getContext().startActivity(intent);
	}
	
}
