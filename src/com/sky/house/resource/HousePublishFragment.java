package com.sky.house.resource;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.house.resource.publish.HouseAddressFragment;
import com.sky.house.resource.publish.HousePublishDetailFragment;
import com.sky.house.resource.publish.HousePublishNextFragment;
import com.sky.house.resource.publish.HouseRentModeFragment;
import com.sky.widget.SHDialog;
import com.sky.widget.sweetdialog.SweetDialog;

/**
 * 发布房源
 * 
 * @author skypan
 * 
 */
public class HousePublishFragment extends BaseFragment implements ITaskListener {

	@ViewInit(id = R.id.ll_address, onClick = "onClick")
	private LinearLayout mLlAddress;

	@ViewInit(id = R.id.ll_add)
	private LinearLayout mLlAdd;

	@ViewInit(id = R.id.btn_add, onClick = "onClick")
	private Button mBtnAdd;

	@ViewInit(id = R.id.ll_rent_mode, onClick = "onClick")
	private LinearLayout mLlRentMode;

	@ViewInit(id = R.id.ll_detail, onClick = "onClick")
	private LinearLayout mLlDetail;

	@ViewInit(id = R.id.ll_tese, onClick = "onClick")
	private LinearLayout mLlTese;

	@ViewInit(id = R.id.tv_tese)
	private TextView mTvTese;

	@ViewInit(id = R.id.btn_next, onClick = "onClick")
	private Button mBtnNext;

	@ViewInit(id = R.id.rg_type)
	private RadioGroup mRgType;

	private SHPostTaskM publishTask;

	private int type_rent = 0;

	private final int CODE_ADDRESS = 0;// 房源地址
	private final int CODE_DETAIL = 1;// 房屋详情
	private final int CODE_RENT_TYPE = 2;// 租金方式

	private JSONObject json = new JSONObject();// 存储界面参数

	private JSONObject addressJson = new JSONObject();// 房源地址
	private JSONObject detailJson = new JSONObject();// 房源详情
	private JSONObject rentJson = new JSONObject();// 租金方式

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("发布房源");
		mRgType.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.rb_type_0:
					type_rent = 0;
					mLlTese.setVisibility(View.VISIBLE);
					mBtnAdd.setVisibility(View.GONE);
					mLlAdd.removeAllViews();
					clearJson();
					break;
				case R.id.rb_type_1:
					type_rent = 1;
					mLlTese.setVisibility(View.GONE);
					mBtnAdd.setVisibility(View.VISIBLE);
					clearJson();
					break;
				}
			}
		});

		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_publish_house, container, false);
		return view;
	}

	private void clearJson(){
		CommonUtil.JSONUtil.clear(addressJson);//清除数据
		CommonUtil.JSONUtil.clear(detailJson);
		CommonUtil.JSONUtil.clear(rentJson);
	}
	
	private void init() {
		try {
			json.put("lordType", 1);// 房东类型
			json.put("rentType", 1);// 出租类型
			json.put("Gender", 1);// 性别
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 房东类型
	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_address:
			Intent intent = new Intent(getActivity(), SHContainerActivity.class);
			intent.putExtra("class", HouseAddressFragment.class.getName());
			intent.putExtra("json", addressJson.toString());
			startActivityForResult(intent, CODE_ADDRESS);
			getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
			break;
		case R.id.btn_add:
			final View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_part_rent, null);
			LinearLayout llDetail = (LinearLayout) view.findViewById(R.id.ll_detail);
			LinearLayout llRentType = (LinearLayout) view.findViewById(R.id.ll_rent_mode);
			llDetail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), SHContainerActivity.class);
					intent.putExtra("class", HousePublishDetailFragment.class.getName());
					intent.putExtra("type_rent", type_rent);
					startActivityForResult(intent, CODE_DETAIL);
				}
			});
			llRentType.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), SHContainerActivity.class);
					intent.putExtra("class", HouseRentModeFragment.class.getName());
					intent.putExtra("type_rent", type_rent);
					startActivityForResult(intent, CODE_RENT_TYPE);
				}
			});
			TextView tvDelete = (TextView) view.findViewById(R.id.tv_delete);
			tvDelete.setVisibility(View.VISIBLE);
			tvDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mLlAdd.removeView(view);
				}
			});
			mLlAdd.addView(view);
			break;
		case R.id.ll_rent_mode:
			Intent intent_rent = new Intent(getActivity(), SHContainerActivity.class);
			intent_rent.putExtra("class", HouseRentModeFragment.class.getName());
			startActivity(intent_rent);
			break;
		case R.id.ll_detail:
			Intent intent_detail = new Intent(getActivity(), SHContainerActivity.class);
			intent_detail.putExtra("class", HousePublishDetailFragment.class.getName());
			intent_detail.putExtra("type_rent", type_rent);
			startActivityForResult(intent_detail, CODE_DETAIL);
			break;
		case R.id.ll_tese:
			final String[] items_tese_all = getResources().getStringArray(R.array.array_tese);
			String[] items_tese = new String[items_tese_all.length - 1];
			for (int i = 0; i < items_tese_all.length; i++) {
				if (i != 0) {
					items_tese[i - 1] = items_tese_all[i];
				}
			}
			new AlertDialog.Builder(getActivity()).setTitle("房源特色").setMultiChoiceItems(items_tese, null, new OnMultiChoiceClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
					// TODO Auto-generated method stub
				}
			}).setPositiveButton("确定", null).setNegativeButton("取消", null).show();
			break;
		case R.id.btn_next:
			SHDialog.ShowProgressDiaolg(getActivity(), null);
			publishTask = new SHPostTaskM();
			publishTask.setListener(this);
			publishTask.setUrl(ConfigDefinition.URL + "publishhouse");
			publishTask.start();
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && data != null) {
			JSONObject json;
			try {
				json = new JSONObject(data.getStringExtra("json"));
				switch (requestCode) {
				case CODE_ADDRESS:
					addressJson.put("city", json.getString("city"));
					addressJson.put("district", json.getString("district"));
					addressJson.put("houseZoneName", json.getString("houseZoneName"));
					break;
				case CODE_DETAIL:
					break;
				case CODE_RENT_TYPE:
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		if (task == publishTask) {
			Intent intent_next = new Intent(getActivity(), SHContainerActivity.class);
			intent_next.putExtra("class", HousePublishNextFragment.class.getName());
			startActivity(intent_next);
		}
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
