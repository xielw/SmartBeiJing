package com.itheima.zhbj;

import java.util.ArrayList;
import java.util.List;

import com.itheima.zhbj.utils.DensityUtils;
import com.itheima.zhbj.utils.PrefUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class GuideActivity extends Activity {

	private ViewPager pager;
	private ImageView ivRedPoint;
	private int[] mImageIds = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
	private List<ImageView> mImageViewList;
	private LinearLayout llContainer;
	private int mPointDis;
	private Button btnStart;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
		llContainer = (LinearLayout) findViewById(R.id.ll_container);
		pager = (ViewPager) findViewById(R.id.vp_guide);
		btnStart = (Button) findViewById(R.id.btn_start);
		initData();
		GuideAdapter adapter = new GuideAdapter();
		pager.setAdapter(adapter);
		mPointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
			
				if(position == mImageViewList.size() - 1){
					btnStart.setVisibility(View.VISIBLE);
				}else {
					btnStart.setVisibility(View.INVISIBLE);
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			
				RelativeLayout.LayoutParams params = (LayoutParams) ivRedPoint.getLayoutParams();
				int leftMargin = (int) (mPointDis * positionOffset) + position * mPointDis;
				params.leftMargin = leftMargin;
				ivRedPoint.setLayoutParams(params);
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
			
				
			}
		});
		
		ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			
			

			@Override
			public void onGlobalLayout() {
				ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				mPointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
				System.out.println("mPointDis:"+mPointDis);
			}
		});
		
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				PrefUtils.setBoolean("is_frist_enter", getApplicationContext(), false);
				Intent intent = new Intent(GuideActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	

	private void initData() {
		
		
		mImageViewList = new ArrayList<ImageView>();
		for(int i =0;i < mImageIds.length;i++){
			ImageView view = new ImageView(getApplicationContext());
			view.setBackgroundResource(mImageIds[i]);
			mImageViewList.add(view);
			
			ImageView point = new ImageView(getApplicationContext());
			point.setImageResource(R.drawable.shape_point_gray);
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								      LinearLayout.LayoutParams.WRAP_CONTENT,
									  LinearLayout.LayoutParams.WRAP_CONTENT);
			if(i > 0){
				params.leftMargin = DensityUtils.dip2Px(10, this);
			}
			point.setLayoutParams(params);
			llContainer.addView(point);
			
			
		}
		
	}



	class GuideAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mImageViewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			View view = mImageViewList.get(position);
			container.addView(view);
			return view;
		}
	
	}
	

}
