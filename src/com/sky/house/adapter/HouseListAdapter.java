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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eroad.base.util.ImageLoaderUtil;
import com.sky.house.R;

public class HouseListAdapter extends BaseAdapter {
	private Context context;

	private int flag;// 判断参数，不同界面展现不一样

	private JSONArray jsonArray;

	public static final int FLAG_HOUSE_LIST = 0;// 房源列表
	public static final int FLAG_STATE_LIST_TENANT = 1;// 房源状态列表  含有头部 租客
	public static final int FLAG_STATE_LIST_LANDLORD = 2;// 房源状态列表  含有头部 房东
	public static final int FLAG_STATE_LIST_COMPLAINT = 3;// 房源状态列表  含有头部 投诉

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
			holder.tvSecond = (TextView) convertView.findViewById(R.id.tv_second);
			holder.tvRent = (TextView) convertView.findViewById(R.id.tv_rent);
			holder.llBottom = (LinearLayout) convertView.findViewById(R.id.ll_bottom);
			holder.btnLeft = (Button) convertView.findViewById(R.id.btn_contact);
			holder.btnRight = (Button) convertView.findViewById(R.id.btn_remind);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			if(this.flag != FLAG_HOUSE_LIST){
				holder.rlTop.setVisibility(View.VISIBLE);
//				holder.tvHouser.setText("");
				holder.tvHouseState.setText(jsonArray.getJSONObject(pos).getString("houseDetaiStatusName"));
				holder.llBottom.setVisibility(View.VISIBLE);
				if(this.flag == FLAG_STATE_LIST_TENANT){
					holder.btnLeft.setText("拨打电话");
					holder.btnRight.setText("确认入住");
				}else if(this.flag == FLAG_STATE_LIST_TENANT){
					holder.btnLeft.setText("电话沟通");
					holder.btnRight.setText("提醒交租");
				}else if(this.flag == FLAG_STATE_LIST_TENANT){
					holder.btnLeft.setText("拨打电话");
					holder.btnRight.setText("撤回投诉");
				}
			}

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
		private RelativeLayout rlTop;
		private TextView tvHouser,tvHouseState,tvTitle,tvSecond,tvRent;
		private ImageView ivHouse;
		private LinearLayout llBottom;
		private Button btnRight,btnLeft;
	}
}
