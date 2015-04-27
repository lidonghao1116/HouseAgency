package com.sky.house.resource.publish;

import android.app.AlertDialog;
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
import com.sky.house.adapter.GridAdapter;
import com.sky.house.widget.MyGridView;

/**
 * 发布时填写房屋详情
 * 
 * @author skypan
 * 
 */
public class HousePublishDetailFragment extends BaseFragment {

	@ViewInit(id = R.id.ll_room, onClick = "onClick")
	private LinearLayout mLlRoom;

	@ViewInit(id = R.id.ll_sex, onClick = "onClick")
	private LinearLayout mLlSex;

	@ViewInit(id = R.id.ll_fixture, onClick = "onClick")
	private LinearLayout mLlFixture;

	@ViewInit(id = R.id.tv_room)
	private TextView mTvRoom;

	@ViewInit(id = R.id.tv_sex)
	private TextView mTvSex;

	@ViewInit(id = R.id.tv_fixture)
	private TextView mTvFixture;
	
	@ViewInit(id = R.id.gv_electrical)
	private MyGridView mGvElectrical;
	
	@ViewInit(id = R.id.gv_furniture)
	private MyGridView mGvFuniture;
	
	private final String[] facilities = new String[] { "床", "宽带", "电视", "洗衣机", "暖气", "空调", "冰箱", "热水器" };

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("房屋详情");
		mDetailTitlebar.setRightButton1("确定", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mGvElectrical.setAdapter(new GridAdapter(getActivity(), facilities));
		mGvFuniture.setAdapter(new GridAdapter(getActivity(), facilities));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_detail_publish, container, false);
		return view;
	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_room:
			final String[] items_room = getResources().getStringArray(R.array.array_room);
			new AlertDialog.Builder(getActivity()).setTitle("合租类型").setItems(items_room, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int witch) {
					// TODO Auto-generated method stub
					mTvRoom.setText(items_room[witch]);
				}
			}).show();
			break;
		case R.id.ll_sex:
			final String[] items_sex = getResources().getStringArray(R.array.array_sex);
			new AlertDialog.Builder(getActivity()).setTitle("性别限制").setItems(items_sex, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int witch) {
					// TODO Auto-generated method stub
					mTvSex.setText(items_sex[witch]);
				}
			}).show();
			break;
		case R.id.ll_fixture:
			final String[] items_fixture = getResources().getStringArray(R.array.array_fixture);
			new AlertDialog.Builder(getActivity()).setTitle("装修类型").setItems(items_fixture, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int witch) {
					// TODO Auto-generated method stub
					mTvFixture.setText(items_fixture[witch]);
				}
			}).show();
			break;
		}
	}
}
