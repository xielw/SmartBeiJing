package com.itheima.zhbj.base.impl;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.itheima.zhbj.MainActivity;
import com.itheima.zhbj.base.BaseMenuDetailPager;
import com.itheima.zhbj.base.BasePager;
import com.itheima.zhbj.base.impl.menu.InteractMenuDetailPager;
import com.itheima.zhbj.base.impl.menu.NewsMenuDetailPager;
import com.itheima.zhbj.base.impl.menu.PhotosMenuDetailPager;
import com.itheima.zhbj.base.impl.menu.TopicMenuDetailPager;
import com.itheima.zhbj.fragment.LeftMenuFragment;
import com.itheima.zhbj.global.GlobalConstants;
import com.itheima.zhbj.utils.CacheUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import domain.NewsMenu;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;


public class NewsCenterPager extends BasePager {

	

	private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;
	private NewsMenu mMenuData;

	public NewsCenterPager(Activity activity) {
		super(activity);
	}

	@Override
	public void iniData() {
	

		tvTitle.setText("新闻");
		ibMenu.setVisibility(View.VISIBLE);
		String json = CacheUtils.getCache(mActivity, GlobalConstants.CATEGORY_URL);
		if(!TextUtils.isEmpty(json)){
			processData(json);
		}
		getDataFromServer();
		
	}

	private void getDataFromServer() {
		
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, GlobalConstants.CATEGORY_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
		
				String result = responseInfo.result;
				System.out.println(result);
				processData(result);  
				CacheUtils.setCache(mActivity, GlobalConstants.CATEGORY_URL, result);
				
			}


			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 0).show();
				System.out.println(msg);
				//getDataFromLocal();
			}
		});
	}

	/*private void getDataFromLocal() {
		
		
		File f = new File(Environment.getExternalStorageDirectory(),"json.txt");
		byte[] b = new byte[1024]; 
		int len = 0;
		String json = "";
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			FileInputStream fis = new FileInputStream(f);
			while((len = fis.read(b)) != -1){
				bos.write(b, 0, len);
			}
			
			String string = new String(bos.toByteArray(), "utf-8");
			json = string;
			CacheUtils.setCache(mActivity, GlobalConstants.CATEGORY_URL, json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}*/
	

	private void processData(String json) {
		
		Gson gson = new Gson();
		mMenuData = gson.fromJson(json, NewsMenu.class);
		LeftMenuFragment fragment = ((MainActivity)mActivity).getLeftMenuFragment();
		fragment.setMenuData(mMenuData.data);
		System.out.println(mMenuData.toString());
		
		mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
		mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity,mMenuData.data.get(0).children));
		mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
		mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity,btnPhoto));
		mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));
		
		setCurrentDetailPager(0);
		}

	public void setCurrentDetailPager(int position) {
		
		flContent.removeAllViews();
		BaseMenuDetailPager menuDetailPager = mMenuDetailPagers.get(position);
		View view = menuDetailPager.mRootView;
		
		menuDetailPager.initData();
		flContent.addView(view);
		
		String title = mMenuData.data.get(position).title;
		//Toast.makeText(mActivity, title, 0).show();
		tvTitle.setText(title);
		btnPhoto.setVisibility(View.GONE);
		if(menuDetailPager instanceof PhotosMenuDetailPager){
			
			btnPhoto.setVisibility(View.VISIBLE);
			
		}
	}
}
