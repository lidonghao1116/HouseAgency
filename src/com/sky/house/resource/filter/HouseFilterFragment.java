package com.sky.house.resource.filter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;

/**
 * 筛选
 * 
 * @author skypan
 * 
 */
public class HouseFilterFragment extends BaseFragment {

	@ViewInit(id = R.id.tv_jushi, onClick = "onClick")
	private TextView mTvJushi;
	@ViewInit(id = R.id.tv_fangshi, onClick = "onClick")
	private TextView mTvFangshi;
	@ViewInit(id = R.id.tv_tese, onClick = "onClick")
	private TextView mTvTese;
	@ViewInit(id = R.id.tv_zhuangxiu, onClick = "onClick")
	private TextView mTvZhuangxiu;
	@ViewInit(id = R.id.tv_type, onClick = "onClick")
	private TextView mTvType;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("筛选");
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
		View view = inflater.inflate(R.layout.fragment_filter, container, false);
		return view;
	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_jushi:
			final String[] items_jushi = getResources().getStringArray(R.array.array_jushi);
			new AlertDialog.Builder(getActivity()).setTitle("居室").setItems(items_jushi, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int witch) {
					// TODO Auto-generated method stub
					mTvJushi.setText(items_jushi[witch]);
				}
			}).show();
			break;
		case R.id.tv_fangshi:
			final String[] items_fangshi = getResources().getStringArray(R.array.array_fangshi);
			new AlertDialog.Builder(getActivity()).setTitle("方式").setItems(items_fangshi, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int witch) {
					// TODO Auto-generated method stub
					mTvFangshi.setText(items_fangshi[witch]);
				}
			}).show();
			break;
		case R.id.tv_tese:
			final String[] items_tese = getResources().getStringArray(R.array.array_tese);
			new AlertDialog.Builder(getActivity()).setTitle("特色").setItems(items_tese, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int witch) {
					// TODO Auto-generated method stub
					mTvTese.setText(items_tese[witch]);
				}
			}).show();
			break;
		case R.id.tv_zhuangxiu:
			final String[] items_zhuangxiu = getResources().getStringArray(R.array.array_zhuangxiu);
			new AlertDialog.Builder(getActivity()).setTitle("装修").setItems(items_zhuangxiu, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int witch) {
					// TODO Auto-generated method stub
					mTvZhuangxiu.setText(items_zhuangxiu[witch]);
				}
			}).show();
			break;
		case R.id.tv_type:
			final String[] items_type = getResources().getStringArray(R.array.array_type);
			new AlertDialog.Builder(getActivity()).setTitle("类型").setItems(items_type, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int witch) {
					// TODO Auto-generated method stub
					mTvType.setText(items_type[witch]);
				}
			}).show();
			break;
		}
	}
}
