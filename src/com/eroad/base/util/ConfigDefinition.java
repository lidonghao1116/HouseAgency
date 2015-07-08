package com.eroad.base.util;

public class ConfigDefinition {
	public static final String PREFS_DATA = "housedata";
	public static final String URL = "http://120.26.119.158/CFDH/data.ashx?function=";// 测试
	
	public static final String PAY_URL = "http://120.26.119.158/AlipayNotify/notify_url.aspx";//支付回调URL
	// public static final String URL = "http://mobile.9191offer.com/";// 生产
	
	public static final int ELE_NUM = 9;//家电数量
	
	public static final int FUNI_NUM = 8;//家具数量
	
	public static boolean isAuth;//是否认证？
	
	public static boolean hasSetPass;//是否设置密码

}
