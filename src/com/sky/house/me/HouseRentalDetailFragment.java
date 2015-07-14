package com.sky.house.me;

import java.math.BigDecimal;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
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
import com.sky.car.pay.ali.Keys;
import com.sky.car.pay.ali.Result;
import com.sky.car.pay.ali.Rsa;
import com.sky.house.R;
import com.sky.house.adapter.HouseListAdapter;
import com.sky.house.business.HouseContactFragment;
import com.sky.house.resource.HouseDetailFragment;
import com.sky.widget.SHDialog;
import com.sky.widget.SHToast;
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
	private LinearLayout rlHouseContent;

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

	@ViewInit(id = R.id.tv_rent_money)
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
	private SHPostTaskM taskDetail,taskComplait,taskCancle,taskPayOther,taskHasPass,taskNextPay,taskRefund,taskRemind;
	private SHPostTaskM getOrderIdTask,accountTask;
	private JSONObject mResult;
	private boolean isSetPass;
	private Dialog dilogPass;

	TextView tvDilogTitle ;
	EditText etDilogMoney;
	EditText etDilogPass;
	Button btnDilogCancle;
	Button btnDilogComfirm;
	
	String aliOrderId;
	private static final int RQF_PAY = 1;
	private static final int RQF_LOGIN = 2;
	private double aliPayMoney;//支付宝 支付金额
	private int amount;//余额
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			request();
		}
	};
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
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("JPUSH_MSG");
		getActivity().registerReceiver(receiver, intentFilter);
		
		mDetailTitlebar.setTitle("租房详情");
		type  = getActivity().getIntent().getIntExtra("type", HouseListAdapter.FLAG_HOUSE_LIST);
		initView();
		request();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		requestHasPass();
		requestAccount();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(receiver!=null)
			getActivity().unregisterReceiver(receiver);
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
	/**
	 * 是否设置密码
	 */
	private void requestHasPass(){

		taskHasPass = new SHPostTaskM();
		taskHasPass.setUrl(ConfigDefinition.URL + "GetUserIsSetPayPassword");
		taskHasPass.setListener(this);
		taskHasPass.start();
	}
	/**
	 * 余额
	 */
	private void requestAccount(){
		accountTask = new SHPostTaskM();
		accountTask.setListener(this);
		accountTask.setUrl(ConfigDefinition.URL+"GetMyAccountDetail");
		accountTask.start();
	}
	/**
	 * 添加支付宝访问日志
	 */
	private void requestOrderId(int type){
		getOrderIdTask = new SHPostTaskM();
		getOrderIdTask.setListener(this);
		getOrderIdTask.setUrl(ConfigDefinition.URL+"AlipayOptInfoAdd");
		getOrderIdTask.getTaskArgs().put("orderId", getActivity().getIntent().getIntExtra("orderId", 0));
		getOrderIdTask.getTaskArgs().put("payAmt", aliPayMoney+amount);//支付金额
		getOrderIdTask.getTaskArgs().put("rechargeAmt", aliPayMoney );//充值金额
		getOrderIdTask.getTaskArgs().put("optType", type);//1 交定金 2 交房租 3 交杂费 4 交押金 5交租金 6 退杂费 7 退押金 8续交房租
		getOrderIdTask.start();
	}
	/**
	 * 支付宝发起支付
	 */
	private void alipay(){
		try {
			String info = getNewOrderInfo();
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			Log.i("ExternalPartner", "start pay");
			// start the pay.
			Log.i("pay", "info = " + info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(getActivity(), mHandler);
					
					//设置为沙箱模式，不设置默认为线上环境
					//alipay.setSandBox(true);

					String result = alipay.pay(orderInfo);

					Log.i("SHOPDETAIL", "result = " + result);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(getActivity(), "Failure calling remote service",
					Toast.LENGTH_SHORT).show();
		}
	}
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);
			switch (msg.what) {
			case RQF_PAY:
				result.parseResult();
				if(result.isPayOK()){
					Log.i("支付回调", "支付成功");
//					changeOrderStatus();
//					requestData();
				}
				else{
					
				}
				break;
			case RQF_LOGIN: {
				Toast.makeText(getActivity(), result.getResult(),
						Toast.LENGTH_SHORT).show();

			}
				break;
			default:
				break;
			}
		};
	};
	
	private String getNewOrderInfo() throws JSONException {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(aliOrderId);
		sb.append("\"&subject=\"");
		sb.append("阳光租房");
		sb.append("\"&body=\"");
		sb.append("阳光租房");
		sb.append("\"&total_fee=\"");
		sb.append(aliPayMoney);
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode(ConfigDefinition.PAY_URL));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), SHContainerActivity.class);
		switch (v.getId()) {
		case R.id.btn_top_left:// 查看租金
			try {
				intent.putExtra("class", HouseRentPieChartFragment.class.getName());
				intent.putExtra("orderId", getActivity().getIntent().getIntExtra("orderId", 0));
				intent.putExtra("nextPayAmt", mResult.getInt("nextPayAmt"));
				intent.putExtra("nextPayMonths", mResult.getInt("nextPayMonths"));
				intent.putExtra("orderStatus", mResult.getInt("orderStatus"));
				intent.putExtra("type", type);
				startActivity(intent);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case R.id.btn_top_right:// 查看合同
			try {
				intent.putExtra("class", HouseContractDetailFragment.class.getName());
				intent.putExtra("orderId", getActivity().getIntent().getIntExtra("orderId", 0));
				intent.putExtra("url", mResult.getString("contractUrl"));
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
		if(task == taskComplait){
			SHToast.showToast(getActivity(), "投诉成功");
		}else if(task == accountTask ){//余额查询
			JSONObject accountJson = (JSONObject) task.getResult();
			amount =  accountJson.getInt("amount");
		}else if(task == getOrderIdTask ){
			JSONObject jsonObj = (JSONObject) task.getResult();
			aliOrderId = jsonObj.getString("requestNo");
			alipay();
		}else if(task == taskHasPass ){
			JSONObject object  = (JSONObject) task.getResult() ;
			isSetPass  = object.getInt("isSet")==0?false:true;
		}else if(task == taskCancle){
			SHToast.showToast(getActivity(), "已经成功申请退租");
		}else if(task == taskPayOther){
			SHToast.showToast(getActivity(), "交易成功");
		}else if(task == taskNextPay){
			SHToast.showToast(getActivity(), "交租金成功");
		}else if(task == taskRefund){
			SHToast.showToast(getActivity(), "退押金成功");
		}else if(task == taskRemind){
			SHToast.showToast(getActivity(), "已成功发送提醒通知");
		}else if(task == taskDetail){
			mResult = (JSONObject) task.getResult();

			tvPaidMoney.setText("已支付租金（"+mResult.getInt("hasPayMonth")+"个月）：￥"+mResult.optDouble("totalPayAmount"));
			tvDeposit.setText("房租押金（"+mResult.getInt("wagerMonth")+"月）：￥"+mResult.optDouble("wagerAmt"));
			tvTotalPaid.setText("总计支付：￥"+mResult.optDouble("orderPayAmount"));
			tvRentMoney.setText(mResult.getString("rentAmt"));
			tvLease.setText(mResult.getString("payTypeName"));
			tvRentLong.setText(CommonUtil.Date.toYMR(mResult.getString("tenantBeginDate"))+" 至 "+CommonUtil.Date.toYMR(mResult.getString("tenantEndDate")));
			tvOrderNum.setText(mResult.getString("contractId"));
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
			btnHouseBottomLeft.setText("我要投诉");
			btnHouseBottomLeft.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SHDialog.ShowProgressDiaolg(getActivity(), null);
					taskComplait = new SHPostTaskM();
					taskComplait.setUrl(ConfigDefinition.URL+"AddUserComplaint");
					taskComplait.getTaskArgs().put("complaintType", "10");
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
						try {
							Intent intent = new Intent(getActivity(), SHContainerActivity.class);
							intent.putExtra("class", HouseContactFragment.class.getName());
							intent.putExtra("name", "房东信息");
							intent.putExtra("id", mResult.getInt("houseDetailId"));
							intent.putExtra("pageType", 1);
							startActivity(intent);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				if(mResult.getInt("isCancelLease") == 1 && mResult.getInt("orderStatus") ==60){
					btnHouseBottomRight.setVisibility(View.VISIBLE);
				}else{
					btnHouseBottomRight.setVisibility(View.GONE);
				}
				btnHouseBottomRight.setText("我要退租");
				btnHouseBottomRight.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						taskCancle = new SHPostTaskM();
						taskCancle.setUrl(ConfigDefinition.URL+"CancelLease");
						taskCancle.getTaskArgs().put("orderId", getActivity().getIntent().getIntExtra("orderId", 0));
						taskCancle.setListener(HouseRentalDetailFragment.this);
						taskCancle.start();

					}
				});

				btnBottomLeft.setText("缴杂费");
				btnBottomLeft.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						playOtherAmt(2);

					}
				});

				btnBottomRight.setText("交租金");
				btnBottomRight.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try {
							PayNextRent(mResult.getInt("nextPayAmt"),mResult.getInt("nextPayMonths"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				//交租金 只有60可以缴
				if(mResult.getInt("orderStatus") ==60 && mResult.getInt("nextPayAmt")>0){
					btnBottomRight.setVisibility(View.VISIBLE);
				}else{
					btnBottomRight.setVisibility(View.GONE);
				}

			}else{//房东
				tvHouser.setText(mResult.getString("tenantName"));
				tvHouseState.setText("租客信息");
				rlHouseTop.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try {
							Intent intent = new Intent(getActivity(), SHContainerActivity.class);
							intent.putExtra("class", HouseContactFragment.class.getName());
							intent.putExtra("name", "租客信息");
							intent.putExtra("id", mResult.getInt("tenantId"));
							intent.putExtra("orderId",getActivity().getIntent().getIntExtra("orderId", 0));
							intent.putExtra("pageType", 2);//1房东   2房客
							startActivity(intent);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
//    			退租金 70 80
				if(mResult.getInt("orderStatus") == 70 || mResult.getInt("orderStatus") == 80){
					btnHouseBottomRight.setVisibility(View.VISIBLE);
				}else{
					btnHouseBottomRight.setVisibility(View.GONE);
				}
				btnHouseBottomRight.setText("退押金");
				btnHouseBottomRight.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try {
							refundWager(mResult.getDouble("wagerAmt"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

				btnBottomLeft.setText("退杂费");
				btnBottomLeft.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						playOtherAmt(1);

					}
				});

				btnBottomRight.setText("提醒交租");
				btnBottomRight.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						taskRemind = new SHPostTaskM();
						taskRemind.setUrl(ConfigDefinition.URL+"AlertPayRentAmt");
						taskRemind.getTaskArgs().put("orderId", getActivity().getIntent().getIntExtra("orderId", 0));
						taskRemind.setListener(HouseRentalDetailFragment.this);
						taskRemind.start();

					}
				});
			}
