package com.example.zhbj.bean;

import java.util.ArrayList;

public class PhotoNews {

	public PhotoNewsData data;
	
	public class PhotoNewsData{
		public ArrayList<PhotoNewsDetail> news;
	}
	public class PhotoNewsDetail{
		public int id;
		public String listimage;
		public String title;
		
	}
}
