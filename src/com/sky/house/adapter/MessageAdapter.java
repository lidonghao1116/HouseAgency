package com.sky.house.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sky.house.R;

public class MessageAdapter extends BaseAdapter {
	private Context context;
	private JSONArray jsonArray = new JSONArray();
	public enum AdapterType{
		
		showReport,
		showTime
	}
	private AdapterType  type;

	public MessageAdapter(Context context) {
		super();
		this.context = context;
	}
	
	public MessageAdapter(Context context, JSONArray jsonArray, AdapterType type) {
		super();
		this.context = context;
		this.jsonArray = jsonArray;
		this.type = type;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(jsonArray == null){
			return 0;
		}
		return jsonArray.length();
	}

	@Override
	public JSONObject getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return jsonArray.getJSONObject(position);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JSONObject();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_message, null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.btnReport = (Button) convertView.findViewById(R.id.btn_report);
			if(type == AdapterType.showReport){
				holder.tvDate.setVisibility(View.GONE);
				holder.btnReport.setVisibility(View.VISIBLE);
			}else{
				holder.tvDate.setVisibility(View.VISIBLE);
				holder.btnReport.setVisibility(View.GONE);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			JSONObject object =  jsonArray.getJSONObject(position);
			holder.tvTitle.setText(object.optString("accountTypeName"));
			holder.tvDate.setText(object.optString("createDate"));
			holder.tvContent.setText(object.optString("content"));
			
			if(type == AdapterType.showReport){
				holder.btnReport.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
	private  class ViewHolder {
		private TextView tvTitle;
		private TextView tvContent;
		private TextView tvDate;
		private Button btnReport;
	}

}
