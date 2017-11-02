package com.yzhk.smartsh.domain;

import java.util.ArrayList;

public class NewsTabData {
	
	public Data data;
	public int retcode;
	
	public class Data{
		public String more;
		public String title;
		public ArrayList<News> news;
		public ArrayList<TopNews> topnews;
		@Override
		public String toString() {
			return "Data [more=" + more + ", news=" + news + ", title=" + title
					+ ", topnews=" + topnews + "]";
		}
	};
	
	public class News{
		public String listimage;
		public String pubdate;
		public String title;
		public String url;
		public String type;
		public int id;
		@Override
		public String toString() {
			return "News [listimage=" + listimage + ", pubdate=" + pubdate
					+ ", title=" + title + ", url=" + url + ", type=" + type
					+ ", id=" + id + "]";
		}
		
	}
	
	public class TopNews{
		public String topimage;
		public String title;
		public String type;
		public int id;
		@Override
		public String toString() {
			return "TopNews [topimage=" + topimage + ", title=" + title
					+ ", type=" + type + ", id=" + id + "]";
		}
	}

	@Override
	public String toString() {
		return "NewsTabData [data=" + data + ", retcode=" + retcode + "]";
	}
	
	
}
