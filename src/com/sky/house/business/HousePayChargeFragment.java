package com.sky.house.business;

import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
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
import com.eroad.base.util.ViewUtil;
import com.eroad.base.util.location.SHLocationManager;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.car.pay.ali.Keys;
import com.sky.car.pay.ali.Result;
import com.sky.car.pay.ali.Rsa;
import com.sky.house.R;
import com.sky.house.adapter.ZukeListAdapter;
import com.sky.house.adapter.ZukeListAdapter.IOnOrderConfirm;
import com.sky.house.me.HouseChangePayPassword;
import com.sky.house.resource.HTMLFragment;
import com.sky.house.resource.publish.HouseSuccessFragment;
import com.sky.widget.SHDialog;
import com.sky.widget.SHDialog.DialogItemClickListener;
import com.sky.widget.SHToast;
import com.sky.widget.sweetdialog.SweetDialog;
import com.sky.widget.sweetdialog.SweetDialog.OnSweetClickListener;

/**
 * 支付订金
 * 
 * @author skypan
 * 
 */
public class HousePayChargeFragment extends BaseFragment implements ITaskListener {

	private SHPostTaskM orderTask;
	private JSONObject json;// requestData
	private JSONObject payDetailJson;// 支付房租
	@ViewInit(id = R.id.iv_one)
	private ImageView mIvOne;
	@ViewInit(id = R.id.iv_two)
	private ImageView mIvTwo;
	@ViewInit(id = R.id.iv_three)
	private ImageView mIvThree;
	@ViewInit(id = R.id.iv_four)
	private ImageView mIvFour;
	@ViewInit(id = R.id.iv_line_1)
	private ImageView mIvLine1;
	@ViewInit(id = R.id.iv_line_2)
	private ImageView mIvLine2;
	@ViewInit(id = R.id.iv_line_3)
	private ImageView mIvLine3;
	@ViewInit(id = R.id.iv_line_4)
	private ImageView mIvLine4;
	@ViewInit(id = R.id.iv_line_5)
	private ImageView mIvLine5;
	@ViewInit(id = R.id.iv_line_6)
	private ImageView mIvLine6;

	@ViewInit(id = R.id.label_1)
	private TextView mTvLabel;

	@ViewInit(id = R.id.btn_pay)
	private Button mBtnPay;

	@ViewInit(id = R.id.iv_house)
	private ImageView mIvHouse;

	@ViewInit(id = R.id.tv_title)
	private TextView mTvTitle;

	@ViewInit(id = R.id.tv_rent)
	private TextView mTvRent;

	@ViewInit(id = R.id.tv_read_times)
	private TextView mTvReadTimes;

	@ViewInit(id = R.id.tv_rent_type)
	private TextView mTvRentType;

	@ViewInit(id = R.id.ll_tese)
	private LinearLayout mLlTese;

	@ViewInit(id = R.id.tv_yuyue_money)
	private TextView mTvYuyueMoney;

	@ViewInit(id = R.id.tv_yingfu)
	private TextView mTvYingFu;

	@ViewInit(id = R.id.rl_pay_1)
	private RelativeLayout mRlPay;

	@ViewInit(id = R.id.lv_zuke)
	private ListView mLvZuke;// 确认订金列表

	@ViewInit(id = R.id.ll_step_pay)
	private LinearLayout mLlStepPay;

	@ViewInit(id = R.id.ll_step_agreement)
	private LinearLayout mLlStepAgreement;

	@ViewInit(id = R.id.ll_pay_rent)
	private LinearLayout mLlPayRent;

	@ViewInit(id = R.id.iv_agreement, onClick = "onClick")
	private ImageView mIvAgreement;

	@ViewInit(id = R.id.btn_complete)
	private Button mBtnCompleteAgreement;

	@ViewInit(id = R.id.tv_fukuan_type)
	private TextView mTvPayType;

	@ViewInit(id = R.id.tv_month_pay)
	private TextView mTvMonthPay;

	@ViewInit(id = R.id.tv_months)
	private TextView mTvMonths;

	@ViewInit(id = R.id.tv_yajin)
	private TextView mTvYajin;

