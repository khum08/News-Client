package com.yzhk.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yzhk.mobilesafe.db.BlackNumbOpenHelper;
import com.yzhk.mobilesafe.db.domain.BlackNumber;

public class BlackNumbDao {

	private static BlackNumbDao BlackNumbDao = null;
	private BlackNumbOpenHelper openHelper;

	private BlackNumbDao(Context context) {
		openHelper = new BlackNumbOpenHelper(context);
	}

	public static BlackNumbDao getInstanse(Context context) {
		if (BlackNumbDao == null) {
			BlackNumbDao = new BlackNumbDao(context);
		}
		return BlackNumbDao;
	}

	public void insert(String phone, String mode) {
		SQLiteDatabase db = openHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("mode", mode);
		db.insert("blacknumber", null, values);

		db.close();
	}

	public void delete(String phone) {
		SQLiteDatabase db = openHelper.getWritableDatabase();

		db.delete("blacknumber", "phone=?", new String[] { phone });
		db.close();

	}

	public void update(String phone, String mode) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("mode", mode);

		db.update("blacknumber", values, "phone=?", new String[] { phone });
		db.close();
	}

	public List<BlackNumber> queryAll() {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		List<BlackNumber> list = new ArrayList<BlackNumber>();
		Cursor cursor = db.query("blacknumber",
				new String[] { "phone", "mode" }, null, null, null, null,
				"_id desc");
		while(cursor.moveToNext()){
			String phone = cursor.getString(0);
			String mode = cursor.getString(1);
			BlackNumber bn = new BlackNumber(phone,mode);
			
			list.add(bn);
		}
		
		cursor.close();
		db.close();
		
		return list;
	}
	
	/**
	 * 查询20条
	 * @param startIndex 查询起始位置
	 * @return
	 */
	public List<BlackNumber> findTwenty(int startIndex){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		List<BlackNumber> list = new ArrayList<BlackNumber>();
		Cursor cursor = db.rawQuery("select phone,mode from blacknumber order by _id desc limit ?,20", new String[]{startIndex+""});
		while(cursor.moveToNext()){
			String phone = cursor.getString(0);
			String mode =  cursor.getString(1);
			BlackNumber bn = new BlackNumber(phone, mode);
			list.add(bn);
		}
		cursor.close();
		db.close();
		return list;
	}

	/**
	 * 获取表的数据数量
	 * @return
	 */
	public int getCount(){
		int count = 0;
		SQLiteDatabase db = openHelper.getWritableDatabase();
		
		Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
		if(cursor.moveToNext()){
			count = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return count;
	}
	
	public int getMode(String phone){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		int mode = 0;
		Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "phone=?", new String[]{phone}, null, null, null);
		if(cursor!=null){
			if(cursor.moveToNext()){
				mode = Integer.parseInt(cursor.getString(0));
			}
		}
		
		return mode;
	}
}









