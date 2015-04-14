package com.sky.house.resource;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eroad.base.BaseFragment;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;
import com.sky.house.adapter.GridAdapter;
import com.sky.house.widget.MyGridView;
/**
 * 发布房源
 * @author skypan
 *
 */
public class HousePublishFragment extends BaseFragment {

	@ViewInit(id = R.id.gv_facilities)
	private MyGridView mGvFacilities;
	private final String[] facilities = new String[]{"床","宽带","电视","洗衣机","暖气","空调","冰箱","热水器"};
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mGvFacilities.setAdapter(new GridAdapter(getActivity(),facilities));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_publish, container, false);
		return view;
	}

}
