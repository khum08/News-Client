package com.yzhk.smartsh.domain;

import java.util.ArrayList;

public class PhotosData {
	public Data data;
	public int retcode;
	
	public class Data{
		public String title;
		public String more;
		public ArrayList<News> news;
		
	}
	
	public class News{
		public String title;
		public String listimage;
	}
}
