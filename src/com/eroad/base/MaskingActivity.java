package com.eroad.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.sky.house.R;
/**
 * 半透明蒙层引导页
 * v1.0
 * @author skypan
 *
 */
public class MaskingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(getIntent().getIntExtra("layoutID", -1));
		findViewById(R.id.view_parent).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(0, R.anim.base_slide_right_out);
			}
		});
	}

}
