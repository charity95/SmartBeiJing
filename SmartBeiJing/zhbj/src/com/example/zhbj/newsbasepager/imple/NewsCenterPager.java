package com.example.zhbj.newsbasepager.imple;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhbj.R;
import com.example.zhbj.activity.HomeActivity;
import com.example.zhbj.bean.NewsMenu.NewsChildrenData;
import com.example.zhbj.newsbasepager.NewsBasePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

public class NewsCenterPager extends NewsBasePager implements OnPageChangeListener{

	
	private ArrayList<TabPager> mTabPagers;

	@ViewInject(R.id.vp_tab)
	private ViewPager mViewPager;
	@ViewInject(R.id.indicator)
	private  TabPageIndicator indicator;
	
	private ArrayList<NewsChildrenData> mDataList;

	public NewsCenterPager(Activity activity, ArrayList<NewsChildrenData> data) {
		super(activity);
		mDataList = data;
	}

	@Override
	public View initView() {
		View view  = View.inflate(mActiviy, R.layout.tab_pager, null);

		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		mTabPagers = new ArrayList<TabPager>();
		for (int i = 0; i < mDataList.size(); i++) {
			TabPager pager = new TabPager(mActiviy,mDataList.get(i));
			mTabPagers.add(pager);
		}
		
        mViewPager.setAdapter(new TabPagerAdapter());
        indicator.setViewPager(mViewPager);
        
        //只能给indicator设置监控事件
        indicator.setOnPageChangeListener(this);
  
	}

	class TabPagerAdapter extends PagerAdapter{

		@Override
		public CharSequence getPageTitle(int position) {
			
			return mDataList.get(position).title;
		}
		
		@Override
		public int getCount() {
			return mTabPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabPager pager = mTabPagers.get(position);
			View view = pager.mNewsBaseView;
			pager.initData();
			container.addView(view);
			
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
              container.removeView((View) object);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int position) {
		if(position == 0){
			//开启侧边栏
			setSlidingMenu(true);
		}else{
			//禁用侧边栏
			setSlidingMenu(false);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}
	
	protected void setSlidingMenu(boolean enable) {
		HomeActivity homeUI = (HomeActivity) mActiviy;
		SlidingMenu slidingMenu = homeUI.getSlidingMenu();
		if(enable){
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}else{
			//禁用侧边栏
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
	
	@OnClick(R.id.ib_next)
	public void nextPage(View view){
		//跳到下一个页签
		mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
	}
}
