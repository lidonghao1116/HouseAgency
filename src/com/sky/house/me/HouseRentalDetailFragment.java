package com.sky.house.me;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHApplication;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ImageLoaderUtil;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.house.adapter.HouseListAdapter;
import com.sky.house.resource.HouseDetailFragment;
import com.sky.widget.SHDialog;
import com.sky.widget.sweetdialog.SweetDialog;

public class HouseRentalDetailFragment extends BaseFragment implements
OnClickListener, ITaskListener {

	@ViewInit(id = R.id.tv_paid)
	private TextView tvPaidMoney;

	@ViewInit(id = R.id.tv_deposit)
	private TextView tvDeposit;

	@ViewInit(id = R.id.tv_total_payment)
	private TextView tvTotalPaid;

	@ViewInit(id = R.id.btn_top_left, onClick = "onClick")
	private Button btnTopLeft;

	@ViewInit(id = R.id.btn_top_right, onClick = "onClick")
	private Button btnTopRight;

	//house_list_item
	@ViewInit(id = R.id.rl_top, onClick = "onClick")
	private RelativeLayout rlHouseTop;

	@ViewInit(id = R.id.ll_content, onClick = "onClick")
	private RelativeLayout rlHouseContent;

	@ViewInit(id = R.id.tv_houser)
	private TextView tvHouser;

	@ViewInit(id = R.id.tv_house_state)
	private TextView tvHouseState;

	@ViewInit(id = R.id.iv_house)
	private ImageView ivHouse;

	@ViewInit(id = R.id.tv_title)
	private TextView tvHouseTitle;

	@ViewInit(id = R.id.tv_rent)
	private TextView tvHouseRent;

	@ViewInit(id = R.id.tv_read_times)
	private TextView tvHouseReadTimes;

	@ViewInit(id = R.id.tv_rent_type)
	private TextView tvHouseRentType;

	@ViewInit(id = R.id.ll_tese)
	private LinearLayout llHouseTese;
	@ViewInit(id = R.id.ll_bottom)
	private LinearLayout llHouseBottom;

	@ViewInit(id = R.id.btn_contact, onClick = "onClick")
	private Button btnHouseBottomLeft;
	@ViewInit(id = R.id.btn_remind, onClick = "onClick")
	private Button btnHouseBottomRight;

	@ViewInit(id = R.id.tv_rent)
	private TextView tvRentMoney;//租金/月

	@ViewInit(id = R.id.tv_lease)
	private TextView tvLease;// 租赁方式

	@ViewInit(id = R.id.tv_rent_long)
	private TextView tvRentLong;// 租期

	@ViewInit(id = R.id.tv_order_num)
	private TextView tvOrderNum;// 订单号

	@ViewInit(id = R.id.tv_deal_time)
	private TextView tvDealTime;// 成交时间

	@ViewInit(id = R.id.tv_checkin)
	private TextView tvCheckin;// 入住时间

	@ViewInit(id = R.id.btn_bottom_left, onClick = "onClick")
	private Button btnBottomLeft;

	@ViewInit(id = R.id.btn_bottom_right, onClick = "onClick")
	private Button btnBottomRight;

	private  int  type;// 列表类型 查看HouseListAdapter说明
	private SHPostTaskM taskDetail,taskComplait;
	private JSONObject mResult;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view   = inflater.inflate(R.layout.fragment_rental_detail, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mDetailTitlebar.setTitle("租房详情");
		mDetailTitlebar.setRightButton1("清空", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			}
		});
		type  = getActivity().getIntent().getIntExtra("type", HouseListAdapter.FLAG_HOUSE_LIST);
		initView();
		request();
	}
	/**
	 * 初始数据界面
	 */
	private void initView(){
		rlHouseTop.setVisibility(View.VISIBLE);
		llHouseBottom.setVisibility(View.VISIBLE);
	}
	private void request(){
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		taskDetail = new SHPostTaskM();
		taskDetail.setUrl(ConfigDefinition.URL+"GetOrderDetail");
		taskDetail.getTaskArgs().put("orderId", getActivity().getIntent().getIntExtra("orderId", 0));
		taskDetail.setListener(this);
		taskDetail.start();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), SHContainerActivity.class);
		switch (v.getId()) {
		case R.id.btn_top_left:// 查看租金
			intent.putExtra("class", HouseRentPieChartFragment.class.getName());
			intent.putExtra("orderId", getActivity().getIntent().getIntExtra("orderId", 0));
			startActivity(intent);
			break;
		case R.id.btn_top_right:// 查看合同
			try {
				intent.putExtra("class", HouseContractDetailFragment.class.getName());
				intent.putExtra("orderId", getActivity().getIntent().getIntExtra("orderId", 0));
				intent.putExtra("url", mResult.getString("contracturl"));
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}
	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		mResult = (JSONObject) task.getResult();

		tvPaidMoney.setText("已支付租金（"+mResult.getInt("hasPayMonth")+"个月）：￥"+mResult.optDouble("totalPayAmount"));
		tvDeposit.setText("房租押金（"+mResult.getInt("wagerMonth")+"月）：￥"+mResult.optDouble("wagerAmt"));
		tvTotalPaid.setText("总计支付：￥"+mResult.optDouble("totalPayAmount"));

		tvRentMoney.setText("￥"+mResult.getString("rentAmt"));
		tvLease.setText(mResult.getString("payTypeName"));
		tvRentLong.setText(CommonUtil.Date.toYMR(mResult.getString("tenantBeginDate"))+" 至 "+CommonUtil.Date.toYMR(mResult.getString("tenantEndDate")));
		tvOrderNum.setText(mResult.optInt("contractId")+"");
		tvDealTime.setText(mResult.getString("dealTime"));
		tvCheckin.setText(mResult.getString("carryingInTime"));


		JSONArray tese = mResult.getJSONArray("houseFeature");
		String[] items_tese = getActivity().getResources().getStringArray(R.array.array_tese);
		for (int i = 0; i < tese.length(); i++) {
			TextView tv = new TextView(getActivity());
			tv.setTextSize(12);
			tv.setPadding(5, 1, 5, 1);
			tv.setText(items_tese[tese.getInt(i)]);
			LinearLayout.LayoutParams lay = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lay.setMargins(0, 0, 10, 0);
			tv.setLayoutParams(lay);
			tv.setTextColor(getActivity().getResources().getColor(R.color.color_black));
			switch (i) {
			case 0:
				tv.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.stroke_yellow_zhi));
				break;
			case 1:
				tv.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.stroke_green_zhi));
				break;
			case 2:
				tv.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.stroke_red_zhi));
				break;
			}
			llHouseTese.addView(tv);
		}

		ImageLoaderUtil.displayImage(mResult.getString("houseImgUrl"), ivHouse);
		tvHouseTitle.setText(mResult.getString("houseTitle"));
		tvHouseRent.setText(mResult.getString("rentAmt"));
		tvHouseRentType.setText(mResult.getString("payTypeName"));
		tvHouseReadTimes.setText(mResult.getString("browseCount"));

		tvHouseState.setText(mResult.getString("orderStatusName"));
		Drawable drawableLeft = getActivity().getResources().getDrawable(R.drawable.ic_house_people);
		Drawable drawableRight = getActivity().getResources().getDrawable(R.drawable.ic_arrow_right);
		tvHouser.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null);
		rlHouseContent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Intent intent = new Intent(getActivity(), SHContainerActivity.class);
					intent.putExtra("class", HouseDetailFragment.class.getName());
					intent.putExtra("id", mResult.getInt("houseDetailId"));
					intent.putExtra("name", mResult.getString("houseName"));
					startActivity(intent);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnBottomLeft.setText("我要投诉");
		btnBottomLeft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				taskComplait = new SHPostTaskM();
				taskComplait.setUrl(ConfigDefinition.URL+"AddUserComplaint");
				taskComplait.getTaskArgs().put("complaintType", "6");
				taskComplait.getTaskArgs().put("complaintOrderId", getActivity().getIntent().getIntExtra("orderId", 0));
				taskComplait.getTaskArgs().put("complaintContent", "");
				taskComplait.setListener(HouseRentalDetailFragment.this);
				taskComplait.start();

			}
		});
		if(type==HouseListAdapter.FLAG_STATE_LIST_TENANT){//房客
			tvHouser.setText(mResult.getString("lordName"));
			tvHouseState.setText("房东信息");
			rlHouseTop.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});
		}else{
			tvHouser.setText(mResult.getString("tenantName"));
			tvHouseState.setText("租客信息");
			rlHouseTop.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});
		}


	}

	@Override
	public void onTaskFailed(SHTask task) {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		new SweetDialog(SHApplication.getInstance().getCurrentActivity(), SweetDialog.ERROR_TYPE).setTitleText("提示").setContentText(task.getRespInfo().getMessage()).show();
	}

	@Override
	public void onTaskUpdateProgress(SHTask task, int count, int total) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTaskTry(SHTask task) {
		// TODO Auto-generated method stub

	}



}
