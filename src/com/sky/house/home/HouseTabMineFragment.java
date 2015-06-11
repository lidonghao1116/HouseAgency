package com.sky.house.home;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ImageLoaderUtil;
import com.eroad.base.util.ImageTools;
import com.eroad.base.util.UserInfoManager;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.house.adapter.HouseListAdapter;
import com.sky.house.me.HouseAuthenticationFragment;
import com.sky.house.me.HouseBalanceFragment;
import com.sky.house.me.HouseFeedbackFragment;
import com.sky.house.me.HouseMessageFragment;
import com.sky.house.me.HouseRentalListFragment;
import com.sky.house.me.HouseSettingFragment;
import com.sky.house.widget.RoundImageView;
import com.sky.widget.SHDialog;
import com.sky.widget.SHDialog.DialogItemClickListener;
import com.sky.widget.sweetdialog.SweetDialog;
/**
 * 我的
 * @author skypan
 *
 */
public class HouseTabMineFragment extends BaseFragment implements OnClickListener,ITaskListener{
	@ViewInit(id = R.id.rl_myinfo, onClick = "onClick")
	private RelativeLayout rlMyInfo;

	@ViewInit(id = R.id.ll_landlord)
	private LinearLayout llLandlord;
	@ViewInit(id = R.id.ll_tenant)
	private LinearLayout llRenant;

	@ViewInit(id = R.id.rl_balance, onClick = "onClick")
	private RelativeLayout rlBalance;
	@ViewInit(id = R.id.rl_points, onClick = "onClick")
	private RelativeLayout rlScord;
	@ViewInit(id = R.id.btn_tenant, onClick = "onClick")
	private Button btnTenant;
	@ViewInit(id = R.id.btn_landlord, onClick = "onClick")
	private Button btnLandlord;
	@ViewInit(id = R.id.rl_message, onClick = "onClick")
	private RelativeLayout rlMessage;
	@ViewInit(id = R.id.rl_store, onClick = "onClick")
	private RelativeLayout rlStore;
	@ViewInit(id = R.id.rl_complaint, onClick = "onClick")
	private RelativeLayout rlComplaint;
	@ViewInit(id = R.id.rl_feedback, onClick = "onClick")
	private RelativeLayout rlFeedback;

	@ViewInit(id = R.id.iv_photo, onClick = "onClick")
	private RoundImageView imagePhoto;

	@ViewInit(id = R.id.tv_phone)
	private TextView tvPhone;


	@ViewInit(id = R.id.tv_state)
	private TextView tvState;

	@ViewInit(id = R.id.tv_balance)
	private TextView tvBalance;

	@ViewInit(id = R.id.tv_points)
	private TextView tvSunPoints;

	private SHPostTaskM taskUserinfo,uploadTask,taskBalance;
	private JSONObject mResultBalance = new JSONObject();

