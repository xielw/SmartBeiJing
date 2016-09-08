package com.itheima.zhbj.base.impl.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.itheima.zhbj.base.BaseMenuDetailPager;

/**
 * 菜单详情叶-互动
 * @author xielianwu
 *
 */
public class InteractMenuDetailPager extends BaseMenuDetailPager {

	public InteractMenuDetailPager(Activity mActivity) {
		super(mActivity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initView() {
		
		TextView view =  new TextView(mActivity);
		view.setText("菜单详情叶-互动");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
	
		return view;
	}

}
