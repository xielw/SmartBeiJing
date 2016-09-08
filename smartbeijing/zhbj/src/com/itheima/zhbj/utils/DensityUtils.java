package com.itheima.zhbj.utils;

import android.content.Context;

public class DensityUtils {

	public static int dip2Px(float dip,Context ctx){
		float density = ctx.getResources().getDisplayMetrics().density;
		int px = (int) (dip * density + 0.5f);
		return px;
	}
	
	
	public static float px2Dip(int px,Context ctx){
		
		float density = ctx.getResources().getDisplayMetrics().density;
		float dp = px / density;
		return dp;
		
	}
}
