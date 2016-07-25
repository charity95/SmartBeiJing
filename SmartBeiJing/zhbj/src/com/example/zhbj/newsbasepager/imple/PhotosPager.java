package com.example.zhbj.newsbasepager.imple;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhbj.R;
import com.example.zhbj.bean.PhotoNews;
import com.example.zhbj.bean.PhotoNews.PhotoNewsDetail;
import com.example.zhbj.global.ConstantValue;
import com.example.zhbj.newsbasepager.NewsBasePager;
import com.example.zhbj.utils.PrefsUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class PhotosPager extends NewsBasePager {

	protected static final String tag = "PhotosPager";
	private ListView lv_photo;
	private GridView gv_photo;
	private ArrayList<PhotoNewsDetail> mPhotoNewsDetail;
	private ImageView mImagePhoto;
	private boolean isListView = true;

	public PhotosPager(Activity activity, ImageView iv_photo) {
		super(activity);
		mImagePhoto = iv_photo;
	}
	

	@Override
	public View initView() {
		View view = View.inflate(mActiviy, R.layout.photo_pager, null);
		
		lv_photo = (ListView) view.findViewById(R.id.lv_photo);
		gv_photo = (GridView) view.findViewById(R.id.gv_photo);
		
		return view;
	}

	@Override
	public void initData() {
		String photoCacheData = PrefsUtil.getString(mActiviy, "photo_news_cache", "");
		if(!TextUtils.isEmpty(photoCacheData)){
			Log.i(tag, "组图："+photoCacheData);
			processData(photoCacheData);
		}
		getDataFromServer();
		
		if(mPhotoNewsDetail!=null){
			//gridview的item布局跟listview一模一样所以可以用同样的适配器
			lv_photo.setAdapter(new PhotoAdapter());
			gv_photo.setAdapter(new PhotoAdapter());
		}
		
		mImagePhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isListView){
					//切换成gridview
					lv_photo.setVisibility(View.GONE);
					gv_photo.setVisibility(View.VISIBLE);
					mImagePhoto.setBackgroundResource(R.drawable.icon_pic_list_type);
					isListView = false;
				}else{
					//切换成listview
					lv_photo.setVisibility(View.VISIBLE);
					gv_photo.setVisibility(View.GONE);
					mImagePhoto.setBackgroundResource(R.drawable.icon_pic_grid_type);
					isListView = true;
				}
				
			}
		});
		
	}


	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();

		// HttpUtils的send方法底层是在子线程中访问网络数据，onSuccess/onFailure方法是在主线程中执行
		utils.send(HttpMethod.GET, ConstantValue.PHOTO_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				
				Log.i(tag, "组图："+result);
				// 网络缓存
				PrefsUtil.putString(mActiviy, "photo_news_cache", result);

				processData(result);

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActiviy, msg, 0).show();

			}
		});
		
	}


	protected void processData(String result) {
		Gson gson = new Gson();
		PhotoNews photoNews = gson.fromJson(result, PhotoNews.class);
		mPhotoNewsDetail = photoNews.data.news;
		
	}
	
	class PhotoAdapter extends BaseAdapter{

		private BitmapUtils bitmapUtils;

		public PhotoAdapter(){
			bitmapUtils = new BitmapUtils(mActiviy);
			bitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}
		
		@Override
		public int getCount() {
			return mPhotoNewsDetail.size();
		}

		@Override
		public PhotoNewsDetail getItem(int position) {
			return mPhotoNewsDetail.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				convertView = View.inflate(mActiviy, R.layout.listview_item_photo, null);
				holder = new ViewHolder();
				holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_item_photo);
				holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			PhotoNewsDetail item = getItem(position);
			holder.tv_title.setText(item.title);
			
			bitmapUtils.display(holder.iv_photo, item.listimage);
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		public ImageView iv_photo;
		public TextView tv_title;
	}
}
