package com.sky.house.adapter;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.eroad.base.util.ImageLoaderUtil;
import com.sky.house.R;

/**
 * 
 * @author skypan
 */

public class HouseGridAdapter extends BaseAdapter {

	private Context context;
	private JSONArray jsonArray;

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public HouseGridAdapter(Context context, JSONArray jsonArray) {
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
	public View getView(final int pos, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_house_grid, null);
			holder = new ViewHolder();
			holder.ivHouse = (ImageView) convertView.findViewById(R.id.iv_house);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tvRent = (TextView) convertView.findViewById(R.id.tv_rent);
//			holder.tvRentType = (TextView) convertView.findViewById(R.id.tv_rent_type);
			holder.tvReadTimes = (TextView) convertView.findViewById(R.id.tv_read_times);
			holder.llTese = (LinearLayout) convertView.findViewById(R.id.ll_tese);
			JSONArray tese;
			try {
				tese = jsonArray.getJSONObject(pos).getJSONArray("houseFeature");
				String[] items_tese = context.getResources().getStringArray(R.array.array_tese);
				for (int i = 0; i < tese.length(); i++) {
					TextView tv = new TextView(context);
					tv.setTextSize(10);
					tv.setPadding(5, 1, 5, 1);
					tv.setText(items_tese[tese.getInt(i)]);
					LinearLayout.LayoutParams lay = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					lay.setMargins(0, 0, 10, 0);
					tv.setLayoutParams(lay);
					tv.setTextColor(context.getResources().getColor(R.color.color_black));
					switch (i) {
					case 0:
						tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.stroke_yellow_zhi));
						break;
					case 1:
						tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.stroke_green_zhi));
						break;
					case 2:
						tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.stroke_red_zhi));
						break;
					}
					holder.llTese.addView(tv);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			ImageLoaderUtil.displayImage(jsonArray.getJSONObject(pos).getString("houseImgUrl"), holder.ivHouse);
			holder.tvTitle.setText(jsonArray.getJSONObject(pos).getString("houseTitle"));
			holder.tvRent.setText(jsonArray.getJSONObject(pos).getString("rentAmt"));
//			holder.tvRentType.setText(jsonArray.getJSONObject(pos).getString("payTypeName"));
			holder.tvReadTimes.setText(jsonArray.getJSONObject(pos).getString("browseCount"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;

	}

	private static class ViewHolder {
		private TextView tvTitle, tvRent, tvRentType, tvReadTimes;
		private ImageView ivHouse;
		private LinearLayout llTese;
	}
}
