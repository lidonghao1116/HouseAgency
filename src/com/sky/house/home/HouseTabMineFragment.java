package com.sky.house.home;

import java.net.URLEncoder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;
import com.sky.house.me.HouseBalanceFragment;
import com.sky.house.me.HouseMessageFragment;
import com.sky.house.me.HouseSettingFragment;
/**
 * 我的
 * @author skypan
 *
 */
public class HouseTabMineFragment extends BaseFragment implements OnClickListener{
	@ViewInit(id = R.id.rl_myinfo, onClick = "onClick")
	private RelativeLayout rlMyInfo;
	@ViewInit(id = R.id.rl_balance, onClick = "onClick")
	private RelativeLayout rlBalance;
	@ViewInit(id = R.id.rl_points, onClick = "onClick")
	private RelativeLayout rlScord;
	@ViewInit(id = R.id.btn_tenant, onClick = "onClick")
	private Button btnTenant;
	@ViewInit(id = R.id.btn_landlord, onClick = "onClick")
	private Button btnLandlord;
	@ViewInit(id = R.id.rl_message, onClick = "onClick")
	private RelativeLayout rlMessage;
	@ViewInit(id = R.id.rl_store, onClick = "onClick")
	private RelativeLayout rlStore;
	@ViewInit(id = R.id.rl_complaint, onClick = "onClick")
	private RelativeLayout rlComplaint;
	@ViewInit(id = R.id.rl_feedback, onClick = "onClick")
	private RelativeLayout rlFeedback;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_mine, container, false);
		return view;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("我的");
		mDetailTitlebar.setLeftButton(null, null);
		mDetailTitlebar.setRightButton1("设置", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),SHContainerActivity.class);
				intent.putExtra("class", HouseSettingFragment.class.getName());
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(),SHContainerActivity.class);
		switch (v.getId()) {
		case R.id.rl_myinfo:
			intent.putExtra("class", HouseBalanceFragment.class.getName());
			break;
		case R.id.rl_balance:
			intent.putExtra("class", HouseBalanceFragment.class.getName());
			break;
		case R.id.rl_points:
			intent.putExtra("class", HouseBalanceFragment.class.getName());
			break;
		case R.id.btn_tenant:
			intent.putExtra("class", HouseBalanceFragment.class.getName());
			break;
		case R.id.btn_landlord:
			intent.putExtra("class", HouseBalanceFragment.class.getName());
			break;
		case R.id.rl_message:
			intent.putExtra("class", HouseMessageFragment.class.getName());
			break;
		case R.id.rl_store:
			intent.putExtra("class", HouseBalanceFragment.class.getName());
			break;
		case R.id.rl_complaint:
			intent.putExtra("class", HouseBalanceFragment.class.getName());
			break;
		case R.id.rl_feedback:
			intent.putExtra("class", HouseBalanceFragment.class.getName());
			break;

		default:
			break;
		}

		startActivity(intent);
	}

}
