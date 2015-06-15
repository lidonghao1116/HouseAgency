package com.sky.house.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.eroad.base.SHApplication;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ImageLoaderUtil;
import com.sky.house.R;
import com.sky.house.me.HouseRentalDetailFragment;
import com.sky.widget.sweetdialog.SweetDialog;
import com.sky.widget.sweetdialog.SweetDialog.OnSweetClickListener;

/**
 * @author yebaohua
 *
 *0初始状态 去支付定金
 *10 已付定金 --待确认定金
20 已确认定金 --待完善合同
30 已完善合同 --待确认合同
31 已驳回 --待完善合同
40 已确认合同 --待付款
50 已付款 --待入住
60 已入住
70 已申请退租 
80 合同到期  70 80 有退押金按钮
90 合同结束
-1 用户定金取消
-2 房东取消定金
-3 订单取消
-4 介入取消
 *
 */
public class HouseListAdapter extends BaseAdapter {
	private Context context;

	private int flag;// 判断参数，不同界面展现不一样

	private JSONArray jsonArray;

	public static final int FLAG_HOUSE_LIST = 0;// 房源列表
	public static final int FLAG_STATE_LIST_TENANT = 1;// 房源状态列表 含有头部 租客
	public static final int FLAG_STATE_LIST_LANDLORD = 2;// 房源状态列表 含有头部 房东
	public static final int FLAG_STATE_LIST_COMPLAINT = 3;// 房源状态列表 含有头部 投诉
	private ItemButtonSelectListencr itemButtonSelectListener;
	public interface ItemButtonSelectListencr{
		public void setLeftButtonOnselect(int pos,JSONObject object);

		public void setRightButtonOnselect(int pos,JSONObject object);

	}

	public void setItemButtonSelectListener(
			ItemButtonSelectListencr itemButtonSelectListener) {
		this.itemButtonSelectListener = itemButtonSelectListener;
	}

	public HouseListAdapter(Context context, int flag, JSONArray jsonArray) {
		super();
		this.context = context;
		this.flag = flag;
		this.jsonArray = jsonArray;
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
			JSONArray tese;
			try {
				tese = jsonArray.getJSONObject(pos).getJSONArray("houseFeature");
				String[] items_tese = context.getResources().getStringArray(R.array.array_tese);
				for (int i = 0; i < tese.length(); i++) {
					TextView tv = new TextView(context);
					tv.setTextSize(12);
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
			final JSONObject object =  jsonArray.getJSONObject(pos);
			if (this.flag != FLAG_HOUSE_LIST) {
				holder.rlTop.setVisibility(View.VISIBLE);
				holder.llBottom.setVisibility(View.VISIBLE);
				if (this.flag == FLAG_STATE_LIST_TENANT) {//房客
					holder.tvHouseState.setText(object.getString("orderStatusName"));
					switch (object.getInt("orderStatus")) {
					case 0://10 已付定金 --待确认定金
						holder.btnRight.setText("等待定金");
						holder.btnRight.setEnabled(false);
						break;
					case 10://10 已付定金 --待确认定金
						holder.btnRight.setText("等待确认");
						holder.btnRight.setEnabled(false);
						break;
					case 20://20 已确认定金 --待完善合同
						holder.btnRight.setText("等待合同");
						break;
					case 30://30 已完善合同 --待确认合同
						holder.btnRight.setText("确认合同");
						break;
					case 31://31 已驳回 --待完善合同
						holder.btnRight.setText("完善合同");
						break;
					case 40://40 已确认合同 --待付款
						holder.btnRight.setText("支付房租");
						break;
					case 50://50 已付款 --待入住
						holder.btnRight.setText("确认入住");
						break;
					default:
						if(object.getInt("orderStatus")<0){
							holder.btnRight.setVisibility(View.GONE);
						}else{
							holder.btnRight.setText("交易成功");
							holder.btnRight.setVisibility(View.VISIBLE);
//							holder.btnRight.setEnabled(false);
						}
						break;
					}
					holder.btnRight.setOnClickListener(new  View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							itemButtonSelectListener.setRightButtonOnselect(pos,object);
						}
					});

				} else if (this.flag == FLAG_STATE_LIST_LANDLORD) {// 房东
					holder.tvHouseState.setText(object.getString("orderStatusName"));
					switch (object.getInt("orderStatus")) {
					case 0://10 已付定金 --待确认定金
						holder.btnRight.setText("等待定金");
						break;
					case 10://10 已付定金 --待确认定金
						holder.btnRight.setText("确认定金");
						break;
					case 20://20 已确认定金 --待完善合同
						holder.btnRight.setText("完善合同");
						break;
					case 30://30 已完善合同 --待确认合同
						holder.btnRight.setText("等待确认");
						break;
					case 31://31 已驳回 --待完善合同
						holder.btnRight.setText("完善合同");
						break;
					case 40://40 已确认合同 --待付款
						holder.btnRight.setText("等待房租");
						break;
					case 50://50 已付款 --待入住
						holder.btnRight.setText("等待入住");
						break;
					default:
						if(object.getInt("orderStatus")<0){
							holder.btnRight.setVisibility(View.GONE);
						}else{
							holder.btnRight.setText("交易成功");
							holder.btnRight.setVisibility(View.VISIBLE);
//							holder.btnRight.setEnabled(false);
						}
						break;
					}
					holder.btnRight.setOnClickListener(new  View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							itemButtonSelectListener.setRightButtonOnselect(pos,object);
						}
					});
				} else if (this.flag == FLAG_STATE_LIST_COMPLAINT) {
					holder.tvHouseState.setText(object.getString("complaintStatusName"));
					//0 是 已投诉  1 是 解决 2 是 撤销
					if(object.getInt("complaintStatus") == 0){
						holder.btnRight.setText("撤回投诉");
						holder.btnRight.setOnClickListener(new  View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								itemButtonSelectListener.setRightButtonOnselect(pos,object);
							}
						});
						holder.btnRight.setVisibility(View.VISIBLE);
					}else{
						holder.btnRight.setVisibility(View.GONE);
					}
					
				}
				holder.btnLeft.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try {
							telMoblie(object.getString("mobilePhone"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}

			ImageLoaderUtil.displayImage(jsonArray.getJSONObject(pos).getString("houseImgUrl"), holder.ivHouse);
			holder.tvTitle.setText(jsonArray.getJSONObject(pos).getString("houseTitle"));
			holder.tvRent.setText(jsonArray.getJSONObject(pos).getString("rentAmt"));
			holder.tvRentType.setText(jsonArray.getJSONObject(pos).getString("payTypeName"));
			holder.tvReadTimes.setText(jsonArray.getJSONObject(pos).getString("browseCount"));
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

	private void telMoblie(final String moblie){
		final SweetDialog dia_call = new SweetDialog(SHApplication.getInstance().getCurrentActivity(), SweetDialog.WARNING_TYPE);
		dia_call.setTitleText("提示");
		dia_call.setContentText("是否拨打电话"+moblie+"？");
		dia_call.showCancelButton(true);
		dia_call.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+moblie));
				context.startActivity(intent);
			}
		});
		dia_call.show();
	}
}
