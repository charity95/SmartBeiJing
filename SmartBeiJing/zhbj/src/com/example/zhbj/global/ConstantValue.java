package com.example.zhbj.global;

public class ConstantValue {

	//线上服务器	
	public static final String SERVER_URL = "http://zhihuibj.sinaapp.com/zhbj/";
	
	//public static final String SERVER_URL = "http://10.0.2.2:8080/zhbj/";
	/**
	 * 获取新闻数据的url
	 */
	public static final String NEWS_URL = SERVER_URL + "categories.json";
	/**
	 * 获取组图数据的url
	 */
	public static final String PHOTO_URL = SERVER_URL + "photos/photos_1.json";
}
