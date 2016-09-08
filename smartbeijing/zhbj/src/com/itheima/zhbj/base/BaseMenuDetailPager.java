package com.itheima.zhbj.base;

import android.app.Activity;
import android.view.View;

public abstract class BaseMenuDetailPager {

	public Activity mActivity;
	public View mRootView;
	public BaseMenuDetailPager(Activity mActivity) {
		this.mActivity = mActivity;
		mRootView = initView();
		System.out.println("BaseMenuDetailPager构造函数----initView");
	}

	public abstract View initView();
	
	public void initData(){
		
	}
}
