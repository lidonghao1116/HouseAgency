package com.eroad.base;

import java.util.ArrayList;

import android.content.Context;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.baidu.mapapi.SDKInitializer;
import com.next.app.StandardApplication;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sky.house.home.HouseMainActivity;

public class SHApplication extends StandardApplication {

	private ArrayList<BaseActivity> activity_list = new ArrayList<BaseActivity>();
	private BaseActivity currentActivity;
	private static SHApplication application;

	public BaseActivity getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(BaseActivity currentActivity) {
		this.currentActivity = currentActivity;
	}

	public ArrayList<BaseActivity> getActivity_list() {
		return activity_list;
	}

	public void onCreate() {
		super.onCreate();
		application = this;
		SDKInitializer.initialize(this);
		initImageLoader(this);
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
	}
	
	public void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public static Context getContext() {
		return application;
	}

	public void addActivity(BaseActivity a) {
		activity_list.add(a);

	}

	/**
	 * 
	 */
	public void exitApplication() {
		for (BaseActivity a : activity_list) {
			a.finish();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public void onlyHome(){
		for (BaseActivity a : activity_list) {
			if(!(a instanceof HouseMainActivity)){
				a.finish();
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public static SHApplication getInstance() {

		return (SHApplication) StandardApplication.getInstance();
	}

}
