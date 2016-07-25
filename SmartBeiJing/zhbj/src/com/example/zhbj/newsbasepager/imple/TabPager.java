package com.example.zhbj.newsbasepager.imple;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhbj.R;
import com.example.zhbj.activity.NewsDetailActivity;
import com.example.zhbj.base.imple.NewsPager;
import com.example.zhbj.bean.NewsMenu.NewsChildrenData;
import com.example.zhbj.bean.TopNews;
import com.example.zhbj.bean.TopNews.NewsDetail;
import com.example.zhbj.bean.TopNews.TopNewsDetail;
import com.example.zhbj.global.ConstantValue;
import com.example.zhbj.newsbasepager.NewsBasePager;
import com.example.zhbj.utils.PrefsUtil;
import com.example.zhbj.view.PullToRefreshListView;
import com.example.zhbj.view.PullToRefreshListView.onRefreshListener;
import com.example.zhbj.view.TopNewsViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

public class TabPager extends NewsBasePager {

	private NewsChildrenData mChildrenData;

	@ViewInject(R.id.vp_top_news)
	private TopNewsViewPager vp_top_news;
	@ViewInject(R.id.indicator)
	private CirclePageIndicator indicator;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.lv_news)
	private PullToRefreshListView lv_news;

	private TopNews mTopNews;
	private ArrayList<TopNewsDetail> mTopNewsDetail;
	private ArrayList<NewsDetail> mNewsDetail;

	private NewsListAdapter mListAdapter;

	private String mMoreUrl;
	private Handler mHandler;

	public TabPager(Activity activity, NewsChildrenData newsChildrenData) {
		super(activity);
		mChildrenData = newsChildrenData;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActiviy, R.layout.top_news_pager, null);
		ViewUtils.inject(this, view);

		View headView = View.inflate(mActiviy, R.layout.listview_header, null);
		ViewUtils.inject(this, headView);

		lv_news.addHeaderView(headView);

		// 下拉刷新，接口回调
		lv_news.setOnRefreshListener(new onRefreshListener() {

			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				if (mMoreUrl != null) {
					getMoreDataFromServer();
				} else {
					Toast.makeText(mActiviy, "没有更多数据了", 0).show();
					lv_news.onRefreshComplete(true);
				}

			}
		});
		lv_news.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 将条目标题变成灰色，并把 id 存储到sp中
				int headViewCount = lv_news.getHeaderViewsCount();
				int currentPostion = position - headViewCount;

				NewsDetail newsDetail = mNewsDetail.get(currentPostion);

				String readIds = PrefsUtil.getString(mActiviy, "read_ids", "");

				if (!readIds.contains(newsDetail.id + "")) {
					readIds = readIds + newsDetail.id + ",";
					PrefsUtil.putString(mActiviy, "read_ids", readIds);

				}

				TextView textView = (TextView) view.findViewById(R.id.tv_title);
				textView.setTextColor(Color.GRAY);

				// 跳转到新闻详情页
				Intent intent = new Intent(mActiviy, NewsDetailActivity.class);
				intent.putExtra("url", newsDetail.url);
				mActiviy.startActivity(intent);
			}
		});

		return view;
	}

	@Override
	public void initData() {
		String topNewsCacheData = PrefsUtil.getString(mActiviy,
				"top_news_cache", "");
		if (!TextUtils.isEmpty(topNewsCacheData)) {
			processData(topNewsCacheData, false);
		}

		getDataFromServer();
		
		vp_top_news.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//停止循播，取消handler
					mHandler.removeCallbacksAndMessages(null);
					break;
				case MotionEvent.ACTION_CANCEL:
					mHandler.sendEmptyMessageDelayed(0, 5000);
					break;
				case MotionEvent.ACTION_UP:
					//继续循播
					mHandler.sendEmptyMessageDelayed(0, 5000);
					break;

				}
				
				return false;
			}
		});

	}

	protected void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();

		// HttpUtils的send方法底层是在子线程中访问网络数据，onSuccess/onFailure方法是在主线程中执行
		utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;

				processData(result, true);
				lv_news.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActiviy, msg, 0).show();
				lv_news.onRefreshComplete(false);
			}
		});

	}

	// 获取网络数据
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();

		String url = ConstantValue.SERVER_URL + mChildrenData.url;

		// HttpUtils的send方法底层是在子线程中访问网络数据，onSuccess/onFailure方法是在主线程中执行
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				// 网络缓存
				PrefsUtil.putString(mActiviy, "top_news_cache", result);

				processData(result, false);

				lv_news.onRefreshComplete(true);

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActiviy, msg, 0).show();

				lv_news.onRefreshComplete(false);

			}
		});
	}

	// 解析数据
	protected void processData(String result, boolean isMoreLoad) {
		Gson gson = new Gson();
		mTopNews = gson.fromJson(result, TopNews.class);

		String more = mTopNews.data.more;

		if (!TextUtils.isEmpty(more)) {
			mMoreUrl = ConstantValue.SERVER_URL + more;
		} else {
			mMoreUrl = null;
		}

		if (!isMoreLoad) {
			mTopNewsDetail = mTopNews.data.topnews;
			mNewsDetail = mTopNews.data.news;
			if (mTopNewsDetail != null) {

				vp_top_news.setAdapter(new TopNewsAdapter());
				indicator.setViewPager(vp_top_news);
				indicator.setSnap(true);

				indicator.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						tv_title.setText(mTopNewsDetail.get(position).title);
					}

					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {

					}

					@Override
					public void onPageScrollStateChanged(int state) {

					}
				});

				tv_title.setText(mTopNewsDetail.get(0).title);
				// 默认让第一个选中(解决页面销毁后重新初始化时,Indicator仍然保留上次圆点位置的bug)
				indicator.onPageSelected(0);
			}
			if (mNewsDetail != null) {
				mListAdapter = new NewsListAdapter();
				lv_news.setAdapter(mListAdapter);
			}
			
			//viewpager 无限循播
			if(mHandler == null){
				mHandler = new Handler(){
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
                        int currentItem = vp_top_news.getCurrentItem();
						
                        if(currentItem == mTopNewsDetail.size()-1){
                        	currentItem =-1;
                        }
                        vp_top_news.setCurrentItem(++currentItem);
                        
						//循环更新
						sendEmptyMessageDelayed(0, 5000);
					}
				};
				//保证只启动一次
				mHandler.sendEmptyMessageDelayed(0, 5000);
			}
			
		} else {
			mNewsDetail.addAll(mTopNews.data.news);
			mListAdapter.notifyDataSetChanged();
		}
	}

	class TopNewsAdapter extends PagerAdapter {

		private BitmapUtils bitmapUtils;

		public TopNewsAdapter() {
			bitmapUtils = new BitmapUtils(mActiviy);
			bitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}

		@Override
		public int getCount() {
			return mTopNewsDetail.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(mActiviy);
			imageView.setScaleType(ScaleType.FIT_XY);

			// 网络加载图片，设置图片，缓存图片，防止内存溢出
			bitmapUtils.display(imageView,
					mTopNewsDetail.get(position).topimage);

			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	class NewsListAdapter extends BaseAdapter {

		private BitmapUtils bitmapUtils;

		public NewsListAdapter() {
			bitmapUtils = new BitmapUtils(mActiviy);
			bitmapUtils
					.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}

		@Override
		public int getCount() {
			return mNewsDetail.size();
		}

		@Override
		public NewsDetail getItem(int position) {
			return mNewsDetail.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHold hold;
			if (convertView == null) {
				convertView = View.inflate(mActiviy,
						R.layout.listview_news_item, null);
				hold = new ViewHold();
				hold.iv_icon = (ImageView) convertView
						.findViewById(R.id.iv_icon);
				hold.tv_title = (TextView) convertView
						.findViewById(R.id.tv_title);
				hold.tv_date = (TextView) convertView
						.findViewById(R.id.tv_date);
				convertView.setTag(hold);
			} else {
				hold = (ViewHold) convertView.getTag();
			}

			NewsDetail newsDetail = getItem(position);
			hold.tv_title.setText(newsDetail.title);
			hold.tv_date.setText(newsDetail.pubdate);

			bitmapUtils.display(hold.iv_icon, newsDetail.listimage);

			String readIds = PrefsUtil.getString(mActiviy, "read_ids", "");
			// 将已读的标题设置成灰色
			if (readIds != null) {
				if (readIds.contains(getItem(position).id + "")) {
					hold.tv_title.setTextColor(Color.GRAY);
				} else {
					// 一定要把未读的item置成黑色，因为convertview会复用
					hold.tv_title.setTextColor(Color.BLACK);
				}
			}
			return convertView;
		}

	}

	static class ViewHold {
		public ImageView iv_icon;
		public TextView tv_title;
		public TextView tv_date;
	}
}
