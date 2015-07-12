package com.sky.house.resource.publish;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;
import com.sky.house.adapter.GridAdapter;
import com.sky.house.widget.MyGridView;
import com.sky.widget.SHToast;

/**
 * 发布时填写房屋详情
 * 
 * @author skypan
 * 
 */
public class HousePublishDetailFragment extends BaseFragment {

	@ViewInit(id = R.id.ll_room, onClick = "onClick")
	private LinearLayout mLlRoom;

	@ViewInit(id = R.id.ll_sex, onClick = "onClick")
	private LinearLayout mLlSex;

	@ViewInit(id = R.id.ll_fixture, onClick = "onClick")
	private LinearLayout mLlFixture;

	@ViewInit(id = R.id.ll_shi)
	private LinearLayout mLlShi;

	@ViewInit(id = R.id.label_square)
	private TextView mTvSquare;

	@ViewInit(id = R.id.tv_room)
	private TextView mTvRoom;

	@ViewInit(id = R.id.tv_sex)
	private TextView mTvSex;

	@ViewInit(id = R.id.tv_fixture)
	private TextView mTvFixture;

	@ViewInit(id = R.id.et_shi)
	private EditText mEtShi;

	@ViewInit(id = R.id.et_ting)
	private EditText mEtTing;

	@ViewInit(id = R.id.et_wei)
	private EditText mEtWei;

	@ViewInit(id = R.id.et_square)
	private EditText mEtSquare;

	@ViewInit(id = R.id.gv_electrical)
	private MyGridView mGvElectrical;

	@ViewInit(id = R.id.gv_furniture)
	private MyGridView mGvFuniture;

	private String[] facilities;

	private JSONObject json;// 存储界面数据

	String[] items_fixture_all;// 装修情况数组
	String[] items_room;// 合租类型：主卧 次卧
	String[] items_gender;// 性别限制

	private GridAdapter eleAdapter, funiAdapter;

