package com.sky.house.me;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.eroad.base.BaseFragment;
import com.eroad.base.util.ConfigDefinition;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.house.adapter.MessageAdapter;
import com.sky.house.adapter.MessageAdapter.AdapterType;
import com.sky.house.widget.SHListView;
import com.sky.widget.SHDialog;

public class HouseMessageFragment extends BaseFragment implements ITaskListener{
	private MessageAdapter mAdapter;
	SHListView listView;
	private int pagenum = 1;
	private SHPostTaskM taskMessage,taskClear;
	private JSONArray jsonArray = new JSONArray();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view  = inflater.inflate(R.layout.fragment_message, container,false);
		listView  = (SHListView) view.findViewById(R.id.lv_message);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mDetailTitlebar.setTitle("我的消息");
		mDetailTitlebar.setRightButton1("清空", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				requestClear();
			}
		});
		mAdapter  = new  MessageAdapter(getActivity(),jsonArray,AdapterType.showReport);
		listView.setAdapter(mAdapter);
		listView.setTipsMessage("暂时还没有您的消息哦！加油...");
//		listView.setOnLoadMoreListener(new SHListView.OnLoadMoreListener() {
//			
//			@Override
//			public void onLoadMore() {
//				// TODO Auto-generated method stub
//				pagenum++;
//				requestMessage();
//			}
//		});
		requestMessage();
	}
	private void requestMessage(){
		taskMessage = new SHPostTaskM();
		taskMessage.setUrl(ConfigDefinition.URL + "GetUserPushMsgList");
//		taskMessage.getTaskArgs().put("pageSize", 10);
//		taskMessage.getTaskArgs().put("pageIndex",pagenum);
		taskMessage.setListener(this);
		taskMessage.start();
	}
	private void requestClear(){
		taskClear = new SHPostTaskM();
		taskClear.setUrl(ConfigDefinition.URL + "UpdateUserPushMsgStatus");
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
			jsonArray = json.getJSONArray("pushMsgs");
			listView.setTotalNum(json.getInt("recordCount"));
			mAdapter.setJsonArray(jsonArray);
			mAdapter.notifyDataSetChanged();
		}else if(task == taskClear){
			jsonArray = new JSONArray();
			listView.setTotalNum(0);
			mAdapter.setJsonArray(jsonArray);
			mAdapter.notifyDataSetChanged();
		}
	}
	@Override
	public void onTaskFailed(SHTask task) {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
//		new SweetDialog(SHApplication.getInstance().getCurrentActivity(), SweetDialog.ERROR_TYPE).setTitleText("提示").setContentText(task.getRespInfo().getMessage()).show();
		jsonArray = new JSONArray();
		listView.setTotalNum(0);
		mAdapter.setJsonArray(jsonArray);
		mAdapter.notifyDataSetChanged();
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
