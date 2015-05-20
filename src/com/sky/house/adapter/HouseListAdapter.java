package com.sky.house.adapter;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ImageLoaderUtil;
import com.sky.house.R;

public class HouseListAdapter extends BaseAdapter {
	private Context context;

	private int flag;// 判断参数，不同界面展现不一样

	private JSONArray jsonArray;

	public static final int FLAG_HOUSE_LIST = 0;// 房源列表
	public static final int FLAG_STATE_LIST_TENANT = 1;// 房源状态列表 含有头部 租客
	public static final int FLAG_STATE_LIST_LANDLORD = 2;// 房源状态列表 含有头部 房东
	public static final int FLAG_STATE_LIST_COMPLAINT = 3;// 房源状态列表 含有头部 投诉

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
			holder.rlTop = (RelativeLayout) convertView.findViewById(R.id.rl_top);
			holder.tvHouser = (TextView) convertView.findViewById(R.id.tv_houser);
			holder.tvHouseState = (TextView) convertView.findViewById(R.id.tv_house_state);
			holder.ivHouse = (ImageView) convertView.findViewById(R.id.iv_house);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tvRent = (TextView) convertView.findViewById(R.id.tv_rent);
			holder.llBottom = (LinearLayout) convertView.findViewById(R.id.ll_bottom);
			holder.btnLeft = (Button) convertView.findViewById(R.id.btn_contact);
			holder.btnRight = (Button) convertView.findViewById(R.id.btn_remind);
			holder.tvRentType = (TextView) convertView.findViewById(R.id.tv_rent_type);
			holder.tvReadTimes = (TextView) convertView.findViewById(R.id.tv_read_times);
			holder.llTese = (LinearLayout) convertView.findViewById(R.id.ll_tese);
			String[] tese;
			try {
				tese = jsonArray.getJSONObject(pos).getString("houseFeature").split(",");
				String[] items_tese = context.getResources().getStringArray(R.array.array_tese);
				for (int i = 0; i < tese.length; i++) {
					TextView tv = new TextView(context);
					tv.setTextSize(12);
					tv.setPadding(5, 1, 5, 1);
					tv.setText(items_tese[Integer.valueOf(tese[i])]);
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
			if (this.flag != FLAG_HOUSE_LIST) {
				holder.rlTop.setVisibility(View.VISIBLE);
				// holder.tvHouser.setText("");
				holder.tvHouseState.setText(jsonArray.getJSONObject(pos).getString("houseDetaiStatusName"));
				holder.llBottom.setVisibility(View.VISIBLE);
				if (this.flag == FLAG_STATE_LIST_TENANT) {
					holder.btnLeft.setText("拨打电话");
					holder.btnRight.setText("确认入住");
				} else if (this.flag == FLAG_STATE_LIST_TENANT) {
					holder.btnLeft.setText("电话沟通");
					holder.btnRight.setText("提醒交租");
				} else if (this.flag == FLAG_STATE_LIST_TENANT) {
					holder.btnLeft.setText("拨打电话");
					holder.btnRight.setText("撤回投诉");
				}
			}

			ImageLoaderUtil.displayImage(jsonArray.getJSONObject(pos).getString("houseImgUrl"), holder.ivHouse);
			holder.tvTitle.setText(jsonArray.getJSONObject(pos).getString("houseTitle"));
			holder.tvRent.setText(jsonArray.getJSONObject(pos).getString("rentAmt"));
			holder.tvRentType.setText(jsonArray.getJSONObject(pos).getString("payTypeName"));
			holder.tvReadTimes.setText(jsonArray.getJSONObject(pos).getInt("browseCount") + "人浏览");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

	private static class ViewHolder {
		private RelativeLayout rlTop;
		private TextView tvHouser, tvHouseState, tvTitle, tvRent, tvRentType, tvReadTimes;
		private ImageView ivHouse;
		private LinearLayout llBottom, llTese;
		private Button btnRight, btnLeft;
	}
}
