package com.sky.house.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHApplication;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.widget.SHDialog;
import com.sky.widget.sweetdialog.SweetDialog;
/**
 * 举报
 * @author skypan
 *
 */
public class HouseReportFragment extends BaseFragment implements ITaskListener{

	@ViewInit(id = R.id.rg_report)
	private RadioGroup mRg;
	
	@ViewInit(id = R.id.btn_confirm,onClick = "onClick")
	private Button mBtnConfirm;
	
	@ViewInit(id = R.id.et_des)
	private EditText mEtContent;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("用户举报");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_report, container, false);
		return view;
	}

	private void onClick(View v){
		switch(v.getId()){
		case R.id.btn_confirm:
			int check = 1;
			switch(mRg.getCheckedRadioButtonId()){
			case R.id.rb_0:
				check = 1;
				break;
			case R.id.rb_1:
				check = 2;
				break;
			case R.id.rb_2:
				check = 3;
				break;
			case R.id.rb_3:
				check = 4;
				break;
			}
			SHDialog.ShowProgressDiaolg(getActivity(), null);
			SHPostTaskM reportTask = new SHPostTaskM();
			reportTask.setListener(this);
			reportTask.setUrl(ConfigDefinition.URL+"AddUserReport");
			reportTask.getTaskArgs().put("complaintType", check);
			reportTask.getTaskArgs().put("complaintHouseDetailId", getActivity().getIntent().getIntExtra("id", -1));
			reportTask.getTaskArgs().put("complaintContent", mEtContent.getText().toString().trim());
			reportTask.start();
			break;
		}
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
