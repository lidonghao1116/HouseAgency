package com.sky.house.resource;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.house.adapter.HouseListAdapter;
import com.sky.house.adapter.OptionAdapter;
import com.sky.house.entity.MenuItem;
import com.sky.house.interfaces.CascadingMenuViewOnSelectListener;
import com.sky.house.resource.filter.HouseFilterFragment;
import com.sky.house.widget.CascadingMenuPopWindow;
import com.sky.house.widget.SHListView;
import com.sky.widget.SHDialog;
import com.sky.widget.sweetdialog.SweetDialog;

/**
 * 房源
 * 
 * @author skypan
 * 
 */
public class HouseListFragment extends BaseFragment implements ITaskListener {

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

	private SHPostTaskM areaTask, houseListTask;

	@ViewInit(id = R.id.lv_house)
	private SHListView mLvHouse;

	private HouseListAdapter listAdapter;

	private JSONArray jsonArray;// 房源数组

	private JSONArray areaArray;// 区域数组

	private int pageNum = 1;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setRightButton1("发布房源", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SHContainerActivity.class);
				intent.putExtra("class", HousePublishFragment.class.getName());
				startActivity(intent);
			}
		});

		view.findViewById(R.id.btn_test).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SHContainerActivity.class);
				intent.putExtra("class", HouseDetailFragment.class.getName());
				startActivity(intent);
			}
		});

		requestArea();
		requestHouseList();

		// 假数据
		// ArrayList<MenuItem> tempMenuItems = null;
		// for (int j = 0; j < 2; j++) {
		// tempMenuItems = new ArrayList<MenuItem>();
		// for (int i = 0; i < 15; i++) {
		// tempMenuItems.add(new MenuItem(false, "子菜单" + j + "" + i, null));
		// }
		// menuItems.add(new MenuItem(true, "区域" + j, tempMenuItems));
		// }

		final String[] items_near = getResources().getStringArray(R.array.array_near);
		ArrayList<MenuItem> tempMenuItems = new ArrayList<MenuItem>();
		for (int i = 0; i < items_near.length; i++) {
			tempMenuItems.add(new MenuItem(false, items_near[i], null));
		}
		menuItems.add(new MenuItem(true,"附近",tempMenuItems));

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_source, container, false);
		return view;
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
			startActivity(intent);
			break;
		}
	}

	private void requestArea() {
		areaTask = new SHPostTaskM();
		areaTask.setListener(this);
		areaTask.setUrl(ConfigDefinition.URL + "getcountybycityid");
		areaTask.getTaskArgs().put("cityid", getActivity().getIntent().getIntExtra("cityId", -1));
		// areaTask.getTaskArgs().put("cityid", 1);
		areaTask.start();
	}

	private void requestHouseList() {
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		houseListTask = new SHPostTaskM();
		houseListTask.setListener(this);
		houseListTask.setUrl(ConfigDefinition.URL + "SearchHouse");
		houseListTask.getTaskArgs().put("cityId", getActivity().getIntent().getIntExtra("cityId", -1));
		houseListTask.getTaskArgs().put("pageSize", 20);
		houseListTask.getTaskArgs().put("pageIndex", pageNum);
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
			Toast.makeText(getActivity(), "" + menuItem.toString(), 1000).show();
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
			ListView lv = (ListView) view.findViewById(R.id.lv_option);
			mAdapterRent = new OptionAdapter(getActivity());
			lv.setAdapter(mAdapterRent);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					mPopRent.dismiss();
					mTvRent.setText("1000-3000");
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
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		JSONObject json = (JSONObject) task.getResult();
		if (task == areaTask) {
			areaArray = json.getJSONArray("Counties");
			ArrayList<MenuItem> tempMenuItems = new ArrayList<MenuItem>();
			for (int i = 0; i < areaArray.length(); i++) {
				tempMenuItems.add(new MenuItem(false, areaArray.getJSONObject(i).getString("name"), null));
			}
			menuItems.add(new MenuItem(true,"区域",tempMenuItems));
		} else if (task == houseListTask) {
			jsonArray = CommonUtil.combineArray(jsonArray, json.getJSONArray("rentHouseList"));
			if (listAdapter == null) {
				listAdapter = new HouseListAdapter(getActivity(), HouseListAdapter.FLAG_HOUSE_LIST, jsonArray);
			}
			mLvHouse.setTotalNum(json.getInt("recordCount"));
			mLvHouse.setAdapter(listAdapter);
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
