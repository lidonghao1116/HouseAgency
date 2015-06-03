package com.sky.house.business;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHApplication;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ImageLoaderUtil;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.widget.SHDialog;
import com.sky.widget.sweetdialog.SweetDialog;

/**
 * 联系看房
 * 
 * @author skypan
 * 
 */
public class HouseContactFragment extends BaseFragment implements ITaskListener{

	@ViewInit(id = R.id.btn_pay, onClick = "onClick")
	private Button mBtnPay;

	@ViewInit(id = R.id.iv_question, onClick = "onClick")
	private ImageView mIvQuestion;

	@ViewInit(id = R.id.ll_tips)
	private LinearLayout mLlQuestion;

	private boolean isTipsShow;

	private SHPostTaskM task;
	
	@ViewInit(id = R.id.tv_fangdong)
	private TextView mTvFangDong;
	
	@ViewInit(id = R.id.tv_goutong)
	private TextView mTvGoutong;
	
	@ViewInit(id = R.id.tv_xiangchu)
	private TextView mTvXiangchu;
	
	@ViewInit(id = R.id.tv_chuli)
	private TextView mTvChuli;
	
	@ViewInit(id = R.id.tv_publish_num)
	private TextView mTvPublishNum;
	
	@ViewInit(id = R.id.tv_complaint_num)
	private TextView mTvComplaintNum;
	
	@ViewInit(id = R.id.tv_recommend_percent)
	private TextView mTvRecommendNum;
	
	@ViewInit(id = R.id.ll_credit)
	private LinearLayout llRenant;
	
	@ViewInit(id = R.id.tv_phone)
	private TextView mTvPhone;
	
	@ViewInit(id = R.id.iv_photo)
	private ImageView mIvPhoto;
	
	@ViewInit(id = R.id.btn_contact,onClick = "onClick")
	private Button mBtnContact;
	
	private JSONObject json;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle(getActivity().getIntent().getStringExtra("name"));
		request();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_contact, container, false);
		return view;
	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_pay:
			Intent intent = new Intent(getActivity(), SHContainerActivity.class);
			intent.putExtra("class", HousePayChargeFragment.class.getName());
			startActivity(intent);
			break;
		case R.id.iv_question:
			if (isTipsShow) {
				mLlQuestion.setVisibility(View.GONE);
				isTipsShow = false;
			} else {
				mLlQuestion.setVisibility(View.VISIBLE);
				isTipsShow = true;
			}
			break;
		case R.id.btn_contact:
			Intent intent_call = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+json.optString("phoneFor400")));  
            startActivity(intent_call);  
			break;
		}
	}
	
	private void request(){
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		task = new SHPostTaskM();
		task.setListener(this);
		task.setUrl(ConfigDefinition.URL+"GetLandlordDetail");
		task.getTaskArgs().put("houseDetailId", getActivity().getIntent().getIntExtra("id", -1));
		task.start();
	}

	private void craetTenant(JSONObject json) throws JSONException{
		JSONObject object  = json.getJSONObject("credibility");
		int sun  = Integer.parseInt(object.getString("sun"));
		int diamond  = Integer.parseInt(object.getString("diamond"));
		int star  = Integer.parseInt(object.getString("star"));
		llRenant.removeAllViews();
		for (int i = 0; i < sun; i++) {
			llRenant.addView(getImageView(R.drawable.img_diamond));
		}
		for (int j = 0; j < diamond; j++) {
			llRenant.addView(getImageView(R.drawable.img_diamond));
		}
		for (int x = 0; x < star; x++) {
			llRenant.addView(getImageView(R.drawable.img_start));
		}
	}
	private ImageView getImageView(int resId){
		ImageView imageView = new ImageView(getActivity());
		imageView.setImageResource(resId);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		LinearLayout.LayoutParams lay = new LinearLayout.LayoutParams(30, 30);
		lay.setMargins(0, 0, 10, 0);
		imageView.setLayoutParams(lay);
		return imageView;
	}
	
	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		json = (JSONObject) task.getResult();
		mTvFangDong.setText(json.optString("userRealName"));
		mTvGoutong.setText(json.optString("communicateEvaluation"));
		mTvXiangchu.setText(json.optString("attitudeEvaluation"));
		mTvChuli.setText(json.optString("speedEvaluation"));
		mTvPublishNum.setText(json.optString("publishHouseCount"));
		mTvComplaintNum.setText(json.optString("complaintCount"));
		mTvRecommendNum.setText(json.optString("pfRecommend"));
		mTvPhone.setText(json.optString("phoneFor400"));
		ImageLoaderUtil.displayImage(json.optString("picUrl"), mIvPhoto);
		craetTenant(json);
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
}
