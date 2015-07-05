package com.sky.house.resource;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ToggleButton;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ViewInit;
import com.eroad.base.util.location.Location;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.next.util.SHEnvironment;
import com.sky.house.R;
import com.sky.house.adapter.HouseGridAdapter;
import com.sky.house.adapter.HouseListAdapter;
import com.sky.house.adapter.OptionAdapter;
import com.sky.house.entity.MenuItem;
import com.sky.house.home.HouseLoginFragment;
import com.sky.house.interfaces.CascadingMenuViewOnSelectListener;
import com.sky.house.me.HouseAuthenticationFragment;
import com.sky.house.resource.filter.HouseFilterFragment;
import com.sky.house.widget.CascadingMenuPopWindow;
import com.sky.house.widget.SHGridView;
import com.sky.house.widget.SHGridView.OnGridLoadMoreListener;
import com.sky.house.widget.SHListView;
import com.sky.house.widget.SHListView.OnLoadMoreListener;
import com.sky.widget.SHDialog;
import com.sky.widget.SHEditText;
import com.sky.widget.SHToast;
import com.sky.widget.sweetdialog.SweetDialog;

/**
 * 房源
 * 
 * @author skypan
 * 
 */
public class HouseListFragment extends BaseFragment implements ITaskListener, OnLoadMoreListener ,OnGridLoadMoreListener{

	private ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
	private PopupWindow mPopRent;
	private OptionAdapter mAdapterRent;

	private CascadingMenuPopWindow cascadingMenuPopWindow = null;

	@ViewInit(id = R.id.tv_rent, onClick = "onClick")
	private TextView mTvRent;

	@ViewInit(id = R.id.tv_area, onClick = "onClick")
	private TextView mTvArea;

	@ViewInit(id = R.id.tv_filter, onClick = "onClick")
	private TextView mTvFilter;

	@ViewInit(id = R.id.ll_search)
	private LinearLayout mLlSearch;

	@ViewInit(id = R.id.et_keyword)
	private SHEditText mEtKeyword;

	private SHPostTaskM areaTask, houseListTask;

	@ViewInit(id = R.id.lv_house)
	private SHListView mLvHouse;
	
	@ViewInit(id = R.id.gv_house)
	private SHGridView mGvHouse;
	
	@ViewInit(id = R.id.tb_layout)
	private ToggleButton mTbLayout;

	private HouseListAdapter listAdapter;
	
	private HouseGridAdapter gridAdapter;

	private JSONArray jsonArray;// 房源数组

	private JSONArray areaArray;// 区域数组

	private int pageNum = 1;

	private int countyId;// 区域id

	private int distance;// 距离

	private int minRent;// 最低租金

	private int maxRent;// 最高租金

	private int roomNum;// 几室

	private int rentType;// 出租类型：1:整组／2:合租

	private int houseFeature;// 房源特色

	private int fitment;// 装修

	private int houseType;// 房屋类型：公寓、住宅、床位等等

	String[] items_money;
	
