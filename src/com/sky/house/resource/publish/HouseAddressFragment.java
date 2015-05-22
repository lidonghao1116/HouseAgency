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
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;

/**
 * 填写房源地址
 * 
 * @author skypan
 * 
 */
public class HouseAddressFragment extends BaseFragment {

	@ViewInit(id = R.id.tv_xiaoqu, onClick = "onClick")
	private TextView mTvAddress;
	
	private JSONObject json;//存储当前界面的参数

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("房源地址");
		mDetailTitlebar.setRightButton1("确定", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("json", json.toString());
				getActivity().setResult(Activity.RESULT_OK, intent);
				finish();
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

	private void initData(){
		mTvAddress.setText(json.optString("houseZoneName"));
	}
	
	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_xiaoqu:
			Intent intent = new Intent(getActivity(),SHContainerActivity.class);
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
		if(resultCode == Activity.RESULT_OK && data != null){
			mTvAddress.setText(data.getStringExtra("houseZoneName"));
			try {
				json.put("city", data.getStringExtra("city"));
				json.put("district", data.getStringExtra("district"));
				json.put("houseZoneName", data.getStringExtra("houseZoneName"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
}
