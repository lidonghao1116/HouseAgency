package com.sky.house.me;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ConfigDefinition;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.house.adapter.HouseListAdapter;
import com.sky.house.widget.SHListView;
import com.sky.widget.SHDialog;

/**
 * @author yebaohua
 *我的租房
 */
public class HouseRentalListFragment extends BaseFragment implements ITaskListener {
	private HouseListAdapter mAdapter;
	SHListView listView;
	private int pagenum = 1;
	private SHPostTaskM taskMessage,taskClear;
	private JSONArray jsonArray = new JSONArray();
    private  int  type;// 列表类型 查看HouseListAdapter说明
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
					requestClear();
				}
			});
			break;
		case HouseListAdapter.FLAG_STATE_LIST_TENANT:
			listView.setTipsMessage("暂时还没有您的租房信息哦！加油...");
			break;
		case HouseListAdapter.FLAG_STATE_LIST_LANDLORD:
			listView.setTipsMessage("暂时还没有您的租房信息哦！加油...");
			break;
		case HouseListAdapter.FLAG_STATE_LIST_COMPLAINT:
			listView.setTipsMessage("沟通一定能解决很多问题，您保持的很好哦！32个赞...");
			break;
		}
		
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
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intenttest = new Intent(getActivity(), SHContainerActivity.class);
				intenttest.putExtra("class", HouseRentalDetailFragment.class.getName());
				startActivity(intenttest);
			}
		});
	}
	private void requestMessage(){
		taskMessage = new SHPostTaskM();
		taskMessage.setUrl(ConfigDefinition.URL + "GetUserHouseCollectList");
//		taskMessage.getTaskArgs().put("pageSize", 10);
//		taskMessage.getTaskArgs().put("pageIndex",pagenum);
		taskMessage.setListener(this);
		taskMessage.start();
	}
	private void requestClear(){
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
