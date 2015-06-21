package com.sky.house.resource.publish;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;
import com.sky.widget.SHToast;

/**
 * 填写房源地址
 * 
 * @author skypan
 * 
 */
public class HouseAddressFragment extends BaseFragment {

	@ViewInit(id = R.id.tv_xiaoqu, onClick = "onClick")
	private TextView mTvAddress;

	@ViewInit(id = R.id.et_louhao)
	private EditText mEtLouHao;

	@ViewInit(id = R.id.et_danyuan)
	private EditText mEtDanYuan;

	@ViewInit(id = R.id.et_menpaihao)
	private EditText mEtMenPaiHao;

	@ViewInit(id = R.id.et_floor)
	private EditText mEtFloor;

	@ViewInit(id = R.id.et_floor_all)
	private EditText mEtFloorAll;

	private JSONObject json;// 存储当前界面的参数

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("房源地址");
		mDetailTitlebar.setRightButton1("确定", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (validate()) {
					try {
						json.put("buildingNum", mEtLouHao.getText().toString().trim());// 楼号
						json.put("unitNum", mEtDanYuan.getText().toString().trim());// 单元号
						json.put("menpaiNum", mEtMenPaiHao.getText().toString().trim());// 门牌号
						json.put("currentFloor", Integer.valueOf(mEtFloor.getText().toString().trim()));// 层数
						json.put("totalFloor", Integer.valueOf(mEtFloorAll.getText().toString().trim()));// 总层数
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
		View view = inflater.inflate(R.layout.fragment_house_address, container, false);
		return view;
	}

	private boolean validate() {
		if (CommonUtil.isEmpty(mTvAddress.getText().toString().trim())) {
			SHToast.showToast(getActivity(), "请选择小区");
			return false;
		}
		if (CommonUtil.isEmpty(mEtLouHao.getText().toString().trim())) {
			SHToast.showToast(getActivity(), "请输入楼号");
			return false;
		}
//		if (CommonUtil.isEmpty(mEtDanYuan.getText().toString().trim())) {
//			SHToast.showToast(getActivity(), "请输入单元号");
//			return false;
//		}
		if (CommonUtil.isEmpty(mEtMenPaiHao.getText().toString().trim())) {
			SHToast.showToast(getActivity(), "请输入门牌号");
			return false;
		}
		if (CommonUtil.isEmpty(mEtFloor.getText().toString().trim())) {
			SHToast.showToast(getActivity(), "请输入所在楼层");
			return false;
		}
		if (CommonUtil.isEmpty(mEtFloorAll.getText().toString().trim())) {
			SHToast.showToast(getActivity(), "请输入总楼层");
			return false;
		}
		return true;
	}

	private void initData() {
		mTvAddress.setText(json.optString("houseZoneName"));
		mEtLouHao.setText(json.optString("buildingNum"));
		mEtDanYuan.setText(json.optString("unitNum"));
		mEtMenPaiHao.setText(json.optString("menpaiNum"));
		if (json.has("currentFloor")) {
			mEtFloor.setText(json.optInt("currentFloor") + "");
		}
		if (json.has("totalFloor")) {
			mEtFloorAll.setText(json.optInt("totalFloor") + "");
		}
	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_xiaoqu:
			Intent intent = new Intent(getActivity(), SHContainerActivity.class);
			intent.putExtra("class", HouseSelectCommunityFragment.class.getName());
			startActivityForResult(intent, 0);
			getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && data != null) {
			mTvAddress.setText(data.getStringExtra("houseZoneName"));
			try {
				json.put("cityName", data.getStringExtra("city"));
				json.put("districtName", data.getStringExtra("district"));
				json.put("houseZoneName", data.getStringExtra("houseZoneName"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
