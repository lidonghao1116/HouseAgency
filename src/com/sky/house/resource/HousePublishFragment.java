package com.sky.house.resource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.sky.widget.SHEditText;
import com.sky.widget.SHToast;
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

	@ViewInit(id = R.id.et_phone)
	private SHEditText mEtPhone;

	@ViewInit(id = R.id.et_des)
	private EditText mEtDes;
	
	@ViewInit(id = R.id.rg_identi)
	private RadioGroup mRgLord;

	@ViewInit(id = R.id.rg_type)
	private RadioGroup mRgType;

//	@ViewInit(id = R.id.rg_sex)
//	private RadioGroup mRgSex;

	private SHPostTaskM publishTask;

	private int type_rent = 0;

	private final int CODE_ADDRESS = 0;// 房源地址
	private final int CODE_DETAIL = 1;// 房屋详情
	private final int CODE_RENT_TYPE = 2;// 租金方式

	private JSONObject json = new JSONObject();// 存储界面参数
	JSONObject jsonObj;// 每天家一个房间 就创建一个实例
	// private JSONObject addressJson = new JSONObject();// 房源地址
	// private JSONObject detailJson = new JSONObject();// 房源详情
	// private JSONObject rentJson = new JSONObject();// 租金方式
	private int roomNum = 1;

	private HashMap<Integer, JSONObject> map = new HashMap<Integer, JSONObject>();

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
					map.clear();
					roomNum = 1;
					try {
						jsonObj = new JSONObject();
						jsonObj.put("roomNum", roomNum);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					map.put(roomNum, jsonObj);
					clearJson();
					break;
				case R.id.rb_type_1:
					type_rent = 1;
					mLlTese.setVisibility(View.GONE);
					mBtnAdd.setVisibility(View.VISIBLE);
					map.clear();
					roomNum = 1;
					try {
						jsonObj = new JSONObject();
						jsonObj.put("roomNum", roomNum);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					map.put(roomNum, jsonObj);
					clearJson();
					break;
				}
			}
		});

		try {
			jsonObj = new JSONObject();
			jsonObj.put("roomNum", roomNum);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put(roomNum, jsonObj);
		mEtDes.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				try {
					map.get(1).put("memo", mEtDes.getText().toString().trim());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		// init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_publish_house, container, false);
		return view;
	}

	private void clearJson() {
		// CommonUtil.JSONUtil.clear(addressJson);// 清除数据
		// CommonUtil.JSONUtil.clear(detailJson);
		// CommonUtil.JSONUtil.clear(rentJson);
	}

	// private void init() {
	// try {
	// json.put("lordType", 1);// 房东类型
	// json.put("rentType", 1);// 出租类型
	// json.put("Gender", 1);// 性别
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }// 房东类型
	// }

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_address:
			Intent intent = new Intent(getActivity(), SHContainerActivity.class);
			intent.putExtra("class", HouseAddressFragment.class.getName());
			intent.putExtra("json", json.toString());
			startActivityForResult(intent, CODE_ADDRESS);
			getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
			break;
		case R.id.btn_add:
			roomNum++;
			jsonObj = new JSONObject();
			try {
				jsonObj.put("roomNum", roomNum);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			map.put(roomNum, jsonObj);
			final View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_part_rent, null);
			view.setTag(roomNum);
			LinearLayout llDetail = (LinearLayout) view.findViewById(R.id.ll_detail);
			LinearLayout llRentType = (LinearLayout) view.findViewById(R.id.ll_rent_mode);
			final EditText etDes = (EditText) view.findViewById(R.id.et_des);
			llDetail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), SHContainerActivity.class);
					intent.putExtra("class", HousePublishDetailFragment.class.getName());
					intent.putExtra("type_rent", type_rent);
					intent.putExtra("json", map.get(view.getTag()).toString());
					intent.putExtra("from", "more");//more表示多个房间  第二个房间开始不显示几室几厅几卫
					startActivityForResult(intent, CODE_DETAIL);
					getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
				}
			});
			llRentType.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), SHContainerActivity.class);
					intent.putExtra("class", HouseRentModeFragment.class.getName());
					intent.putExtra("type_rent", type_rent);
					intent.putExtra("json", map.get(view.getTag()).toString());
					startActivityForResult(intent, CODE_RENT_TYPE);
					getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
				}
			});
			etDes.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
					try {
						map.get(view.getTag()).put("memo", etDes.getText().toString().trim());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			TextView tvDelete = (TextView) view.findViewById(R.id.tv_delete);
			tvDelete.setVisibility(View.VISIBLE);
			tvDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mLlAdd.removeView(view);
					map.remove(view.getTag());
				}
			});
			mLlAdd.addView(view);
			break;
		case R.id.ll_rent_mode:
			Intent intent_rent = new Intent(getActivity(), SHContainerActivity.class);
			intent_rent.putExtra("class", HouseRentModeFragment.class.getName());
			intent_rent.putExtra("json", map.get(1).toString());
			startActivityForResult(intent_rent, CODE_RENT_TYPE);
			getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
			break;
		case R.id.ll_detail:
			Intent intent_detail = new Intent(getActivity(), SHContainerActivity.class);
			intent_detail.putExtra("class", HousePublishDetailFragment.class.getName());
			intent_detail.putExtra("type_rent", type_rent);
			intent_detail.putExtra("json", map.get(1).toString());
			startActivityForResult(intent_detail, CODE_DETAIL);
			getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
			break;
		case R.id.ll_tese:
			final String[] items_tese_all = getResources().getStringArray(R.array.array_tese);
			final String[] items_tese = new String[items_tese_all.length - 1];
			for (int i = 0; i < items_tese_all.length; i++) {
				if (i != 0) {
					items_tese[i - 1] = items_tese_all[i];
				}
			}
			final boolean[] boo = new boolean[items_tese.length];
			AlertDialog b= new AlertDialog.Builder(getActivity()).setTitle("房源特色（最多3项）").setMultiChoiceItems(items_tese, null, new OnMultiChoiceClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
					// TODO Auto-generated method stub
					boo[arg1] = arg2;
				
				}
			}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					JSONArray arr = new JSONArray();
					StringBuilder str = new StringBuilder();
					for(int i = 0;i<items_tese.length;i++){
						if(boo[i]){
							arr.put(i+1);
							str.append(items_tese[i]).append("；");
						}
					}
					if(arr.length()>3){
						arr = new JSONArray();
						SHToast.showToast(getActivity(), "最多选择3项");
					}else{
						try {
							json.put("HouseLableList", arr);
							mTvTese.setText(str);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).setNegativeButton("取消", null).show();
			break;
		case R.id.btn_next:
			if(map.size() == 0  ||  CommonUtil.isEmpty(mEtPhone.getText().toString().trim()) || CommonUtil.isEmpty(mEtDes.getText().toString().trim())){
				SHToast.showToast(getActivity(), "请先完善信息");
				return;
			}
			try {
				json.put("lordType", mRgLord.getCheckedRadioButtonId() == R.id.rb_0 ? 1 : 2);// 房东类型
				json.put("rentType", mRgType.getCheckedRadioButtonId() == R.id.rb_type_0 ? 1 : 2);// 出租类型
//				json.put("Gender", mRgSex.getCheckedRadioButtonId() == R.id.rb_male ? 0 : 1);// 性别
				json.put("contractPhone", mEtPhone.getText().toString().trim());
				JSONArray detailArray = new JSONArray();
				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					detailArray.put(entry.getValue());
//					CommonUtil.JSONUtil.copyJson(json, ((JSONObject)entry.getValue()));
					JSONObject jsonObj = (JSONObject)entry.getValue();
					if(jsonObj.optInt("office") != 0 && jsonObj.optInt("room") != 0 && jsonObj.optInt("toilet") != 0){
						json.put("office", jsonObj.optInt("office"));
						json.put("room", jsonObj.optInt("room"));
						json.put("toilet", jsonObj.optInt("toilet"));
					}
				}
				json.put("houseDetailInfoList", detailArray);
//				json.
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// 房东类型
			SHDialog.ShowProgressDiaolg(getActivity(), null);
			publishTask = new SHPostTaskM();
			publishTask.setListener(this);
			publishTask.setUrl(ConfigDefinition.URL + "publishhouse");
			Iterator it = json.keys();  
			while(it.hasNext()){
				String key = (String) it.next();
				try {
					publishTask.getTaskArgs().put(key, json.get(key));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
					// addressJson.put("city", json.optString("city"));
					// addressJson.put("district", json.optString("district"));
					// addressJson.put("houseZoneName",
					// json.optString("houseZoneName"));
					// addressJson.put("buildingNum",
					// json.optString("buildingNum"));
					// addressJson.put("unitNum", json.optString("unitNum"));
					// addressJson.put("menpaiNum",
					// json.optString("menpaiNum"));
					// addressJson.put("currentFloor",
					// json.optInt("currentFloor"));
					// addressJson.put("totalFloor", json.optInt("totalFloor"));
					CommonUtil.JSONUtil.copyJson(this.json, json);
					break;
				case CODE_DETAIL:
					// detailJson.put("room", json.optInt("room"));
					// detailJson.put("office", json.optInt("office"));
					// detailJson.put("toilet", json.optInt("toilet"));
					// detailJson.put("area", json.optString("area"));
					// if(json.has("fitment")){//装修情况
					// detailJson.put("fitment", json.optInt("fitment"));
					// }
					// detailJson.put("HouseFitmentList",
					// json.optJSONArray("HouseFitmentList"));
					// detailJson.put("HouseDeviceList",
					// json.optJSONArray("HouseDeviceList"));
					System.out.println("detail_json:"+json);
					JSONObject newJson = map.get(json.getInt("roomNum"));
					newJson.put("room", json.optInt("room"));
					newJson.put("office", json.optInt("office"));
					newJson.put("toilet", json.optInt("toilet"));
					CommonUtil.JSONUtil.copyJson(newJson, json);
					break;
				case CODE_RENT_TYPE:
					JSONObject newJson2 = map.get(json.getInt("roomNum"));
					CommonUtil.JSONUtil.copyJson(newJson2, json);
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
		JSONObject json = (JSONObject) task.getResult();
		if (task == publishTask) {
			Intent intent_next = new Intent(getActivity(), SHContainerActivity.class);
			intent_next.putExtra("class", HousePublishNextFragment.class.getName());
			intent_next.putExtra("lordType", mRgLord.getCheckedRadioButtonId() == R.id.rb_0 ? 1 : 2);
			intent_next.putExtra("houseId", json.getInt("houseId"));
			startActivity(intent_next);
			finish();
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
