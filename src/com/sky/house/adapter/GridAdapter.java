package com.sky.house.adapter;


import org.json.JSONArray;
import org.json.JSONException;

import com.eroad.base.util.ImageLoaderUtil;
import com.sky.house.R;

import android.app.Activity;
import android.media.ToneGenerator;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ToggleButton;


/**
 * 
 * @author skypan
 */

public class GridAdapter extends BaseAdapter {

	private Activity mActivity;
	private String[] facilities;

	public GridAdapter(Activity a,String[] s) {
		this.mActivity = a;
		this.facilities = s;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ToggleButton tb;
		if (arg1 == null) {
			tb = new ToggleButton(mActivity);
			tb.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tb.setPadding(0, 8, 0, 8);
			tb.setText(facilities[arg0]);
			tb.setTextOff(facilities[arg0]);
			tb.setTextOn(facilities[arg0]);
			tb.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.btn_toggle_round));
			tb.setTextColor(mActivity.getResources().getColorStateList(R.color.color_white));
		} else {
			tb = (ToggleButton) arg1;
		}
		return tb;
	}

}
