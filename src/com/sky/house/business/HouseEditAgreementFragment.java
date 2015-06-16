package com.sky.house.business;

import org.json.JSONException;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.eroad.base.BaseFragment;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;
import com.sky.house.widget.CalendarDialog;
import com.sky.house.widget.CalendarDialog.CalendarResultListener;

/**
 * 编辑合同
 * @author skypan
 *
 */
public class HouseEditAgreementFragment extends BaseFragment {

	@ViewInit(id = R.id.btn_start,onClick = "onClick")
	private Button mBtnStart;
	
	@ViewInit(id = R.id.btn_end,onClick = "onClick")
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
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("编辑合同");
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_hetong, container, false);
		return view;
	}
	
	private void onClick(View v){
		switch(v.getId()){
		case R.id.btn_start:
			CalendarDialog dia = new CalendarDialog(getActivity(), new CalendarResultListener() {

				@Override
				public void onCalendarResult(Dialog d, String date, boolean whenever) {
					// TODO Auto-generated method stub
					d.dismiss();
//					if (whenever) {
//						mTvTime.setText("随时入住");
//						try {
//							json.put("inTime", "");
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					} else {
//						mTvTime.setText(date);
//						try {
//							json.put("inTime", date);
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
				}
			});
			dia.show();
			break;
		case R.id.btn_end:
			break;
		}
	}

}