	private final int TAKE_PICTURE = 0;// 拍照
	private final int CHOOSE_PICTURE = 1;// 相册


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_mine, container, false);
		return view;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("我的");
		mDetailTitlebar.setLeftButton(null, null);
		mDetailTitlebar.setRightButton1("设置", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),SHContainerActivity.class);
				intent.putExtra("class", HouseSettingFragment.class.getName());
				startActivity(intent);
			}
		});
		tvPhone.setText(UserInfoManager.getInstance().getMoblie());

	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		requestUserInfo();
	}
	/** 获取用户信息 */
	private void requestUserInfo() {
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		taskUserinfo = new SHPostTaskM();
		taskUserinfo.setUrl(ConfigDefinition.URL + "GetUserDetail");
		taskUserinfo.setListener(this);
		taskUserinfo.start();
		requestBalanceInfo();
	}
	/**
	 * 账号金额信息 
	 */
	private void requestBalanceInfo(){
		taskBalance = new SHPostTaskM();
		taskBalance.setUrl(ConfigDefinition.URL+"GetMyAccountDetail");
		taskBalance.setListener(this);
		taskBalance.start();
	}
	/** 上传头像 */
	private void uploadPhoto(Bitmap bitmap) {
		SHDialog.ShowProgressDiaolg(getActivity(), "正在上传...");
		uploadTask = new SHPostTaskM();
		uploadTask.setUrl(ConfigDefinition.URL + "AddUserImage");
		uploadTask.getTaskArgs().put("picString", CommonUtil.bitmap2Base64(bitmap));
		uploadTask.setListener(this);
		uploadTask.start();
	}
	/**
	 * 加载房东信誉图
	 * @throws JSONException 
	 */
	private void craetLandlord(JSONObject json) throws JSONException{
		JSONObject object  = json.getJSONObject("landlordCredibility");
		int sun  = Integer.parseInt(object.getString("sun"));
		int diamond  = Integer.parseInt(object.getString("diamond"));
		int star  = Integer.parseInt(object.getString("star"));
		llLandlord.removeAllViews();
		for (int i = 0; i < sun; i++) {
			llLandlord.addView(getImageView(R.drawable.img_sun));
		}
		for (int j = 0; j < diamond; j++) {
			llLandlord.addView(getImageView(R.drawable.img_diamond));
		}
		for (int x = 0; x < star; x++) {
			llLandlord.addView(getImageView(R.drawable.img_start));
		}
	}
	/**
	 * 加载租客信誉图
	 * @throws JSONException 
	 */
	private void craetTenant(JSONObject json) throws JSONException{
		JSONObject object  = json.getJSONObject("renterCredibility");
		int sun  = Integer.parseInt(object.getString("sun"));
		int diamond  = Integer.parseInt(object.getString("diamond"));
		int star  = Integer.parseInt(object.getString("star"));
		llRenant.removeAllViews();
		for (int i = 0; i < sun; i++) {
			llRenant.addView(getImageView(R.drawable.img_sun));
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(),SHContainerActivity.class);
		switch (v.getId()) {
		case R.id.rl_myinfo:
			intent.putExtra("class", HouseAuthenticationFragment.class.getName());
			startActivity(intent);
			break;
		case R.id.iv_photo:
			modifyPhoto();
			break;
		case R.id.rl_balance:
			intent.putExtra("class", HouseBalanceFragment.class.getName());
			intent.putExtra("detail", mResultBalance.toString());
			startActivity(intent);
			break;
		case R.id.rl_points:

			break;
		case R.id.btn_tenant:
			intent.putExtra("class", HouseRentalListFragment.class.getName());
			intent.putExtra("title", "我是租客");
			intent.putExtra("type", HouseListAdapter.FLAG_STATE_LIST_TENANT);
			startActivity(intent);
			break;
		case R.id.btn_landlord:
			intent.putExtra("class", HouseRentalListFragment.class.getName());
			intent.putExtra("title", "我是房东");
			intent.putExtra("type", HouseListAdapter.FLAG_STATE_LIST_LANDLORD);
			startActivity(intent);
			break;
		case R.id.rl_message:
			intent.putExtra("class", HouseMessageFragment.class.getName());
			startActivity(intent);
			break;
		case R.id.rl_store:
			intent.putExtra("class", HouseRentalListFragment.class.getName());
			intent.putExtra("title", "我的关注");
			intent.putExtra("type", HouseListAdapter.FLAG_HOUSE_LIST);
			startActivity(intent);
			break;
		case R.id.rl_complaint:
			intent.putExtra("class", HouseRentalListFragment.class.getName());
			intent.putExtra("title", "我的投诉");
			intent.putExtra("type", HouseListAdapter.FLAG_STATE_LIST_COMPLAINT);
			startActivity(intent);
			break;
		case R.id.rl_feedback:
			intent.putExtra("class", HouseFeedbackFragment.class.getName());
			startActivity(intent);
			break;

		default:
			break;
		}


	}
	/**
	 * 修改头像
	 */
	private void modifyPhoto() {
		final String[] items = new String[] { "拍照", "相册" };
		SHDialog.showActionSheet(getActivity(), "上传头像", items, new DialogItemClickListener() {
			@Override
			public void onSelected(String result) {
				// TODO Auto-generated method stub
				if (result.equals(items[0])) {
					Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "eroad_temp.png"));
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(openCameraIntent, TAKE_PICTURE);
				} else {

					Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, CHOOSE_PICTURE);

					//					Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
					//					openAlbumIntent.setType("image/*");
					//					startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inSampleSize = 4;
			switch (requestCode) {
			case TAKE_PICTURE:
				Bitmap bitmap_tack = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/eroad_temp.png", bitmapOptions);
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap_tack, bitmap_tack.getWidth() / 6, bitmap_tack.getHeight() / 6);
				// bitmap_tack.recycle();
				imagePhoto.setImageBitmap(newBitmap);
				uploadPhoto(bitmap_tack);
				break;
			case CHOOSE_PICTURE:
				// ContentResolver resolver =
				// getActivity().getContentResolver();
				Uri originalUri = data.getData();
				String[] filePathColumns = { MediaStore.Images.Media.DATA };
				Cursor c = getActivity().getContentResolver().query(originalUri, filePathColumns, null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePathColumns[0]);
				String picturePath = c.getString(columnIndex);
				c.close();
				try {
					// Bitmap bitmap_choose =
					// MediaStore.Images.Media.getBitmap(resolver, originalUri);
					Bitmap bitmap_choose = BitmapFactory.decodeFile(picturePath, bitmapOptions);
					Bitmap smallBitmap = ImageTools.zoomBitmap(bitmap_choose, bitmap_choose.getWidth() / 6, bitmap_choose.getHeight() / 6);
					// bitmap_choose.recycle();
					imagePhoto.setImageBitmap(smallBitmap);
					uploadPhoto(bitmap_choose);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}

		}
	}

	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		if (task == taskUserinfo) {
			JSONObject json = (JSONObject) task.getResult();
			ImageLoaderUtil.displayImage(json.getString("userHeadImg"), imagePhoto);
			tvPhone.setText(json.optString("mobilePhone"));
			tvState.setText(json.optString("userAuditStatusName"));
			craetLandlord(json);
			craetTenant(json);
		}else if(task == taskBalance){
			mResultBalance = (JSONObject) task.getResult();
			tvBalance.setText(mResultBalance.optDouble("amount")+"");
			tvSunPoints.setText(mResultBalance.optInt("sunnyAmt")+"");
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