	@ViewInit(id = R.id.tv_pay_all)
	private TextView mTvPayAll;

	@ViewInit(id = R.id.tv_pay_have)
	private TextView mTvPayHave;

	@ViewInit(id = R.id.tv_need_pay)
	private TextView mTvPayNeed;

	@ViewInit(id = R.id.cb_use_sunny)
	private CheckBox mCbSunny;

	@ViewInit(id = R.id.btn_pay_rent)
	private Button mBtnPayRent;
	
	@ViewInit(id = R.id.btn_edit,onClick = "onClick")
	private Button mBtnEdit;

	private int identification;// 0:默认房客 1:房东

//	private boolean isSetPass;// 是否设置过密码

	private static final int RQF_PAY = 1;
	private static final int RQF_LOGIN = 2;

	private SHPostTaskM takebackTask, accountTask, getOrderIdTask, taskHasPass, payTask, getContactTimeTask, zukeListTask;// 取回订金

	private SHPostTaskM confirmAgreeTask, payDetailTask;
	
	private SHPostTaskM firstGetOrderId;

	private SHPostTaskM payRentTask;
	
	@ViewInit(id = R.id.tv_fangdong)
	private TextView mTvFangDong;
	
	@ViewInit(id = R.id.tv_time)
	private TextView mTvTime;
	
	@ViewInit(id = R.id.label_step1)
	private TextView mLabel1;
	
	@ViewInit(id = R.id.label_step2)
	private TextView mLabel2;
	
	@ViewInit(id = R.id.label_step3)
	private TextView mLabel3;
	
	@ViewInit(id = R.id.label_step4)
	private TextView mLabel4;

	private double payMoney;

	private String orderId;

	private String demoUrl, newUrl;

	private int status;// 订单状态

	ZukeListAdapter zukeAdapter;

	private int payType = 1;// 1:订金 2:房租

	private BroadcastReceiver rec;
	
	int seconds;
	
	private int PaySunnyAmount;
	
	private Timer timer = new Timer();
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("支付订金");
		identification = getActivity().getIntent().getIntExtra("identification", 0);
		orderId = getActivity().getIntent().getStringExtra("orderId");
//		System.out.println("identification:"+identification);
//		if(identification == 0){
			requestData();
//		}else{
//			requestZukeList();
//		}
		requestHasPass();
		rec = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				requestData();
			}
		};
		registerBroadcast();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_pay_charge, container, false);
		return view;
	}

	private void registerBroadcast() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SHLocationManager.BROADCAST_LOCATION);
		intentFilter.addAction("JPUSH_MSG");
		getActivity().registerReceiver(rec, intentFilter);
	}
	
