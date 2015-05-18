package com.sky.house.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sky.house.R;

public class CityAdapter extends BaseAdapter{
	private Context context;
	private JSONArray jsonArray;
	
	public CityAdapter(Context c,JSONArray jsonArray){
		this.context = c;
		this.jsonArray = jsonArray;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jsonArray.length();
	}

	@Override
	public JSONObject getItem(int position) {
		try {
			return jsonArray.getJSONObject(position);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		String a = "";
		try {
			a = jsonArray.getJSONObject(position).optString("carcategoryname");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if("-1".equals(a)){
			return 0;
		}
		return 1;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public class ViewHolder{
		private TextView parenrttext;
		private TextView tvCity;
	}
	
	public String getFirstPinyin(int position) {
		return getItem(position).optString("sort");
	}
	
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){

			int viewType = getItemViewType(arg0);
			switch (viewType) {
			case 0:
				convertView = LayoutInflater.from(context).inflate(R.layout.item_letter, null);
				holder = new ViewHolder();
				holder.parenrttext = (TextView) convertView.findViewById(R.id.text);
				break;
			case 1:
				convertView = LayoutInflater.from(context).inflate(R.layout.item_city, null);
				holder = new ViewHolder();
				holder.tvCity = (TextView) convertView.findViewById(R.id.tv_city);
				break;
			}
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}

		int viewType = getItemViewType(arg0);
		switch (viewType) {
		case 0:
			try {
//				System.out.println(jsonArray.getJSONObject(arg0).getString("sort"));
				holder.parenrttext.setText(jsonArray.getJSONObject(arg0).getString("sort"));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case 1:
			try {
				holder.tvCity.setText(jsonArray.getJSONObject(arg0).getString("cityName"));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			break;
		}
		return convertView;
	}

}
