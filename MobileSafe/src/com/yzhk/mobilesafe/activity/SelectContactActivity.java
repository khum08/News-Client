package com.yzhk.mobilesafe.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yzhk.mobilesafe.R;

public class SelectContactActivity extends Activity {

	private ListView lv_contacts;
	private List<Map<String,String>> list;

	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			pb_contacts.setVisibility(View.GONE);
			lv_contacts.setAdapter(new MBaseAdapter());
		};
	};
	private ProgressBar pb_contacts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact);
		
		getContacts();
		lv_contacts = (ListView) findViewById(R.id.lv_contacts);
		pb_contacts = (ProgressBar) findViewById(R.id.pb_contacts);
		
		lv_contacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(SelectContactActivity.this,Guide3Activity.class);
				intent.putExtra("phone", list.get(position).get("phone"));
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});

	}

	class MBaseAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			System.out.println(list.size());
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if(convertView==null){
				view = View.inflate(SelectContactActivity.this,
						R.layout.view_item_contacts, null);
			}
			else{
				view = convertView;
			}
			TextView contactName = (TextView) view.findViewById(R.id.tv_contactName);
			TextView contactPhone = (TextView) view.findViewById(R.id.tv_contactPhone);
			
			contactName.setText(list.get(position).get("name"));
			contactPhone.setText(list.get(position).get("phone"));
			
			return view;
		}

	}
	

	public void getContacts() {

		new Thread(){
			public void run() {
				Uri rawContactUri = Uri
						.parse("content://com.android.contacts/raw_contacts");
				Uri dataUri = Uri.parse("content://com.android.contacts/data");
				ContentResolver resolver = getContentResolver();

				Cursor contactIdCursor = resolver.query(rawContactUri,
						new String[] { "contact_id"}, null, null, null);
				if (contactIdCursor != null) {
					list = new ArrayList<Map<String,String>>();
					while (contactIdCursor.moveToNext()) {
						String contactId = contactIdCursor.getString(0);
						
						Cursor dataCursor = resolver.query(dataUri, new String[] { "mimetype", "data1" },
								"contact_id=?", new String[] { contactId }, null);
						if(dataCursor != null){
							
							Map<String,String> map = new HashMap<String,String>();
							while(dataCursor.moveToNext()){
								String mimetype = dataCursor.getString(0);
								String data1 = dataCursor.getString(1);
								if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
									map.put("phone", data1);
								}else if("vnd.android.cursor.item/name".equals(mimetype)){
									map.put("name", data1);
								}
							}
							list.add(map);
							dataCursor.close();
						}
						
					}
					contactIdCursor.close();
				}
				
				mHandler.sendEmptyMessage(0);
			};
		}.start();
		
	}

}