//	private void init(){
//		
//	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_agreement:
			Intent intent = new Intent(getActivity(), SHContainerActivity.class);
			intent.putExtra("class", HTMLFragment.class.getName());
			if (status == 20) {
				intent.putExtra("url", demoUrl);
			} else {
				intent.putExtra("url", newUrl);
			}
			startActivity(intent);
			break;
		case R.id.btn_edit:
			Intent intent2 = new Intent(getActivity(), SHContainerActivity.class);
			intent2.putExtra("class", HouseEditAgreementFragment.class.getName());
			intent2.putExtra("orderId", json.optInt("orderId"));
			startActivityForResult(intent2, 0);
			getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
			break;
		}
	}

	private void requestData() {
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		orderTask = new SHPostTaskM();
		orderTask.setListener(this);
		orderTask.setUrl(ConfigDefinition.URL + "PayDownDetail");
		orderTask.getTaskArgs().put("houseDetailId", getActivity().getIntent().getIntExtra("id", -1));
		orderTask.getTaskArgs().put("optType", getActivity().getIntent().getIntExtra("optType", 0));
		if(identification == 1){//如果是房东，则传orderId，后台根据orderId返回对应的状态，我这边显示不同的试图
			orderTask.getTaskArgs().put("orderId", orderId);
		}
		orderTask.start();
	}

	private void firstGetId(){
		firstGetOrderId = new SHPostTaskM();
		firstGetOrderId.setListener(this);
		firstGetOrderId.setUrl(ConfigDefinition.URL + "AlipayOptInfoAdd");
		firstGetOrderId.getTaskArgs().put("orderId", json.optInt("orderId"));
		firstGetOrderId.getTaskArgs().put("payAmt", json.optString("appointmentAmt").substring(1));
		firstGetOrderId.getTaskArgs().put("rechargeAmt", payMoney);
		firstGetOrderId.getTaskArgs().put("optType", 1);// 1;订金
		firstGetOrderId.start();
	}
	
	private void requestHasPass() {
		taskHasPass = new SHPostTaskM();
		taskHasPass.setUrl(ConfigDefinition.URL + "GetUserIsSetPayPassword");
		taskHasPass.setListener(this);
		taskHasPass.start();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode == Activity.RESULT_OK){
			requestData();
		}
	}

	private void initView() throws JSONException {
		// 房源信息
		ImageLoaderUtil.displayImage(json.getString("houseImgUrl"), mIvHouse);
		mTvTitle.setText(json.getString("houseTitle"));
		mTvRent.setText(json.getString("rentAmt"));
		mTvReadTimes.setText(json.getString("browseCount"));
		mTvRentType.setText(json.getString("payTypeName"));
		JSONArray tese;
		try {
			tese = json.getJSONArray("houseFeature");
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
				if(mLlTese.getChildCount() < 3){
					mLlTese.addView(tv);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTvYuyueMoney.setText("10%房屋订金：" + json.getString("appointmentAmt"));
		mTvYingFu.setText(json.getString("appointmentAmt"));

		// 根据状态初始化视图显示
		status = json.getInt("orderStatus");
		switch (status) {
		case 0:// 初始
			mLlStepAgreement.setVisibility(View.GONE);
			mLlStepPay.setVisibility(View.VISIBLE);
			mBtnPay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// requestOrderId();//调用接口获取某个编号作为orderid
					requestAccount();
				}
			});
			break;
		case 10:// 已支付订金，等待确认
			mLlStepAgreement.setVisibility(View.GONE);
			mLlStepPay.setVisibility(View.VISIBLE);
			if (identification == 0) {// 房客
				mTvLabel.setText("我们已通知出租方尽快确认，请稍等哦～");
				mBtnPay.setText("取回订金");
				mBtnPay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						new SweetDialog(getActivity(), SweetDialog.WARNING_TYPE).setTitleText("提示").setContentText("房东马上确认订金，确认取回吗？").showCancelButton(true).setConfirmClickListener(new OnSweetClickListener() {
							
							@Override
							public void onClick(SweetDialog sweetAlertDialog) {
								// TODO Auto-generated method stub
								sweetAlertDialog.dismiss();
								SHDialog.ShowProgressDiaolg(getActivity(), null);
								takebackTask = new SHPostTaskM();
								takebackTask.setListener(HousePayChargeFragment.this);
								takebackTask.setUrl(ConfigDefinition.URL + "PayDownCancel");
								takebackTask.getTaskArgs().put("orderId", json.optInt("orderId"));
								takebackTask.getTaskArgs().put("houseDetailId", getActivity().getIntent().getIntExtra("id", -1));
								takebackTask.getTaskArgs().put("payMoney", json.optString("appointmentAmt").substring(1));
								takebackTask.start();
								
							}
						}).show();
					}
				});
			} else {// 房东
				mRlPay.setVisibility(View.GONE);
				mLvZuke.setVisibility(View.VISIBLE);
				requestZukeList();
			}
			break;
		case -1:
			mLlStepAgreement.setVisibility(View.GONE);
			mLlStepPay.setVisibility(View.VISIBLE);
			mBtnPay.setText("等待确认");
			mBtnPay.setEnabled(false);
			mBtnPay.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.btn_gray_round));
			break;
		case -2:
			SHToast.showToast(getActivity(), "房东取消订金");
			break;
		case -3:
			SHToast.showToast(getActivity(), "订单取消");
			break;
		case -4:
			SHToast.showToast(getActivity(), "介入取消");
			break;
		case 20:// 已确认定金 待完善合同
			initProgress(2);
			mLlStepPay.setVisibility(View.GONE);
			mLlStepAgreement.setVisibility(View.VISIBLE);
			requestContactTime();// 获取合同范本URL 以及 剩余时间 房东信息等等
			if (identification == 0) {
				mBtnCompleteAgreement.setEnabled(false);
				mBtnCompleteAgreement.setText("房东正在完善合同");
				mBtnCompleteAgreement.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.btn_gray_round));
			} else {
				mBtnCompleteAgreement.setEnabled(true);
				mBtnCompleteAgreement.setText("完善合同");
				mBtnCompleteAgreement.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent2 = new Intent(getActivity(), SHContainerActivity.class);
						intent2.putExtra("class", HouseEditAgreementFragment.class.getName());
						intent2.putExtra("orderId", json.optInt("orderId"));
						startActivityForResult(intent2, 0);
						getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
					}
				});
			}
			break;
		case 30:// 已完善合同，待确认合同
			initProgress(2);
			// SHToast.showToast(getActivity(), "已完善合同，待确认合同");
			mLlStepPay.setVisibility(View.GONE);
			mLlStepAgreement.setVisibility(View.VISIBLE);
			if(timer != null){
				timer.cancel();
			}
			mTvTime.setText("已完善");
			mIvAgreement.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.ic_agreement_done));
			requestContactTime();// 获取合同 以及 剩余时间 房东信息等等
			if (identification == 0) {
				mBtnCompleteAgreement.setEnabled(true);
				mBtnCompleteAgreement.setText("确认合同");
				mBtnCompleteAgreement.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						SHDialog.ShowProgressDiaolg(getActivity(), null);
						confirmAgreeTask = new SHPostTaskM();
						confirmAgreeTask.setListener(HousePayChargeFragment.this);
						confirmAgreeTask.setUrl(ConfigDefinition.URL + "UpdateContractStatus");
						confirmAgreeTask.getTaskArgs().put("orderId", json.optInt("orderId"));
						confirmAgreeTask.getTaskArgs().put("status", 20);
						confirmAgreeTask.start();
					}
				});
			} else {
				mBtnEdit.setVisibility(View.VISIBLE);
				mBtnCompleteAgreement.setEnabled(false);
				mBtnCompleteAgreement.setText("等待确认");
				mBtnCompleteAgreement.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.btn_gray_round));
			}
			break;
		case 40:// 已确认合同，待付款
			initProgress(3);
			// SHToast.showToast(getActivity(), "已确认合同，待付款");
			mLlStepAgreement.setVisibility(View.GONE);
			mLlStepPay.setVisibility(View.VISIBLE);
			mRlPay.setVisibility(View.GONE);
			mLlPayRent.setVisibility(View.VISIBLE);
			if(identification == 0){
				mBtnPayRent.setText("确认支付");
				mBtnPayRent.setEnabled(true);
			}else{
				mBtnPayRent.setText("等待支付");
				mBtnPayRent.setEnabled(false);
				mBtnPayRent.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.btn_gray_round));
				mCbSunny.setVisibility(View.GONE);
			}
			requestPayDetail();
			// asd
			break;
		case 50:// 已付款，待入住
			initProgress(4);
			Intent intent = new Intent(getActivity(), SHContainerActivity.class);
			// 支付成功跳转
			intent.putExtra("class", HouseSuccessFragment.class.getName());
			intent.putExtra("identification", identification);
			intent.putExtra("flag", identification == 0 ? 1:2);
			startActivity(intent);
			// SHToast.showToast(getActivity(), "已付款，待入住");
			break;
		case 60:// 已入住
			initProgress(4);
			// SHToast.showToast(getActivity(), "已入住");
			Intent intent60 = new Intent(getActivity(), SHContainerActivity.class);
			// 支付成功跳转
			intent60.putExtra("class", HouseSuccessFragment.class.getName());
			intent60.putExtra("identification", identification);
			intent60.putExtra("flag", identification == 0 ? 1:2);
			startActivity(intent60);
			break;
		case 70:// 已续租
			initProgress(4);
