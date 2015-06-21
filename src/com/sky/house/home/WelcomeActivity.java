package com.sky.house.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import cn.jpush.android.api.JPushInterface;

import com.eroad.base.util.UserInfoManager;
import com.eroad.base.util.location.SHLocationManager;
import com.sky.house.R;

public class WelcomeActivity extends Activity {
	private Runnable mEnterAppRunnable;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		SHLocationManager.getInstance().start();//定位
		mEnterAppRunnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent;
				if (UserInfoManager.getInstance().isFirstInstall()) {
					intent = new Intent(WelcomeActivity.this, GuideActivity.class);
				} else {
					intent = new Intent(WelcomeActivity.this, HouseMainActivity.class);
				}
				startActivity(intent);
				overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
				finish();
			}
		};
		mHandler = new Handler();
		mHandler.postDelayed(mEnterAppRunnable, 1000);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(WelcomeActivity.this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(WelcomeActivity.this);
	}


}
