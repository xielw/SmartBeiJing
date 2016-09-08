package com.itheima.zhbj.base;

import com.itheima.zhbj.MainActivity;
import com.itheima.zhbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class BasePager implements OnClickListener{

	public TextView tvTitle;
	public ImageButton ibMenu;
	public FrameLayout flContent;
	public Activity mActivity;
	public View mRootView;
	public ImageButton btnPhoto;
	public BasePager(Activity activity){
		mActivity = activity;
		mRootView = initView();
	}
	public  View initView() {
		
		View view = View.inflate(mActivity, R.layout.base_pager, null);
		
	    tvTitle = (TextView) view.findViewById(R.id.tv_title);
	    ibMenu = (ImageButton) view.findViewById(R.id.ib_menu);
	    ibMenu.setOnClickListener(this);
	    flContent = (FrameLayout) view.findViewById(R.id.fl_content);
	    btnPhoto = (ImageButton) view.findViewById(R.id.ib_photo);
		return view;
	}
	
	
	public void iniData(){
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_menu:
			toggle();
			break;

		}
	}
	
	private void toggle() {
		
		MainActivity uiMain = (MainActivity) mActivity;
		SlidingMenu slidingMenu = uiMain.getSlidingMenu();
		slidingMenu.toggle();
	}
}
