package com.itheima.zhbj.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	public Activity mActivity;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = initView();
		return view;
	}


	public abstract View initView();

	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		initData();
		super.onActivityCreated(savedInstanceState);
	}


	public abstract void initData();
	
	
}
