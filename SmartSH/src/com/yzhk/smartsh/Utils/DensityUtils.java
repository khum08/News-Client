package com.yzhk.smartsh.Utils;

import android.content.Context;

public class DensityUtils {
	
	public static int dp2px(int dp,Context ctx){
		float density = ctx.getResources().getDisplayMetrics().density;
		int px = (int) (dp * density);
		return px;
	}

	
	public static float px2dp(float px,Context ctx){
		float density = ctx.getResources().getDisplayMetrics().density;
		float dp = px /density;
		return dp;
		
	}
}
