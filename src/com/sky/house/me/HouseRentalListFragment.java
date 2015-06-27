package com.sky.house.me;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHApplication;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.house.adapter.HouseListAdapter;
import com.sky.house.business.HousePayChargeFragment;
import com.sky.house.resource.HouseDetailFragment;
import com.sky.house.resource.publish.HouseSuccessFragment;
import com.sky.house.widget.SHListView;
import com.sky.widget.SHDialog;
import com.sky.widget.SHToast;
import com.sky.widget.sweetdialog.SweetDialog;

/**
 * @author yebaohua
 *我的租房
 */
public class HouseRentalListFragment extends BaseFragment implements ITaskListener {
	private HouseListAdapter mAdapter;
	SHListView listView;
	private SHPostTaskM taskMessage,taskClear,taskComplain,taskCheckIn,taskHasPass;
	private JSONArray jsonArray = new JSONArray();
	private  int  type;// 列表类型 查看HouseListAdapter说明
	private boolean isSetPass;// 是否设置过密码

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view  = inflater.inflate(R.layout.fragment_rental, container,false);
		listView  = (SHListView) view.findViewById(R.id.lv_rental);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		type  = getActivity().getIntent().getIntExtra("type", HouseListAdapter.FLAG_HOUSE_LIST);
		mDetailTitlebar.setTitle(getActivity().getIntent().getStringExtra("title"));
		mAdapter  = new HouseListAdapter(getActivity(), type, jsonArray);
		listView.setAdapter(mAdapter);
		switch (type) {
		case HouseListAdapter.FLAG_HOUSE_LIST:
			listView.setTipsMessage("您还没有关注任何房源，赶紧联系房东吧！");
			mDetailTitlebar.setRightButton1("清空", new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					requestCollectClear();
				}
			});

			break;
		case HouseListAdapter.FLAG_STATE_LIST_TENANT:
			listView.setTipsMessage("暂时还没有您的租房信息哦！加油...");
			mAdapter.setItemButtonSelectListener(new HouseListAdapter.ItemButtonSelectListencr() {

				@Override
				public void setRightButtonOnselect(int complaintId, JSONObject object) {
					// TODO Auto-generated method stub
					try {
						Intent intent = new Intent(getActivity(), SHContainerActivity.class);
						if(object.getInt("orderStatus")>50){
							intent.putExtra("class", HouseRentalDetailFragment.class.getName());
							intent.putExtra("orderId", object.getInt("orderId"));
							intent.putExtra("orderStatus", object.getInt("orderStatus"));
							intent.putExtra("type", type);
							startActivity(intent);
						}else if(object.getInt("orderStatus")==50){
							checkIn(object.getInt("orderId"));
						}else{
							intent.putExtra("class", HousePayChargeFragment.class.getName());
							intent.putExtra("id",  object.getInt("houseDetailId"));
							intent.putExtra("identification", 0);
							startActivity(intent);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				@Override
				public void setLeftButtonOnselect(int complaintId, JSONObject object) {
					// TODO Auto-generated method stub

				}
			});
			break;
		case HouseListAdapter.FLAG_STATE_LIST_LANDLORD:
			listView.setTipsMessage("暂时还没有您的租房信息哦！加油...");
			mAdapter.setItemButtonSelectListener(new HouseListAdapter.ItemButtonSelectListencr() {

				@Override
				public void setRightButtonOnselect(int complaintId, JSONObject object) {
					// TODO Auto-generated method stub
					try {
						Intent intent = new Intent(getActivity(), SHContainerActivity.class);
						if(object.getInt("orderStatus")>=50){
							intent.putExtra("class", HouseRentalDetailFragment.class.getName());
							intent.putExtra("orderId", object.getInt("orderId"));
							intent.putExtra("orderStatus", object.getInt("orderStatus"));
							intent.putExtra("type", type);
							startActivity(intent);
						}else{
							intent.putExtra("class", HousePayChargeFragment.class.getName());
							intent.putExtra("id",  object.getInt("houseDetailId"));
							intent.putExtra("identification", 1);
							intent.putExtra("optType", 10);
							intent.putExtra("orderId", object.getInt("orderId")+"");
							startActivity(intent);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				@Override
				public void setLeftButtonOnselect(int complaintId, JSONObject object) {
					// TODO Auto-generated method stub

				}
			});

			break;
		case HouseListAdapter.FLAG_STATE_LIST_COMPLAINT:
			listView.setTipsMessage("沟通一定能解决很多问题，您保持的很好哦！32个赞...");

			mAdapter.setItemButtonSelectListener(new HouseListAdapter.ItemButtonSelectListencr() {

				@Override
				public void setRightButtonOnselect(int complaintId,JSONObject object) {
					// TODO Auto-generated method stub
					try {
						SHDialog.ShowProgressDiaolg(getActivity(), null);
						taskComplain =  new SHPostTaskM() ;
						taskComplain.setUrl(ConfigDefinition.URL+"UpdateUserComplaintStatus");
						taskComplain.getTaskArgs().put("complaintId", object.getInt("complaintId"));
						taskComplain.setListener(HouseRentalListFragment.this);
						taskComplain.start();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void setLeftButtonOnselect(int complaintId,JSONObject object) {
					// TODO Auto-generated method stub

				}
			});
			break;
		}
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SHContainerActivity.class);
				intent.putExtra("class", HouseDetailFragment.class.getName());
				try {
					intent.putExtra("id", jsonArray.getJSONObject(position).getInt("houseDetailId"));
					intent.putExtra("name", jsonArray.getJSONObject(position).getString("houseName"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivity(intent);
			}
		});
		
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		requestHasPass();
		requestMessage();
	}
	private void requestHasPass() {
		taskHasPass = new SHPostTaskM();
		taskHasPass.setUrl(ConfigDefinition.URL + "GetUserIsSetPayPassword");
		taskHasPass.setListener(this);
		taskHasPass.start();
	}
	private void requestMessage(){
		taskMessage = new SHPostTaskM();
		switch (type) {
		case HouseListAdapter.FLAG_HOUSE_LIST:
			taskMessage.setUrl(ConfigDefinition.URL + "GetUserHouseCollectList");
			break;
		case HouseListAdapter.FLAG_STATE_LIST_TENANT:
			taskMessage.setUrl(ConfigDefinition.URL + "GetTenantList");
			break;
		case HouseListAdapter.FLAG_STATE_LIST_LANDLORD:
			taskMessage.setUrl(ConfigDefinition.URL + "GetLordList");
			break;
		case HouseListAdapter.FLAG_STATE_LIST_COMPLAINT:
			taskMessage.setUrl(ConfigDefinition.URL + "GetUserComplaintList");
			break;
		}
		taskMessage.setListener(this);
		taskMessage.start();
	}
	private void requestCollectClear(){
		taskClear = new SHPostTaskM();
		taskClear.setUrl(ConfigDefinition.URL + "DeleteUserHouseCollect");
		taskClear.getTaskArgs().put("ids", new JSONArray());
		taskClear.setListener(this);
		taskClear.start();
	}

	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		if (task == taskMessage) {
			JSONObject json = (JSONObject) task.getResult();
			jsonArray = json.getJSONArray("rentHouseList");
			listView.setTotalNum(json.getInt("recordCount"));
			mAdapter.setJsonArray(jsonArray);
			listView.setAdapter(mAdapter);
		}else if(task == taskClear){
			jsonArray = new JSONArray();
			//			listView.setTotalNum(0);
			listView.setTipsMessage("已加载全部");
			mAdapter.setJsonArray(jsonArray);
			listView.setAdapter(mAdapter);
		}else if(task == taskComplain){
			requestMessage();
		}else if(task  == taskCheckIn){
			requestMessage();
			Intent intent = new Intent(getActivity(), SHContainerActivity.class);
			intent.putExtra("class", HouseSuccessFragment.class.getName());
			intent.putExtra("identification", 0);
			intent.putExtra("flag", 1);
			startActivity(intent);

		} else if (task == taskHasPass) {
			JSONObject jsonObj = (JSONObject) task.getResult();
			isSetPass = jsonObj.getInt("isSet") == 0 ? false : true;
		}
	}
	@Override
	public void onTaskFailed(SHTask task) {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		if(task == taskMessage){
			jsonArray = new JSONArray();
			listView.setTotalNum(0);
			mAdapter.setJsonArray(jsonArray);
			listView.setAdapter(mAdapter);
		}else{
			new SweetDialog(SHApplication.getInstance().getCurrentActivity(), SweetDialog.ERROR_TYPE).setTitleText("提示").setContentText(task.getRespInfo().getMessage()).show();
		}


	}
	@Override
	public void onTaskUpdateProgress(SHTask task, int count, int total) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onTaskTry(SHTask task) {
		// TODO Auto-generated method stub

	}	

	/**
	 * 确认入住
	 */
	private void checkIn(final int  orderId){
		if (!isSetPass) {
			Intent intent = new Intent(getActivity(), SHContainerActivity.class);
			intent.putExtra("class", HouseChangePayPassword.class.getName());
			startActivity(intent);
		} else {
			final Dialog dilogPass = new Dialog(getActivity(), R.style.dialog);
			dilogPass.setContentView(R.layout.dialog_input_password);
			final EditText etPass = (EditText) dilogPass.findViewById(R.id.et_password);
			Button btnCancel = (Button) dilogPass.findViewById(R.id.btn_cancle);
			Button btnConfirm = (Button) dilogPass.findViewById(R.id.btn_confirm);
			dilogPass.show();
			btnCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dilogPass.dismiss();
				}
			});
			btnConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (etPass.getText().toString().trim().length() == 0) {
						SHToast.showToast(getActivity(), "请输入密码");
						return;
					}
					SHDialog.ShowProgressDiaolg(getActivity(), null);
					taskCheckIn =  new SHPostTaskM() ;
					taskCheckIn.setUrl(ConfigDefinition.URL+"CheckIn");
					taskCheckIn.getTaskArgs().put("orderId", orderId);
					taskCheckIn.getTaskArgs().put("password",  CommonUtil.encodeMD5(etPass.getText().toString().trim()));
					taskCheckIn.setListener(HouseRentalListFragment.this);
					taskCheckIn.start();
				}
			});
		}

	}
}