	private int searchType = 1;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		if ("map".equals(getActivity().getIntent().getStringExtra("from"))) {
			searchType = 3;
			mLlSearch.setVisibility(View.INVISIBLE);
			mDetailTitlebar.setTitle("房源列表");
		} else {
			mLlSearch.setVisibility(View.VISIBLE);
			mDetailTitlebar.setRightButton1("发布房源", new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
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
					Intent intent = new Intent(getActivity(), SHContainerActivity.class);
					intent.putExtra("class", HousePublishFragment.class.getName());
					startActivity(intent);
				}
			});
		}
		rentType = getActivity().getIntent().getIntExtra("rentType", 0);
		setListeners();
		requestArea();
		requestHouseList();

		items_money = getResources().getStringArray(R.array.array_money);

		if (Location.getInstance().getCityId() == Location.getInstance().getSelectedCityId()) {
			final String[] items_near = getResources().getStringArray(R.array.array_near);
			ArrayList<MenuItem> tempMenuItems = new ArrayList<MenuItem>();
			for (int i = 0; i < items_near.length; i++) {
				tempMenuItems.add(new MenuItem(false, items_near[i], 0, i, null));
			}
			menuItems.add(new MenuItem(true, "附近", tempMenuItems));
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_source, container, false);
		return view;
	}

	private void setListeners() {
		mLvHouse.setOnLoadMoreListener(this);
		mGvHouse.setOnGridLoadMoreListener(this);
		mEtKeyword.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH && !CommonUtil.isEmpty(mEtKeyword.getText().toString().trim())) {
					CommonUtil.InputTools.HideKeyboard(mEtKeyword);
					reset();
					jsonArray = new JSONArray();
					pageNum = 1;
					searchType = 5;
					requestHouseList();
					return true;
				}
				return false;
			}
		});
		mLvHouse.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SHContainerActivity.class);
				intent.putExtra("class", HouseDetailFragment.class.getName());
				try {
					intent.putExtra("id", jsonArray.getJSONObject(arg2).getInt("houseDetailId"));
					intent.putExtra("name", jsonArray.getJSONObject(arg2).getString("houseName"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivity(intent);
			}
		});
		
		mGvHouse.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SHContainerActivity.class);
				intent.putExtra("class", HouseDetailFragment.class.getName());
				try {
					intent.putExtra("id", jsonArray.getJSONObject(arg2).getInt("houseDetailId"));
					intent.putExtra("name", jsonArray.getJSONObject(arg2).getString("houseName"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivity(intent);
			}
		});
		mTbLayout.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(!arg1){
					mLvHouse.setVisibility(View.GONE);
					mGvHouse.setVisibility(View.VISIBLE);
				}else{
					mGvHouse.setVisibility(View.GONE);
					mLvHouse.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_rent:
			showRentPopupWindow();
			break;
		case R.id.tv_area:
			showPopMenu();
			break;
		case R.id.tv_filter:
			Intent intent = new Intent(getActivity(), SHContainerActivity.class);
			intent.putExtra("class", HouseFilterFragment.class.getName());
			intent.putExtra("roomNum", roomNum);
			intent.putExtra("houseFeature", houseFeature);
			intent.putExtra("fitment", fitment);
			intent.putExtra("houseType", houseType);
			startActivityForResult(intent, 0);
			getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
			break;
		}
	}

	private void requestArea() {
		areaTask = new SHPostTaskM();
		areaTask.setListener(this);
		areaTask.setUrl(ConfigDefinition.URL + "getcountybycityid");
		areaTask.getTaskArgs().put("cityid", Location.getInstance().getSelectedCityId());
		// areaTask.getTaskArgs().put("cityid", 1);
		areaTask.start();
	}

	// 重置
	private void reset() {
		minRent = 0;
		maxRent = 0;
		countyId = 0;
		distance = 0;
		houseFeature = 0;
		fitment = 0;
		houseType = 0;
	}

	private void requestHouseList() {
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		houseListTask = new SHPostTaskM();
		houseListTask.setListener(this);
		houseListTask.setUrl(ConfigDefinition.URL + "SearchHouse");
		houseListTask.getTaskArgs().put("cityId", Location.getInstance().getSelectedCityId());
		houseListTask.getTaskArgs().put("searchType", searchType);
		if(searchType == 3){
			houseListTask.getTaskArgs().put("houseZoneId", getActivity().getIntent().getIntExtra("zoneId", -1));
		}
		houseListTask.getTaskArgs().put("pageSize", 20);
		houseListTask.getTaskArgs().put("pageIndex", pageNum);
		houseListTask.getTaskArgs().put("cityId", Location.getInstance().getSelectedCityId());
		houseListTask.getTaskArgs().put("houseZoneName", mEtKeyword.getText().toString().trim());
		if (minRent != 0) {
			houseListTask.getTaskArgs().put("minRent", minRent);
		}
		if (maxRent != 0) {
			houseListTask.getTaskArgs().put("maxRent", maxRent);
		}
		houseListTask.getTaskArgs().put("roomNum", roomNum);// 居室
		if (rentType != 0) {
			houseListTask.getTaskArgs().put("rentType", rentType);
		}
		if (countyId != 0) {// 要么传区域id要么传距离//按区域/按距离
			houseListTask.getTaskArgs().put("countyId", countyId);
		} else if (distance != 0) {
			houseListTask.getTaskArgs().put("distance", distance);
		}
		houseListTask.getTaskArgs().put("houseFeature", houseFeature);
		houseListTask.getTaskArgs().put("fitment", fitment);
		houseListTask.getTaskArgs().put("houseType", houseType);
		houseListTask.start();
	}

	private void showPopMenu() {
		mTvArea.setTextAppearance(getActivity(), R.style.TextMidGreen);
		Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_soild_up);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		mTvArea.setCompoundDrawables(null, null, drawable, null);
		if (cascadingMenuPopWindow == null) {
			cascadingMenuPopWindow = new CascadingMenuPopWindow(getActivity(), menuItems);
			cascadingMenuPopWindow.setMenuViewOnSelectListener(new NMCascadingMenuViewOnSelectListener());
		}
		cascadingMenuPopWindow.setBackgroundDrawable(getResources().getDrawable(R.color.color_full_transparent));
		cascadingMenuPopWindow.setFocusable(true);
		// 设置允许在外点击消失
		cascadingMenuPopWindow.setOutsideTouchable(true);
		// 设置显示动画
		cascadingMenuPopWindow.setAnimationStyle(R.style.TypeSelAnimationFade);
		cascadingMenuPopWindow.showAsDropDown(mTvArea, 0, 1);
		cascadingMenuPopWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				mTvArea.setTextAppearance(getActivity(), R.style.TextMidBlack);
				Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_soild_down);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				mTvArea.setCompoundDrawables(null, null, drawable, null);
			}
		});
	}

	// 级联菜单选择回调接口
	class NMCascadingMenuViewOnSelectListener implements CascadingMenuViewOnSelectListener {

		@Override
		public void getValue(MenuItem menuItem) {
			// Toast.makeText(getActivity(), "" + menuItem.toString(),
			// 1000).show();
			if (menuItem.getType() == 0) {
				switch (menuItem.getId()) {
				case 0:
					distance = 1;
					break;
				case 1:
					distance = 2;
					break;
				case 2:
					distance = 3;
					break;
				}
				countyId = 0;
			} else {
				countyId = menuItem.getId();
			}
			jsonArray = new JSONArray();
			pageNum = 1;
			requestHouseList();
		}

	}

	private void showRentPopupWindow() {
		mTvRent.setTextAppearance(getActivity(), R.style.TextMidGreen);
		Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_soild_up);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		mTvRent.setCompoundDrawables(null, null, drawable, null);
		int[] location = new int[2];
		mTvRent.getLocationInWindow(location);
		if (mPopRent == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = layoutInflater.inflate(R.layout.pop_option, null);
			final EditText etStart = (EditText) view.findViewById(R.id.et_start);
			final EditText etEnd = (EditText) view.findViewById(R.id.et_end);
			Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
			btnConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (etStart.getText().toString().trim().length() == 0 && etEnd.getText().toString().trim().length() == 0) {
						SHToast.showToast(getActivity(), "请输入金额");
						return;
					}
					if (etStart.getText().toString().trim().length() == 0) {
						minRent = 0;
					} else {
						minRent = Integer.valueOf(etStart.getText().toString().trim());
					}
					if (etEnd.getText().toString().trim().length() == 0) {
						maxRent = 0;
					} else {
						maxRent = Integer.valueOf(etEnd.getText().toString().trim());
					}
					jsonArray = new JSONArray();
					pageNum = 1;
					requestHouseList();
					mPopRent.dismiss();
				}
			});
			ListView lv = (ListView) view.findViewById(R.id.lv_option);
			mAdapterRent = new OptionAdapter(getActivity(), items_money);
			lv.setAdapter(mAdapterRent);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					mPopRent.dismiss();
					mTvRent.setText(items_money[arg2]);
					switch (arg2) {
					case 0:
						minRent = 0;
						maxRent = 1000;
						break;
					case 1:
						minRent = 1000;
						maxRent = 2000;
						break;
					case 2:
						minRent = 2000;
						maxRent = 3000;
						break;
					case 3:
						minRent = 3000;
						maxRent = 5000;
						break;
					case 4:
						minRent = 5000;
						maxRent = 8000;
						break;
					case 5:
						minRent = 8000;
						maxRent = 0;
						break;
					}
					jsonArray = new JSONArray();
					pageNum = 1;
					requestHouseList();
				}
			});
			// 创建一个PopuWidow对象
			mPopRent = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		// 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		mPopRent.setBackgroundDrawable(getResources().getDrawable(R.color.color_full_transparent));
		// 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
		mPopRent.setFocusable(true);
		// 设置允许在外点击消失
		mPopRent.setOutsideTouchable(true);
		// 设置显示动画
		mPopRent.setAnimationStyle(R.style.TypeSelAnimationFade);
		// 设置菜单显示的位置
		mPopRent.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.NO_GRAVITY, location[0], location[1] + mTvRent.getHeight() + 1);
		mPopRent.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				mTvRent.setTextAppearance(getActivity(), R.style.TextMidBlack);
				Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_soild_down);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				mTvRent.setCompoundDrawables(null, null, drawable, null);
			}
		});
	}

	private void setData() {

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK && data != null) {
			roomNum = data.getIntExtra("roomNum", 0);
			houseFeature = data.getIntExtra("houseFeature", 0);
			fitment = data.getIntExtra("fitment", 0);
			houseType = data.getIntExtra("houseType", 0);
			jsonArray = new JSONArray();
			pageNum = 1;
			requestHouseList();
		}
	}

	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		JSONObject json = (JSONObject) task.getResult();
		if (task == areaTask) {
			areaArray = json.getJSONArray("Counties");
			ArrayList<MenuItem> tempMenuItems = new ArrayList<MenuItem>();
			for (int i = 0; i < areaArray.length(); i++) {
				tempMenuItems.add(new MenuItem(false, areaArray.getJSONObject(i).getString("name"), 1, areaArray.getJSONObject(i).getInt("id"), null));
			}
			menuItems.add(new MenuItem(true, "区域", tempMenuItems));
		} else if (task == houseListTask) {
			jsonArray = CommonUtil.combineArray(jsonArray, json.getJSONArray("rentHouseList"));
			if (listAdapter == null) {
				listAdapter = new HouseListAdapter(getActivity(), HouseListAdapter.FLAG_HOUSE_LIST, jsonArray);
				mLvHouse.setAdapter(listAdapter);
			}
			if (gridAdapter == null) {
				gridAdapter = new HouseGridAdapter(getActivity(), jsonArray);
				mGvHouse.setAdapter(gridAdapter);
			}
			listAdapter.setJsonArray(jsonArray);
			gridAdapter.setJsonArray(jsonArray);
			mLvHouse.setTotalNum(json.getInt("recordCount"));
			mGvHouse.setTotalNum(json.getInt("recordCount"));
			listAdapter.notifyDataSetChanged();
			gridAdapter.notifyDataSetChanged();
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

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		pageNum++;
		requestHouseList();
	}

	@Override
	public void onGridLoadMore() {
		// TODO Auto-generated method stub
		pageNum++;
		requestHouseList();
	}

}
