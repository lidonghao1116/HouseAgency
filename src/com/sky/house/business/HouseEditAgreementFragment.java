package com.sky.house.business;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHApplication;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.house.widget.CalendarDialog;
import com.sky.house.widget.CalendarDialog.CalendarResultListener;
import com.sky.widget.SHDialog;
import com.sky.widget.SHToast;
import com.sky.widget.sweetdialog.SweetDialog;

/**
 * 编辑合同
 * 
 * @author skypan
 * 
 */
public class HouseEditAgreementFragment extends BaseFragment implements ITaskListener {

	@ViewInit(id = R.id.btn_start, onClick = "onClick")
	private Button mBtnStart;

	@ViewInit(id = R.id.btn_end, onClick = "onClick")
	private Button mBtnEnd;

	@ViewInit(id = R.id.et_rent)
	private EditText mEtRent;

	@ViewInit(id = R.id.et_yajin)
	private EditText mEtYaJin;

	@ViewInit(id = R.id.et_ya)
	private EditText mEtYa;

	@ViewInit(id = R.id.et_fu)
	private EditText mEtFu;

	@ViewInit(id = R.id.et_chengdan)
	private EditText mEtChengdan;

	@ViewInit(id = R.id.et_beizhu)
	private EditText mEtBeizhu;

	@ViewInit(id = R.id.rg_zhengjian)
	private RadioGroup mRgZhengjian;
	
	@ViewInit(id = R.id.btn_commit,onClick = "onClick")
	private Button mBtnCommit;

	private int zhengjian = 1;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("编辑合同");
//		mDetailTitlebar.setRightButton1("提交", new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				commit();
//			}
//		});
		mRgZhengjian.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg0.getCheckedRadioButtonId()) {
				case R.id.rb_0:
					zhengjian = 1;
					break;
				case R.id.rb_1:
					zhengjian = 2;
					break;
				case R.id.rb_2:
					zhengjian = 3;
					break;
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_hetong, container, false);
		return view;
	}

	private void commit() {
		if (mBtnEnd.getText().toString().compareTo(mBtnStart.getText().toString()) <= 0) {
			SHToast.showToast(getActivity(), "结束时间必须大于开始时间");
			return;
		}
		if(CommonUtil.isEmpty(mEtRent.getText().toString().trim()) || CommonUtil.isEmpty(mEtYaJin.getText().toString().trim()) || CommonUtil.isEmpty(mEtFu.getText().toString().trim()) || CommonUtil.isEmpty(mEtYa.getText().toString().trim())){
			return;
		}
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		SHPostTaskM task = new SHPostTaskM();
		task.setListener(this);
		task.setUrl(ConfigDefinition.URL + "AddContract");
		task.getTaskArgs().put("orderId", getActivity().getIntent().getIntExtra("orderId", -1));
		task.getTaskArgs().put("beginDate", mBtnStart.getText().toString());
		task.getTaskArgs().put("endDate", mBtnEnd.getText().toString());
		task.getTaskArgs().put("monthPrice", mEtRent.getText().toString().trim());
		task.getTaskArgs().put("wagerAmt", mEtYaJin.getText().toString().trim());
		task.getTaskArgs().put("wagerMonth", mEtYa.getText().toString().trim());
		task.getTaskArgs().put("payMonth", mEtFu.getText().toString().trim());
		task.getTaskArgs().put("lordPay", mEtChengdan.getText().toString().trim());
		task.getTaskArgs().put("memo", mEtBeizhu.getText().toString().trim());
		task.getTaskArgs().put("lordHaveCertify", zhengjian);
		task.start();
	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_start:
			CalendarDialog dia_start = new CalendarDialog(getActivity(), CalendarDialog.TYPE_NO_SUISHI, new CalendarResultListener() {

				@Override
				public void onCalendarResult(Dialog d, String date, boolean whenever) {
					// TODO Auto-generated method stub
					d.dismiss();
					mBtnStart.setText(date);
				}
			});
			dia_start.show();
			break;
		case R.id.btn_end:
			CalendarDialog dia_end = new CalendarDialog(getActivity(), CalendarDialog.TYPE_NO_SUISHI, new CalendarResultListener() {

				@Override
				public void onCalendarResult(Dialog d, String date, boolean whenever) {
					// TODO Auto-generated method stub
					d.dismiss();
					mBtnEnd.setText(date);
				}
			});
			dia_end.show();
			break;
		case R.id.btn_commit:
			commit();
			break;
		}
	}

	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		getActivity().setResult(Activity.RESULT_OK);
		finish();
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
