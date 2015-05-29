package com.sky.house.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ToggleButton;

import com.eroad.base.util.CommonUtil;
import com.sky.house.R;

/**
 * 
 * @author skypan
 */

public class GridAdapter extends BaseAdapter {

	private Activity mActivity;
	private String[] facilities;
	private boolean editable;
	private int flag;// 家具/家电
	public static final int FLAG_ELE = 0;// 家电
	public static final int FLAG_FUNI = 1;// 家具
	JSONArray jsonArray = new JSONArray();//已选
	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	private HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();

	public GridAdapter(Activity a, String[] s, boolean editable, JSONArray jsonArray ,int flag) {
		this.mActivity = a;
		this.facilities = s;
		this.editable = editable;
		this.flag = flag;
		this.jsonArray = jsonArray;
	}
	
	public GridAdapter(Activity a, String[] s, boolean editable, int flag) {
		this.mActivity = a;
		this.facilities = s;
		this.editable = editable;
		this.flag = flag;
	}

	public JSONArray getSelectedArray() {
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			int key = (Integer) entry.getKey();
			boolean val = (Boolean) entry.getValue();
			if(val){
				if(flag == FLAG_ELE){
					if(!CommonUtil.containsObj(jsonArray, key+1)){
						jsonArray.put(key+1);
					}
				}else{
					if(!CommonUtil.containsObj(jsonArray, key+20)){
						jsonArray.put(key+20);
					}
				}
			}else{
				if(flag == FLAG_ELE){
					if(CommonUtil.containsObj(jsonArray, key+1)){
						jsonArray = CommonUtil.removeObj(jsonArray, key+1);
					}
				}else{
					if(CommonUtil.containsObj(jsonArray, key+20)){
						jsonArray = CommonUtil.removeObj(jsonArray, key+20);
					}
				}
			}
		}
		return jsonArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return facilities.length;
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
	public View getView(final int pos, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ToggleButton tb;
		if (arg1 == null) {
			tb = new ToggleButton(mActivity);
			tb.setEnabled(editable);
			if (!editable) {
				tb.setChecked(true);
			}
			tb.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tb.setPadding(0, 8, 0, 8);
			tb.setText(facilities[pos]);
			tb.setTextOff(facilities[pos]);
			tb.setTextOn(facilities[pos]);
			tb.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.btn_toggle_round));
			tb.setTextColor(mActivity.getResources().getColorStateList(R.color.color_white));
			tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					// TODO Auto-generated method stub
					map.put(pos, arg1);
				}
			});
		} else {
			tb = (ToggleButton) arg1;
		}
		if(flag == FLAG_ELE){
			if(CommonUtil.containsObj(jsonArray, pos+1)){
				tb.setChecked(true);
			}
		}else{
			if(CommonUtil.containsObj(jsonArray, pos+20)){
				tb.setChecked(true);
			}
		}
		return tb;
	}

}
