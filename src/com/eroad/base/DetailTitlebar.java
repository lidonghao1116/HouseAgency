package com.eroad.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sky.house.R;

public class DetailTitlebar extends RelativeLayout {
	private TextView titleText;
	private TextView backButton;
	private TextView rightButton1,rightButton2;
	private TextView subTitleText;
	private LinearLayout ll_right1,ll_right2;

	public DetailTitlebar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DetailTitlebar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DetailTitlebar(Context context) {
		super(context);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		backButton = (TextView) findViewById(R.id.btn_back);
		titleText = (TextView) findViewById(android.R.id.title);
		rightButton1 = (TextView) findViewById(R.id.right_button1);
		rightButton2 = (TextView) findViewById(R.id.right_button2);
		subTitleText = (TextView) findViewById(R.id.tv_sub_title);
		ll_right1 = (LinearLayout) findViewById(R.id.ll_right1);
		ll_right2 = (LinearLayout) findViewById(R.id.ll_right2);
	}

	public void setTitle(CharSequence title) {
		if (titleText != null)
			titleText.setText(title);
	}

	public void setSubTitle(CharSequence title) {
		if (subTitleText != null) {
			subTitleText.setText(title);
		}
	}

	public void setRightButton1(String text, OnClickListener listener) {
		rightButton1.setText(text);
		ll_right1.setOnClickListener(listener);
		ll_right1.setVisibility(View.VISIBLE);
	}

	public void setRightButton1(int res, OnClickListener listener) {
		ll_right1.setOnClickListener(listener);
		ll_right1.setVisibility(View.VISIBLE);
		rightButton1.setBackgroundResource(res);
	}
	
	public void setRightButton2(String text, OnClickListener listener) {
		rightButton2.setText(text);
		ll_right2.setOnClickListener(listener);
		ll_right2.setVisibility(View.VISIBLE);
	}

	public void setRightButton2(int res, OnClickListener listener) {
		ll_right2.setOnClickListener(listener);
		ll_right2.setVisibility(View.VISIBLE);
		rightButton2.setBackgroundResource(res);
	}

	public void setLeftButton(String text, OnClickListener listener) {
		if(text != null){
			backButton.setBackgroundResource(0);
			backButton.setText(text);
			backButton.setOnClickListener(listener);
			backButton.setVisibility(View.VISIBLE);
		}else{
			backButton.setCompoundDrawables(null, null, null, null);
			backButton.setClickable(false);
		}
		
	}

	public void setLeftButton(int res, OnClickListener listener) {
		backButton.setOnClickListener(listener);
		Drawable drawable = getResources().getDrawable(res);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		backButton.setCompoundDrawables(drawable, null, null, null);
		backButton.setVisibility(View.VISIBLE);
	}

	public void setLeftButton(int res, String text, OnClickListener listener) {
		backButton.setOnClickListener(listener);
		Drawable drawable = getResources().getDrawable(res);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		backButton.setCompoundDrawables(drawable, null, null, null);
		backButton.setText(text);
		backButton.setVisibility(View.VISIBLE);
	}

}
