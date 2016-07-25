package com.example.zhbj.base.imple;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.zhbj.base.BasePager;

public class GovAffairsPager extends BasePager {

	public GovAffairsPager(Activity activity) {
		super(activity);
	}

	
	@Override
	public void initData() {
		tv_title.setText("政务");
		
		TextView textView = new TextView(mActivity);
		textView.setText("政务");
		textView.setTextSize(20);
		textView.setTextColor(Color.RED);
		textView.setGravity(Gravity.CENTER);
		fl_pager.addView(textView);
		
		
	}
}
