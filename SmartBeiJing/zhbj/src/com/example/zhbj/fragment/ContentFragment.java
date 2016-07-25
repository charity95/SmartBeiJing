package com.example.zhbj.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.zhbj.R;
import com.example.zhbj.activity.HomeActivity;
import com.example.zhbj.base.BasePager;
import com.example.zhbj.base.imple.GovAffairsPager;
import com.example.zhbj.base.imple.MainPager;
import com.example.zhbj.base.imple.NewsPager;
import com.example.zhbj.base.imple.SettingPager;
import com.example.zhbj.base.imple.SmartServicePager;
import com.example.zhbj.view.NoScollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ContentFragment extends BaseFragment {
    
	//自定义viewpager ，不对滑动做出处理
	public NoScollViewPager viewPager;
	public List<BasePager> pagerList = new ArrayList<BasePager>();
	private RadioGroup rg_group;
	
	@Override
	public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
		
        viewPager = (NoScollViewPager) view.findViewById(R.id.viewPager);
        rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
       
       
		return view;
	}

	

	@Override
	public void initData() {
         
		pagerList.add(new MainPager(mActivity));
		pagerList.add(new NewsPager(mActivity));
		pagerList.add(new SmartServicePager(mActivity));
		pagerList.add(new GovAffairsPager(mActivity));
		pagerList.add(new SettingPager(mActivity));
		
		viewPager.setAdapter(new MyAdapter());
		
		rg_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					switch (checkedId) {
					case R.id.rb_main:
						viewPager.setCurrentItem(0,false);
						break;
					case R.id.rb_news:
						viewPager.setCurrentItem(1,false);
						break;
					case R.id.rb_startservice:
						viewPager.setCurrentItem(2,false);
						break;
					case R.id.rb_govaffairs:
						viewPager.setCurrentItem(3,false);
						break;
					case R.id.rb_setting:
						viewPager.setCurrentItem(4,false);
						break;
					}
					
				}
			});
	        
	        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int position) {
					//选中页面时才加载数据
					pagerList.get(position).initData();
					
					if(position ==0 || position == 4){
						//禁用侧边栏
						setSlidingMenu(false);
					}else{
						setSlidingMenu(true);
					}
				}
				
				@Override
				public void onPageScrolled(int position, float positionOffset,
						int positionOffsetPixels) {
					
				}
				
				@Override
				public void onPageScrollStateChanged(int state) {
					
				}
			});
	        	
	        //初始化显示首页
	        pagerList.get(0).initData();
	        //手动禁用首页侧边栏
	        setSlidingMenu(false);
	}
	
	protected void setSlidingMenu(boolean enable) {
		HomeActivity homeUI = (HomeActivity)mActivity;
		SlidingMenu slidingMenu = homeUI.getSlidingMenu();
		if(enable){
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}else{
			//禁用侧边栏
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}

	class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return pagerList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			
			//pagerList.get(position).initData();  
			View view = pagerList.get(position).mBaseView;
			
			container.addView(view);
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
			
		}
	}
	
	/**获取新闻中心页面
	 * @return
	 */
	public NewsPager getNewsPager(){
		NewsPager pager = (NewsPager) pagerList.get(1);
		return pager;
	}

}
