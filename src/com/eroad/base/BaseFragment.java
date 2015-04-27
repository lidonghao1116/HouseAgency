package com.eroad.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.eroad.base.util.MaskingPreference;
import com.eroad.base.util.SHFrame;
import com.sky.house.R;
import com.sky.widget.SHDialog;

public class BaseFragment extends Fragment implements ISHKeyEvent {

	protected DetailTitlebar mDetailTitlebar;
	
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		return false;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {

		return false;
	}

	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		return false;

	}

	public void finish(){
		getActivity().finish();
		getActivity().overridePendingTransition(0, R.anim.base_slide_right_out);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SHDialog.dismissProgressDiaolg();
			getActivity().finish();
			getActivity().overridePendingTransition(0, R.anim.base_slide_right_out);
		}
		return true;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		new SHFrame(this, view).initFragmentView();
		mDetailTitlebar = (DetailTitlebar) view
				.findViewById(R.id.detailTitlebar);
		if (mDetailTitlebar != null) {
			this.setNabiagtionBar();
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 设置标题栏
	 */
	protected void setNabiagtionBar() {
		mDetailTitlebar.setLeftButton(R.drawable.ic_back,
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onKeyDown(KeyEvent.KEYCODE_BACK, null);
					}
				});
	}
	
	public void startActivity(Intent intent){
		getActivity().startActivity(intent);
		getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
	}
	
	public void setGuideLayout(int id){
		if(!MaskingPreference.viewIsGuided(getActivity(), BaseFragment.this.getClass().getName())){
			MaskingPreference.setViewIsGuided(getActivity(), BaseFragment.this.getClass().getName());
			Intent intent = new Intent(getActivity(),MaskingActivity.class);
			intent.putExtra("layoutID", id);
			getActivity().startActivity(intent);
		}
	}
}
