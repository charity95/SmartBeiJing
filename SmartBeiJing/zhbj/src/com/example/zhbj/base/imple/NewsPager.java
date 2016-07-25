package com.example.zhbj.base.imple;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.zhbj.activity.HomeActivity;
import com.example.zhbj.base.BasePager;
import com.example.zhbj.bean.NewsMenu;
import com.example.zhbj.fragment.LeftMenuFragment;
import com.example.zhbj.global.ConstantValue;
import com.example.zhbj.newsbasepager.NewsBasePager;
import com.example.zhbj.newsbasepager.imple.InteractPager;
import com.example.zhbj.newsbasepager.imple.NewsCenterPager;
import com.example.zhbj.newsbasepager.imple.PhotosPager;
import com.example.zhbj.newsbasepager.imple.TopicPager;
import com.example.zhbj.utils.PrefsUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NewsPager extends BasePager {

	protected static final String tag = "NewsPager";
	private ArrayList<NewsBasePager> mNewsPagerList;
	private NewsMenu newsMenu;
	
	public NewsPager(Activity activity) {
		super(activity);
	}

	
	@Override
	public void initData() { 
		
		String newsCacheData = PrefsUtil.getString(mActivity, "news_cache", "");
		if(!TextUtils.isEmpty(newsCacheData)){
			processData(newsCacheData);
		}
		
		//请求网络数据(加权限)
		getDataFromServer();

	}


	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		
		httpUtils.send(HttpMethod.GET, ConstantValue.NEWS_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				
				//网络缓存
				PrefsUtil.putString(mActivity, "news_cache", result);
				Log.i(tag, "新闻数据："+result);
				
				//解析返回结果
				processData(result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
			     error.printStackTrace();
				Toast.makeText(mActivity, msg, 0).show();
			}
		});
		
	}


	/**解析返回结果
	 * @param result 返回的json数据
	 */
	protected void processData(String result) {
		Gson gson = new Gson();
		newsMenu = gson.fromJson(result, NewsMenu.class);

		//获取侧边栏对象
		
		LeftMenuFragment leftMenuFragment = ((HomeActivity) mActivity).getLeftMenuFragment();
		leftMenuFragment.setMenuData(newsMenu.data);
			
		mNewsPagerList = new ArrayList<NewsBasePager>();
		mNewsPagerList.add(new NewsCenterPager(mActivity,newsMenu.data.get(0).children));
		mNewsPagerList.add(new TopicPager(mActivity));
		mNewsPagerList.add(new PhotosPager(mActivity,iv_photo));
		mNewsPagerList.add(new InteractPager(mActivity));
		
		//初始手动设置新闻中心页面就是新闻详情页
	    setCurrentPager(0);
	}


	/**侧边栏切换新闻中心的页面
	 * @param position
	 */
	public void setCurrentPager(int position) {
		
		NewsBasePager newsBasePager = mNewsPagerList.get(position);
		
		fl_pager.removeAllViews();
		fl_pager.addView(newsBasePager.mNewsBaseView);
		
		//如果是组图界面,就将图标显示
		if(newsBasePager instanceof  PhotosPager ){
			iv_photo.setVisibility(View.VISIBLE);
		}else{
			iv_photo.setVisibility(View.INVISIBLE);

		}
	
		
		newsBasePager.initData();
		
		//设置标题
		tv_title.setText(newsMenu.data.get(position).title);
	}
}
