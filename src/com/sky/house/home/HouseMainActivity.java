package com.sky.house.home;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.sharesdk.framework.ShareSDK;

import com.eroad.base.BaseActivity;
import com.eroad.base.SHApplication;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.UserInfoManager;
import com.next.util.SHEnvironment;
import com.sky.house.R;
import com.sky.widget.sweetdialog.SweetDialog;
import com.sky.widget.sweetdialog.SweetDialog.OnSweetClickListener;

public class HouseMainActivity extends BaseActivity {
	private RadioGroup rg;
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private String[] tabs = new String[] { "home", "map", "order", "mine" };
	private int lastCheckId = -1;
	private BroadcastReceiver rec;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ShareSDK.initSDK(this);
		ShareSDK.setConnTimeout(20000);
		ShareSDK.setReadTimeout(20000);
		SHApplication.getInstance().addActivity(this);
		rg = (RadioGroup) findViewById(R.id.rg);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (group.getCheckedRadioButtonId()) {
				case R.id.rb_0:
					changeFragment(tabs[0]);
					break;
				case R.id.rb_1:
					changeFragment(tabs[1]);
					break;
//				case R.id.rb_2:
//					changeFragment(tabs[3]);
//					break;
				case R.id.rb_3:
					if(CommonUtil.isEmpty(SHEnvironment.getInstance().getSession())){
						Intent intent = new Intent(HouseMainActivity.this,SHContainerActivity.class);
						intent.putExtra("class", HouseLoginFragment.class.getName());
						startActivity(intent);
						if(lastCheckId!= R.id.rb_3 && lastCheckId!= -1){
							rg.check(lastCheckId);
						}else{
							rg.check(R.id.rb_0);
						}
						return;
					}
					changeFragment(tabs[3]);
					break;
				}
				lastCheckId = group.getCheckedRadioButtonId();
			}
		});
		changeFragment(tabs[0]);
		rec = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				Log.i("jPush_MainActivity", "异地登陆");
				UserInfoManager.getInstance().setSession("");
				UserInfoManager.getInstance().sync(HouseMainActivity.this, true);
				Intent intent = new Intent(HouseMainActivity.this,SHContainerActivity.class);
				intent.putExtra("class", HouseLoginFragment.class.getName());
				startActivity(intent);
			}
		};
		registerBroadcast();
	}

	private void registerBroadcast() {
		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction(SHLocationManager.BROADCAST_LOCATION);
		intentFilter.addAction("JPUSH_EXIT");
		registerReceiver(rec, intentFilter);
	}
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (rec != null) {
			unregisterReceiver(rec);
		}
	}

	/**
	 * 切换界面
	 * 
	 * @param tag
	 */
	public void changeFragment(String tag) {
		hideFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
		if (fragment != null) {
			transaction.show(fragment);
		} else {
			if (tag.equals(tabs[0])) {
				fragment = new HouseTabHomeFragment();
			} else if (tag.equals(tabs[1])) {
				fragment = new HouseTabMapFragment();
			} else if (tag.equals(tabs[2])) {
				fragment = new HouseTabOrderFragment();
			} else if (tag.equals(tabs[3])) {
				fragment = new HouseTabMineFragment();
			}
			mFragmentList.add(fragment);
			transaction.add(R.id.frame, fragment, tag);
		}
		transaction.commit();
		
	}

	private void hideFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		for (Fragment f : mFragmentList) {
			ft.hide(f);
		}
		ft.commit();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		final SweetDialog dia_quit = new SweetDialog(this, SweetDialog.NORMAL_TYPE);
		dia_quit.setTitleText("提示");
		dia_quit.setContentText("是否退出应用？");
		dia_quit.showCancelButton(true);
		dia_quit.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				UserInfoManager.getInstance().setSession("");
				
				UserInfoManager.getInstance().sync(HouseMainActivity.this, true);
				SHApplication.getInstance().exitApplication();
			}
		});
		dia_quit.show();
	}
	
	
}
