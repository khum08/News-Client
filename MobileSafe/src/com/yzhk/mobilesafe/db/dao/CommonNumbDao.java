package com.yzhk.mobilesafe.db.dao;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonNumbDao {
	

	/**
	 * 获取所有group数据
	 * @return
	 */
	public ArrayList<Group> getGroup(){
		String PATH = "data/data/com.yzhk.mobilesafe/files/commonnum.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.query("classlist", null, null, null, null, null, null);
		ArrayList<Group> list = new ArrayList<Group>();
		while(cursor.moveToNext()){
			Group group = new Group();
			group.name = cursor.getString(0);
			group.idx = cursor.getString(1);
			ArrayList<Child> child = getChild(cursor.getString(1));
			group.childList = child;
			list.add(group);
		}
		return list;
	}
	
	/**
	 * 获取group数据中的字节点，此方法有getGroup自行调用
	 * @param idx
	 */
	public ArrayList<Child> getChild(String idx){
		String PATH = "data/data/com.yzhk.mobilesafe/files/commonnum.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.query("table"+idx, null, null, null, null, null, null);
		ArrayList<Child> childList = new ArrayList<Child>();
		while(cursor.moveToNext()){
			Child child = new Child();
			child._id = cursor.getString(0);
			child.number = cursor.getString(1);
			child.name = cursor.getString(2);
			childList.add(child);
		}
		
		return childList;
	}
	
	
	public class Group{
		public String name;
		public String idx;
		public ArrayList<Child> childList;
	}
	public class Child{
		public String _id;
		public String number;
		public String name;
	}
}
