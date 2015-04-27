package com.sky.house.resource;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;
import com.sky.house.business.HouseContactFragment;
import com.sky.house.interfaces.ScrollViewListener;
import com.sky.house.widget.MyGridView;
import com.sky.house.widget.ObservableScrollView;
/**
 * 房屋详情
 * @author skypan
 *
 */
public class HouseDetailFragment extends BaseFragment {

	@ViewInit(id = R.id.sv_detail)
	private ObservableScrollView mSvDetail;
	
	@ViewInit(id = R.id.ll_bottom)
	private LinearLayout mLlBottom;
	
	@ViewInit(id = R.id.btn_collect,onClick = "onClick")
	private Button mBtnCollect;
	
	@ViewInit(id = R.id.btn_contact,onClick = "onClick")
	private Button mBtnContact;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setLeftButton(R.drawable.ic_back, "更多房源", new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mDetailTitlebar.setTitle("XXXXXX");
		mDetailTitlebar.setRightButton1("举报", null);
		mDetailTitlebar.setRightButton2("分享", null);
		mSvDetail.setOnScrollListener(new ScrollViewListener() {
			
			@Override
			public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
				// TODO Auto-generated method stub
				if(y<mLlBottom.getHeight()*2){
					mLlBottom.setTranslationY(-y/2);
				}else{
					mLlBottom.setTranslationY(-mLlBottom.getHeight()+1);
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_house_detail, container, false);
		return view;
	}

	private void onClick(View v){
		switch(v.getId()){
		case R.id.btn_collect:
			break;
		case R.id.btn_contact:
			Intent intent = new Intent(getActivity(),SHContainerActivity.class);
			intent.putExtra("class", HouseContactFragment.class.getName());
			startActivity(intent);
			break;
		}
	}
}
