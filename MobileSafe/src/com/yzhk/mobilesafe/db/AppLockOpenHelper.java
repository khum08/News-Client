package com.yzhk.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AppLockOpenHelper extends SQLiteOpenHelper {

	public AppLockOpenHelper(Context context) {
		super(context, "applock.db", null, 1);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		CREATE TABLE blacknumber (_id integer primary key autoincrement,phone varchar(20),mode varchar(20))
		db.execSQL("create table applocked (_id integer primary key autoincrement,packagename varchar(50))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
