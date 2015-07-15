package com.sky.house.resource;

import org.json.JSONArray;
import org.json.JSONException;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.sharesdk.onekeyshare.OneKeyShareCallback;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHApplication;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ImageLoaderUtil;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.next.util.SHEnvironment;
import com.sky.house.R;
import com.sky.house.adapter.GridAdapter;
import com.sky.house.adapter.TopAdvertPagerAdapter;
import com.sky.house.business.HouseContactFragment;
import com.sky.house.home.HouseLoginFragment;
import com.sky.house.interfaces.ScrollViewListener;
import com.sky.house.map.HouseMapActivity;
import com.sky.house.report.HouseReportFragment;
import com.sky.house.widget.MyGridView;
import com.sky.house.widget.ObservableScrollView;
import com.sky.widget.SHDialog;
import com.sky.widget.SHToast;
import com.sky.widget.sweetdialog.SweetDialog;

/**
 * 房屋详情
 * 
 * @author skypan
 * 
 */
public class HouseDetailFragment extends BaseFragment implements ITaskListener {

	@ViewInit(id = R.id.sv_detail)
	private ObservableScrollView mSvDetail;

	@ViewInit(id = R.id.ll_bottom)
	private LinearLayout mLlBottom;

	@ViewInit(id = R.id.tv_index)
	private TextView mTvIndex;

	@ViewInit(id = R.id.ll_feature)
	private LinearLayout mLayout;

	@ViewInit(id = R.id.tv_title)
	private TextView mTvTitle;

	@ViewInit(id = R.id.tv_update_time)
	private TextView mTvUpdateTime;

	@ViewInit(id = R.id.tv_rent)
	private TextView mTvRent;

	@ViewInit(id = R.id.tv_read_times)
	private TextView mTvReadTimes;

	@ViewInit(id = R.id.tv_pay_type)
	private TextView mTvPayType;

	@ViewInit(id = R.id.tv_time_in)
	private TextView mTvTimeIn;

	@ViewInit(id = R.id.tv_square)
	private TextView mTvSquare;

	@ViewInit(id = R.id.tv_auth)
	private TextView mTvAuth;

	@ViewInit(id = R.id.tv_floor)
	private TextView mTvFloor;

	@ViewInit(id = R.id.tv_des)
	private TextView mTvDes;

	@ViewInit(id = R.id.tv_xiaoqu)
	private TextView mTvXiaoQu;

	@ViewInit(id = R.id.gv_electrical)
	private MyGridView mGvElectrical;

	@ViewInit(id = R.id.gv_funi)
	private MyGridView mGvFuni;

	@ViewInit(id = R.id.tv_area)
	private TextView mTvArea;

	@ViewInit(id = R.id.tv_address)
	private TextView mTvAddress;

	@ViewInit(id = R.id.ll_same,onClick = "onClick")
	private LinearLayout mLlSame;

	@ViewInit(id = R.id.iv_map,onClick = "onClick")
	private ImageView mIvMap;

	@ViewInit(id = R.id.btn_collect, onClick = "onClick")
	private Button mBtnCollect;

	@ViewInit(id = R.id.btn_contact, onClick = "onClick")
	private Button mBtnContact;

	@ViewInit(id = R.id.ll_xianzhi)
	private LinearLayout mLlXianzhi;

	@ViewInit(id = R.id.tv_xianzhi)
	private TextView mTvXianzhi;

	@ViewInit(id = R.id.pager_banner)
	private ViewPager mPagerView_TopAdvert;

	@ViewInit(id = R.id.tv_same)
	private TextView mTvSame;

	@ViewInit(id = R.id.tv_num)
	private TextView mTvNum;

	private SHPostTaskM detailTask, collectTask;

	private JSONObject json;// 详情对象

