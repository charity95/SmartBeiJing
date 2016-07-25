package com.example.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScollViewPager extends ViewPager {

	public NoScollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}



	public NoScollViewPager(Context context) {
		super(context);
	}
	
	
	//自定义viewpager ，不对滑动做出处理
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return true;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		//不拦截子事件
		return false;
	}
}