//			SHToast.showToast(getActivity(), "已续租");
			Intent intent70 = new Intent(getActivity(), SHContainerActivity.class);
			// 支付成功跳转
			intent70.putExtra("class", HouseSuccessFragment.class.getName());
			intent70.putExtra("identification", identification);
			intent70.putExtra("flag", identification == 0 ? 1:2);
			startActivity(intent70);
			break;
		}

	}

	private void initProgress(int step){
		
		if(identification == 0){
			mDetailTitlebar.setTitle("支付订金");
			mLabel1.setText("支付订金");
			mLabel2.setText("签订合同");
			mLabel3.setText("支付房租");
			mLabel4.setText("成功入住");
		}else{
			mDetailTitlebar.setTitle("确认订金");
			mLabel1.setText("确认订金");
			mLabel2.setText("完善合同");
			mLabel3.setText("确认房租");
			mLabel4.setText("成功出租");
		}
		
		
		switch(step){
		case 4:
			mIvFour.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_four_orange));
			mIvLine6.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_line_orange));
			mIvThree.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_three_orange));
			mIvLine5.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_line_orange));
			mIvLine4.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_line_orange));
			mIvTwo.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_two_orange));
			mIvLine3.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_line_orange));
			mIvLine2.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_line_orange));
			if(identification == 0){//房客
				mDetailTitlebar.setTitle("成功入住");
			}else{//房东
				mDetailTitlebar.setTitle("成功出租");
			}
			break;
		case 3:
			mIvThree.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_three_orange));
			mIvLine5.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_line_orange));
			mIvLine4.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_line_orange));
			mIvTwo.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_two_orange));
			mIvLine3.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_line_orange));
			mIvLine2.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_line_orange));
			if(identification == 0){//房客
				mDetailTitlebar.setTitle("支付房租");
			}else{//房东
				mDetailTitlebar.setTitle("确认房租");
			}
			break;
		case 2:
			mIvTwo.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_two_orange));
			mIvLine3.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_line_orange));
			mIvLine2.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.progress_line_orange));
			if(identification == 0){//房客
				mDetailTitlebar.setTitle("签订合同");
			}else{//房东
				mDetailTitlebar.setTitle("完善合同");
			}
			break;
		}
	}
	
	private void requestPayDetail() {
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		payDetailTask = new SHPostTaskM();
		payDetailTask.setListener(this);
		payDetailTask.setUrl(ConfigDefinition.URL + "PayRentDetail");
		payDetailTask.getTaskArgs().put("orderId", json.optInt("orderId"));
		payDetailTask.start();
	}

	private void requestZukeList() {
		zukeListTask = new SHPostTaskM();
		zukeListTask.setUrl(ConfigDefinition.URL + "orderpaydownlist");
		zukeListTask.setListener(this);
		zukeListTask.getTaskArgs().put("housedetailId", getActivity().getIntent().getIntExtra("id", -1));
		zukeListTask.start();
	}

	private void requestContactTime() {
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		getContactTimeTask = new SHPostTaskM();
		getContactTimeTask.setListener(this);
		getContactTimeTask.setUrl(ConfigDefinition.URL + "OrderContractTime");
		getContactTimeTask.getTaskArgs().put("orderId", json.optInt("orderId"));
		getContactTimeTask.start();
	}

	private void requestOrderId() {
		getOrderIdTask = new SHPostTaskM();
		getOrderIdTask.setListener(this);
		getOrderIdTask.setUrl(ConfigDefinition.URL + "AlipayOptInfoAdd");
		getOrderIdTask.getTaskArgs().put("orderId", json.optInt("orderId"));
		
		if(payType == 1){
			getOrderIdTask.getTaskArgs().put("payAmt", json.optString("appointmentAmt").substring(1));//  支付金额  
		}else{
			if(mCbSunny.isChecked()){
				getOrderIdTask.getTaskArgs().put("payAmt", Double.valueOf(mTvPayNeed.getText().toString()) - PaySunnyAmount < 0?0:(Double.valueOf(mTvPayNeed.getText().toString()) - PaySunnyAmount) );//  支付金额  
			}else{
				getOrderIdTask.getTaskArgs().put("payAmt", Double.valueOf(mTvPayNeed.getText().toString()));//  支付金额  
			}
		}
		getOrderIdTask.getTaskArgs().put("PaySunnyAmount", mCbSunny.isChecked()?PaySunnyAmount:0);
		getOrderIdTask.getTaskArgs().put("rechargeAmt", payMoney);//
//		System.out.println("requestOrderId():"+payMoney);
		getOrderIdTask.getTaskArgs().put("optType", payType);// 1;订金
		getOrderIdTask.start();
	}

	private void requestAccount() {
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		accountTask = new SHPostTaskM();
		accountTask.setListener(this);
		accountTask.setUrl(ConfigDefinition.URL + "GetMyAccountDetail");
		accountTask.start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (rec != null) {
			getActivity().unregisterReceiver(rec);
		}
	}

	private void pay() {
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

					// 设置为沙箱模式，不设置默认为线上环境
					// alipay.setSandBox(true);

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
			Toast.makeText(getActivity(), "Failure calling remote service", Toast.LENGTH_SHORT).show();
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
				if (result.isPayOK()) {
					Log.i("支付回调", "支付成功");
					// changeOrderStatus();
					requestData();
				} else {

				}
				break;
			case RQF_LOGIN: {
				Toast.makeText(getActivity(), result.getResult(), Toast.LENGTH_SHORT).show();

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
		sb.append(orderId);
		sb.append("\"&subject=\"");
		sb.append("阳光租房");
		sb.append("\"&body=\"");
		sb.append("阳光租房");
		sb.append("\"&total_fee=\"");
		sb.append(payMoney);
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
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		if (task == orderTask) {
			json = (JSONObject) task.getResult();
			initView();
		} else if (task == takebackTask) {
			requestData();
		} else if (task == accountTask) {
			final JSONObject accountJson = (JSONObject) task.getResult();
			if (payType == 2) {// 支付房租
				// 余额 > 支付金额
				if (accountJson.getInt("amount") >= Double.valueOf(mTvPayNeed.getText().toString())) {
					final String[] items = new String[] { "余额支付" };
					SHDialog.showActionSheet(getActivity(), "选择支付方式", items, new DialogItemClickListener() {

						@Override
						public void onSelected(String result) {
							// TODO Auto-generated method stub
							if (result.equals(items[0])) {
								// 调用支付接口
								if (!ConfigDefinition.hasSetPass) {
									Intent intent = new Intent(getActivity(), SHContainerActivity.class);
									intent.putExtra("class", HouseChangePayPassword.class.getName());
									startActivity(intent);
								} else {
									final Dialog dilogPass = new Dialog(getActivity(), R.style.dialog);
									dilogPass.setContentView(R.layout.dialog_input_password);
									final EditText etPass = (EditText) dilogPass.findViewById(R.id.et_password);
									Button btnCancel = (Button) dilogPass.findViewById(R.id.btn_cancle);
									Button btnConfirm = (Button) dilogPass.findViewById(R.id.btn_confirm);
									dilogPass.show();
									btnCancel.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											// TODO Auto-generated method stub
											dilogPass.dismiss();
										}
									});
									btnConfirm.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											// TODO Auto-generated method stub
											if (etPass.getText().toString().trim().length() == 0) {
												SHToast.showToast(getActivity(), "请输入密码");
												return;
											}
											dilogPass.dismiss();
											SHDialog.ShowProgressDiaolg(getActivity(), null);
											payRentTask = new SHPostTaskM();
											payRentTask.setListener(HousePayChargeFragment.this);
											payRentTask.setUrl(ConfigDefinition.URL + "PayRentEvent");
											payRentTask.getTaskArgs().put("orderId", json.optInt("orderId"));
											payRentTask.getTaskArgs().put("payActualAmount", mTvPayNeed.getText().toString());
											payRentTask.getTaskArgs().put("PaySunnyAmount", mCbSunny.isChecked()?PaySunnyAmount:0);
											payRentTask.getTaskArgs().put("password", CommonUtil.encodeMD5(etPass.getText().toString().trim()));
											payRentTask.start();
										}
									});
								}
							}
						}
					});
				} else {// 余额不足
					final String[] items = new String[] { "支付宝支付" };
					SHDialog.showActionSheet(getActivity(), "选择支付方式", items, new DialogItemClickListener() {

						@Override
						public void onSelected(String result) {
							// TODO Auto-generated method stub
							if (result.equals(items[0])) {
								// 调用支付接口
								payMoney = Double.valueOf(mTvPayNeed.getText().toString()) - Double.valueOf(String.valueOf(accountJson.optInt("amount")));
//								System.out.println("payMoney:"+payMoney);
								requestOrderId();
							}
						}
					});
				}
			} else {
				// 余额 > 支付金额
				if (accountJson.getInt("amount") >= Double.valueOf(json.optString("appointmentAmt").substring(1))) {
					final String[] items = new String[] { "余额支付" };
					SHDialog.showActionSheet(getActivity(), "选择支付方式", items, new DialogItemClickListener() {

						@Override
						public void onSelected(String result) {
							// TODO Auto-generated method stub
							if (result.equals(items[0])) {
								// 调用支付接口
								if (!ConfigDefinition.hasSetPass) {
									Intent intent = new Intent(getActivity(), SHContainerActivity.class);
									intent.putExtra("class", HouseChangePayPassword.class.getName());
									startActivity(intent);
								} else {
									final Dialog dilogPass = new Dialog(getActivity(), R.style.dialog);
									dilogPass.setContentView(R.layout.dialog_input_password);
									final EditText etPass = (EditText) dilogPass.findViewById(R.id.et_password);
									Button btnCancel = (Button) dilogPass.findViewById(R.id.btn_cancle);
									Button btnConfirm = (Button) dilogPass.findViewById(R.id.btn_confirm);
									dilogPass.show();
									btnCancel.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											// TODO Auto-generated method stub
											dilogPass.dismiss();
										}
									});
									btnConfirm.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											// TODO Auto-generated method stub
											if (etPass.getText().toString().trim().length() == 0) {
												SHToast.showToast(getActivity(), "请输入密码");
												return;
											}
											dilogPass.dismiss();
											SHDialog.ShowProgressDiaolg(getActivity(), null);
											payTask = new SHPostTaskM();
											payTask.setListener(HousePayChargeFragment.this);
											System.out.println("-----------");
											payTask.setUrl(ConfigDefinition.URL + "PayDownEvent");
											payTask.getTaskArgs().put("houseDetailId", getActivity().getIntent().getIntExtra("id", -1));
											payTask.getTaskArgs().put("payMoney", json.optString("appointmentAmt").substring(1));
											payTask.getTaskArgs().put("orderId", json.optInt("orderId"));
											payTask.getTaskArgs().put("password", CommonUtil.encodeMD5(etPass.getText().toString().trim()));
											payTask.start();
										}
									});
								}
							}
						}
					});
				} else {// 余额不足
					final String[] items = new String[] { "支付宝支付" };
					SHDialog.showActionSheet(getActivity(), "选择支付方式", items, new DialogItemClickListener() {

						@Override
						public void onSelected(String result) {
							// TODO Auto-generated method stub
							if (result.equals(items[0])) {
								// 调用支付接口
								payMoney = Double.valueOf(json.optString("appointmentAmt").substring(1)) - Double.valueOf(String.valueOf(accountJson.optInt("amount")));
								requestOrderId();
							}
						}
					});
				}
			}

		} else if (task == getOrderIdTask) {
			JSONObject jsonObj = (JSONObject) task.getResult();
			orderId = jsonObj.getString("requestNo");
			pay();
		} else if (task == taskHasPass) {
			JSONObject jsonObj = (JSONObject) task.getResult();
			ConfigDefinition.hasSetPass = jsonObj.getInt("isSet") == 0 ? false : true;
		} else if (task == payTask) {
			requestData();
		} else if (task == getContactTimeTask) {
			JSONObject jsonObj = (JSONObject) task.getResult();
			demoUrl = jsonObj.getString("contractDemoUrl");
			newUrl = jsonObj.getString("contractUrl");
			if(identification == 0){//当前房客
				mTvFangDong.setText("房 东："+jsonObj.optString("lordName"));
			}else{//当前房东
				mTvFangDong.setText("房 客："+jsonObj.optString("rentName"));
			}
			mTvFangDong.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(),SHContainerActivity.class);
					intent.putExtra("class", HouseContactFragment.class.getName());
					if(identification == 0){
						intent.putExtra("pageType", 1);
					}else{
						intent.putExtra("pageType", 2);
					}
					intent.putExtra("id", json.optInt("houseDetailId"));
					intent.putExtra("orderId", json.optInt("orderId"));
					startActivity(intent);
				}
			});
			
			seconds = jsonObj.getInt("LeftTimeSeconds");
			mTvTime.setText(getTime(seconds));
			final Handler handler = new Handler(){ 
		        @Override 
		        public void handleMessage(Message msg){ 
		            switch (msg.what) { 
		            case 1: 
//		            	seconds--;
		                if(seconds < 0){ 
		                    timer.cancel(); 
//		                    txtView.setVisibility(View.GONE); 
		                } else{
		                	mTvTime.setText(getTime(seconds));
		                }
		            } 
		        } 
		    }; 
		  
		    TimerTask timetask = new TimerTask() { 
		        @Override 
		        public void run() { 
		            seconds--; 
		            Message message = new Message(); 
		            message.what = 1; 
		            handler.sendMessage(message); 
		        } 
		    };  
			timer.schedule(timetask, 1000, 1000);       // timeTask
		} else if (task == zukeListTask) {
			JSONObject jsonObj = (JSONObject) task.getResult();
			JSONArray jsonArray = jsonObj.getJSONArray("payDownInfos");
			zukeAdapter = new ZukeListAdapter(getActivity(), jsonArray, new IOnOrderConfirm() {

				@Override
				public void onConfirm(int id) {
					// TODO Auto-generated method stub
					orderId = id+"";
					requestData();
				}
			});
			mLvZuke.setAdapter(zukeAdapter);
			ViewUtil.setListViewHeight(mLvZuke);
		} else if (task == confirmAgreeTask) {
			requestData();
		} else if (task == payDetailTask) {
			JSONObject jsonObj = (JSONObject) task.getResult();
			payDetailJson = jsonObj;
			final JSONObject infoJson = jsonObj.getJSONObject("payInfo");
			mTvPayType.setText(infoJson.getString("payType"));
			mTvMonthPay.setText(infoJson.getString("payMonthAmt"));
			mTvMonths.setText(infoJson.getString("payMonth"));
			mTvYajin.setText(infoJson.getString("payDown"));
			mTvPayAll.setText(infoJson.getInt("shouldPay") + "");
			mTvPayHave.setText(infoJson.getString("havePay"));
			mCbSunny.setText("可用阳光值" + infoJson.getInt("sunny") + "抵扣" + infoJson.getInt("sunny") + "元");
			PaySunnyAmount = infoJson.getInt("sunny");
			mTvPayNeed.setText((infoJson.getInt("shouldPay") - infoJson.getInt("havePay")) + "");
			mCbSunny.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					// TODO Auto-generated method stub
					if (arg1 == true) {
						int temp = infoJson.optInt("shouldPay") - infoJson.optInt("havePay") - infoJson.optInt("sunny");
						if(temp < 0){
							mTvPayNeed.setText( 0 + "");
						}else{
							mTvPayNeed.setText(temp + "");
						}
					}else{
						mTvPayNeed.setText((infoJson.optInt("shouldPay") - infoJson.optInt("havePay"))+"");
					}
				}
			});
			mBtnPayRent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					payType = 2;
					requestAccount();
				}
			});
		} else if (task == payRentTask) {
			System.out.println("success...");
			Intent intent = new Intent(getActivity(), SHContainerActivity.class);
			// 支付成功跳转
			intent.putExtra("class", HouseSuccessFragment.class.getName());
			intent.putExtra("flag", 1);
			startActivity(intent);
		} else if(task == firstGetOrderId){
			JSONObject jsonObj = (JSONObject) task.getResult();
			orderId = jsonObj.getString("requestNo");
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
	
	private String getTime(int secs){
		String str = null;
		int h = secs/3600;
		int mins = (secs - h*3600)/60;
		int s = secs - h*3600 - mins*60;
		str = h+"小时"+mins+"分"+s+"秒";
		return str;
	}
}
