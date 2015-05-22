package com.sky.house.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.sug.SuggestionResult;
import com.sky.house.R;

public class AddressAdapter extends BaseAdapter{
	private Context context;
	private List<SuggestionResult.SuggestionInfo> list;
	

	public void setList(List<SuggestionResult.SuggestionInfo> list) {
		this.list = list;
	}

	public AddressAdapter(Context c){
		this.context = c;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public class ViewHolder{
		public TextView tv_address,tv_district;   
	}
	
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_address, null);
			holder = new ViewHolder();
			holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
			holder.tv_district = (TextView) convertView.findViewById(R.id.tv_district);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.tv_address.setText(list.get(arg0).key);
		holder.tv_district.setText(list.get(arg0).district);
		return convertView;
	}

}
