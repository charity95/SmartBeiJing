package com.example.zhbj.base;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhbj.R;
import com.example.zhbj.activity.HomeActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class BasePager {
     
	public Activity mActivity;
	public View mBaseView;
	public ImageView iv_menu;
	public TextView tv_title;
	public FrameLayout fl_pager;
	public ImageView iv_photo;
	
	public BasePager(Activity activity){
		mActivity = activity;
		initView();
	}
	
	public View initView(){
		mBaseView = View.inflate(mActivity, R.layout.base_viewpager, null);
		
		iv_menu = (ImageView) mBaseView.findViewById(R.id.iv_menu);
		tv_title = (TextView) mBaseView.findViewById(R.id.tv_title);
		fl_pager = (FrameLayout) mBaseView.findViewById(R.id.fl_pager);
		iv_photo = (ImageView) mBaseView.findViewById(R.id.iv_photo);
		
		
		iv_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                toggle();		
			}
		});
		
		return mBaseView;
	}
	public void initData(){
		
	}
	
	/**
	 * 关闭侧边栏
	 */
	protected void toggle() {
		
		HomeActivity homeUI = (HomeActivity) mActivity;
		SlidingMenu slidingMenu = homeUI.getSlidingMenu();
		slidingMenu.toggle();
	}

}
