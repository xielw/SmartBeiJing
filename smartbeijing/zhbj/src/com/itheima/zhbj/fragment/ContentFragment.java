package com.itheima.zhbj.fragment;

import java.util.ArrayList;
import java.util.List;

import com.itheima.zhbj.MainActivity;
import com.itheima.zhbj.R;
import com.itheima.zhbj.base.BasePager;
import com.itheima.zhbj.base.impl.GovAffairsPager;
import com.itheima.zhbj.base.impl.HomePager;
import com.itheima.zhbj.base.impl.NewsCenterPager;
import com.itheima.zhbj.base.impl.SettingPager;
import com.itheima.zhbj.base.impl.SmartServicePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ContentFragment extends BaseFragment implements OnCheckedChangeListener,OnPageChangeListener{

	
	private List<BasePager> mPagers;
	private ContentAdapter adapter;
	private ViewPager viewPager;
	private RadioGroup rgGroup;
	private SlidingMenu slidingMenu;
	

	
	@Override
	public View initView() {

		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		viewPager = (ViewPager) view.findViewById(R.id.vp_content);
		rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
		adapter = new ContentAdapter();
		viewPager.setAdapter(adapter);
		return view;
		
	}

	@Override
	public void initData() {
		
		mPagers = new ArrayList<BasePager>();
		mPagers.add(new HomePager(getActivity()));
		mPagers.add(new NewsCenterPager(getActivity()));
		mPagers.add(new SmartServicePager(getActivity()));
		mPagers.add(new GovAffairsPager(getActivity()));
		mPagers.add(new SettingPager(getActivity()));
		rgGroup.setOnCheckedChangeListener(this);
		viewPager.setOnPageChangeListener(this);
		mPagers.get(0).iniData();
		slidingMenu = ((MainActivity)mActivity).getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	}
	
	class ContentAdapter extends PagerAdapter{

		
		@Override
		public int getCount() {
			
			return mPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			
			BasePager pager = mPagers.get(position);
			View view = pager.mRootView;
			container.addView(view);
			return view;
			
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			
			container.removeView((View) object);
		}
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		
		case R.id.rb_home:
			viewPager.setCurrentItem(0, false);
			break;
		case R.id.rb_news:
			viewPager.setCurrentItem(1, false);
			break;
		case R.id.rb_smart:
			viewPager.setCurrentItem(2, false);
			break;
		case R.id.rb_gov:
	
			viewPager.setCurrentItem(3, false);
			break;
		case R.id.rb_setting:
			viewPager.setCurrentItem(4, false);
			break;

		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {

		mPagers.get(position).iniData();
		
		if(position == 0 || position == mPagers.size() - 1){
			
			setSlideMenuEnable(false);
			
		}else{
			setSlideMenuEnable(true);
			
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	
		
	}
	
	private void setSlideMenuEnable(boolean enable) {
		
		int mode = enable ? SlidingMenu.TOUCHMODE_FULLSCREEN : SlidingMenu.TOUCHMODE_NONE;
		slidingMenu.setTouchModeAbove(mode);
	}

	/**
	 * 
	 * @return
	 */
	public NewsCenterPager getNewsCenterPager() {
		
		return (NewsCenterPager) mPagers.get(1);
	}

}
