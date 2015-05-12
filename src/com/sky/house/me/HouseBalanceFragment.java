package com.sky.house.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;

public class HouseBalanceFragment extends BaseFragment implements
		OnClickListener {

	@ViewInit(id = R.id.rl_acconut, onClick = "onClick")
	private RelativeLayout rlAccount;
	@ViewInit(id = R.id.rl_bank, onClick = "onClick")
	private RelativeLayout rlBank;
	@ViewInit(id = R.id.rl_frozen, onClick = "onClick")
	private RelativeLayout rlFrozen;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_balance, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mDetailTitlebar.setTitle("余额");
		mDetailTitlebar.setRightButton1("记录", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),SHContainerActivity.class);
				intent.putExtra("class", HouseSettingFragment.class.getName());
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent  = new Intent(getActivity(),SHContainerActivity.class);
		switch (v.getId()) {
		case R.id.rl_acconut:

			break;
		case R.id.rl_bank:

			break;
		case R.id.rl_frozen:

			break;
		}
	}

}
