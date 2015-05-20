package com.sky.house.adapter;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ImageLoaderUtil;
import com.sky.house.R;

public class NewsAdapter extends BaseAdapter {
	private Context context;

	private JSONArray jsonArray;

	public NewsAdapter(Context context, JSONArray jsonArray) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_news, null);
			holder = new ViewHolder();
			holder.ivPhoto = (ImageView) convertView.findViewById(R.id.iv_photo);
			holder.tvItem = (TextView) convertView.findViewById(R.id.tv_des);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			holder.tvName.setText(jsonArray.getJSONObject(pos).getString("userName"));
			SpannableStringBuilder sb = new SpannableStringBuilder();
			SpannableString ss = new SpannableString(jsonArray.getJSONObject(pos).getString("contentBefore"));
			sb.append(ss);
			ss = new SpannableString(jsonArray.getJSONObject(pos).getString("contentAfter"));
			ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_orange)), 0, ss.toString().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			sb.append(ss);
			holder.tvItem.setText(sb);
			holder.tvTime.setText(CommonUtil.Date.toYMR(jsonArray.getJSONObject(pos).getString("updateDate")));
			ImageLoaderUtil.displayImage(jsonArray.getJSONObject(pos).getString("userImgUrl"), holder.ivPhoto);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

	private static class ViewHolder {
		private TextView tvItem, tvName, tvTime;
		private ImageView ivPhoto;
	}
}
