package com.sky.house.home;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.eroad.base.BaseActivity;
import com.sky.house.R;
import com.sky.house.resource.HouseListFragment;

public class HouseMainActivity extends BaseActivity {
	private RadioGroup rg;
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private String[] tabs = new String[] { "home", "map", "order", "mine" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
				case R.id.rb_2:
					changeFragment(tabs[2]);
					break;
				case R.id.rb_3:
					changeFragment(tabs[3]);
					break;
				}
			}
		});
		changeFragment(tabs[0]);
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
}
