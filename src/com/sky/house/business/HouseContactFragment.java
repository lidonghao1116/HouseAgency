package com.sky.house.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;

/**
 * 联系看房
 * 
 * @author skypan
 * 
 */
public class HouseContactFragment extends BaseFragment {

	@ViewInit(id = R.id.btn_pay, onClick = "onClick")
	private Button mBtnPay;

	@ViewInit(id = R.id.iv_question, onClick = "onClick")
	private ImageView mIvQuestion;

	@ViewInit(id = R.id.ll_tips)
	private LinearLayout mLlQuestion;

	private boolean isTipsShow;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_contact, container, false);
		return view;
	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_pay:
			Intent intent = new Intent(getActivity(), SHContainerActivity.class);
			intent.putExtra("class", HousePayChargeFragment.class.getName());
			startActivity(intent);
			break;
		case R.id.iv_question:
			if (isTipsShow) {
				mLlQuestion.setVisibility(View.GONE);
				isTipsShow = false;
			} else {
				mLlQuestion.setVisibility(View.VISIBLE);
				isTipsShow = true;
			}
			break;
		}
	}
}
