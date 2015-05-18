package com.sky.house.me;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.eroad.base.BaseFragment;
import com.next.intf.ITaskListener;
import com.next.net.SHTask;

public class HouseRentalDetailFragment extends BaseFragment implements
		OnClickListener, ITaskListener {

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mDetailTitlebar.setTitle("我的记录");
		mDetailTitlebar.setRightButton1("清空", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTaskFailed(SHTask task) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTaskUpdateProgress(SHTask task, int count, int total) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTaskTry(SHTask task) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
