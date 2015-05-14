package com.sky.house.home;

import java.io.File;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ImageLoaderUtil;
import com.eroad.base.util.ImageTools;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.house.me.HouseAuthenticationFragment;
import com.sky.house.me.HouseBalanceFragment;
import com.sky.house.me.HouseMessageFragment;
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
	
	@ViewInit(id = R.id.tv_name)
	private TextView tvName;
	
	@ViewInit(id = R.id.tv_state)
	private TextView tvState;
	
	@ViewInit(id = R.id.tv_balance)
	private TextView tvBalance;
	
	@ViewInit(id = R.id.tv_points)
	private TextView tvSunPoints;
	
	private SHPostTaskM taskAuthinfo,uploadTask,taskBalance;
	
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
		requestAuthInfo();
		
	}
	private void requestAuthInfo(){
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		taskAuthinfo = new SHPostTaskM();
		taskAuthinfo.setUrl(ConfigDefinition.URL+"GetUserAuthInfo");
		taskAuthinfo.setListener(this);
		taskAuthinfo.start();
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
//		SHDialog.ShowProgressDiaolg(getActivity(), "正在上传...");
//		uploadTask = new SHPostTaskM();
//		uploadTask.setUrl(ConfigDefinition.URL + "uploaddata");
//		uploadTask.getTaskArgs().put("type", "image");
//		uploadTask.getTaskArgs().put("data", CommonUtil.bitmap2Base64(bitmap));
//		uploadTask.setListener(this);
//		uploadTask.start();
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
			startActivity(intent);
			break;
		case R.id.rl_points:
			intent.putExtra("class", HouseBalanceFragment.class.getName());
			startActivity(intent);
			break;
		case R.id.btn_tenant:
			intent.putExtra("class", HouseBalanceFragment.class.getName());
			startActivity(intent);
			break;
		case R.id.btn_landlord:
			intent.putExtra("class", HouseBalanceFragment.class.getName());
			startActivity(intent);
			break;
		case R.id.rl_message:
			intent.putExtra("class", HouseMessageFragment.class.getName());
			startActivity(intent);
			break;
		case R.id.rl_store:
			intent.putExtra("class", HouseBalanceFragment.class.getName());
			startActivity(intent);
			break;
		case R.id.rl_complaint:
			intent.putExtra("class", HouseBalanceFragment.class.getName());
			startActivity(intent);
			break;
		case R.id.rl_feedback:
			intent.putExtra("class", HouseBalanceFragment.class.getName());
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
		if (task == taskAuthinfo) {
			JSONObject json = (JSONObject) task.getResult();
			ImageLoaderUtil.displayImage(json.getString("PicUrl"), imagePhoto);
			tvPhone.setText(json.optString("IdentityNo"));
			tvName.setText(json.optString("UserRealName"));
			tvState.setText(json.optString("AuditStatusName"));
		}else if(task == taskBalance){
			JSONObject json = (JSONObject) task.getResult();
			tvBalance.setText(json.optInt("amount"));
			tvSunPoints.setText(json.optInt("sunnyAmt"));
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
