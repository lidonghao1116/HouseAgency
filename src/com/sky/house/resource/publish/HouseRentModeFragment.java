package com.sky.house.resource.publish;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;
import com.sky.house.widget.CalendarDialog;
import com.sky.house.widget.CalendarDialog.CalendarResultListener;
import com.sky.widget.SHEditText;
import com.sky.widget.SHToast;

/**
 * 租金方式
 * 
 * @author skypan
 * 
 */
public class HouseRentModeFragment extends BaseFragment {

	@ViewInit(id = R.id.ll_time, onClick = "onClick")
	private LinearLayout mLlTime;

	@ViewInit(id = R.id.tv_time)
	private TextView mTvTime;
	
	@ViewInit(id = R.id.ll_rent_type,onClick = "onClick")
	private LinearLayout mLlRentType;
	
	@ViewInit(id = R.id.tv_rent_type)
	private TextView mTvRentType;
	
	@ViewInit(id = R.id.et_rent)
	private SHEditText mEtRent;
	
	private JSONObject json;//存储界面数据
	
	String[] items_rent;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("租金方式");
		mDetailTitlebar.setRightButton1("确定", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(validate()){
					try {
						json.put("rentAmt", mEtRent.getText().toString().trim());
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
		items_rent = getResources().getStringArray(R.array.array_rent_type);
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
		View view = inflater.inflate(R.layout.fragment_rent_mode, container, false);
		return view;
	}

	private void initData(){
		mEtRent.setText(json.optString("rentAmt"));
		if(json.has("payType")){
			mTvRentType.setText(items_rent[json.optInt("payType")-1]);
		}else{
			try {
				json.put("payType", 3);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(json.has("inTime")){
			if(!CommonUtil.isEmpty(json.optString("inTime"))){
				mTvTime.setText(json.optString("inTime"));
			}else{
				mTvTime.setText("随时入住");
			}
		}else{
			try {
				json.put("inTime", "");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//默认随时入住，随时入住填""?
		}
	}
	
	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_time:
			CalendarDialog dia = new CalendarDialog(getActivity(), new CalendarResultListener() {

				@Override
				public void onCalendarResult(Dialog d, String date, boolean whenever) {
					// TODO Auto-generated method stub
					d.dismiss();
					if (whenever) {
						mTvTime.setText("随时入住");
						try {
							json.put("inTime", "");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						mTvTime.setText(date);
						try {
							json.put("inTime", date);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			dia.show();
			break;
		case R.id.ll_rent_type:
			new AlertDialog.Builder(getActivity()).setTitle("收租方式").setItems(items_rent, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int witch) {
					// TODO Auto-generated method stub
					mTvRentType.setText(items_rent[witch]);
					try {
						json.put("payType", witch+1);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).show();
			break;
		}
	}
	
	private boolean validate(){
		if(CommonUtil.isEmpty(mEtRent.getText().toString().trim())){
			SHToast.showToast(getActivity(), "请填写租金");
			return false;
		}
		return true;
	}
}
