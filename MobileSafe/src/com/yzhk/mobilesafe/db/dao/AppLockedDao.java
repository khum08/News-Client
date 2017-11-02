package com.yzhk.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.yzhk.mobilesafe.db.AppLockOpenHelper;

public class AppLockedDao {

	private static AppLockedDao dao = null;
	private AppLockOpenHelper openHelper;
	private Context context;


	private AppLockedDao(Context context){
		openHelper = new AppLockOpenHelper(context);
		this.context = context;
	}
	
	public static AppLockedDao getInstance(Context context){
		if(dao==null){
			dao = new AppLockedDao(context);
		}
		return dao;
	}
	
	public void insert(String packageName){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("packagename", packageName);
		db.insert("applocked", null, values);
		
		context.getContentResolver().notifyChange(Uri.parse("content://applock/change"), null);
		db.close();
	}
	
	public void delete(String packageName){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		
		db.delete("applocked", "packagename=?", new String[]{packageName});
		context.getContentResolver().notifyChange(Uri.parse("content://applock/change"), null);
		db.close();
	}
	
	public List<String> findAll(){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		
		Cursor cursor = db.query("applocked", new String[]{"packagename"} , null, null, null, null, null);
		List<String> list = new ArrayList<String>();
		while(cursor.moveToNext()){
			String packageName = cursor.getString(0);
			list.add(packageName);
		}
		cursor.close();
		db.close();
		return list;
	}
}

















