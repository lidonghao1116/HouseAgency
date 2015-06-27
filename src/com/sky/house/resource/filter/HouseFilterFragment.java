package com.sky.house.resource.filter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
	@ViewInit(id = R.id.tv_tese, onClick = "onClick")
	private TextView mTvTese;
	@ViewInit(id = R.id.tv_zhuangxiu, onClick = "onClick")
	private TextView mTvZhuangxiu;
	
	private int roomNum;//居室
	
	private int houseFeature;//特色
	
	private int fitment;//装修
	
	private int houseType;//公寓还是啥的
	
	String[] items_jushi;
	
	String[] items_fangshi;
	
	String[] items_tese;
	
	String[] items_zhuangxiu;
	
	String[] items_type;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("筛选");
		mDetailTitlebar.setRightButton1("确定", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("roomNum", roomNum);
				intent.putExtra("houseFeature", houseFeature);
				intent.putExtra("fitment", fitment);
				intent.putExtra("houseType", houseType);
				getActivity().setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		
		initData();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_filter, container, false);
		return view;
	}

	private void initData(){
		items_jushi = getResources().getStringArray(R.array.array_jushi);
		items_fangshi = getResources().getStringArray(R.array.array_fangshi);
		items_tese = getResources().getStringArray(R.array.array_tese);
		items_zhuangxiu = getResources().getStringArray(R.array.array_fixture);
		items_type = getResources().getStringArray(R.array.array_type);
		
		Intent intent = getActivity().getIntent();
		roomNum = intent.getIntExtra("roomNum", 0);
		houseFeature = intent.getIntExtra("houseFeature", 0);
		fitment = intent.getIntExtra("fitment", 0);
		houseType = intent.getIntExtra("houseType", houseType);
		
		mTvJushi.setText(items_jushi[roomNum]);
		mTvTese.setText(items_tese[houseFeature]);
		mTvZhuangxiu.setText(items_zhuangxiu[fitment]);
	}
	
	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_jushi:
			new AlertDialog.Builder(getActivity()).setTitle("居室").setItems(items_jushi, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int witch) {
					// TODO Auto-generated method stub
					mTvJushi.setText(items_jushi[witch]);
					roomNum = witch;
				}
			}).show();
			break;
//		case R.id.tv_fangshi:
//			new AlertDialog.Builder(getActivity()).setTitle("方式").setItems(items_fangshi, new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface arg0, int witch) {
//					// TODO Auto-generated method stub
//					mTvFangshi.setText(items_fangshi[witch]);
//					rentType = witch;
//				}
//			}).show();
//			break;
		case R.id.tv_tese:
			new AlertDialog.Builder(getActivity()).setTitle("特色").setItems(items_tese, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int witch) {
					// TODO Auto-generated method stub
					mTvTese.setText(items_tese[witch]);
					houseFeature = witch;
				}
			}).show();
			break;
		case R.id.tv_zhuangxiu:
			new AlertDialog.Builder(getActivity()).setTitle("装修").setItems(items_zhuangxiu, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int witch) {
					// TODO Auto-generated method stub
					mTvZhuangxiu.setText(items_zhuangxiu[witch]);
					fitment = witch;
				}
			}).show();
			break;
		}
	}
}
