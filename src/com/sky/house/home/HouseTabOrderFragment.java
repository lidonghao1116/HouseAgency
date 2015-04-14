package com.sky.house.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eroad.base.BaseFragment;
import com.sky.house.R;
/**
 * 随喜 订
 * @author skypan
 *
 */
public class HouseTabOrderFragment extends BaseFragment {

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("随心订");
		mDetailTitlebar.setLeftButton(null, null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_order, container, false);
		return view;
	}

}
