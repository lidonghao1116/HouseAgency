package com.sky.house.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHApplication;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ImageLoaderUtil;
import com.eroad.base.util.ViewInit;
import com.eroad.base.util.location.SHLocationManager;
import com.next.intf.ITaskListener;
import com.next.net.SHCacheType;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.house.adapter.NewsAdapter;
import com.sky.house.adapter.TopAdvertPagerAdapter;
import com.sky.house.city.HouseCityFragment;
import com.sky.house.resource.HouseListFragment;
import com.sky.house.resource.HousePublishFragment;
import com.sky.widget.SHDialog;

/**
 * 首页
 * 
 * @author skypan
 * 
 */
public class HouseTabHomeFragment extends BaseFragment implements ITaskListener{

	@ViewInit(id = R.id.tv_entire_rent, onClick = "onClick")
	private TextView mTvEntire;
	@ViewInit(id = R.id.tv_joint_rent, onClick = "onClick")
	private TextView mTvJoint;
	@ViewInit(id = R.id.tv_publish_house,onClick = "onClick")
	private TextView mTvPublish;
	
	@ViewInit(id = R.id.lv_news)
	private ListView mLvNews;
	
	private NewsAdapter adapter;
	
	@ViewInit(id = R.id.pager_banner)
	private ViewPager mPagerView_TopAdvert;
	
	@ViewInit(id = R.id.linear_top_indicator)
	private LinearLayout mLinearLayout_TopIndicator;
	
	private List<View> mIndicatorTopList = new ArrayList<View>();
	
	private JSONArray jsonArray;
	
	private SHPostTaskM taskTop;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				HouseTabHomeFragment.this.setTopAdv();
				break;

			default:
				break;
			}
		}
	};

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		ImageLoaderUtil.initImageLoader(SHApplication.getInstance());
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
		
		requestTopAdv();
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
	
	private void setTopAdv() {

		addTopIndicator(jsonArray.length());
		TopAdvertPagerAdapter adapter = new TopAdvertPagerAdapter(getActivity(), jsonArray);
		mPagerView_TopAdvert.setAdapter(adapter);
		int position = jsonArray.length() * 100;
		refreshTopIndicator(position % jsonArray.length());
		mHandler.postDelayed(mTopAdvertPageRunnable, 3000);
		mPagerView_TopAdvert.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				refreshTopIndicator(arg0 % jsonArray.length());
				mHandler.removeCallbacks(mTopAdvertPageRunnable);
				mHandler.postDelayed(mTopAdvertPageRunnable, 3000);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private Runnable mTopAdvertPageRunnable = new Runnable() {
		public void run() {
			int position = mPagerView_TopAdvert.getCurrentItem();
			position = (position + 1) % jsonArray.length();
			mPagerView_TopAdvert.setCurrentItem(position, true);
		}
	};
	
	/**
	 * 请求图片广告
	 */
	private void requestTopAdv() {
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		taskTop = new SHPostTaskM();
		taskTop.setUrl(ConfigDefinition.URL + "GetBanner");
		taskTop.getTaskArgs().put("bannerType", 1);
		taskTop.getTaskArgs().put("bannerCount", 3);
		taskTop.setChacheType(SHCacheType.NORMAL);
		taskTop.setListener(this);
		taskTop.start();
	}
	
	/**
	 * 根据图片数量动态添加圆点
	 * 
	 * @param size
	 *            图片数量
	 */
	private void addTopIndicator(int size) {
		int imgSize = (int) (getResources().getDisplayMetrics().density *12.5);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imgSize, imgSize);
		params.setMargins(5, 0, 5, 0);
		mLinearLayout_TopIndicator.setOrientation(LinearLayout.HORIZONTAL);
		for (int i = 0; i < size; i++) {
			View view = new View(getActivity());
			view.setLayoutParams(params);
			view.setBackgroundResource(R.drawable.banner_dian_blur);

			view.setSelected(false);
			mIndicatorTopList.add(view);
			mLinearLayout_TopIndicator.addView(view);
		}
	}

	/**
	 * 当前圆点设为选中，其他设为默认
	 * 
	 * @param position
	 */
	private void refreshTopIndicator(int position) {
		int length = mIndicatorTopList.size();
		for (int i = 0; i < length; i++) {
			if (i == position) {
				mIndicatorTopList.get(i).setBackgroundResource(R.drawable.banner_dian_focus);
			} else {
				mIndicatorTopList.get(i).setBackgroundResource(R.drawable.banner_dian_blur);
			}
		}
	}

	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		JSONObject json = (JSONObject) task.getResult();
		if (task == taskTop) {
			jsonArray = json.getJSONArray("banners");
			if (jsonArray.length() != 0) {
				mHandler.sendEmptyMessage(0);
			}
		}
	}

	@Override
	public void onTaskFailed(SHTask task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskUpdateProgress(SHTask task, int count, int total) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskTry(SHTask task) {
		// TODO Auto-generated method stub
		
	}
}
