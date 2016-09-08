package com.itheima.zhbj;

import com.itheima.zhbj.utils.PrefUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {

	private RelativeLayout  rlRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        
        RotateAnimation animRate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        animRate.setDuration(1000);
        animRate.setFillAfter(true);
        ScaleAnimation animScale = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        animScale.setDuration(1000);
        animScale.setFillAfter(true);
        AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
        animAlpha.setDuration(2000);
        animAlpha.setFillAfter(true);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animRate);
        set.addAnimation(animScale);
        set.addAnimation(animAlpha);
        rlRoot.startAnimation(set);
        
        set.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			
				
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				
				boolean isFrist = PrefUtils.getBoolean("is_frist_enter", SplashActivity.this, true);
				if(isFrist){
					
					Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
					startActivity(intent);
					finish();
				}else{
					Intent intent = new Intent(SplashActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
    }


}
