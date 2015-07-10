package com.sky.house.home;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

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
import com.sky.house.resource.HTMLFragment;
import com.sky.widget.SHDialog;
import com.sky.widget.SHToast;
import com.sky.widget.TimerButton;
import com.sky.widget.sweetdialog.SweetDialog;

/**
 * 登录
 * 
 * @author skypan
 * 
 */
public class HouseLoginFragment extends BaseFragment implements ITaskListener {

	@ViewInit(id = R.id.tv_agreement, onClick = "onClick")
	private TextView mTvAgreement;

	@ViewInit(id = R.id.btn_timer, onClick = "onClick")
	private TimerButton mBtnTimer;

	@ViewInit(id = R.id.et_phone)
	private EditText mEtPhone;

	@ViewInit(id = R.id.et_validate)
	private EditText mEtValidate;
	
	@ViewInit(id = R.id.btn_login,onClick = "onClick")
	private Button mBtnLogin;

	private SHPostTaskM validateTask, loginTask;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("登录");
		mTvAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		return view;
	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_agreement:
			Intent intent = new Intent(getActivity(), SHContainerActivity.class);
			intent.putExtra("class", HTMLFragment.class.getName());
			intent.putExtra("title", "服务协议及隐私政策");
			intent.putExtra("url", "http://www.isheely.com/SunnyRent/CFDH/Display/ServiceAndPrivate.aspx");
			startActivity(intent);
			break;
		case R.id.btn_login:
			login();
			break;
		case R.id.btn_timer:
			requestYZM();
			break;
		}
	}

	private void login() {
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
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		loginTask = new SHPostTaskM();
		loginTask.setListener(this);
		loginTask.setUrl(ConfigDefinition.URL + "checkCodeAndRegister");
//		SHEnvironment.getInstance().setLoginId(phone);
		loginTask.getTaskArgs().put("Mobile", phone);
		loginTask.getTaskArgs().put("Code", validate);
		loginTask.getTaskArgs().put("Token_jpush", JPushInterface.getRegistrationID(getActivity()));
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
		validateTask.getTaskArgs().put("SmsType", 1);
		SHEnvironment.getInstance().setLoginId(mEtPhone.getText().toString());
		validateTask.start();
	}

	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		JSONObject json = (JSONObject) task.getResult();
		if (task == validateTask) {
//			mEtValidate.setText(json.getString("Code"));
		} else if (task == loginTask) {
			ConfigDefinition.isAuth = json.optBoolean("isAuth");
			SHEnvironment.getInstance().setSession(json.getString("SessionId"));
			UserInfoManager.getInstance().setAutoLogin(true);
			UserInfoManager.getInstance().setSession(json.getString("SessionId"));
			UserInfoManager.getInstance().setAuth(json.optBoolean("isAuth"));
			UserInfoManager.getInstance().setMoblie(mEtPhone.getText().toString().trim());
			UserInfoManager.getInstance().setPassword(mEtValidate.getText().toString().trim());
			UserInfoManager.getInstance().sync(getActivity(), true);
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
