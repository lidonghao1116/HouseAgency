package com.sky.house.resource.publish;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;
/**
 * 各种成功界面
 * @author skypan
 *
 */
public class HouseSuccessFragment extends BaseFragment {

	@ViewInit(id = R.id.tv_option)
	private TextView mTvOption;
	
	@ViewInit(id = R.id.tv_reward)
	private TextView mTvReward;
	
	private int flag;//0:发布房源   1:成功入住
	
	@ViewInit(id = R.id.btn_back,onClick = "onClick")
	private Button mBtnBack;
	
	@ViewInit(id = R.id.view1)
	private View view1;
	
	@ViewInit(id = R.id.view2)
	private View view2;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("发布房源");
		flag = getActivity().getIntent().getIntExtra("flag", 0);
//		view.findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				finish();
////				SHApplication.getInstance().onlyHome();
//			}
//		});
		if(flag == 1){
			mTvOption.setText("成功入住新的小窝");
			mTvReward.setText("成功交易奖励10阳光值");
		}
		if(getActivity().getIntent().getIntExtra("identification", 0) == 1){
			mTvOption.setText("出租房源成功");
			view1.setVisibility(View.INVISIBLE);
			view2.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_success, container, false);
		return view;
	}

	private void onClick(View v){
		switch(v.getId()){
		case R.id.btn_back:
			finish();
			break;
		}
	}
}
