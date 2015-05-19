package com.sky.house.adapter;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eroad.base.util.ImageLoaderUtil;
import com.sky.house.R;

public class HouseListAdapter extends BaseAdapter {
	private Context context;

	private int flag;// 判断参数，不同界面展现不一样

	private JSONArray jsonArray;

	public static final int FLAG_HOUSE_LIST = 0;// 房源列表

	public HouseListAdapter(Context context, int flag, JSONArray jsonArray) {
		super();
		this.context = context;
		this.flag = flag;
		this.jsonArray = jsonArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jsonArray.length();
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
	public View getView(int pos, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_house_list, null);
			holder = new ViewHolder();
			holder.ivHouse = (ImageView) convertView.findViewById(R.id.iv_house);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tvSecond = (TextView) convertView.findViewById(R.id.tv_second);
			holder.tvRent = (TextView) convertView.findViewById(R.id.tv_rent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			ImageLoaderUtil.displayImage(jsonArray.getJSONObject(pos).getString("houseImgUrl"), holder.ivHouse);
			holder.tvTitle.setText(jsonArray.getJSONObject(pos).getString("houseName"));
			holder.tvSecond.setText(jsonArray.getJSONObject(pos).getString("layout")+"    "+jsonArray.getJSONObject(pos).getString("payTypeName"));
			holder.tvRent.setText(jsonArray.getJSONObject(pos).getString("rentAmt"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

	private static class ViewHolder {
		private TextView tvTitle,tvSecond,tvRent;
		private ImageView ivHouse;
	}
}
