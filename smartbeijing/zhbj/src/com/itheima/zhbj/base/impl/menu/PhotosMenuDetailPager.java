package com.itheima.zhbj.base.impl.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itheima.zhbj.R;
import com.itheima.zhbj.base.BaseMenuDetailPager;
import com.itheima.zhbj.global.GlobalConstants;
import com.itheima.zhbj.utils.CacheUtils;
import com.itheima.zhbj.view.PullToRefreshListView;
import com.itheima.zhbj.view.PullToRefreshListView.onRefreshListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import domain.PhotosBean;
import domain.PhotosBean.PhotosNews;

/**
 * 菜单详情叶-组图
 * 
 * @author xielianwu
 * 
 */
public class PhotosMenuDetailPager extends BaseMenuDetailPager implements OnClickListener {

	private ImageButton btnPhoto;
	@ViewInject(R.id.lv_photo)
	private PullToRefreshListView listView;
	@ViewInject(R.id.gv_photo)
	private GridView gridView;
	private PhotosBean photosBean;
	private ArrayList<PhotosNews> newsList;
	private BitmapUtils bitmapUtils;
	private PhotosAdapter adapter;
	private boolean isListView = true;
	private String moreUrl;
	private boolean isMore = false;
	public PhotosMenuDetailPager(Activity mActivity, ImageButton btnPhoto) {
		super(mActivity);

		this.btnPhoto = btnPhoto;

	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail,
				null);
		ViewUtils.inject(this, view);
		return view;
	}

	// 加载初始化数据
	@Override
	public void initData() {

		String json = CacheUtils.getCache(mActivity, GlobalConstants.PHOTOS_URL);
		if(!TextUtils.isEmpty(json)){
			processData(json);
		}
		bitmapUtils = new BitmapUtils(mActivity);
		getDataFromServer();
		btnPhoto.setOnClickListener(this);
		listView.setOnRefreshListener(new onRefreshListener() {
			
			@Override
			public void onRefresh() {
				getDataFromServer();
			}
			
			@Override
			public void onLoadingMore() {
				
				if(TextUtils.isEmpty(moreUrl)){
					
					Toast.makeText(mActivity, "没有更多数据...", Toast.LENGTH_LONG).show();
					listView.refreshCompleted(false);
					
				}else {
					
					getMoreDataFromServer();
					isMore = true;
					
				}
				
			}

			private void getMoreDataFromServer() {
			
				
				HttpUtils httpUtils = new HttpUtils();
				httpUtils.send(HttpMethod.GET, moreUrl,  //加载更多，URL可能不存在，注意下
						new RequestCallBack<String>() {

							@Override
							public void onSuccess(ResponseInfo<String> responseInfo) {
								
								listView.refreshCompleted(true);
								String json = responseInfo.result;
								processData(json);
							}

							@Override
							public void onFailure(HttpException error, String msg) {
						
								listView.refreshCompleted(false);
								Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
								error.printStackTrace();
								
							}
						});
			}
		});
	}

	
	private void getDataFromServer() {

		isMore = false;
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, GlobalConstants.PHOTOS_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						
						listView.refreshCompleted(true);
						String json = responseInfo.result;
						processData(json);
						CacheUtils.setCache(mActivity, GlobalConstants.PHOTOS_URL, json);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
				
						listView.refreshCompleted(false);
						Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
						error.printStackTrace();
						
					}
				});
	}

	protected void processData(String json) {

		Gson gson = new Gson();
		photosBean = gson.fromJson(json, PhotosBean.class);
		//moreUrl = photosBean.data.more;
		moreUrl = "";
		if (!isMore) {
			newsList = photosBean.data.news;
			adapter = new PhotosAdapter();
			listView.setAdapter(adapter);
			gridView.setAdapter(adapter);
		}else{
			newsList.addAll(photosBean.data.news);
			adapter.notifyDataSetChanged();
		}
	}

	public class PhotosAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return newsList.size();
		}

		@Override
		public Object getItem(int position) {

			return newsList.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			HolderView holderView = null;
			if (convertView == null) {
				holderView = new HolderView();
				convertView = View.inflate(mActivity,
						R.layout.list_item_photos, null);
				holderView.ivPic = (ImageView) convertView
						.findViewById(R.id.iv_pic);
				holderView.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				convertView.setTag(holderView);
			}
			holderView = (HolderView) convertView.getTag();
			holderView.tvTitle.setText(newsList.get(position).title);
			String imgUrl = newsList.get(position).listimage;
			imgUrl = GlobalConstants.SERVER_URL + imgUrl.split("zhbj")[1];
			bitmapUtils.display(holderView.ivPic, imgUrl);

			return convertView;
		}

		public class HolderView {
			public ImageView ivPic;
			public TextView tvTitle;
		}
	}

	@Override
	public void onClick(View v) {
		
		if(isListView){
			
			btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
			gridView.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			isListView = false;
			
		}else{
			
			btnPhoto.setImageResource(R.drawable.icon_pic_list_type);
			listView.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.GONE);
			isListView = true;
					
		}
	}
}
