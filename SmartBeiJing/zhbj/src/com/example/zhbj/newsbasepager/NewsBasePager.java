package com.example.zhbj.newsbasepager;

import com.example.zhbj.R;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

public abstract class NewsBasePager {

	public Activity mActiviy;
	public FrameLayout fl_news_pager;
	public View mNewsBaseView;
	
	public NewsBasePager(Activity activity){
		mActiviy = activity;
		mNewsBaseView = initView();
	}
	
	public abstract View initView();

	public void initData(){
		
	}
}

