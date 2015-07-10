package com.sky.house.me;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHApplication;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.widget.SHDialog;
import com.sky.widget.sweetdialog.SweetDialog;
import com.sky.widget.sweetdialog.SweetDialog.OnSweetClickListener;

public class HouseBalanceFragment extends BaseFragment implements
OnClickListener,ITaskListener {

	@ViewInit(id = R.id.rl_acconut, onClick = "onClick")
	private RelativeLayout rlAccount;
	@ViewInit(id = R.id.rl_bank, onClick = "onClick")
	private RelativeLayout rlBank;
	@ViewInit(id = R.id.rl_frozen, onClick = "onClick")
	private RelativeLayout rlFrozen;

	@ViewInit(id = R.id.tv_account)
	private TextView tvAccount;
	@ViewInit(id = R.id.tv_bank)
	private TextView tvBank;
	@ViewInit(id = R.id.tv_frozen)
	private TextView tvFrozen;

	private JSONObject mResultBalance = new JSONObject();
	
	private SHPostTaskM taskHasPass,taskBalance;
	
	private boolean isSetPass;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_balance, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mDetailTitlebar.setTitle("余额");
		mDetailTitlebar.setRightButton1("记录", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),SHContainerActivity.class);
				intent.putExtra("class", HouseRechargeRecordList.class.getName());
				startActivity(intent);
			}
		});
		
	}
	private void requestHasPass(){
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		taskHasPass = new SHPostTaskM();
		taskHasPass.setUrl(ConfigDefinition.URL + "GetUserIsSetPayPassword");
		taskHasPass.setListener(this);
		taskHasPass.start();
	}
	/**
	 * 账号金额信息 
	 */
	private void requestBalanceInfo(){
		taskBalance = new SHPostTaskM();
		taskBalance.setUrl(ConfigDefinition.URL+"GetMyAccountDetail");
		taskBalance.setListener(this);
		taskBalance.start();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		requestHasPass();
		requestBalanceInfo();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent  = new Intent(getActivity(),SHContainerActivity.class);
		switch (v.getId()) {
		case R.id.rl_acconut:
			intent.putExtra("class",HouseCardList.class.getName());
			break;
		case R.id.rl_bank:
			if(isSetPass){
				intent.putExtra("class",HouseCardList.class.getName());
				intent.putExtra("detail", mResultBalance.toString());
				startActivity(intent);
			}else{
				intent.putExtra("class", HouseChangePayPassword.class.getName());
				startActivity(intent);
			}
			break;
		case R.id.rl_frozen:

			break;
		}
	}
	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		if(task  ==  taskHasPass){
			JSONObject object  = (JSONObject) task.getResult() ;
			isSetPass  = object.getInt("isSet")==0?false:true;
		}else if(task == taskBalance){
			mResultBalance = (JSONObject) task.getResult();
			tvFrozen.setText(mResultBalance.optDouble("frozenAmt")+"");
		}
		
	}
	@Override
	public void onTaskFailed(SHTask task) {
		// TODO Auto-generated method stub
		
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
