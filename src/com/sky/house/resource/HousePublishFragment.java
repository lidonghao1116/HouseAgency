package com.sky.house.resource;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;
import com.sky.house.resource.publish.HouseAddressFragment;
import com.sky.house.resource.publish.HousePublishDetailFragment;
import com.sky.house.resource.publish.HousePublishNextFragment;
import com.sky.house.resource.publish.HouseRentModeFragment;

/**
 * 发布房源
 * 
 * @author skypan
 * 
 */
public class HousePublishFragment extends BaseFragment {

	@ViewInit(id = R.id.ll_address, onClick = "onClick")
	private LinearLayout mLlAddress;

	@ViewInit(id = R.id.ll_add)
	private LinearLayout mLlAdd;

	@ViewInit(id = R.id.iv_add, onClick = "onClick")
	private ImageView mIvAdd;

	@ViewInit(id = R.id.ll_rent_mode, onClick = "onClick")
	private LinearLayout mLlRentMode;

	@ViewInit(id = R.id.ll_detail, onClick = "onClick")
	private LinearLayout mLlDetail;

	@ViewInit(id = R.id.ll_tese,onClick = "onClick")
	private LinearLayout mLlTese;
	
	@ViewInit(id = R.id.tv_tese)
	private TextView mTvTese;
	
	@ViewInit(id = R.id.btn_next,onClick = "onClick")
	private Button mBtnNext;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("发布房源");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_publish_house, container, false);
		return view;
	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_address:
			Intent intent = new Intent(getActivity(), SHContainerActivity.class);
			intent.putExtra("class", HouseAddressFragment.class.getName());
			startActivity(intent);
			break;
		case R.id.iv_add:
			final View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_part_rent, null);
			TextView tvDelete = (TextView) view.findViewById(R.id.tv_delete);
			tvDelete.setVisibility(View.VISIBLE);
			tvDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mLlAdd.removeView(view);
				}
			});
			mLlAdd.addView(view);
			break;
		case R.id.ll_rent_mode:
			Intent intent_rent = new Intent(getActivity(), SHContainerActivity.class);
			intent_rent.putExtra("class", HouseRentModeFragment.class.getName());
			startActivity(intent_rent);
			break;
		case R.id.ll_detail:
			Intent intent_detail = new Intent(getActivity(), SHContainerActivity.class);
			intent_detail.putExtra("class", HousePublishDetailFragment.class.getName());
			startActivity(intent_detail);
			break;
		case R.id.ll_tese:
			final String[] items_tese = getResources().getStringArray(R.array.array_tese);
			new AlertDialog.Builder(getActivity()).setTitle("房源特色").setMultiChoiceItems(items_tese, null, new OnMultiChoiceClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
					// TODO Auto-generated method stub
				}
			}).setPositiveButton("确定", null).setNegativeButton("取消", null).show();
			break;
		case R.id.btn_next:
			Intent intent_next = new Intent(getActivity(), SHContainerActivity.class);
			intent_next.putExtra("class", HousePublishNextFragment.class.getName());
			startActivity(intent_next);
			break;
		}
	}

}
