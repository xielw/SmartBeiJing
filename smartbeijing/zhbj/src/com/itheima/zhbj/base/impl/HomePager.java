package com.itheima.zhbj.base.impl;

import com.itheima.zhbj.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class HomePager extends BasePager {

	public HomePager(Activity activity) {
		super(activity);
	}

	@Override
	public void iniData() {
	
		TextView view =  new TextView(mActivity);
		view.setText("首页");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		flContent.addView(view);
		tvTitle.setText("智慧北京");
		ibMenu.setVisibility(View.GONE);
	}
}
