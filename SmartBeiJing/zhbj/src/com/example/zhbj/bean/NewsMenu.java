package com.example.zhbj.bean;

import java.util.ArrayList;

/**
 * 逢{}建对象 ，逢[]建集合
 * 所有字段名称和json返回字段一致
 * @author Administrator
 *
 */
public class NewsMenu {
     
	public String retcode;
	public ArrayList<String> extend;
	public ArrayList<NewsMenuData> data;
	



   public class NewsMenuData{
	 public String id;
	 public String title;
	 public String type;
	 public ArrayList<NewsChildrenData> children;
    }
 
     public class NewsChildrenData{
	 public String id;
	 public String title;
	 public String type;
	 public String url;
    }
 
}
