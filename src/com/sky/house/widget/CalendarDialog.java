package com.sky.house.widget;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sky.house.R;
import com.sky.widget.calendar.KCalendar;
import com.sky.widget.calendar.KCalendar.OnCalendarClickListener;
import com.sky.widget.calendar.KCalendar.OnCalendarDateChangedListener;

public class CalendarDialog {

	private Dialog dia;
	private Context context;
	private KCalendar mCalendar;
	private TextView mTvTime,tv_whenever;
	private Button mBtnConfirm;
	private String date;// 选中时间
	private boolean flag;// 
	private CalendarResultListener calendarListener;
	private int type;//
	public static final int TYPE_NO_SUISHI = 1;//没有随时

	public CalendarDialog(Context context, CalendarResultListener listener) {
		this.context = context;
		this.calendarListener = listener;
		dia = new Dialog(context, R.style.CalendarDialog);
		dia.setContentView(R.layout.dialog_calendar);
		dia.setCancelable(true);
		dia.setCanceledOnTouchOutside(true);
	}
	
	public CalendarDialog(Context context, int type,CalendarResultListener listener) {
		this.context = context;
		this.calendarListener = listener;
		dia = new Dialog(context, R.style.CalendarDialog);
		dia.setContentView(R.layout.dialog_calendar);
		dia.setCancelable(true);
		dia.setCanceledOnTouchOutside(true);
		this.type = type;
	}

	public void show() {
		dia.show();
		setDialogPosition();
		findViews();
	}

	public void dismiss() {
		if (dia != null) {
			dia.dismiss();
		}
	}

	private void findViews() {
		mCalendar = (KCalendar) dia.findViewById(R.id.dialog_calendar);
		mTvTime = (TextView) dia.findViewById(R.id.dialog_calendar_time);
		tv_whenever = (TextView) dia.findViewById(R.id.tv_whenever);
		if(type == 1){
			tv_whenever.setVisibility(View.GONE);
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		date = df.format(c.getTime());
		mCalendar.setCalendarDayBgColor(date, R.drawable.calendar_date_focused);
		mCalendar.setOnCalendarClickListener(new OnCalendarClickListener() {

			public void onCalendarClick(int row, int col, String dateFormat) {
				int month = Integer.parseInt(dateFormat.substring(dateFormat.indexOf("-") + 1, dateFormat.lastIndexOf("-")));

				if (mCalendar.getCalendarMonth() - month == 1// 跨年跳转
						|| mCalendar.getCalendarMonth() - month == -11) {
					mCalendar.lastMonth();

				} else if (month - mCalendar.getCalendarMonth() == 1 // 跨年跳转
						|| month - mCalendar.getCalendarMonth() == -11) {
					mCalendar.nextMonth();

				} else {
					mCalendar.removeAllBgColor();
					mCalendar.setCalendarDayBgColor(dateFormat, R.drawable.calendar_date_focused);
					date = dateFormat;// 最后返回给全局 date
				}
			}
		});

		// 监听当前月份
		mCalendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
			public void onCalendarDateChanged(int year, int month) {
				mTvTime.setText(year + "年" + month + "月");
			}
		});

		dia.findViewById(R.id.rl_last_month).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCalendar.lastMonth();
			}
		});
		dia.findViewById(R.id.rl_next_month).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCalendar.nextMonth();
			}
		});

		mBtnConfirm = (Button) dia.findViewById(R.id.btn_confirm);
		mBtnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				calendarListener.onCalendarResult(dia, date, flag);

			}
		});
		tv_whenever.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				calendarListener.onCalendarResult(dia, date, true);
			}
		});
		mTvTime.setText(mCalendar.getCalendarYear() + "年" + mCalendar.getCalendarMonth() + "月");
	}

	/**
	 * dialog 位置 （宽屏，底部）
	 * 
	 * @param dialog
	 */
	private void setDialogPosition() {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dia.getWindow().getAttributes();
		lp.width = (display.getWidth()); // 设置宽度
		lp.x = 0;
		lp.y = display.getHeight();
		dia.getWindow().setAttributes(lp);
	}

	public interface CalendarResultListener {
		public void onCalendarResult(Dialog d, String date, boolean whenever);// 2010-12-12
		// 0:上午
		// 1:下午
	}
}
