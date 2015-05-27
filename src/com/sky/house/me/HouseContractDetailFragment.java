package com.sky.house.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;

public class HouseContractDetailFragment extends BaseFragment implements OnClickListener{
 
	@ViewInit(id = R.id.btn_email, onClick = "onClick")
	private Button btnEmail;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_contract_detail,container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mDetailTitlebar.setTitle("合同详情");
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent  = new Intent(getActivity(),SHContainerActivity.class);
		switch (v.getId()) {
		case R.id.btn_email:
			intent.putExtra("class", HouseContractEmailFragment.class.getName());
			intent.putExtra("orderId", getActivity().getIntent().getIntExtra("orderId", -1));
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
