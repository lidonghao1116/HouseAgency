package com.sky.house.me;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.util.ConfigDefinition;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.widget.SHDialog;
import com.sky.widget.sweetdialog.SweetDialog;

public class HouseRentPieChartFragment extends BaseFragment implements OnChartValueSelectedListener,ITaskListener{
	private TextView tvTitleTime;
	private PieChart mChart;
	private TextView tvTitleDeal;
	private Button btnSubmit;
	private SHPostTaskM taskSubmit;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view  = inflater.inflate(R.layout.fragment_rent_piechart, container, false);
		tvTitleTime = (TextView) view.findViewById(R.id.tv_title);
		mChart = (PieChart) view.findViewById(R.id.chart_pie);
		tvTitleDeal = (TextView) view.findViewById(R.id.tv_deal);
		btnSubmit = (Button) view.findViewById(R.id.btn_submit);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mDetailTitlebar.setTitle("租金详情");
		initPieDate();
		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		request();
	}
	private void request(){
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		taskSubmit = new SHPostTaskM();
		taskSubmit.setListener(this);
		taskSubmit.setUrl(ConfigDefinition.URL + "GetOrderPayDetail");
		taskSubmit.getTaskArgs().put("orderId", getActivity().getIntent().getIntExtra("orderId", -1));
		taskSubmit.start();
	}
	private void initPieDate(){
		mChart.setUsePercentValues(true);
		mChart.setDescription("");
		mChart.setDragDecelerationFrictionCoef(0.95f);

		mChart.setDrawHoleEnabled(true);
		mChart.setHoleColorTransparent(true);
		mChart.setTransparentCircleColor(Color.WHITE);

		mChart.setHoleRadius(58f);
		mChart.setTransparentCircleRadius(61f);

		mChart.setDrawCenterText(true);   

		mChart.setRotationAngle(0);
		// enable rotation of the chart by touch
		mChart.setRotationEnabled(true);

		// mChart.setUnit(" €");
		// mChart.setDrawUnitsInChart(true);

		// add a selection listener
		mChart.setOnChartValueSelectedListener(this);

		mChart.setCenterText("已交租金\n0.0\n押金\n0.0");

		setData(0, 0);

		mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
		// mChart.spin(2000, 0, 360);

		Legend l = mChart.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART);
		l.setXEntrySpace(7f);
		l.setYEntrySpace(5f);
	}
	private void setData(float payAmount, float unPayAmount) {

		// IMPORTANT: In a PieChart, no values (Entry) should have the same
		// xIndex (even if from different DataSets), since no values can be
		// drawn above each other.
		ArrayList<Entry> yVals1 = new ArrayList<Entry>();
		yVals1.add(new Entry((float) payAmount, 0));
		yVals1.add(new Entry((float) unPayAmount, 1));

		ArrayList<String> xVals = new ArrayList<String>();
		xVals.add("已交租金");
		xVals.add("未交押金");

		PieDataSet dataSet = new PieDataSet(yVals1, "");
		dataSet.setSliceSpace(3f);
		dataSet.setSelectionShift(5f);

		// add a lot of colors

		ArrayList<Integer> colors = new ArrayList<Integer>();
		colors.add( Color.rgb(189, 59, 71));
		colors.add( Color.rgb(160, 160, 160));
		dataSet.setColors(colors);

		PieData data = new PieData(xVals, dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(11f);
		data.setValueTextColor(Color.WHITE);
		//	        data.setValueTypeface(tf);
		mChart.setData(data);

		// undo all highlights
		mChart.highlightValues(null);

		mChart.invalidate();
	}
	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
		// TODO Auto-generated method stub
		if (e == null)
			return;
		Log.i("VAL SELECTED",
				"Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
				+ ", DataSet index: " + dataSetIndex);
	}
	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub

	}
	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		JSONObject mResult = (JSONObject) task.getResult() ;
		
		mChart.setCenterText("已交租金\n"+mResult.optDouble("payAmount")+"\n未交押金\n"+mResult.optDouble("unPayAmount"));
		setData((float)mResult.optDouble("payAmount"), (float)mResult.optDouble("payAmount"));
	}
	@Override
	public void onTaskFailed(SHTask task) {
		// TODO Auto-generated method stub

		SHDialog.dismissProgressDiaolg();
		new SweetDialog(getActivity(), SweetDialog.ERROR_TYPE).setTitleText("提示").setContentText(task.getRespInfo().getMessage()).show();
	}
	@Override
	public void onTaskUpdateProgress(SHTask task, int count, int total) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onTaskTry(SHTask task) {
		// TODO Auto-generated method stub

	}
}
