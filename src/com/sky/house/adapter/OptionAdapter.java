package com.sky.house.adapter;

import org.json.JSONArray;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sky.house.R;

public class OptionAdapter extends BaseAdapter {
	private Context context;
	private JSONArray jsonArray;
	private String[] str;

	public OptionAdapter(Context context,String[] str) {
		super();
		this.context = context;
		this.str = str;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return str.length;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_option, null);
			holder = new ViewHolder();
			holder.tvItem = (TextView) convertView.findViewById(R.id.tv_option);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvItem.setText(str[pos]);
		return convertView;
	}

	private static class ViewHolder {
		private TextView tvItem;
		private ImageView ivSelect;
	}
}
