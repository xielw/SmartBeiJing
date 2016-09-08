package com.itheima.zhbj;

import com.itheima.zhbj.fragment.ContentFragment;
import com.itheima.zhbj.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends SlidingFragmentActivity {

	
	private static final String TAG_LEFT_MENU = "leftMenufragment";
	private static final String TAG_CONTENT = "content_fragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.left_menu);
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		WindowManager manager = getWindowManager();
		int w = manager.getDefaultDisplay().getWidth();
		slidingMenu.setBehindOffset(270*w/480);
		System.out.println(".......with: " + w);
		initFragment();
		//ViewPager
	}

	private void initFragment() {
		
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.fl_left_menu, new LeftMenuFragment(), TAG_LEFT_MENU);
		transaction.add(R.id.fl_main, new ContentFragment(), TAG_CONTENT);
		transaction.commit();
	}

	public LeftMenuFragment getLeftMenuFragment() {
		
		FragmentManager manager = getSupportFragmentManager();
		LeftMenuFragment fragment = (LeftMenuFragment) manager.findFragmentByTag(TAG_LEFT_MENU);
		return fragment;
		
	}

	/**
	 * 获得ContentFrament对象
	 * @return
	 */
	public ContentFragment getContentFragment() {
		// TODO Auto-generated method stub
		
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
		return fragment;
	}


}
