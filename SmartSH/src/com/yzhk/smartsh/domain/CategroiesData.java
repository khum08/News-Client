package com.yzhk.smartsh.domain;

import java.util.ArrayList;

public class CategroiesData {

	public int recode;
	public ArrayList<SubData> data;
	public ArrayList<Integer> extend;
	
	public class SubData{
		public int id;
		public String title;
		public int type;
		public String url;
		public ArrayList<SubChildren> children;
		
		@Override
		public String toString() {
			return "SubData [id=" + id + ", title=" + title + ", type=" + type
					+ ", url=" + url + ", children=" + children + "]";
		}
		
		
	}
	public class SubChildren{
		public int id;
		public String title;
		public int type;
		public String url;
		
		@Override
		public String toString() {
			return "SubChildren [id=" + id + ", title=" + title + ", type="
					+ type + ", url=" + url + "]";
		}
		
		
	}
	
	@Override
	public String toString() {
		return "CategroiesData [recode=" + recode + ", data=" + data
				+ ", extend=" + extend + "]";
	}
	
	
}
