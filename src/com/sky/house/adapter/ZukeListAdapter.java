package com.sky.house.adapter;

import org.json.JSONArray;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sky.house.R;

public class ZukeListAdapter extends BaseAdapter {
	private Context context;
	private JSONArray jsonArray;

	public ZukeListAdapter(Context context,JSONArray jsonArray) {
		super();
		this.context = context;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_zuke, null);
			holder = new ViewHolder();
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
			holder.btnConfirm = (Button) convertView.findViewById(R.id.btn_confirm);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		holder.tvItem.setText(str[pos]);
		return convertView;
	}

	private static class ViewHolder {
		private Button btnConfirm;
		private TextView tvTime,tvName,tvMoney;
	}
}
