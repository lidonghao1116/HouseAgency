package com.sky.house.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ViewInit;
import com.eroad.base.util.location.SHLocationManager;
import com.sky.house.R;
import com.sky.house.adapter.NewsAdapter;
import com.sky.house.city.HouseCityFragment;
import com.sky.house.resource.HouseListFragment;
import com.sky.house.resource.HousePublishFragment;

/**
 * 首页
 * 
 * @author skypan
 * 
 */
public class HouseTabHomeFragment extends BaseFragment {

	@ViewInit(id = R.id.tv_entire_rent, onClick = "onClick")
	private TextView mTvEntire;
	@ViewInit(id = R.id.tv_joint_rent, onClick = "onClick")
	private TextView mTvJoint;
	@ViewInit(id = R.id.tv_publish_house,onClick = "onClick")
	private TextView mTvPublish;
	
	@ViewInit(id = R.id.lv_news)
	private ListView mLvNews;
	
	private NewsAdapter adapter;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		SHLocationManager.getInstance().start();//定位
		mDetailTitlebar.setTitle("阳光租房");
		mDetailTitlebar.setSubTitle("-从此租房如此简单");
		mDetailTitlebar.setLeftButton(R.drawable.ic_back, "定位中", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SHContainerActivity.class);
				intent.putExtra("class", HouseCityFragment.class.getName());
				startActivity(intent);
			}
		});
		mDetailTitlebar.setRightButton1("登录", new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),SHContainerActivity.class);
				intent.putExtra("class", HouseLoginFragment.class.getName());
				startActivity(intent);
			}
		});
		
		mLvNews.setAdapter(new NewsAdapter(getActivity()));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		return view;
	}

	private void onClick(View v){
		Intent intent = new Intent(getActivity(),SHContainerActivity.class);
		switch(v.getId()){
		case R.id.tv_entire_rent:
			intent.putExtra("class", HouseListFragment.class.getName());
			break;
		case R.id.tv_joint_rent:
			intent.putExtra("class", HouseListFragment.class.getName());
			break;
		case R.id.tv_publish_house:
			intent.putExtra("class", HousePublishFragment.class.getName());
			break;
		}
		startActivity(intent);
	}
}
