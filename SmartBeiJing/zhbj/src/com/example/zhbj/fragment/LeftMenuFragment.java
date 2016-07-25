package com.example.zhbj.fragment;

import java.util.ArrayList;

import com.example.zhbj.R;
import com.example.zhbj.activity.HomeActivity;
import com.example.zhbj.base.imple.NewsPager;
import com.example.zhbj.bean.NewsMenu.NewsMenuData;
import com.example.zhbj.newsbasepager.NewsBasePager;
import com.example.zhbj.newsbasepager.imple.InteractPager;
import com.example.zhbj.newsbasepager.imple.NewsCenterPager;
import com.example.zhbj.newsbasepager.imple.PhotosPager;
import com.example.zhbj.newsbasepager.imple.TopicPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LeftMenuFragment extends BaseFragment {
    
	private ListView lv_left_menu;
	private ArrayList<NewsMenuData> mTitleList;
	private TextView tv_left_title;
	private ListAdapter adapter;
	private int mCurrentItem; // 当前选择的侧边栏item条目位置
	
	
	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		lv_left_menu = (ListView) view.findViewById(R.id.lv_left_menu);
		
		return view;
	}

	@Override
	public void initData() {
		
	}

	public void setMenuData(ArrayList<NewsMenuData> data) {
		mCurrentItem = 0;
		
		mTitleList = data;
		
		adapter = new ListAdapter();
		lv_left_menu.setAdapter(adapter);
		
		lv_left_menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    mCurrentItem = position;
				adapter.notifyDataSetChanged();
				
				//关闭侧边栏
				toggle();
				//打开对应的页面
				openNewsPager(position);
			}
		});
	}
	
	protected void openNewsPager(int position) {       
		HomeActivity homeUI = (HomeActivity) mActivity;
		ContentFragment contentFragment = homeUI.getContentFragment();
		//获取新闻中心页面
		NewsPager newsPager = contentFragment.getNewsPager();
		newsPager.setCurrentPager(position);
	}

	/**
	 * 关闭侧边栏
	 */
	protected void toggle() {
		
		HomeActivity homeUI = (HomeActivity) mActivity;
		SlidingMenu slidingMenu = homeUI.getSlidingMenu();
		slidingMenu.toggle();
	}

	class ListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mTitleList.size();
		}

		@Override
		public NewsMenuData getItem(int position) {
			return mTitleList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.listview_item_left, null);
			tv_left_title = (TextView) view.findViewById(R.id.tv_left_title);
			
			tv_left_title.setText(getItem(position).title);
			
			if(position == mCurrentItem){
				tv_left_title.setEnabled(true);
			}else{
				tv_left_title.setEnabled(false);
			}
			
			return view;
		}
		
	}
	
}
