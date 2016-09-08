package com.itheima.zhbj.base.impl.menu;

import java.util.ArrayList;

import android.R.integer;
import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itheima.zhbj.MainActivity;
import com.itheima.zhbj.R;
import com.itheima.zhbj.base.BaseMenuDetailPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import domain.NewsMenu.NewsTabData;

/**
 * 
 * @author xielianwu
 * 菜单详情叶-新闻
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements OnPageChangeListener {

	@ViewInject(R.id.vp_news_menu_detail)
	private ViewPager mViewPager;
	private ArrayList<NewsTabData> mNewsTabDatas;  //页签网络数据
	private ArrayList<TabDetailPager> mTabDetailPagers;  //页签页面集合
	private TabPageIndicator mIndicator;
	private SlidingMenu slidingMenu;
	public NewsMenuDetailPager(Activity mActivity, ArrayList<NewsTabData> children) {
		super(mActivity);
		mNewsTabDatas = children;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
		ViewUtils.inject(this, view);
		mIndicator = (TabPageIndicator) view.findViewById(R.id.ti_indicator);
		
		return view;
	}

	@Override
	public void initData() {
	
		mTabDetailPagers = new ArrayList<TabDetailPager>();
		for(int i = 0; i < mNewsTabDatas.size(); i++){
			
			mTabDetailPagers.add(new TabDetailPager(mActivity,mNewsTabDatas.get(i)));
			
		}
		
		mViewPager.setAdapter(new NewsMenuDetailAdapter());
		mIndicator.setViewPager(mViewPager);//将ViewPager和指示器邦定在一起，必须在viewPager设置完之后再绑定
		//设置页面滑动监听
		//mViewPager.setOnPageChangeListener(this); 此处必须给mIndicator设置监听,不能给mViewPager设置监听
		mIndicator.setOnPageChangeListener(this);
		slidingMenu = ((MainActivity)mActivity).getSlidingMenu();
	}
	
	class NewsMenuDetailAdapter extends PagerAdapter{

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return mNewsTabDatas.get(position).title;
		}
		@Override
		public int getCount() {
		
			return mTabDetailPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
		
			return view == object;
		}
		
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			TabDetailPager tabDetailPager = mTabDetailPagers.get(position);
			View view = tabDetailPager.mRootView;
			tabDetailPager.initData();
			container.addView(view);
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}
	}

	

	private void setSlideMenuEnable(boolean enable) {
		
		int mode = enable ? SlidingMenu.TOUCHMODE_FULLSCREEN : SlidingMenu.TOUCHMODE_NONE;
		slidingMenu.setTouchModeAbove(mode);
	}

	@OnClick(R.id.btn_next)
	public void nextPager(View view){
		
		int currentItem = mViewPager.getCurrentItem();
		mViewPager.setCurrentItem(++currentItem);
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		System.out.println("当前的位置:"+ position);
		if(position == 0){
			setSlideMenuEnable(true);
			
		}else{
			setSlideMenuEnable(false);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		
	}
	
}
