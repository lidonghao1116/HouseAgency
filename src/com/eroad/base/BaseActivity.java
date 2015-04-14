package com.eroad.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.ImageView;

public class BaseActivity extends FragmentActivity {
	/**
	 * 
	 */
	private ImageView testMarkImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (this.shouldShowTestMark()) {
			// if (!CommonUtil.isMainServer(this)) {
			// this.setTestInfoHidden(false);
			// }
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (testMarkImageView != null && testMarkImageView.getParent() != null) {
			WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
			wm.removeView(testMarkImageView);
			testMarkImageView = null;
		}

	}

	protected boolean shouldShowTestMark() {
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SHApplication.getInstance().setCurrentActivity(this);
	}


	@Override
	protected void onStop() {
		super.onStop();

	}

}
