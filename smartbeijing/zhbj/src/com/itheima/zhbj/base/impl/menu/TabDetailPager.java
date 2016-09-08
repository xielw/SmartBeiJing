package com.itheima.zhbj.base.impl.menu;

import java.util.ArrayList;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itheima.zhbj.NewsDetailActivity;
import com.itheima.zhbj.R;
import com.itheima.zhbj.base.BaseMenuDetailPager;
import com.itheima.zhbj.global.GlobalConstants;
import com.itheima.zhbj.utils.CacheUtils;
import com.itheima.zhbj.utils.PrefUtils;
import com.itheima.zhbj.view.PullToRefreshListView;
import com.itheima.zhbj.view.PullToRefreshListView.onRefreshListener;
import com.itheima.zhbj.view.TopNewsViewPager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import domain.NewsTabBean;
import domain.NewsMenu.NewsTabData;
import domain.NewsTabBean.NewsData;
import domain.NewsTabBean.TopNews;

/**
 * 页签页面对象
 * @author xielianwu	
 */
public class TabDetailPager extends BaseMenuDetailPager implements OnPageChangeListener
,OnItemClickListener{

	private  int c = 0;
	private NewsTabData mTabData;//单个页签的网络数据
	private TextView view;
	@ViewInject(R.id.vp_top_news)
	private TopNewsViewPager viewPager;
	@ViewInject(R.id.lv_listView)
	private PullToRefreshListView listView;
	private String mUrl;
	private ArrayList<TopNews> topNews;
	private TextView tvTitle;
	private CirclePageIndicator indicator;
	private ArrayList<NewsData> newsList;

	private NewsAdapter adapter;
	private String moreUrl;
	private ArrayList<NewsData> morenews;
	private Handler handler;
	
	public TabDetailPager(Activity mActivity, NewsTabData newsTabData) {
		super(mActivity);
		
		mTabData = newsTabData;	
		mUrl = GlobalConstants.SERVER_URL + mTabData.url;
		
	
	}

	@Override
	public View initView() {
		
//		view = new TextView(mActivity);
//	
//		view.setTextColor(Color.RED);
//		view.setTextSize(22);
//		view.setGravity(Gravity.CENTER);
	
	View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
	View headerView = View.inflate(mActivity, R.layout.list_item_header, null);
	
	ViewUtils.inject(this, view);
	ViewUtils.inject(this,headerView);
	
    tvTitle = (TextView) headerView.findViewById(R.id.tv_title);
    indicator = (CirclePageIndicator) headerView.findViewById(R.id.indicator);
	listView.addHeaderView(headerView);
	listView.setOnRefreshListener(new onRefreshListener() {
		
		@Override
		public void onRefresh() {
		
				getDataFromServer();
		
		}

		@Override
		public void onLoadingMore() {
		
		
			System.out.println("--moreUrl--" + moreUrl);
			if(!TextUtils.isEmpty(moreUrl)){
				
				getMoreFromeServer();
				
			}else {
				
				listView.refreshCompleted(false);
				Toast.makeText(mActivity, "没有更多数据...", 0).show();
				
			}
		}
	});
	
	listView.setOnItemClickListener(this);
		return view;
	}

	protected void getMoreFromeServer() {
		

		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, moreUrl, new RequestCallBack<String>() {

		

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processData(result,true);
				listView.refreshCompleted(true);
			}
		
			

			@Override
			public void onFailure(HttpException error, String msg) {
				
				error.printStackTrace();
				
				Toast.makeText(mActivity, msg, 0).show();
				
			}
		});
		
	}
	

	@Override
	public void initData() {
		
//		view.setText(mTabData.title);
	
		String jsonString = CacheUtils.getCache(mActivity, mUrl);
		if(!TextUtils.isEmpty(jsonString)){
			
			processData(jsonString,false);
			
		}
		getDataFromServer();
		
	}
	
	private void getDataFromServer() {
		
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

		

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processData(result,false);
				CacheUtils.setCache(mActivity, mUrl, result);
				listView.refreshCompleted(true);
			}
		
			

			@Override
			public void onFailure(HttpException error, String msg) {
				
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 0).show();
				listView.refreshCompleted(false);
			}
		});
	}

	private void processData(String result,boolean isLoadingMore) {
		
		
		Gson gson = new Gson();
		NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);
		moreUrl= newsTabBean.data.more;
		if(!TextUtils.isEmpty(moreUrl)){
			moreUrl = GlobalConstants.SERVER_URL + newsTabBean.data.more;
		}
		if(!isLoadingMore){
			topNews = newsTabBean.data.topnews;
			if(topNews != null){
				
				viewPager.setAdapter(new TopNewsAdapter());
				
			}
			
			tvTitle.setText(topNews.get(0).title);
			indicator.setViewPager(viewPager);
			indicator.onPageSelected(0);
			indicator.setSnap(true);
			indicator.setOnPageChangeListener(this);
			newsList = newsTabBean.data.news;
			if(newsList != null){
				adapter = new NewsAdapter();
				listView.setAdapter(adapter);
			}
			
			if(handler == null){
				
				handler = new Handler(){
					public void handleMessage(android.os.Message msg) {
						
						int currentItem = viewPager.getCurrentItem();
						currentItem ++;
						if(currentItem >= viewPager.getAdapter().getCount()){
							currentItem = 0;
						}
						viewPager.setCurrentItem(currentItem,false);
						handler.sendEmptyMessageDelayed(1, 2000);
					};
				};
				
				handler.sendEmptyMessageDelayed(1, 2000);
				
				viewPager.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							handler.removeCallbacksAndMessages(null);
							break;
						case MotionEvent.ACTION_CANCEL:
							handler.sendEmptyMessageDelayed(1, 2000);
							break;
						case MotionEvent.ACTION_UP:
							handler.sendEmptyMessageDelayed(1, 2000);
							break;
						default:
							break;
						}
						return false;
					}
				});
			}
		
		
		}else{
			
			morenews = newsTabBean.data.news;
			newsList.addAll(morenews);
			adapter.notifyDataSetChanged();
			
		}
		
		
	}
	
	class TopNewsAdapter extends PagerAdapter{

		private String imageUrl;
		private BitmapUtils bitmapUtils;

		public TopNewsAdapter(){
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.topnews_item_default);
		}
		
		@Override
		public int getCount() {
			
			return topNews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			
			return view == object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			
			ImageView imageView = new ImageView(mActivity);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageUrl = topNews.get(position).topimage;
			imageUrl = GlobalConstants.SERVER_URL + imageUrl.split("zhbj")[1];
			bitmapUtils.display(imageView, imageUrl);
			container.addView(imageView);
			return imageView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		
			container.removeView((View) object);
			
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		
		
	}

	@Override
	public void onPageSelected(int position) {
	
		String title = topNews.get(position).title;
		tvTitle.setText(title);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	
		
	}


	public class NewsAdapter extends BaseAdapter{

		private BitmapUtils bitmapUtils;

		public NewsAdapter(){
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}
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

		@SuppressWarnings("null")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HolderView holderView = null;
			if(convertView == null){
				convertView = View.inflate(mActivity, R.layout.list_item_news, null);
				holderView = new HolderView();
				holderView.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				holderView.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
				holderView.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
				convertView.setTag(holderView);
			}else {
				holderView = (HolderView) convertView.getTag();
			}
			NewsData newsData = newsList.get(position);
			holderView.tvTitle.setText(newsData.title);
			holderView.tvDate.setText(newsData.pubdate);
			String imgUrl = newsData.listimage.split("zhbj")[1];
			imgUrl = GlobalConstants.SERVER_URL + imgUrl;
			bitmapUtils.display(holderView.ivIcon, imgUrl);
			
			String newsId = PrefUtils.getString("newsId", mActivity, "");
			if(newsId.contains(newsData.id + "")){
				holderView.tvTitle.setTextColor(Color.GRAY);
			}else{
				holderView.tvTitle.setTextColor(Color.BLACK);
			}
			return convertView;
		}
		
	}
	
	 public class HolderView{
		
		public TextView tvTitle;
		public TextView tvDate;
		public ImageView ivIcon;
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int count = listView.getHeaderViewsCount();
		position = position - count;
		String newsId = PrefUtils.getString("newsId", mActivity, "");
		NewsData news = newsList.get(position);
		
		if(!newsId.contains(news.id + "")){
			newsId = newsId + news.id + ",";
			PrefUtils.setString("newsId", mActivity, newsId);
		}
		
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setTextColor(Color.GRAY);
		
		//跳到新闻详情页面
		
		Intent intent = new Intent(mActivity, NewsDetailActivity.class);
		intent.putExtra("url", news.url);
		mActivity.startActivity(intent);
		
	}
	
}
