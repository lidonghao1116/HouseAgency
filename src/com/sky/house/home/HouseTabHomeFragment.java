package com.sky.house.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.UserInfoManager;
import com.eroad.base.util.ViewInit;
import com.eroad.base.util.location.Location;
import com.eroad.base.util.location.SHLocationManager;
import com.next.intf.ITaskListener;
import com.next.net.SHCacheType;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.next.util.SHEnvironment;
import com.sky.house.R;
import com.sky.house.adapter.NewsAdapter;
import com.sky.house.adapter.TopAdvertPagerAdapter;
import com.sky.house.city.HouseCityFragment;
import com.sky.house.me.HouseAuthenticationFragment;
import com.sky.house.resource.HouseListFragment;
import com.sky.house.resource.HousePublishFragment;
import com.sky.widget.SHDialog;
import com.sky.widget.SHToast;
import com.sky.widget.sweetdialog.SweetDialog;

/**
 * 首页
 * 
 * @author skypan
 * 
 */
public class HouseTabHomeFragment extends BaseFragment implements ITaskListener {

	@ViewInit(id = R.id.tv_entire_rent, onClick = "onClick")
	private TextView mTvEntire;
	@ViewInit(id = R.id.tv_joint_rent, onClick = "onClick")
	private TextView mTvJoint;
	@ViewInit(id = R.id.tv_publish_house, onClick = "onClick")
	private TextView mTvPublish;

	@ViewInit(id = R.id.tv_update_msg)
	private TextView mTvDes;

	@ViewInit(id = R.id.lv_news)
	private ListView mLvNews;

	private NewsAdapter adapter;

	@ViewInit(id = R.id.pager_banner)
	private ViewPager mPagerView_TopAdvert;

//	@ViewInit(id = R.id.linear_top_indicator)
//	private LinearLayout mLinearLayout_TopIndicator;

	private List<View> mIndicatorTopList = new ArrayList<View>();

	private JSONArray jsonArray;

	private SHPostTaskM taskTop, taskCity, newsTask;

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

	private BroadcastReceiver rec = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(SHLocationManager.BROADCAST_LOCATION)) {
				requestCity();
			}
		}
	};

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		IntentFilter filter = new IntentFilter();
		filter.addAction(SHLocationManager.BROADCAST_LOCATION);
		getActivity().registerReceiver(rec, filter);
		mDetailTitlebar.setTitle("阳光租房");
		mDetailTitlebar.setSubTitle("-从此租房如此简单");
		mDetailTitlebar.setLeftButton("定位中..", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SHContainerActivity.class);
				intent.putExtra("class", HouseCityFragment.class.getName());
				startActivityForResult(intent, 0);
				getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
			}
		});
		// mDetailTitlebar.setRightButton1("登录", new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(getActivity(), SHContainerActivity.class);
		// intent.putExtra("class", HouseLoginFragment.class.getName());
		// startActivity(intent);
		// }
		// });
		mPagerView_TopAdvert.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, CommonUtil.Window.getWidth() / 5 * 2));
		if (!CommonUtil.isEmpty(Location.getInstance().getCity())) {
			requestCity();
		}
		requestTopAdv();
		requestNews();
		SHEnvironment.getInstance().setSession(UserInfoManager.getInstance().getSession());
		UserInfoManager.getInstance().setAuth(UserInfoManager.getInstance().isAuth());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		return view;
	}

	private void onClick(View v) {
		Intent intent = new Intent(getActivity(), SHContainerActivity.class);
		switch (v.getId()) {
		case R.id.tv_entire_rent:
			if(CommonUtil.isEmpty(Location.getInstance().getSelectedCityId()+"")){
				SHToast.showToast(getActivity(), "请选择城市");
				return;
			}
			intent.putExtra("class", HouseListFragment.class.getName());
			intent.putExtra("rentType", 1);
			startActivity(intent);
			break;
		case R.id.tv_joint_rent:
			if(CommonUtil.isEmpty(Location.getInstance().getSelectedCityId()+"")){
				SHToast.showToast(getActivity(), "请选择城市");
				return;
			}
			intent.putExtra("class", HouseListFragment.class.getName());
			intent.putExtra("rentType", 2);
			startActivity(intent);
			break;
		case R.id.tv_publish_house:
			if(CommonUtil.isEmpty(SHEnvironment.getInstance().getSession())){
				Intent intent_login = new Intent(getActivity(),SHContainerActivity.class);
				intent_login.putExtra("class", HouseLoginFragment.class.getName());
				startActivity(intent_login);
				return;
			}
			if(!ConfigDefinition.isAuth){
				Intent intent_auth = new Intent(getActivity(),SHContainerActivity.class);
				intent_auth.putExtra("class", HouseAuthenticationFragment.class.getName());
				startActivity(intent_auth);
				return;
			}
			intent.putExtra("class", HousePublishFragment.class.getName());
			startActivity(intent);
			break;
		}
	}

	private void requestCity() {
		taskCity = new SHPostTaskM();
		taskCity.setUrl(ConfigDefinition.URL + "GetCityByLL");
		taskCity.setListener(this);
		taskCity.getTaskArgs().put("lng", Location.getInstance().getLng());
		taskCity.getTaskArgs().put("lat", Location.getInstance().getLat());
		taskCity.start();
	}

	private void setTopAdv() {

//		addTopIndicator(jsonArray.length());
		TopAdvertPagerAdapter adapter = new TopAdvertPagerAdapter(getActivity(), jsonArray, TopAdvertPagerAdapter.FLAG_HOME_ADV);
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
		taskTop = new SHPostTaskM();
		taskTop.setUrl(ConfigDefinition.URL + "GetBanner");
		taskTop.getTaskArgs().put("bannerType", 1);
		taskTop.getTaskArgs().put("bannerCount", 3);
		taskTop.setChacheType(SHCacheType.NORMAL);
		taskTop.setListener(this);
		taskTop.start();
	}

	private void requestNews() {
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		newsTask = new SHPostTaskM();
		newsTask.setListener(this);
		newsTask.setUrl(ConfigDefinition.URL + "gethomepagemsg");
		newsTask.start();
	}

	/**
	 * 根据图片数量动态添加圆点
	 * 
	 * @param size
	 *            图片数量
	 */
