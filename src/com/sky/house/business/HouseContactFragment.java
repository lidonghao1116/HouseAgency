package com.sky.house.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHApplication;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.widget.SHDialog;
import com.sky.widget.sweetdialog.SweetDialog;

/**
 * 联系看房
 * 
 * @author skypan
 * 
 */
public class HouseContactFragment extends BaseFragment implements ITaskListener{

	@ViewInit(id = R.id.btn_pay, onClick = "onClick")
	private Button mBtnPay;

	@ViewInit(id = R.id.iv_question, onClick = "onClick")
	private ImageView mIvQuestion;

	@ViewInit(id = R.id.ll_tips)
	private LinearLayout mLlQuestion;

	private boolean isTipsShow;

	private SHPostTaskM task;
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle(getActivity().getIntent().getStringExtra("name"));
		request();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_contact, container, false);
		return view;
	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_pay:
			Intent intent = new Intent(getActivity(), SHContainerActivity.class);
			intent.putExtra("class", HousePayChargeFragment.class.getName());
			startActivity(intent);
			break;
		case R.id.iv_question:
			if (isTipsShow) {
				mLlQuestion.setVisibility(View.GONE);
				isTipsShow = false;
			} else {
				mLlQuestion.setVisibility(View.VISIBLE);
				isTipsShow = true;
			}
			break;
		}
	}
	
	private void request(){
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		task = new SHPostTaskM();
		task.setListener(this);
		task.setUrl(ConfigDefinition.URL+"GetLandlordDetail");
		task.getTaskArgs().put("houseDetailId", getActivity().getIntent().getIntExtra("id", -1));
		task.start();
	}

	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
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
