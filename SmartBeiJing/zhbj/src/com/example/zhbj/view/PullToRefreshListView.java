package com.example.zhbj.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zhbj.R;

public class PullToRefreshListView extends ListView implements OnScrollListener{

	public static final int PULL_TO_REFERESH = 0;
	public static final int RELEASE_TO_REFERESH = 1;
	public static final int REFERESHING = 2;

	int mCurrentState = PULL_TO_REFERESH;

	private ImageView iv_arrow;
	private TextView tv_state;
	private TextView tv_time;
	private int startY = -1;
	private View mHeaderView;
	private int mMeasuredHeight;
	private ProgressBar progressBar;
	private RotateAnimation animUp;
	private RotateAnimation animDown;
	private View mFooterView;
	private int mMeasuredFooterHeight;
	private boolean isLoad;

	public PullToRefreshListView(Context context) {
		this(context, null);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

		initHeaderView();
		initFooterView();
		initAniamation();
	
	}

	private void initFooterView() {
		mFooterView = View
				.inflate(getContext(), R.layout.pull_to_refresh_footer, null);

		this.addFooterView(mFooterView);

		// 隐藏布局
		mFooterView.measure(0, 0);
		mMeasuredFooterHeight = mFooterView.getMeasuredHeight();
		mFooterView.setPadding(0, -mMeasuredFooterHeight, 0, 0);
		
		this.setOnScrollListener(this);
		
	}

	private void initAniamation() {
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);

		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(200);
		animDown.setFillAfter(true);
	}

	private void initHeaderView() {
		mHeaderView = View
				.inflate(getContext(), R.layout.pull_to_refresh_header, null);

		this.addHeaderView(mHeaderView);

		// 隐藏头布局
		mHeaderView.measure(0, 0);
		mMeasuredHeight = mHeaderView.getMeasuredHeight();
		mHeaderView.setPadding(0, -mMeasuredHeight, 0, 0);

		iv_arrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
		tv_state = (TextView) mHeaderView.findViewById(R.id.tv_state);
		tv_time = (TextView) mHeaderView.findViewById(R.id.tv_time);
		progressBar = (ProgressBar) mHeaderView.findViewById(R.id.progressBar);
		
		setRefreshTime();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:

			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:

			if (startY == -1) {
				// 如果点击viewpager下拉，则ACTION_DOWN会被viewpager消费掉不会传到listview，所以要重新获取starY
				startY = (int) ev.getY();
			}

			int endY = (int) ev.getY();
			int disY = endY - startY;

			if (mCurrentState == REFERESHING) {
				break;
			}

			if (disY > 0 && getFirstVisiblePosition() == 0) {
				// 如果是下拉，且第一个item可见，则显示下拉刷新

				int padding = disY - mMeasuredHeight;
				mHeaderView.setPadding(0, padding, 0, 0);

				if (padding < 0 && mCurrentState != PULL_TO_REFERESH) {
					// 下拉刷新
					mCurrentState = PULL_TO_REFERESH;
					refresh();

				} else if (padding > 0 && mCurrentState != RELEASE_TO_REFERESH) {
					// 松开刷新
					mCurrentState = RELEASE_TO_REFERESH;
					refresh();
				}

				return true;
			}

			break;
		case MotionEvent.ACTION_UP:

			startY = -1;
			// 松开刷新
			if (mCurrentState == RELEASE_TO_REFERESH) {
				mCurrentState = REFERESHING;
				mHeaderView.setPadding(0, -mMeasuredHeight, 0, 0);
				refresh();

				if (mListener != null) {
					mListener.onRefresh();
				}
			} else if (mCurrentState == PULL_TO_REFERESH) {
				mHeaderView.setPadding(0, -mMeasuredHeight, 0, 0);
			}
			break;
		}

		return super.onTouchEvent(ev);
	}

	private void refresh() {
		switch (mCurrentState) {
		case PULL_TO_REFERESH:
			tv_state.setText("下拉刷新");
			progressBar.setVisibility(View.INVISIBLE);
			iv_arrow.setVisibility(View.VISIBLE);
			iv_arrow.startAnimation(animDown);
			break;
		case RELEASE_TO_REFERESH:
			tv_state.setText("松开刷新");
			progressBar.setVisibility(View.INVISIBLE);
			iv_arrow.setVisibility(View.VISIBLE);
			iv_arrow.startAnimation(animUp);
			break;
		case REFERESHING:
			tv_state.setText("正在刷新...");
			progressBar.setVisibility(View.VISIBLE);
			// 先清除动画才能设置不可见
			iv_arrow.clearAnimation();
			iv_arrow.setVisibility(View.INVISIBLE);

			mHeaderView.setPadding(0, 0, 0, 0);
			break;
		}

	}

	public onRefreshListener mListener;

	public void setOnRefreshListener(onRefreshListener listener) {
		mListener = listener;
	}

	public interface onRefreshListener {
		void onRefresh();
		void onLoadMore();
	}

	/**
	 * 下拉刷新完成,隐藏头布局,尾布局
	 */
	public void onRefreshComplete(boolean enable) {
		if (!isLoad) {
			mHeaderView.setPadding(0, -mMeasuredHeight, 0, 0);
			// 重置数据，可以进行下次刷新
			tv_state.setText("下拉刷新");
			progressBar.setVisibility(View.INVISIBLE);
			iv_arrow.setVisibility(View.VISIBLE);
			mCurrentState = PULL_TO_REFERESH;
			// 刷新成功才更新时间
			if (enable) {
				setRefreshTime();
			}
		}else{
			mFooterView.setPadding(0, -mMeasuredFooterHeight, 0, 0);
			isLoad = false;
		}
	}

	/**
	 * 更新刷新时间
	 */
	public void setRefreshTime() {

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		// new Date()就是当前的时间
		String time = dateFormat.format(new Date());

		tv_time.setText(time);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {		
		if(scrollState == SCROLL_STATE_IDLE){//空闲状态
			if(getLastVisiblePosition()==getCount()-1 && !isLoad){
				//最后一个itemke可见，到底了,并且没有正在加载
				mFooterView.setPadding(0, 0, 0, 0);
				
				//让listview显示最后一个item，即不用下拉就显示出尾布局
				setSelection(getCount()-1);
				isLoad = true;					
				
				if(mListener!=null){
					mListener.onLoadMore();	
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}

}
