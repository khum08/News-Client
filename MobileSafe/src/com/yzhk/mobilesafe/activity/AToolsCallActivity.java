package com.yzhk.mobilesafe.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.yzhk.mobilesafe.R;
import com.yzhk.mobilesafe.db.dao.CommonNumbDao;
import com.yzhk.mobilesafe.db.dao.CommonNumbDao.Child;
import com.yzhk.mobilesafe.db.dao.CommonNumbDao.Group;

public class AToolsCallActivity extends Activity {

	private ArrayList<Group> groupList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools_callnum);
		
		initData();
		initUI();
	}

	private void initUI() {
		ExpandableListView elv_commomnum = (ExpandableListView)findViewById(R.id.elv_commomnum);
		MyAdapter mAdapter = new MyAdapter();
		elv_commomnum.setAdapter(mAdapter);
		
		elv_commomnum.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				String number = groupList.get(groupPosition).childList.get(childPosition).number;
				
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:"+number));
				startActivity(intent);
				
				return false;
			}
		});
	}

	private void initData() {
		CommonNumbDao dao = new CommonNumbDao();
		groupList = dao.getGroup();
	}
	
	//初始化expandablelistview 的适配器
	class MyAdapter extends BaseExpandableListAdapter{

		@Override
		public int getGroupCount() {
			return groupList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return groupList.get(groupPosition).childList.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groupList.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return groupList.get(groupPosition).childList.get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView tv =new TextView(AToolsCallActivity.this);
			Group group = (Group) getGroup(groupPosition);
			tv.setText(group.name);
			tv.setTextColor(Color.RED);
			tv.setGravity(Gravity.CENTER);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
			tv.setPadding(5, 20, 5, 20);
			tv.setBackgroundColor(0x88888888);
			return tv;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = View.inflate(AToolsCallActivity.this, R.layout.view_listview_commonnum, null);
			}
			Child child = (Child) getChild(groupPosition, childPosition);
			String name = child.name;
			String number = child.number;
			TextView tv_commonnum_name = (TextView) convertView.findViewById(R.id.tv_commonnum_name);
			TextView tv_commonnum_numb = (TextView) convertView.findViewById(R.id.tv_commonnum_numb);
			tv_commonnum_name.setText(name);
			tv_commonnum_numb.setText(number);
			
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
	}
	
}








