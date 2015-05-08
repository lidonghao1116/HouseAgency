package com.eroad.base;

import java.util.ArrayList;

import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.next.app.StandardApplication;

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
	}

	/**
	 * 
	 * @return
	 */
	public static SHApplication getInstance() {

		return (SHApplication) StandardApplication.getInstance();
	}

}