	private JSONArray jsonArray;// 图片数组

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				HouseDetailFragment.this.setTopAdv();
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
		mDetailTitlebar.setLeftButton(R.drawable.ic_back, "更多房源", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mDetailTitlebar.setTitle(getActivity().getIntent().getStringExtra("name"));
		mDetailTitlebar.setRightButton1("举报", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(CommonUtil.isEmpty(SHEnvironment.getInstance().getSession())){
					Intent intent = new Intent(getActivity(),SHContainerActivity.class);
					intent.putExtra("class", HouseLoginFragment.class.getName());
					startActivity(intent);
					return;
				}
				Intent intent = new Intent(getActivity(),SHContainerActivity.class);
				intent.putExtra("class", HouseReportFragment.class.getName());
				intent.putExtra("id", json.optInt("houseDetailId"));
				startActivity(intent);
			}
		});
		mDetailTitlebar.setRightButton2("分享", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showShare(false, null, false);
			}
		});
		mSvDetail.setOnScrollListener(new ScrollViewListener() {

			@Override
			public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
				// TODO Auto-generated method stub
				if (y < mLlBottom.getHeight() * 2) {
					mLlBottom.setTranslationY(-y / 2);
				} else {
					mLlBottom.setTranslationY(-mLlBottom.getHeight() + 1);
				}
			}
		});
		mPagerView_TopAdvert.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, CommonUtil.Window.getWidth() / 5 * 3));
		requestDetail();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_house_detail, container, false);
		return view;
	}

	private void requestDetail() {
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		detailTask = new SHPostTaskM();
		detailTask.setListener(this);
		detailTask.setUrl(ConfigDefinition.URL + "GetHouseDetail");
		detailTask.getTaskArgs().put("houseDetailId", getActivity().getIntent().getIntExtra("id", -1));
		detailTask.start();
	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_collect:
			if (isOn()) {
				SHDialog.ShowProgressDiaolg(getActivity(), null);
				collectTask = new SHPostTaskM();
				collectTask.setListener(this);
				collectTask.setUrl(ConfigDefinition.URL + "AddUserHouseCollect");
				try {
					collectTask.getTaskArgs().put("houseDetailId", json.getInt("houseDetailId"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				collectTask.start();
			}
			break;
		case R.id.btn_contact:
			if(json.optInt("isLord") == 1){
				SHToast.showToast(getActivity(), "无法联系自己");
				return;
			}
			if(CommonUtil.isEmpty(SHEnvironment.getInstance().getSession())){
				Intent intent = new Intent(getActivity(),SHContainerActivity.class);
				intent.putExtra("class", HouseLoginFragment.class.getName());
				startActivity(intent);
				return;
			}
			Intent intent = new Intent(getActivity(), SHContainerActivity.class);
			intent.putExtra("class", HouseContactFragment.class.getName());
			intent.putExtra("name", getActivity().getIntent().getStringExtra("name"));
			try {
				intent.putExtra("id", json.getInt("houseDetailId"));
				intent.putExtra("isTrade", json.getInt("isTrade"));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			startActivity(intent);
//			try {
//				// 自己不能联系自己
//				contactTask = new SHPostTaskM();
//				contactTask.setListener(this);
//				contactTask.setUrl(ConfigDefinition.URL+"GetLandlordDetail");
//				contactTask.getTaskArgs().put("houseDetailId", json.getInt("houseDetailId"));
//				contactTask.start();
//
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			break;
		case R.id.iv_map:
			Intent intent_map = new Intent(getActivity(),HouseMapActivity.class);
			intent_map.putExtra("Lng", json.optDouble("lng"));
			intent_map.putExtra("Lat", json.optDouble("lat"));
			intent_map.putExtra("name", json.optString("address"));
			startActivity(intent_map);
			break;
		case R.id.ll_same:
			Intent intent_same = new Intent(getActivity(),SHContainerActivity.class);
			intent_same.putExtra("class", HouseListFragment.class.getName());
			intent_same.putExtra("from", "map");
			try {
				intent_same.putExtra("zoneId", json.getInt("houseZoneId"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			startActivity(intent_same);
			break;
		}
	}

	/**
	 * 是否登录
	 * 
	 * @return
	 */
	private boolean isOn() {
		if (!CommonUtil.isEmpty(SHEnvironment.getInstance().getSession())) {
			return true;
		}
		Intent intent = new Intent(getActivity(), SHContainerActivity.class);
		intent.putExtra("class", HouseLoginFragment.class.getName());
		startActivity(intent);
		return false;
	}

	private void setTopAdv() {
		mTvIndex.setText("1/" + jsonArray.length());
		TopAdvertPagerAdapter adapter = new TopAdvertPagerAdapter(getActivity(), jsonArray, TopAdvertPagerAdapter.FLAG_HOUSE_IMG);
		mPagerView_TopAdvert.setAdapter(adapter);
		mPagerView_TopAdvert.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				mTvIndex.setText((arg0 + 1) + "/" + jsonArray.length());
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

	private void setData() {
		try {
			
			if(json.getInt("isTrade") == 1){
				mLlBottom.setVisibility(View.GONE);
			}else{
				mLlBottom.setVisibility(View.VISIBLE);
			}
			
			mTvTitle.setText(json.getString("houseTitle"));
			mTvUpdateTime.setText(json.getString("updateTime"));
			mTvRent.setText(json.getString("rentAmt"));
			mTvPayType.setText(json.getString("payTypeName"));
			mTvTimeIn.setText(json.getString("inTime"));
			mTvSquare.setText(json.getString("area"));
			mTvFloor.setText(json.getString("floor"));
			mTvDes.setText(json.getString("memo"));
			mTvXiaoQu.setText(json.getString("zoneName"));
			mTvArea.setText(json.getString("zoneArea"));
			mTvAddress.setText(json.getString("address"));
			mTvReadTimes.setText(json.getString("browseCount"));
			mTvSame.setText(json.getString("zoneName")+" 同小区房源");
			mTvNum.setText(json.getString("otherCount"));
			if(json.getInt("rentType") == 2){
				mLlXianzhi.setVisibility(View.VISIBLE);
				mTvXianzhi.setText(json.getString("genderType"));
			}
			ImageLoaderUtil.displayImage(json.optString("addressImgUrl"), mIvMap);
			JSONArray teseArray = json.getJSONArray("houseFeature");
			String[] items_tese = getActivity().getResources().getStringArray(R.array.array_tese);
			for (int i = 0; i < teseArray.length(); i++) {
				TextView tv = new TextView(getActivity());
				tv.setTextSize(12);
				tv.setPadding(5, 1, 5, 1);
				tv.setText(items_tese[teseArray.getInt(i)]);
				LinearLayout.LayoutParams lay = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lay.setMargins(0, 0, 10, 0);
				tv.setLayoutParams(lay);
				tv.setTextColor(getActivity().getResources().getColor(R.color.color_black));
				switch (i) {
				case 0:
					tv.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.stroke_yellow_zhi));
					break;
				case 1:
					tv.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.stroke_green_zhi));
					break;
				case 2:
					tv.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.stroke_red_zhi));
					break;
				case 3:
					tv.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.stroke_gray_zhi));
					break;
				case 4:
					tv.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.stroke_slate_zhi));
					break;
				case 5:
					tv.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.stroke_orange_zhi));
					break;
				case 6:
					tv.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.stroke_purple_zhi));
					break;
				case 7:
					tv.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.stroke_blue_zhi));
					break;
				}
				mLayout.addView(tv);
			}
			String[] funi = getActivity().getResources().getStringArray(R.array.array_funi);
			// 家电
			JSONArray eleArray = json.getJSONArray("electricalFacilityList");
			String[] ele = new String[eleArray.length()];
			for (int i = 0; i < eleArray.length(); i++) {
				ele[i] = funi[eleArray.getInt(i)];
			}
			mGvElectrical.setAdapter(new GridAdapter(getActivity(), ele, false,GridAdapter.FLAG_ELE));
			// 家具
			JSONArray funiArray = json.getJSONArray("furnitureList");
			String[] fu = new String[funiArray.length()];
			for (int i = 0; i < funiArray.length(); i++) {
				fu[i] = funi[funiArray.getInt(i)];
			}
			mGvFuni.setAdapter(new GridAdapter(getActivity(), fu, false,GridAdapter.FLAG_FUNI));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		if (task == detailTask) {
			json = (JSONObject) task.getResult();
			setData();
			jsonArray = json.getJSONArray("imgUrlList");
			if (jsonArray.length() != 0) {
				mHandler.sendEmptyMessage(0);
			}
		} else if (task == collectTask) {
			SHToast.showToast(getActivity(), "收藏成功", 1000);
		}
	}

	@Override
	public void onTaskFailed(SHTask task) {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		new SweetDialog(SHApplication.getInstance().getCurrentActivity(), SweetDialog.ERROR_TYPE).setTitleText("提示").setContentText(task.getRespInfo().getMessage()).show();
	}

	@Override
	public void onTaskUpdateProgress(SHTask task, int count, int total) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTaskTry(SHTask task) {
		// TODO Auto-generated method stub

	}

	private void showShare(boolean silent, String platform, boolean captureView) {
		final OnekeyShare oks = new OnekeyShare();

		// oks.setAddress("12345678901");
		oks.setTitle("阳关租房");
		//		oks.setTitleUrl("http://mob.com");
		oks.setText("我正在使用《阳光租房》App哦～ \nhttp://wap.koudaitong.com/v2/showcase/mpnews?alias=j8pm3up1");

		// oks.setImagePath(CustomShareFieldsPage.getString("imagePath",
		// MainActivity.TEST_IMAGE));
		//		oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg");
		// oks.setImageArray(new String[]{MainActivity.TEST_IMAGE,
		// MainActivity.TEST_IMAGE_URL});

		//		oks.setUrl("http://www.mob.com");
		//		oks.setFilePath(CustomShareFieldsPage.getString("filePath", MainActivity.TEST_IMAGE));
		//		oks.setComment(CustomShareFieldsPage.getString("comment", context.getString(R.string.share)));
		//		oks.setSite(getActivity().getResources().getString(R.string.app_name));
		//		oks.setSiteUrl("http://mob.com");
		// oks.setVenueName(CustomShareFieldsPage.getString("venueName",
		// "ShareSDK"));
		// oks.setVenueDescription(CustomShareFieldsPage.getString("venueDescription",
		// "This is a beautiful place!"));
		// oks.setLatitude(23.056081f);
		// oks.setLongitude(113.385708f);
		oks.setSilent(silent);
		// oks.setShareFromQQAuthSupport(shareFromQQLogin);
		oks.setTheme(OnekeyShareTheme.CLASSIC);

		if (platform != null) {
			oks.setPlatform(platform);
		}

		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();

		// 在自动授权时可以禁用SSO方式
		// if(!CustomShareFieldsPage.getBoolean("enableSSO", true))
		oks.disableSSOWhenAuthorize();

		// 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
		oks.setCallback(new OneKeyShareCallback());

		// 为EditPage设置一个背景的View
		//		oks.setEditPageBackground(getPage());
		oks.show(getActivity());
	}
}
