package com.sky.house.me;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.UserInfoManager;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.next.util.SHEnvironment;
import com.sky.house.R;
import com.sky.house.home.HouseRegisterAgreeFragment;
import com.sky.widget.SHDialog;
import com.sky.widget.SHToast;
import com.sky.widget.TimerButton;
import com.sky.widget.sweetdialog.SweetDialog;

public class HouseChangePayPassword extends BaseFragment implements
		ITaskListener {

	@ViewInit(id = R.id.btn_timer, onClick = "onClick")
	private TimerButton mBtnTimer;

	@ViewInit(id = R.id.et_phone)
	private EditText mEtPhone;

	@ViewInit(id = R.id.et_validate)
	private EditText mEtValidate;
	
	@ViewInit(id = R.id.et_pass)
	private EditText mEtPass;
	
	@ViewInit(id = R.id.et_pass_two)
	private EditText mEtPass2;
	
	@ViewInit(id = R.id.btn_login,onClick = "onClick")
	private Button mBtnLogin;
	
	private SHPostTaskM validateTask, loginTask;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_change_paypass, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mDetailTitlebar.setTitle("设置支付密码");
	}
	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			submit();
			break;
		case R.id.btn_timer:
			requestYZM();
			break;
		}
	}

	private void submit() {
		String phone = mEtPhone.getText().toString().trim();
		if (CommonUtil.isEmpty(phone)) {
			SHToast.showToast(getActivity(), "请输入手机号", Toast.LENGTH_SHORT);
			return;
		}
		if (!CommonUtil.isMobile(phone)) {
			SHToast.showToast(getActivity(), "您输入的是手机号码么？可别逗我", Toast.LENGTH_SHORT);
			return;
		}
		String validate = mEtValidate.getText().toString().trim();
		if (CommonUtil.isEmpty(validate)) {
			SHToast.showToast(getActivity(), "请输入验证码", Toast.LENGTH_SHORT);
			return;
		}
		String password = mEtPass.getText().toString().trim();
		if (CommonUtil.isEmpty(password)) {
			SHToast.showToast(getActivity(), "请填写您的密码", Toast.LENGTH_SHORT);
			return;
		}
		String password2 = mEtPass2.getText().toString().trim();
		if (CommonUtil.isEmpty(password2) || !password.equalsIgnoreCase(password2)) {
			SHToast.showToast(getActivity(), "两次输入密码不一致", Toast.LENGTH_SHORT);
			return;
		}
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		loginTask = new SHPostTaskM();
		loginTask.setListener(this);
		loginTask.setUrl(ConfigDefinition.URL + "SetPayPassword");
		loginTask.getTaskArgs().put("smstype", 2);
		loginTask.getTaskArgs().put("password", CommonUtil.encodeMD5(password));//md5
		loginTask.getTaskArgs().put("code", validate);
		loginTask.start();
	}

	private void requestYZM() {
		String phone = mEtPhone.getText().toString().trim();
		if (CommonUtil.isEmpty(phone)) {
			SHToast.showToast(getActivity(), "请输入手机号", Toast.LENGTH_SHORT);
			return;
		}
		if (!CommonUtil.isMobile(phone)) {
			SHToast.showToast(getActivity(), "您输入的是手机号码么？可别逗我", Toast.LENGTH_SHORT);
			return;
		}
		mBtnTimer.start();
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		validateTask = new SHPostTaskM();
		validateTask.setListener(this);
		validateTask.setUrl(ConfigDefinition.URL + "sendCode");
		validateTask.getTaskArgs().put("Mobile", phone);
		validateTask.getTaskArgs().put("SmsType", 2);
		validateTask.start();
	}

	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		JSONObject json = (JSONObject) task.getResult();
		if (task == validateTask) {
			mEtValidate.setText(json.getString("Code"));
		} else if (task == loginTask) {
			SHToast.showToast(getActivity(), "支付密码更新成功", Toast.LENGTH_SHORT);
			finish();
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

}
