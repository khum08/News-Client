package com.yzhk.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author 大傻春
 *
 */
public class Addressdao {

	private static SQLiteDatabase db;

	public static String searchAddress(String phoneNum) {
		String address = "未知号码";
		
		String PATH = "data/data/com.yzhk.mobilesafe/files/address.db";
		
		db = SQLiteDatabase.openDatabase(PATH, null,
				SQLiteDatabase.OPEN_READONLY);
		
		if(phoneNum.matches("^1[3-8][0-9]{9}$")){
			phoneNum = phoneNum.substring(0,7);

			Cursor outkeyCursor = db.query("data1", new String[] { "outkey" },
					"id=?", new String[] { phoneNum }, null, null, null);
			if (outkeyCursor != null) {
				if (outkeyCursor.moveToNext()) {
					String outkey = outkeyCursor.getString(0);
					System.out.println(outkey);
					// use outkey to search area from table data2

					// select outkey from data1 where id =1302673;
					// select location from data2 where id=345
					Cursor cursor = db.query("data2", new String[] { "location" }, "id=?",
							new String[] { outkey }, null, null, null);
					if(cursor.moveToNext()){
						address = cursor.getString(0);
						System.out.println(address);
					}
					cursor.close();
				}
			}
			outkeyCursor.close();
		}
		else if(phoneNum.matches("^[0-9]+$")){
			switch (phoneNum.length()) {
			case 3:
				address = "报警电话";
				break;
			case 4:
				address = "模拟器";
				break;
			case 5:
				address = "客服电话";
				break;
			case 7:
			case 8:
				address = "固定电话";
				break;
			default:
				
				break;
			}
		}
		db.close();
		

		return address;
	}

}
