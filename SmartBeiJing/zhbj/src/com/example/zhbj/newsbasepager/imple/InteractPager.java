package com.example.zhbj.newsbasepager.imple;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.zhbj.newsbasepager.NewsBasePager;

public class InteractPager extends NewsBasePager {

	public InteractPager(Activity activity) {
		super(activity);
	}


	@Override
	public View initView() {
		TextView textView = new TextView(mActiviy);
		textView.setText("新闻详情页--互动");
		textView.setTextSize(20);
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(Color.RED);
		return textView;
	}

}
