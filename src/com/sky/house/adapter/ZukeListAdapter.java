package com.sky.house.adapter;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.eroad.base.SHApplication;
import com.eroad.base.util.ConfigDefinition;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.widget.SHDialog;
import com.sky.widget.sweetdialog.SweetDialog;

public class ZukeListAdapter extends BaseAdapter implements ITaskListener{
	private Context context;
	private JSONArray jsonArray;
	IOnOrderConfirm iOnOrderConfirm;
	private int orderId;

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public ZukeListAdapter(Context context,JSONArray jsonArray,IOnOrderConfirm i) {
		super();
		this.context = context;
		this.jsonArray = jsonArray;
		this.iOnOrderConfirm = i;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jsonArray.length();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_zuke, null);
			holder = new ViewHolder();
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
			holder.btnConfirm = (Button) convertView.findViewById(R.id.btn_confirm);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		holder.tvItem.setText(str[pos]);
		try {
			holder.tvTime.setText(jsonArray.getJSONObject(pos).getString("payTime"));
			holder.tvName.setText(jsonArray.getJSONObject(pos).getString("userName"));
			holder.tvMoney.setText(jsonArray.getJSONObject(pos).getString("payAmount")+"元");
			holder.btnConfirm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					orderId = jsonArray.optJSONObject(pos).optInt("orderId");
					SHDialog.ShowProgressDiaolg(context, null);
					SHPostTaskM task = new SHPostTaskM();
					task.setListener(ZukeListAdapter.this);
					task.setUrl(ConfigDefinition.URL+"PayDownConfirm");
					task.getTaskArgs().put("orderId", jsonArray.optJSONObject(pos).optInt("orderId"));
					task.start();
				}
			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

	private static class ViewHolder {
		private Button btnConfirm;
		private TextView tvTime,tvName,tvMoney;
	}

	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		iOnOrderConfirm.onConfirm(orderId);
	}

	@Override
	public void onTaskFailed(SHTask task) {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		new SweetDialog(SHApplication.getInstance().getCurrentActivity(), SweetDialog.ERROR_TYPE).setTitleText("提示").setContentText(task.getRespInfo().getMessage()).show();
		
	}

	@Override
	public void onTaskUpdateProgress(SHTask task, int count, int total) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskTry(SHTask task) {
		// TODO Auto-generated method stub
		
	}
	
	public interface IOnOrderConfirm{
		public void onConfirm(int orderId);
	}
}
