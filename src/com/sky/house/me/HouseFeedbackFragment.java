package com.sky.house.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.widget.SHDialog;
import com.sky.widget.SHToast;
import com.sky.widget.sweetdialog.SweetDialog;

public class HouseFeedbackFragment extends BaseFragment implements
OnClickListener, ITaskListener {
	@ViewInit(id = R.id.et_feedback_content)
	private EditText etContent;

	@ViewInit(id = R.id.btn_submit,onClick = "onClick" )
	private Button btn_submit;

	private SHPostTaskM taskSubmit;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_feedback, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mDetailTitlebar.setTitle("我要吐槽");
	}
	private void submit(){
		String content  = etContent.getText().toString().trim();
		if(content.isEmpty()){
			SHToast.showToast(getActivity(), "请先填写您的宝贵意见或建议", SHToast.LENGTH_SHORT);
			return;
		}
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		taskSubmit = new SHPostTaskM();
		taskSubmit.setUrl(ConfigDefinition.URL+"AddUserAppComplain");
		taskSubmit.getTaskArgs().put("content", content);
		taskSubmit.setListener(this);
		taskSubmit.start();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_submit:
			submit();
			break;

		default:
			break;
		}
	}
	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		SHToast.showToast(getActivity(), "感谢您的反馈！",0);
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
