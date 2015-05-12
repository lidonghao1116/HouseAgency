package com.sky.house.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHApplication;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;
import com.sky.widget.sweetdialog.SweetDialog;
import com.sky.widget.sweetdialog.SweetDialog.OnSweetClickListener;

public class HouseSettingFragment extends BaseFragment implements
OnClickListener {
	@ViewInit(id = R.id.rl_password, onClick = "onClick")
	private RelativeLayout rlPassword;
	@ViewInit(id = R.id.rl_service, onClick = "onClick")
	private RelativeLayout rlService;
	@ViewInit(id = R.id.rl_about, onClick = "onClick")
	private RelativeLayout rlAbout;
	@ViewInit(id = R.id.tv_tel, onClick = "onClick")
	private TextView tvTel;
	@ViewInit(id = R.id.btn_signout, onClick = "onClick")
	private Button btnExit;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mDetailTitlebar.setTitle("设置");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent  = new Intent(getActivity(),SHContainerActivity.class);
		switch (v.getId()) {
		case R.id.rl_password:

			break;
		case R.id.rl_service:

			break;
		case R.id.rl_about:

			break;
		case R.id.tv_tel:
			final SweetDialog dia_call = new SweetDialog(SHApplication.getInstance().getCurrentActivity(), SweetDialog.WARNING_TYPE);
			dia_call.setTitleText("提示");
			dia_call.setContentText("是否拨打客服电话4000-391-791？");
			dia_call.showCancelButton(true);
			dia_call.setConfirmClickListener(new OnSweetClickListener() {

				@Override
				public void onClick(SweetDialog sweetAlertDialog) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4000-391-791"));
					startActivity(intent);
				}
			});
			dia_call.show();
			break;
		case R.id.btn_signout:
			final SweetDialog dia_quit = new SweetDialog(SHApplication.getInstance().getCurrentActivity(), SweetDialog.NORMAL_TYPE);
			dia_quit.setTitleText("提示");
			dia_quit.setContentText("是否退出应用？");
			dia_quit.showCancelButton(true);
			dia_quit.setConfirmClickListener(new OnSweetClickListener() {

				@Override
				public void onClick(SweetDialog sweetAlertDialog) {
					// TODO Auto-generated method stub
					SHApplication.getInstance().exitApplication();
				}
			});
			dia_quit.show();
			break;

		default:
			break;
		}
	}

}
