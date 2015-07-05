package com.sky.house.me;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.car.pay.ali.Keys;
import com.sky.car.pay.ali.Result;
import com.sky.car.pay.ali.Rsa;
import com.sky.house.R;
import com.sky.house.adapter.HouseListAdapter;
import com.sky.widget.SHDialog;
import com.sky.widget.SHToast;
import com.sky.widget.sweetdialog.SweetDialog;

public class HouseRentPieChartFragment extends BaseFragment implements OnChartValueSelectedListener,ITaskListener{
	private TextView tvTitleTime;
	private PieChart mChart;
	private TextView tvTitleDeal;
	private Button btnSubmit;
	private SHPostTaskM taskSubmit,taskHasPass,taskNextPay,taskRemind;
	int type ;
	private boolean isSetPass;
	private Dialog dilogPass;

	TextView tvDilogTitle ;
	EditText etDilogMoney;
	EditText etDilogPass;
	Button btnDilogCancle;
	Button btnDilogComfirm;
	
	private SHPostTaskM getOrderIdTask,accountTask;
	String aliOrderId;
	private static final int RQF_PAY = 1;
	private static final int RQF_LOGIN = 2;
	private double aliPayMoney;//支付宝 支付金额
	private int amount;//余额

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view  = inflater.inflate(R.layout.fragment_rent_piechart, container, false);
		tvTitleTime = (TextView) view.findViewById(R.id.tv_title);
		mChart = (PieChart) view.findViewById(R.id.chart_pie);
		tvTitleDeal = (TextView) view.findViewById(R.id.tv_deal);
		btnSubmit = (Button) view.findViewById(R.id.btn_submit);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mDetailTitlebar.setTitle("租金详情");
		type  = getActivity().getIntent().getIntExtra("type", HouseListAdapter.FLAG_HOUSE_LIST);
		initPieDate();
		if(type  == HouseListAdapter.FLAG_STATE_LIST_LANDLORD){
			btnSubmit.setText("提醒交租");
			btnSubmit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					taskRemind = new SHPostTaskM();
					taskRemind.setUrl(ConfigDefinition.URL+"AlertPayRentAmt");
					taskRemind.getTaskArgs().put("orderId", getActivity().getIntent().getIntExtra("orderId", 0));
					taskRemind.setListener(HouseRentPieChartFragment.this);
					taskRemind.start();				}
			});
			//提醒交租 
			if((getActivity().getIntent().getIntExtra("orderStatus", 0)!=60 && getActivity().getIntent().getIntExtra("orderStatus", 0)!=70) || getActivity().getIntent().getIntExtra("nextPayAmt", 0)<=0){
				btnSubmit.setVisibility(View.INVISIBLE);
			}
		}else{
			btnSubmit.setText("交房租");
			btnSubmit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					PayNextRent(getActivity().getIntent().getIntExtra("nextPayAmt", 0), getActivity().getIntent().getIntExtra("nextPayMonths", 0));
				}
			});
			if(getActivity().getIntent().getIntExtra("orderStatus", 0)!=60 || getActivity().getIntent().getIntExtra("nextPayAmt", 0)<=0){
				btnSubmit.setVisibility(View.INVISIBLE);
			}
			
		}
		
		request();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		requestHasPass();
		requestAccount();
	}
	private void requestHasPass(){

		taskHasPass = new SHPostTaskM();
		taskHasPass.setUrl(ConfigDefinition.URL + "GetUserIsSetPayPassword");
		taskHasPass.setListener(this);
		taskHasPass.start();
	}
	private void request(){
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		taskSubmit = new SHPostTaskM();
		taskSubmit.setListener(this);
		taskSubmit.setUrl(ConfigDefinition.URL + "GetOrderPayDetail");
		taskSubmit.getTaskArgs().put("orderId", getActivity().getIntent().getIntExtra("orderId", -1));
		taskSubmit.start();
	}
	private void initPieDate(){
		mChart.setUsePercentValues(true);
		mChart.setDescription("");
		mChart.setDragDecelerationFrictionCoef(0.95f);

		mChart.setDrawHoleEnabled(true);
		mChart.setHoleColorTransparent(true);
		mChart.setTransparentCircleColor(Color.WHITE);

		mChart.setHoleRadius(58f);
		mChart.setTransparentCircleRadius(61f);

		mChart.setDrawCenterText(true);   

		mChart.setRotationAngle(0);
		// enable rotation of the chart by touch
		mChart.setRotationEnabled(true);

		// mChart.setUnit(" €");
		// mChart.setDrawUnitsInChart(true);

		// add a selection listener
		mChart.setOnChartValueSelectedListener(this);

		mChart.setCenterText("已交租金\n0.0\n押金\n0.0");

		setData(0, 0);

		mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
		// mChart.spin(2000, 0, 360);

		Legend l = mChart.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART);
		l.setXEntrySpace(7f);
		l.setYEntrySpace(5f);
	}
	private void setData(float payAmount, float unPayAmount) {

		// IMPORTANT: In a PieChart, no values (Entry) should have the same
		// xIndex (even if from different DataSets), since no values can be
		// drawn above each other.
		ArrayList<Entry> yVals1 = new ArrayList<Entry>();
		yVals1.add(new Entry((float) payAmount, 0));
		yVals1.add(new Entry((float) unPayAmount, 1));

		ArrayList<String> xVals = new ArrayList<String>();
		xVals.add("已交租金");
		xVals.add("未交租金");

		PieDataSet dataSet = new PieDataSet(yVals1, "");
		dataSet.setSliceSpace(3f);
		dataSet.setSelectionShift(5f);

		// add a lot of colors

		ArrayList<Integer> colors = new ArrayList<Integer>();
		colors.add( Color.rgb(189, 59, 71));
		colors.add( Color.rgb(160, 160, 160));
		dataSet.setColors(colors);

		PieData data = new PieData(xVals, dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(11f);
		data.setValueTextColor(Color.WHITE);
		//	        data.setValueTypeface(tf);
		mChart.setData(data);

		// undo all highlights
		mChart.highlightValues(null);

		mChart.invalidate();
	}
	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
		// TODO Auto-generated method stub
		if (e == null)
			return;
		Log.i("VAL SELECTED",
				"Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
				+ ", DataSet index: " + dataSetIndex);
	}
	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub

	}
	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		JSONObject mResult = (JSONObject) task.getResult() ;
		if(task == taskSubmit){
			tvTitleTime.setText(mResult.getString("tenantDate"));
			tvTitleDeal.setText("已缴纳到"+mResult.getString("hasPayDate"));
			mChart.setCenterText("已交租金\n"+mResult.optDouble("payAmount")+"\n未交租金\n"+mResult.optDouble("unPayAmount"));
			setData((float)mResult.optDouble("payAmount"), (float)mResult.optDouble("unPayAmount"));
		}else if(task == taskHasPass){
			isSetPass  = mResult.getInt("isSet")==0?false:true;
		}else if(task == taskRemind){
			SHToast.showToast(getActivity(), "已成功发送提醒通知");
		}else if(task == accountTask ){//余额查询
			JSONObject accountJson = (JSONObject) task.getResult();
			amount =  accountJson.getInt("amount");
		}else if(task == getOrderIdTask ){
			JSONObject jsonObj = (JSONObject) task.getResult();
			aliOrderId = jsonObj.getString("requestNo");
			alipay();
		}
	}
	@Override
	public void onTaskFailed(SHTask task) {
		// TODO Auto-generated method stub

		SHDialog.dismissProgressDiaolg();
		new SweetDialog(getActivity(), SweetDialog.ERROR_TYPE).setTitleText("提示").setContentText(task.getRespInfo().getMessage()).show();
	}
	@Override
	public void onTaskUpdateProgress(SHTask task, int count, int total) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onTaskTry(SHTask task) {
		// TODO Auto-generated method stub

	}

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
						taskNextPay.setListener(HouseRentPieChartFragment.this);
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
		getOrderIdTask.getTaskArgs().put("payAmt", aliPayMoney+ amount);
		getOrderIdTask.getTaskArgs().put("rechargeAmt", aliPayMoney );//支付金额
		getOrderIdTask.getTaskArgs().put("optType", type);//1 定金 2 房租 3 杂费 4 押金
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
}
