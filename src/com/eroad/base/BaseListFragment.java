package com.eroad.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.View;

import com.sky.house.R;
import com.sky.widget.SHDialog;

public class BaseListFragment extends ListFragment implements ISHKeyEvent {
	protected DetailTitlebar mDetailTitlebar;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SHDialog.dismissProgressDiaolg();
			getActivity().finish();
			getActivity().overridePendingTransition(0, R.anim.base_slide_right_out);
		}
		return true;
	}

	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		return false;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {

		return false;
	}

	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		return false;
	}

	/**
	 * view 创建后调用
	 */
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar = (DetailTitlebar) view.findViewById(R.id.detailTitlebar);
		if (mDetailTitlebar != null) {
			this.setNabiagtionBar();
		}
	}

	/**
	 * 设置标题栏
	 */
	protected void setNabiagtionBar() {
		mDetailTitlebar.setLeftButton(R.drawable.ic_back, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
				getActivity().overridePendingTransition(0, R.anim.base_slide_right_out);
			}
		});
	}

	public void startActivity(Intent intent) {
		getActivity().startActivity(intent);
		getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
	}
}
