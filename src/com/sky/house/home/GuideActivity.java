package com.sky.house.home;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.eroad.base.BaseActivity;
import com.eroad.base.util.UserInfoManager;
import com.sky.house.R;

/**
 * 
 * 引导页面
 * 
 * @author skypan
 * 
 */
public class GuideActivity extends BaseActivity{

	private ViewPager mViewPager;
	private ArrayList<View> list = new ArrayList<View>();
	private LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		mInflater = LayoutInflater.from(this);
		mViewPager = (ViewPager) findViewById(R.id.guide_viewpager);
		list.add(mInflater.inflate(R.layout.guide_view1, null));
		list.add(mInflater.inflate(R.layout.guide_view2, null));
		View view3 = mInflater.inflate(R.layout.guide_view3, null);
		view3.findViewById(R.id.btn_in).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GuideActivity.this,HouseMainActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
				finish();
			}
		});
		list.add(view3);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		mViewPager.setAdapter(new MyAdapter());

	}

	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {

			((ViewPager) container).addView(list.get(position));
			return list.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			container.removeView(view);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		UserInfoManager.getInstance().setFirstInstall(false);
		UserInfoManager.getInstance().sync(this, true);
	}

}
