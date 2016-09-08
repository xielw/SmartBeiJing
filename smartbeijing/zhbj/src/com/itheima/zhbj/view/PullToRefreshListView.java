package com.itheima.zhbj.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.itheima.zhbj.R;
import com.itheima.zhbj.utils.PrefUtils;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
public class PullToRefreshListView extends ListView  implements OnScrollListener{

	private static final int STATE_PULL_TO_REFRESH = 1;
	private static final int STATE_RELEASE_TO_REFRESH = 2;
	private static final int STATE_REFRESHING = 3;
	private int currentState = STATE_PULL_TO_REFRESH;
	
	private View headerView;
	private int startY = -1;
	private int mHeaderHeight;
	private ImageView ivArrow;
	private TextView tvTitle;
	private TextView tvDate;
	private ProgressBar pb;
	private RotateAnimation upAnima;
	private RotateAnimation downAnima;

	private onRefreshListener listener;
	private View mFooterView;
	private int mFooterHeight;
	private boolean isLoadingMore = false;
	
	public PullToRefreshListView(Context context) {
		super(context);
	
		initHeaderView();
		initFooderView();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initHeaderView();
		initFooderView();
		
	}

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooderView();
		
	}

	private void initFooderView() {
		
		mFooterView = View.inflate(getContext(), R.layout.pull_to_refresh_footer, null);
		addFooterView(mFooterView);
		mFooterView.measure(0, 0);
		mFooterHeight = mFooterView.getMeasuredHeight();
		System.out.println("------mFooterHeight------" + mFooterHeight);
		mFooterView.setPadding(0, -mFooterHeight, 0, 0);
		setOnScrollListener(this);
	}

	private void initHeaderView() {
	
		headerView = View.inflate(getContext(), R.layout.pull_to_refresh_header, null);
		ivArrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
		tvTitle = (TextView) headerView.findViewById(R.id.tv_title);
		tvDate = (TextView) headerView.findViewById(R.id.tv_refresh_date);
		pb = (ProgressBar) headerView.findViewById(R.id.pb_loading);
		
		addHeaderView(headerView);
		headerView.measure(0, 0);
		mHeaderHeight = headerView.getMeasuredHeight();
		headerView.setPadding(0, -mHeaderHeight, 0, 0);
		String lastRefreshDate = PrefUtils.getString("refreshTime", getContext(), "");
		if(TextUtils.isEmpty(lastRefreshDate)){
			tvDate.setText(getCurrentTime());
		}else{
			tvDate.setText(lastRefreshDate);
		}
		initAnimation();
	}
	
	private void initAnimation() {
	
		upAnima = new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		upAnima.setDuration(200);
		upAnima.setFillAfter(true);
		
		downAnima = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		upAnima.setDuration(200);
		upAnima.setFillAfter(true);
		
	}

	//1.当是下拉时且当前是第一个item位置时，宣示HeaderView,
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY  = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			
			if(currentState == STATE_REFRESHING)
				break;
			if(startY == -1){
				startY = (int) ev.getY();
			}
			int endY = (int) ev.getY();
			int dy = endY - startY;  //下拉
			int fristVisiblePosition = getFirstVisiblePosition();
			if(dy > 0 && fristVisiblePosition == 0){
				int pading = dy - mHeaderHeight;
				headerView.setPadding(0, pading, 0, 0);
				
				if(pading > 0 && currentState != STATE_RELEASE_TO_REFRESH){
					//释放刷新
					currentState = STATE_RELEASE_TO_REFRESH;
					refreshState();
				}else if(pading < 0  && currentState != STATE_PULL_TO_REFRESH){
					//下拉刷新
					currentState = STATE_PULL_TO_REFRESH;
					refreshState();
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			
			dy = -1;
			if(currentState == STATE_RELEASE_TO_REFRESH){
				//正在刷新
				currentState = STATE_REFRESHING;
				headerView.setPadding(0, 0, 0, 0);
				refreshState();
				if(listener != null)
					listener.onRefresh();
				
			}else if(currentState == STATE_PULL_TO_REFRESH){
				
				headerView.setPadding(0, -mHeaderHeight, 0, 0);
				
			}
		}
		return super.onTouchEvent(ev);
	}

	private void refreshState() {
		
		switch (currentState) {
		case STATE_PULL_TO_REFRESH:
			ivArrow.setVisibility(VISIBLE);
			pb.setVisibility(INVISIBLE);
			tvTitle.setText("下拉刷新");
			ivArrow.startAnimation(downAnima);
			break;

		case STATE_RELEASE_TO_REFRESH:
			ivArrow.setVisibility(VISIBLE);
			pb.setVisibility(INVISIBLE);
			tvTitle.setText("释放刷新");
			ivArrow.startAnimation(upAnima);
			break;
			
		case STATE_REFRESHING:
			ivArrow.clearAnimation();
			tvTitle.setText("正在刷新");
			ivArrow.setVisibility(INVISIBLE);
			pb.setVisibility(VISIBLE);
			break;
		}
	}
	
	public void setOnRefreshListener(onRefreshListener listener){
		this.listener = listener;
	}
	
	public void refreshCompleted(boolean success){
		
		if(isLoadingMore){
			
			mFooterView.setPadding(0, -mFooterHeight, 0, 0);
			isLoadingMore = false;
			
			
		}else{
			
			ivArrow.setVisibility(VISIBLE);
			pb.setVisibility(INVISIBLE);
			tvTitle.setText("下拉刷新");
			currentState = STATE_PULL_TO_REFRESH;
			headerView.setPadding(0, -mHeaderHeight, 0, 0);
			if(success){
				tvDate.setText(getCurrentTime());
				PrefUtils.setString("refreshTime", getContext(), getCurrentTime());
			}
			
		}
		
		
		
	}
	private String getCurrentTime() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	public interface onRefreshListener{
		void onRefresh();
		void onLoadingMore();
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	
		if(scrollState == SCROLL_STATE_IDLE  && !isLoadingMore ){
			 int lastVisiblePosition = getLastVisiblePosition();
			 if(lastVisiblePosition == getCount() -1){
				 
				 mFooterView.setPadding(0, 0, 0, 0);
				 setSelection(getCount() - 1);
				 isLoadingMore = true;
				 if(listener != null)
					 listener.onLoadingMore();
			 }	
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		
	}
}
