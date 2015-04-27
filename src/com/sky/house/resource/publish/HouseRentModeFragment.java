package com.sky.house.resource.publish;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;
import com.sky.house.widget.CalendarDialog;
import com.sky.house.widget.CalendarDialog.CalendarResultListener;

/**
 * 租金方式
 * 
 * @author skypan
 * 
 */
public class HouseRentModeFragment extends BaseFragment {

	@ViewInit(id = R.id.ll_time, onClick = "onClick")
	private LinearLayout mLlTime;

	@ViewInit(id = R.id.tv_time)
	private TextView mTvTime;
	
	@ViewInit(id = R.id.ll_rent_type,onClick = "onClick")
	private LinearLayout mLlRentType;
	
	@ViewInit(id = R.id.tv_rent_type)
	private TextView mTvRentType;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("租金方式");
		mDetailTitlebar.setRightButton1("确定", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_rent_mode, container, false);
		return view;
	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_time:
			CalendarDialog dia = new CalendarDialog(getActivity(), new CalendarResultListener() {

				@Override
				public void onCalendarResult(Dialog d, String date, boolean whenever) {
					// TODO Auto-generated method stub
					d.dismiss();
					if (whenever) {
						mTvTime.setText("随时入住");
					} else {
						mTvTime.setText(date);
					}
				}
			});
			dia.show();
			break;
		case R.id.ll_rent_type:
			final String[] items_rent = getResources().getStringArray(R.array.array_rent_type);
			new AlertDialog.Builder(getActivity()).setTitle("收租方式").setItems(items_rent, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int witch) {
					// TODO Auto-generated method stub
					mTvRentType.setText(items_rent[witch]);
				}
			}).show();
			break;
		}
	}
}
