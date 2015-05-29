package com.sky.house.resource.publish;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHApplication;
import com.sky.house.R;
/**
 * 各种成功界面
 * @author skypan
 *
 */
public class HouseSuccessFragment extends BaseFragment {

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("发布房源");
		view.findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SHApplication.getInstance().onlyHome();
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_success, container, false);
		return view;
	}

}
