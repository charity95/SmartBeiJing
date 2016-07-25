package com.example.zhbj.bean;

import java.util.ArrayList;

public class TopNews {
     public NewsData data;
	
     
     public class NewsData{
    	 public String more;
    	 public ArrayList<NewsDetail> news;
    	 public ArrayList<TopNewsDetail> topnews;
    	 
     }
     
     public class NewsDetail{
    	 public int id;
    	 public String listimage;
    	 public String pubdate;
    	 public String title;
    	 public String url;
     }
     public class TopNewsDetail{
    	 public String topimage;
    	 public String pubdate;
    	 public String title;
    	 public String url;
     }
}
