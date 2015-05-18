package com.sky.house.me;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.util.ConfigDefinition;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.house.widget.SHListView;
import com.sky.widget.SHDialog;

/**
 * @author yebaohua
 *我的租房
 */
public class HouseMyRentalFragment extends BaseFragment implements OnItemClickListener,ITaskListener {
	private RentalAdapter mAdapter;
	SHListView listView;
	private int pagenum = 1;
	private SHPostTaskM taskMessage,taskClear;
	private JSONArray jsonArray = new JSONArray();
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
		mDetailTitlebar.setTitle("我的租房");
		mAdapter  = new RentalAdapter();
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		if (task == taskMessage) {
			JSONObject json = (JSONObject) task.getResult();
			jsonArray = json.getJSONArray("rentHouseList");
			listView.setTotalNum(json.getInt("recordCount"));
			mAdapter.notifyDataSetChanged();
		}else if(task == taskClear){
			jsonArray = new JSONArray();
			listView.setTotalNum(0);
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
	class RentalAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(jsonArray == null){
				return 0;
			}
			return jsonArray.length();
		}

		@Override
		public JSONObject getItem(int position) {
			// TODO Auto-generated method stub
			try {
				return jsonArray.getJSONObject(position);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new JSONObject();
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_message, null);
				holder = new ViewHolder();
				holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
				holder.btnReport = (Button) convertView.findViewById(R.id.btn_report);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			try {
				JSONObject object =  jsonArray.getJSONObject(position);
				holder.tvContent.setText(object.optString("content"));
				
				holder.btnReport.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
		}
		private  class ViewHolder {
			private TextView tvContent;
			private Button btnReport;
		}
	}
	
}
