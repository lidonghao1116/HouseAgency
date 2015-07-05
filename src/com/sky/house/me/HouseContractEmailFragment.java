package com.sky.house.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.eroad.base.BaseFragment;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.widget.SHDialog;
import com.sky.widget.SHToast;
import com.sky.widget.sweetdialog.SweetDialog;

public class HouseContractEmailFragment extends BaseFragment implements
OnClickListener,ITaskListener {
	@ViewInit(id = R.id.et_name)
	private EditText etName;
	@ViewInit(id = R.id.et_mobile)
	private EditText etMobile;
	@ViewInit(id = R.id.et_address)
	private EditText etAddress;

	@ViewInit(id = R.id.btn_submit,onClick = "onClick")
	private Button btnSubmit;
	private SHPostTaskM taskSubmit;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_contract_email,container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mDetailTitlebar.setTitle("邮寄合同");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_submit:
			sumbitEmail();
			break;

		default:
			break;
		}
	}
	private void sumbitEmail (){
		String name  = etName.getText().toString().trim();
		String mobile  = etMobile.getText().toString().trim();
		String address  = etAddress.getText().toString().trim();
		if(name.isEmpty()){
			SHToast.showToast(getActivity(), "请填写收件人", 0);
			return;
		}
		if(mobile.isEmpty()){
			SHToast.showToast(getActivity(), "请填写联系电话", 0);
			return;
		}
		if(address.isEmpty()){
			SHToast.showToast(getActivity(), "请填写收件地址", 0);
			return;
		}
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		taskSubmit = new SHPostTaskM();
		taskSubmit.setListener(this);
		taskSubmit.setUrl(ConfigDefinition.URL + "ApplySendContract");
		taskSubmit.getTaskArgs().put("orderId", getActivity().getIntent().getIntExtra("orderId", -1));
		taskSubmit.getTaskArgs().put("phoneNum", mobile);
		taskSubmit.getTaskArgs().put("consignee", name);
		taskSubmit.getTaskArgs().put("consigneeAddress",address);
		taskSubmit.start();
		
	}
	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		SHToast.showToast(getActivity(), "发送邮寄合同申请成功", 0);
		getActivity().finish();
		
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
