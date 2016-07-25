package com.example.zhbj.utils;

import android.content.Context;

public class DesityUtil {
	
	/**dip = px / 屏幕密度
	 * @param dip
	 * @param ctx
	 * @return
	 */
	public static int dip2px(float dip,Context ctx){
		float density = ctx.getResources().getDisplayMetrics().density;

		int px = (int) (dip * density + 0.5f);//0.5f四舍五入
		return px;
	}
	
	public static float px2dip(int px,Context ctx){
		float density = ctx.getResources().getDisplayMetrics().density;
        float dip = px/density;
        return dip;
	}

}
