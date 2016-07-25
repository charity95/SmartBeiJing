package com.example.zhbj.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.zhbj.R;
import com.example.zhbj.utils.DesityUtil;
import com.example.zhbj.utils.PrefsUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class NewGuideAvtivity extends Activity {
	protected static final String tag = "NewGuideAvtivity";
	private ViewPager vp_guide;
	private LinearLayout ll_container;
	private ImageView redPoint;
	private Button btn_start;
	private int[] imageIds;
	private List<ImageView> imageList;
	private int dis;
	
     @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.activity_guide);
    	
    	vp_guide = (ViewPager) findViewById(R.id.vp_guide);
    	ll_container = (LinearLayout) findViewById(R.id.ll_container);
    	redPoint = (ImageView) findViewById(R.id.redPoint);
    	btn_start = (Button) findViewById(R.id.start);
    	
    	
    	initData();
    	
    	vp_guide.setAdapter(new NewGuideAdapter());
    	
    	btn_start.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View v) {
    			startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    			
    			PrefsUtil.putBoolean(getApplicationContext(), "is_first_enter", false);
    			finish();
    		}
    	});
    	
    	vp_guide.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if(position<2){
					btn_start.setVisibility(View.INVISIBLE);
				}else{
					btn_start.setVisibility(View.VISIBLE);

				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				//滑动偏移百分比 positionOffset
				int redPointPosition = (int) (positionOffset*dis + position*dis); 
				RelativeLayout.LayoutParams params =  (LayoutParams) redPoint.getLayoutParams();
			    params.leftMargin = redPointPosition;
			    redPoint.setLayoutParams(params);
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		} );
    	
    	//监听layout方法执行后 自动执行该方法
    	redPoint.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				redPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				    //两个圆点的距离
                  	
				dis = ll_container.getChildAt(1).getLeft()-ll_container.getChildAt(0).getLeft();		
			    Log.i(tag,"dis:"+dis);
			}
		});
    }
     
     private void initData() {
		imageIds = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
		imageList = new ArrayList<ImageView>();
		ImageView pointView;
		
		for(int i = 0;i < imageIds.length;i++){
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imageIds[i]);
			imageList.add(imageView);
			
			pointView = new ImageView(this);
			pointView.setImageResource(R.drawable.shape_point_gray);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			
			if(i>0){
				params.leftMargin = DesityUtil.dip2px(10, this);
			}
			
			pointView.setLayoutParams(params);
			ll_container.addView(pointView);
		}
	}

	class NewGuideAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return imageList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = imageList.get(position);
			container.addView(imageView);
			
			return imageView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			
			container.removeView(imageList.get(position));
		
		}
    	 
     }
}
