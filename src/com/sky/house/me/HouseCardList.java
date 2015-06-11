package com.sky.house.me;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eroad.base.BaseFragment;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.widget.SHDialog;
import com.sky.widget.SHToast;
import com.sky.widget.sweetdialog.SweetDialog;

/**
 * @author yebaohua
 *银行卡提现列表
 */
public class HouseCardList extends BaseFragment implements ITaskListener {
	@ViewInit(id = R.id.et_phone)
	private EditText mEtCount;

	@ViewInit(id = R.id.et_card)
	private AutoCompleteTextView mEtCard;

	@ViewInit(id = R.id.et_pass)
	private EditText mEtPass;

	@ViewInit(id = R.id.btn_login, onClick = "onClick")
	private Button mBtnConfirm;

	private SHPostTaskM taskSubmit, taskCard;

	ArrayAdapter<String> arrayAdapter;

	String[] newArrays;

	private JSONArray comArray;
	private JSONObject mResultBalance = new JSONObject();
	private int amount;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view  = inflater.inflate(R.layout.fragment_card_list, container,false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mDetailTitlebar.setTitle("银行卡提现");
		try {
			mResultBalance  = new JSONObject(getActivity().getIntent().getStringExtra("detail"));
			amount = (int)mResultBalance.optDouble("amount");
			//			tvBank.setText(mResultBalance.optInt("sunnyAmt")+"");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		requestCard();

		mEtCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					((AutoCompleteTextView) v).showDropDown();
				}
			}
		});
		mBtnConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				requestSubmit();
			}
		});
	}
	private void requestCard() {
		taskCard = new SHPostTaskM();
		taskCard.setUrl(ConfigDefinition.URL + "GetUserCardList");
		taskCard.setListener(this);
		taskCard.start();
	}
	private void requestSubmit() {
		int count = Integer.parseInt(mEtCount.getText().toString().trim());
		if (count<=0) {
			SHToast.showToast(getActivity(), "请输入您的提现金额", Toast.LENGTH_SHORT);
			return;
		}
		if (count>amount) {
			SHToast.showToast(getActivity(), "您的提现金额不足", Toast.LENGTH_SHORT);
			return;
		}
		String card = mEtCard.getText().toString().trim();
		if (CommonUtil.isEmpty(card)) {
			SHToast.showToast(getActivity(), "请输入您的银行卡号", Toast.LENGTH_SHORT);
			return;
		}
		String pass = mEtPass.getText().toString().trim();
		if (CommonUtil.isEmpty(pass)) {
			SHToast.showToast(getActivity(), "请输入您的支付密码", Toast.LENGTH_SHORT);
			return;
		}
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		taskSubmit = new SHPostTaskM();
		taskSubmit.setUrl(ConfigDefinition.URL + "ApplyDeposit");
		taskSubmit.getTaskArgs().put("amount", count);
		taskSubmit.getTaskArgs().put("cardNo", card);
		taskSubmit.getTaskArgs().put("password", CommonUtil.encodeMD5(pass));//md5
		taskSubmit.setListener(this);
		taskSubmit.start();
	}
	private void initAuto() {
		arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_drop, newArrays);
		mEtCard.setAdapter(arrayAdapter);
		arrayAdapter.notifyDataSetChanged();
		mEtCard.setThreshold(1);
		mEtCard.setDropDownBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.bg_dropdown));
	}
	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		if(task == taskCard){
			JSONObject object  = (JSONObject) task.getResult();
			comArray = object.getJSONArray("userCards");
			newArrays = new String[comArray.length()];
			for (int i = 0; i < comArray.length(); i++) {
				newArrays[i] = comArray.getJSONObject(i).getString("cardNo");
			}
			initAuto();
		}else if(task == taskSubmit){
			SHToast.showToast(getActivity(), "提现成功，请注意查收");
			finish();
		}
	}

	@Override
	public void onTaskFailed(SHTask task) {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		if(task != taskCard)
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
