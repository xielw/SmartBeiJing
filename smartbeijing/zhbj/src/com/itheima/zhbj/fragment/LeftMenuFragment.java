package com.itheima.zhbj.fragment;

import java.util.ArrayList;


import com.itheima.zhbj.MainActivity;
import com.itheima.zhbj.R;
import com.itheima.zhbj.base.impl.NewsCenterPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import domain.NewsMenu.NewsMenuData;

import android.content.ClipData.Item;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LeftMenuFragment extends BaseFragment implements OnItemClickListener{

	private ArrayList<NewsMenuData> mNewsMenuData;
	@ViewInject(R.id.lv_left_menu)
	private ListView lvLeftMenu;
	private LeftMenuAdapter menuAdapter;
	private int currentSelected = 0;
	@Override
	public View initView() {
		
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		//lvLeftMenu = (ListView) view.findViewById(R.id.lv_left_menu);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		

	}

	public void setMenuData(ArrayList<NewsMenuData> data) {
		currentSelected = 0;
		mNewsMenuData = data;
		menuAdapter = new LeftMenuAdapter();
		lvLeftMenu.setAdapter(menuAdapter);
		lvLeftMenu.setOnItemClickListener(this);
		
	}

	class LeftMenuAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mNewsMenuData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mNewsMenuData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);
			TextView tvView = (TextView) view.findViewById(R.id.tv_menu);
			tvView.setEnabled(position == currentSelected);
			System.out.println("----------getView--------------");
			NewsMenuData itemData  = (NewsMenuData) getItem(position);
			tvView.setText(itemData.title);
			return view;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		System.out.println("-------onItemClick--------------");
		currentSelected = position;
		menuAdapter.notifyDataSetChanged();
		toggle();
	}

	private void toggle() {
		
		MainActivity uiMain = (MainActivity) mActivity;
		SlidingMenu slidingMenu = uiMain.getSlidingMenu();
		slidingMenu.toggle();
		
		int position = currentSelected;
		setCurrentDetailPager(position);
	}

	/**
	 * 设置当前菜单详情页
	 * @param position
	 */
	private void setCurrentDetailPager(int position) {
		// TODO Auto-generated method stub
		MainActivity mMainUi = (MainActivity) mActivity;
		ContentFragment fragment = mMainUi.getContentFragment();
		NewsCenterPager pager = fragment.getNewsCenterPager();
		//修改新闻中心的FrameLayout的布局
		pager.setCurrentDetailPager(position);
	}
}
