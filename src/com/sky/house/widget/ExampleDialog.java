package com.sky.house.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.sky.house.R;

public class ExampleDialog extends Dialog {

	private Context context;
	private int resID;
	private ExampleDialogOnClick exampleListener; 

	public ExampleDialog(Context context, int id,ExampleDialogOnClick exampleListener) {
		super(context, R.style.DialogStyle);
		this.context = context;
		this.resID = id;
		this.exampleListener = exampleListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();

	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_example, null);
		setContentView(view);
		ImageView ivExample = (ImageView) view.findViewById(R.id.iv_example);
		ivExample.setBackgroundResource(resID);
		Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
		btnConfirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				exampleListener.exampleOnClick(ExampleDialog.this);
			}
		});
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		lp.width = (int) (d.widthPixels * 0.9); // 宽度设置为屏幕的0.9
		dialogWindow.setAttributes(lp);
	}

	public interface ExampleDialogOnClick {
		public void exampleOnClick(Dialog d);
	}
}
