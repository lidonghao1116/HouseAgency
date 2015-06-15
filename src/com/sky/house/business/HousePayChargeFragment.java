package com.sky.house.business;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.sky.widget.SHDialog;
import com.sky.widget.SHDialog.DialogItemClickListener;
import com.sky.widget.SHToast;
import com.sky.widget.sweetdialog.SweetDialog;

/**
 * 支付订金
 * 
 * @author skypan
 * 
 */
public class HousePayChargeFragment extends BaseFragment implements ITaskListener {

	private SHPostTaskM orderTask;
	private JSONObject json;
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
	private ListView mLvZuke;//确认订金列表
	
	@ViewInit(id = R.id.ll_step_pay)
	private LinearLayout mLlStepPay;
	
	@ViewInit(id = R.id.ll_step_agreement)
	private LinearLayout mLlStepAgreement;
	
	@ViewInit(id = R.id.iv_agreement,onClick = "onClick")
	private ImageView mIvAgreement;
	
	private int identification;//0:默认房客  1:房东
	
	private static final int RQF_PAY = 1;
	private static final int RQF_LOGIN = 2;
	
	private SHPostTaskM takebackTask,accountTask;//取回订金  //个人帐户

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("支付订金");
		identification = getActivity().getIntent().getIntExtra("identification", 0);
		requestData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_pay_charge, container, false);
		return view;
	}

	private void onClick(View v){
		switch(v.getId()){
		case R.id.iv_agreement:
			Intent intent = new Intent(getActivity(),SHContainerActivity.class);
			startActivity(intent);
			break;
			
		}
	}
	
	private void requestData() {
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		orderTask = new SHPostTaskM();
		orderTask.setListener(this);
		orderTask.setUrl(ConfigDefinition.URL + "PayDownDetail");
		orderTask.getTaskArgs().put("houseDetailId", getActivity().getIntent().getIntExtra("id", -1));
		orderTask.start();
	}

	private void initView() throws JSONException {
		//房源信息
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
				mLlTese.addView(tv);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mTvYuyueMoney.setText("10%房屋预约金："+json.getString("appointmentAmt"));
		mTvYingFu.setText(json.getString("appointmentAmt"));
		
		//根据状态初始化视图显示
		int status = json.getInt("orderStatus");
		switch (status) {
		case 0:// 初始
			mLlStepAgreement.setVisibility(View.GONE);
			mLlStepPay.setVisibility(View.VISIBLE);
			mBtnPay.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					requestAccount();
				}
			});
			break;
		case 10://已支付订金，等待确认
			mLlStepAgreement.setVisibility(View.GONE);
			mLlStepPay.setVisibility(View.VISIBLE);
			if(identification == 0){//房客
				mTvLabel.setText("我们已通知出租方尽快确认，请稍等哦～");
				mBtnPay.setText("取回订金");
				mBtnPay.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						SHDialog.ShowProgressDiaolg(getActivity(), null);
						takebackTask = new SHPostTaskM();
						takebackTask.setListener(HousePayChargeFragment.this);
						takebackTask.setUrl(ConfigDefinition.URL+"PayDownConfirm");
						takebackTask.getTaskArgs().put("orderId", json.optInt("orderId"));
						takebackTask.getTaskArgs().put("houseDetailId", getActivity().getIntent().getIntExtra("id", -1));
						takebackTask.getTaskArgs().put("payMoney", json.optString("appointmentAmt").substring(1));
						takebackTask.start();
					}
				});
			}else{//房东
				mRlPay.setVisibility(View.GONE);
				mLvZuke.setVisibility(View.VISIBLE);
			}
			break;
		case -1:
			mLlStepAgreement.setVisibility(View.GONE);
			mLlStepPay.setVisibility(View.VISIBLE);
			mBtnPay.setText("等待确认");
			mBtnPay.setEnabled(false);
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
		case 20://已确认定金 待完善合同
			mLlStepPay.setVisibility(View.GONE);
			mLlStepAgreement.setVisibility(View.VISIBLE);
			break;
		}

	}
	
	private void requestAccount(){
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		accountTask = new SHPostTaskM();
		accountTask.setListener(this);
		accountTask.setUrl(ConfigDefinition.URL+"GetMyAccountDetail");
		accountTask.start();
	}
	
	private void pay(double payMoney){
		try {
			String info = getNewOrderInfo(payMoney);
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
					requestData();
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
	
	private String getNewOrderInfo(double payMoney) throws JSONException {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(json.optInt("orderId"));
		sb.append("\"&subject=\"");
		sb.append("阳光租房");
		sb.append("\"&body=\"");
		sb.append("阳光租房");
		sb.append("\"&total_fee=\"");
		sb.append(payMoney);
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode(ConfigDefinition.URL+"notify_url.jsp"));
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
		if(task == orderTask){
			json = (JSONObject) task.getResult();
			initView();
		}else if(task == takebackTask){
			requestData();
		}else if(task == accountTask){
			final JSONObject accountJson = (JSONObject) task.getResult();
			//余额 > 支付金额
			if(accountJson.getInt("amount") >= Double.valueOf(json.optString("appointmentAmt").substring(1))){
				final String[] items = new String[]{"余额支付"};
				SHDialog.showActionSheet(getActivity(), "选择支付方式", items, new DialogItemClickListener() {
					
					@Override
					public void onSelected(String result) {
						// TODO Auto-generated method stub
						if(result.equals(items[0])){
							//调用支付接口
						}
					}
				});
			}else {//余额不足
				final String[] items = new String[]{"支付宝支付"};
				SHDialog.showActionSheet(getActivity(), "选择支付方式", items, new DialogItemClickListener() {
					
					@Override
					public void onSelected(String result) {
						// TODO Auto-generated method stub
						if(result.equals(items[0])){
							//调用支付接口
							double payMoney = Double.valueOf(json.optString("appointmentAmt").substring(1)) - Double.valueOf(String.valueOf(accountJson.optInt("amount")));
							pay(payMoney);
						}
					}
				});
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
}
