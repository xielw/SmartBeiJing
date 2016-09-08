package com.itheima.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager {

	private float downX;
	private float downY;

	public TopNewsViewPager(Context context) {
		super(context);
		
	}

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		//1.上下滑动拦截,
		//2.左滑最后一个时，拦截,
		//3.右滑第一个时，拦截,
		getParent().requestDisallowInterceptTouchEvent(true);
		
		switch (ev.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			downX = ev.getX();
			downY = ev.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			float dx = ev.getX() - downX;
			float dy = ev.getY() - downY;
			
			if(Math.abs(dy) < Math.abs(dx)){
				int currentItem = getCurrentItem();
				//左右滑
				if(dx > 0){
					//向右滑
				
					if(currentItem == 0){
						//最后一个
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}else{
					int count = getAdapter().getCount();
					//向左滑
					if(currentItem == count - 1){
						//第一个
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
				
			}else {
				//上下滑
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			
			break;
			
		case MotionEvent.ACTION_UP:
			
			break;
		}
		
		return super.dispatchTouchEvent(ev);
	}
}
