package com.eroad.base.util;

import android.content.Context;

/**
 * 记录蒙层引导页的使用状态
 * 
 * @author skypan
 * 
 */
public class MaskingPreference {
	// 偏好文件名
	public static final String SHAREDPREFERENCES_NAME = "offerdata";
	// 引导界面KEY
	private static final String KEY_GUIDE_ACTIVITY = "guide_activity";

	/**
	 * 判断当前界面（activity||fragment）是否引导过
	 * 
	 * @param context
	 * @param className
	 * @return
	 */
	public static boolean viewIsGuided(Context context, String className) {
		String str = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(KEY_GUIDE_ACTIVITY, "");
		String[] classNames = str.split(";");
		for (String s : classNames) {
			if (className.equals(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 设置当前界面（activity||fragment）已经引导过
	 * 
	 * @param context
	 * @param className 类名
	 */
	public static void setViewIsGuided(Context context, String className) {
		String str = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(KEY_GUIDE_ACTIVITY, "");
		str = str + ";" + className;
		context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE)// 保存修改后的值
				.edit().putString(KEY_GUIDE_ACTIVITY, str).commit();
	}
	
}
