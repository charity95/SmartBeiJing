package com.example.zhbj.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	public Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 获取fragment依附的activity对象
		mActivity = getActivity();
	}

	//初始化fragment的布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView();
		return view;
	}
   
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		initData();
		super.onActivityCreated(savedInstanceState);
	}
	
	public abstract View initView();

	public abstract void initData();

}