//	private void addTopIndicator(int size) {
//		int imgSize = (int) (getResources().getDisplayMetrics().density * 8);
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imgSize, imgSize);
//		params.setMargins(5, 0, 5, 0);
//		mLinearLayout_TopIndicator.setOrientation(LinearLayout.HORIZONTAL);
//		for (int i = 0; i < size; i++) {
//			View view = new View(getActivity());
//			view.setLayoutParams(params);
//			view.setBackgroundResource(R.drawable.banner_dian_blur);
//			view.setSelected(false);
//			mIndicatorTopList.add(view);
//			mLinearLayout_TopIndicator.addView(view);
//		}
//	}

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
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(rec);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && data != null) {
			Location.getInstance().setSelectedCityId(data.getIntExtra("id", -1));
			mDetailTitlebar.setLeftButton(data.getStringExtra("cityName"), new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), SHContainerActivity.class);
					intent.putExtra("class", HouseCityFragment.class.getName());
					startActivityForResult(intent, 0);
					getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
				}
			});
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
		} else if (task == taskCity) {
			JSONObject currentCityJson = json.getJSONObject("city");
			Location.getInstance().setCity(currentCityJson.getString("cityName"));
			// Location.getInstance().setLat(currentCityJson.getDouble("latitude"));
			// Location.getInstance().setLng(currentCityJson.getDouble("longitude"));
			Location.getInstance().setCityId(currentCityJson.getInt("id"));
			mDetailTitlebar.setLeftButton(Location.getInstance().getCity(), new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), SHContainerActivity.class);
					intent.putExtra("class", HouseCityFragment.class.getName());
					startActivityForResult(intent, 0);
					getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
				}
			});
		} else if (task == newsTask) {
			SpannableStringBuilder sb = new SpannableStringBuilder();
			sb.append("已经更新房源");
			SpannableString ss = new SpannableString(json.getInt("updateHouseAmt") + "");
			ss.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.color_orange)), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			sb.append(ss).append("套 ｜ 成交");
			ss = new SpannableString(json.getInt("dealHouseAmt") + "");
			ss.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.color_orange)), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			sb.append(ss).append("套");
			mTvDes.setText(sb);
			JSONArray msgArray = json.getJSONArray("msgList");
			mLvNews.setAdapter(new NewsAdapter(getActivity(), msgArray));
		}
	}

	@Override
	public void onTaskFailed(SHTask task) {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		new SweetDialog(getActivity(), SweetDialog.ERROR_TYPE).setTitleText("提示").setContentText(task.getRespInfo().getMessage()).show();
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