	private JSONArray eleArray = new JSONArray();// 已选的家电数组，从前面界面带过来展示
	private JSONArray funiArray = new JSONArray();

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("房屋详情");
		mDetailTitlebar.setRightButton1("确定", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (validate()) {
					try {
						if (!"more".equals(getActivity().getIntent().getStringExtra("from"))) {
							json.put("room", Integer.valueOf(mEtShi.getText().toString().trim()));
							json.put("office", Integer.valueOf(mEtTing.getText().toString().trim()));
							json.put("toilet", Integer.valueOf(mEtWei.getText().toString().trim()));
						}
						json.put("area", mEtSquare.getText().toString().trim());
						if (!json.has("fitment")) {
							json.put("fitment", 3);// 中等装修
						}
						if(!json.has("rentType")){
							json.put("rentType", 1);//默认主卧
						}
						json.put("HouseFitmentList", eleAdapter.getSelectedArray());
						json.put("HouseDeviceList", funiAdapter.getSelectedArray());
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent intent = new Intent();
					intent.putExtra("json", json.toString());
					getActivity().setResult(Activity.RESULT_OK, intent);
					finish();
				}
			}
		});

		if ("more".equals(getActivity().getIntent().getStringExtra("from"))) {
			mLlShi.setVisibility(View.GONE);
		}

		items_fixture_all = getResources().getStringArray(R.array.array_fixture);
		items_room = getResources().getStringArray(R.array.array_room);
		items_gender = getResources().getStringArray(R.array.array_sex);
		facilities = getResources().getStringArray(R.array.array_funi);
		String[] items_ele = new String[ConfigDefinition.ELE_NUM];
		String[] items_funi = new String[ConfigDefinition.FUNI_NUM];
		// 1-9是家电 20以后是家具
		for (int i = 0; i < facilities.length; i++) {
			if (i >= 1 && i <= ConfigDefinition.ELE_NUM) {
				items_ele[i - 1] = facilities[i];
			}
		}
		for (int i = 0; i < facilities.length; i++) {
			if (i >= 20) {
				items_funi[i - 20] = facilities[i];
			}
		}
		eleAdapter = new GridAdapter(getActivity(), items_ele, true, eleArray, GridAdapter.FLAG_ELE);
		funiAdapter = new GridAdapter(getActivity(), items_funi, true, funiArray, GridAdapter.FLAG_FUNI);
		mGvElectrical.setAdapter(eleAdapter);
		mGvFuniture.setAdapter(funiAdapter);
		if (getActivity().getIntent().getIntExtra("type_rent", 0) == 0) {
			mLlRoom.setVisibility(View.GONE);
			mLlSex.setVisibility(View.GONE);
			mTvSquare.setText("面积：");
		} else {
			mLlRoom.setVisibility(View.VISIBLE);
			mLlSex.setVisibility(View.VISIBLE);
			mTvSquare.setText("房间面积：");
		}
		try {
			json = new JSONObject(getActivity().getIntent().getStringExtra("json"));
			initData();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_detail_publish, container, false);
		return view;
	}

	private void initData() {
		if (json.has("room")) {
			mEtShi.setText(json.optInt("room") + "");
		}
		if (json.has("office")) {
			mEtTing.setText(json.optInt("office") + "");
		}
		if (json.has("toilet")) {
			mEtWei.setText(json.optInt("toilet") + "");
		}
		if (json.has("area")) {
			mEtSquare.setText(json.optString("area") + "");
		}
		if (json.has("fitment")) {
			mTvFixture.setText(items_fixture_all[json.optInt("fitment")]);
		}
		if (json.has("HouseFitmentList")) {
			eleArray = json.optJSONArray("HouseFitmentList");
			eleAdapter.setJsonArray(eleArray);
			eleAdapter.notifyDataSetChanged();
		}
		if (json.has("HouseDeviceList")) {
			funiArray = json.optJSONArray("HouseDeviceList");
			funiAdapter.setJsonArray(funiArray);
			funiAdapter.notifyDataSetChanged();
		}
		if (json.has("shareNumber")) {
			mTvRoom.setText(items_room[json.optInt("shareNumber") - 1]);
		} else {
			try {
				json.put("shareNumber", 1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (json.has("genderType")) {
			mTvSex.setText(items_gender[json.optInt("genderType")]);
		} else {
			try {
				json.put("genderType", 0);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			if (json.getInt("roomNum") > 1) {
				mEtShi.setEnabled(false);
				mEtTing.setEnabled(false);
				mEtWei.setEnabled(false);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean validate() {

		if ("more".equals(getActivity().getIntent().getStringExtra("from"))) {
			if (CommonUtil.isEmpty(mEtSquare.getText().toString().trim())) {
				SHToast.showToast(getActivity(), "请先完善信息");
				return false;
			}
		} else {
			if (CommonUtil.isEmpty(mEtShi.getText().toString().trim()) || CommonUtil.isEmpty(mEtTing.getText().toString().trim()) || CommonUtil.isEmpty(mEtWei.getText().toString().trim())
					|| CommonUtil.isEmpty(mEtSquare.getText().toString().trim())) {
				SHToast.showToast(getActivity(), "请先完善信息");
				return false;
			}
		}
		return true;
	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_room:
			new AlertDialog.Builder(getActivity()).setTitle("合租类型").setItems(items_room, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int witch) {
					// TODO Auto-generated method stub
					mTvRoom.setText(items_room[witch]);
					try {
						json.put("shareNumber", witch + 1);
						json.put("rentType", witch + 1);//里面的renttype表示主卧／次卧。。
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).show();
			break;
		case R.id.ll_sex:
			final String[] items_sex = getResources().getStringArray(R.array.array_sex);
			new AlertDialog.Builder(getActivity()).setTitle("性别限制").setItems(items_sex, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int witch) {
					// TODO Auto-generated method stub
					mTvSex.setText(items_sex[witch]);
					try {
						json.put("genderType", witch);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).show();
			break;
		case R.id.ll_fixture:
			String[] items_fixture = new String[items_fixture_all.length - 1];
			for (int i = 0; i < items_fixture_all.length; i++) {
				if (i != 0) {
					items_fixture[i - 1] = items_fixture_all[i];
				}
			}
			new AlertDialog.Builder(getActivity()).setTitle("装修类型").setItems(items_fixture, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int witch) {
					// TODO Auto-generated method stub
					mTvFixture.setText(items_fixture_all[witch + 1]);
					try {
						json.put("fitment", witch + 1);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).show();
			break;
		}
	}
}
