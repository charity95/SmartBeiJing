package com.example.zhbj.view;

import android.content.Context;
import android.drm.DrmStore.Action;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager {

	private int startX;
	private int startY;

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TopNewsViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		//所有父控件不拦截
		getParent().requestDisallowInterceptTouchEvent(true);
		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			startX = (int)ev.getX();
			startY = (int)ev.getY();
			
			break;
		case MotionEvent.ACTION_MOVE:
			
			int endX = (int)ev.getX();
			int endY = (int)ev.getY();
			
			int disX = endX - startX;
			int disY = endY - startY;
			
			if(Math.abs(disX)<Math.abs(disY)){
				//上下拦截(斜着滑)
				getParent().requestDisallowInterceptTouchEvent(false);
			}else if(disX<0 && getCurrentItem()==getAdapter().getCount()-1){
				//向左滑，是最后一个页签就拦截（会跳到下一个页面）
				getParent().requestDisallowInterceptTouchEvent(false);
			  }else if(disX>0 && getCurrentItem()==0){
				//向右滑，是第一个页签就拦截
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			
		   break;
		
		case MotionEvent.ACTION_UP:
			
			break;
		}
		
		return super.dispatchTouchEvent(ev);
	}
}
