package com.example.zhbj.base.imple;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.zhbj.base.BasePager;

public class MainPager extends BasePager {

	public MainPager(Activity activity) {
		super(activity);
	}

	
	@Override
	public void initData() {
		
		tv_title.setText("首页");
		
		TextView textView = new TextView(mActivity);
		textView.setText("首页");
		textView.setTextSize(20);
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(Color.RED);
		
		fl_pager.addView(textView);
		
		//隐藏菜单图标
		iv_menu.setVisibility(View.GONE);
	}
}