//			交杂费 退杂费 [ 60,90) 
			if(mResult.getInt("orderStatus") >=60 &&mResult.getInt("orderStatus")<90 ){
				btnBottomLeft.setVisibility(View.VISIBLE);
			}else{
				btnBottomLeft.setVisibility(View.GONE);
			}
			//提醒交租 
			if((mResult.getInt("orderStatus") ==60|| mResult.getInt("orderStatus") ==70) && mResult.getInt("nextPayAmt")>0){
				btnBottomRight.setVisibility(View.VISIBLE);
			}else{
				btnBottomRight.setVisibility(View.GONE);
			}
			
			
			
			
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
	/**
	 * 收缴杂费
	 */
	private void playOtherAmt(final int type){
		if(!isSetPass){
			Intent intent  = new Intent(getActivity(),SHContainerActivity.class);
			intent.putExtra("class", HouseChangePayPassword.class.getName());
			startActivity(intent);
		}else{

			showDialog(type == 1?"退杂费":"缴杂费", true, "", new  View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(etDilogMoney.getText().toString().trim().isEmpty()){
						SHToast.showToast(getActivity(), "请输入金额");
						return;
					}
					float money = Float.parseFloat(etDilogMoney.getText().toString().trim());
					String pass = etDilogPass.getText().toString().trim();
					if(money <= 0){
						SHToast.showToast(getActivity(), "请输入金额");
						return;
					}
					if(pass.isEmpty()){
						SHToast.showToast(getActivity(), "请输入密码");
						return;
					}
					if(amount >= money){
						taskPayOther = new SHPostTaskM();
						taskPayOther.setUrl(ConfigDefinition.URL+"PayOtherAmt");
						taskPayOther.getTaskArgs().put("otherType", type);//1.	退杂费 	2.	缴杂费
						taskPayOther.getTaskArgs().put("orderid", getActivity().getIntent().getIntExtra("orderId", 0));
						taskPayOther.getTaskArgs().put("password", CommonUtil.encodeMD5(pass));
						taskPayOther.getTaskArgs().put("otherAmt", new BigDecimal(money).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
						taskPayOther.setListener(HouseRentalDetailFragment.this);
						taskPayOther.start();
					}else{
						aliPayMoney = money - amount;
						requestOrderId(type == 1?6:3);
					}
					dismissDialog();
				}
			});
		}

	}

	/**
	 * 叫租金
	 */
	private void PayNextRent(final int nextPayAmt ,final int nextMonths){
		if(!isSetPass){
			Intent intent  = new Intent(getActivity(),SHContainerActivity.class);
			intent.putExtra("class", HouseChangePayPassword.class.getName());
			startActivity(intent);
		}else{
			showDialog("交租金",false,nextPayAmt+"", new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String pass = etDilogPass.getText().toString().trim();
					if(pass.isEmpty()){
						SHToast.showToast(getActivity(), "请输入密码");
						return;
					}
					if(amount >= nextPayAmt){
						taskNextPay = new SHPostTaskM();
						taskNextPay.setUrl(ConfigDefinition.URL+"PayNextRent");
						taskNextPay.getTaskArgs().put("orderid", getActivity().getIntent().getIntExtra("orderId", 0));
						taskNextPay.getTaskArgs().put("password",CommonUtil.encodeMD5(pass));
						taskNextPay.getTaskArgs().put("rentPay",nextPayAmt);
						taskNextPay.getTaskArgs().put("payMonth",nextMonths);
						taskNextPay.setListener(HouseRentalDetailFragment.this);
						taskNextPay.start();
					}else{
						aliPayMoney = nextPayAmt - amount;
						requestOrderId(8);
					}
					
					dismissDialog();
				}
			});
		}
	}
	/**
	 * 退押金
	 * @throws JSONException 
	 */
	private void refundWager(final Double totalWagerAmt ) {
		if(!isSetPass){
			Intent intent  = new Intent(getActivity(),SHContainerActivity.class);
			intent.putExtra("class", HouseChangePayPassword.class.getName());
			startActivity(intent);
		}else{
			showDialog("退押金", false,totalWagerAmt +"", new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String pass = etDilogPass.getText().toString().trim();
					if(pass.isEmpty()){
						SHToast.showToast(getActivity(), "请输入密码");
						return;
					}
					if(amount >= totalWagerAmt){
						taskRefund = new SHPostTaskM();
						taskRefund.setUrl(ConfigDefinition.URL+"RefundWager");
						taskRefund.getTaskArgs().put("orderid", getActivity().getIntent().getIntExtra("orderId", 0));
						taskRefund.getTaskArgs().put("password", CommonUtil.encodeMD5(pass));
						taskRefund.getTaskArgs().put("totalWagerAmt", totalWagerAmt);
						taskRefund.setListener(HouseRentalDetailFragment.this);
						taskRefund.start();
					}else{
						aliPayMoney = totalWagerAmt - amount;
						requestOrderId(7);
					}
					dismissDialog();
				}
			});
		}

	}
	private void showDialog(String title,boolean enable,String content,View.OnClickListener confirmListener)
	{
		if(dilogPass== null){
			dilogPass = new Dialog(getActivity(),R.style.dialog);
			dilogPass.setContentView(R.layout.dialog_salary);
			tvDilogTitle = (TextView) dilogPass.findViewById(R.id.tv_title);
			etDilogMoney = (EditText) dilogPass.findViewById(R.id.et_money);
			etDilogPass = (EditText) dilogPass.findViewById(R.id.et_password);
			btnDilogCancle =(Button) dilogPass.findViewById(R.id.btn_cancle);
			btnDilogComfirm =(Button) dilogPass.findViewById(R.id.btn_confirm);
		}
		dilogPass.show();

		tvDilogTitle.setText(title);
		etDilogMoney.setText(content);
		etDilogMoney.setEnabled(enable);
		etDilogPass.setText("");
		btnDilogCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismissDialog();
			}
		});
		btnDilogComfirm.setOnClickListener(confirmListener);
	}
	private void dismissDialog(){
		if(dilogPass != null){
			dilogPass.dismiss();
		}
	}




}
