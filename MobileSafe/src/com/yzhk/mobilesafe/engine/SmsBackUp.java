package com.yzhk.mobilesafe.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;
import android.widget.Toast;

public class SmsBackUp {

	private static XmlSerializer newSer;
	private static FileOutputStream fos;
	private static int value = 0;

	/**
	 * 备份短信
	 * @param context
	 */
	public static void backUp(Context context,String path,CallBack cb) {

		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(Uri.parse("content://sms/"), new String[] { "address",
				"date", "type", "body" }, null, null, null);
		
		try {
			
			if(cursor!=null){
				
				cb.setMax(cursor.getCount());
				
				//xml序列化
				fos = new FileOutputStream(new File(path));
				newSer = Xml.newSerializer();
				newSer.setOutput(fos, "utf-8");
				newSer.startDocument("utf-8", true);
				newSer.startTag(null, "smss");
				
				while(cursor.moveToNext()){
					newSer.startTag(null, "sms");
					
					String address = cursor.getString(0);
					String date = cursor.getString(1);
					String type = cursor.getString(2);
					String body = cursor.getString(3);
					
					newSer.startTag(null, "address");
					newSer.text(address);
					newSer.endTag(null, "address");
					
					newSer.startTag(null, "date");
					newSer.text(date);
					newSer.endTag(null, "date");

					newSer.startTag(null, "type");
					newSer.text(type);
					newSer.endTag(null, "type");

					newSer.startTag(null, "body");
					newSer.text(body);
					newSer.endTag(null, "body");
					
					newSer.endTag(null, "sms");
					
					value ++;
					cb.setProgress(value);
					Thread.sleep(200);
				}
			}
			
			newSer.endTag(null, "smss");
			newSer.endDocument();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "备份失败", Toast.LENGTH_SHORT).show();
		} finally{
			if(cursor!=null && fos!=null){
				cursor.close();
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	public interface CallBack{
		public void setMax(int max);
		public void setProgress(int position);
	}
	
}
