package com.sky.house.me;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eroad.base.BaseFragment;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ImageLoaderUtil;
import com.eroad.base.util.ImageTools;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.widget.SHDialog;
import com.sky.widget.SHDialog.DialogItemClickListener;
import com.sky.widget.sweetdialog.SweetDialog;
import com.sky.widget.SHToast;

public class HouseAuthenticationFragment extends BaseFragment implements ITaskListener,OnClickListener{
	@ViewInit(id = R.id.iv_auth)
	private ImageView imgState;

	@ViewInit(id = R.id.et_name)
	private EditText etName;

	@ViewInit(id = R.id.et_card)
	private EditText etCard;

	@ViewInit(id = R.id.rl_upload, onClick = "onClick")
	private RelativeLayout rlUpload;

	@ViewInit(id = R.id.tv_state)
	private TextView tvState;

	@ViewInit(id = R.id.btn_submit, onClick = "onClick")
	private Button btnSubmit;

	@ViewInit(id = R.id.iv_question, onClick = "onClick")
	private ImageView imgQues;

	@ViewInit(id = R.id.iv_card)
	private ImageView imgCard;

	@ViewInit(id = R.id.tv_tips)
	private TextView tvTips;
	
	private final int TAKE_PICTURE = 0;// 拍照
	private final int CHOOSE_PICTURE = 1;// 相册
	
	private SHPostTaskM taskSubmit,taskAuthinfo;
	private Bitmap bitmapCard;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view =  inflater.inflate(R.layout.fragment_authentication, container,false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mDetailTitlebar.setTitle("认证信息");
		requestAuthInfo();
	}
	
	private void requestAuthInfo(){
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		taskAuthinfo = new SHPostTaskM();
		taskAuthinfo.setUrl(ConfigDefinition.URL+"GetUserAuthInfo");
		taskAuthinfo.setListener(this);
		taskAuthinfo.start();
	}
	private void submit() {
		String realName =  etName.getText().toString().trim();
		String card =  etCard.getText().toString().trim();
		if(realName.isEmpty()){
			SHToast.showToast(getActivity(), "请填写您的真实姓名", Toast.LENGTH_SHORT);
			return;
		}
		if(card.isEmpty()|| card.length()!= 18){
			SHToast.showToast(getActivity(), "请正确填写您的证件号", Toast.LENGTH_SHORT);
			return;
		}
		if(bitmapCard==null){
			SHToast.showToast(getActivity(), "请先上传您的证件图片", Toast.LENGTH_SHORT);
			return;
		}
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		taskSubmit = new SHPostTaskM();
		taskSubmit.setUrl(ConfigDefinition.URL + "UpdateUserAuth");
		taskSubmit.getTaskArgs().put("UserRealName", realName);
		taskSubmit.getTaskArgs().put("IdentityNo", card);
		taskSubmit.getTaskArgs().put("PicString", CommonUtil.bitmap2Base64(bitmapCard));
		taskSubmit.setListener(this);
		taskSubmit.start();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_upload:
			modifyPhoto();
			break;
		case R.id.iv_question:
			if(tvTips.getVisibility()== View.INVISIBLE){
				tvTips.setVisibility(View.VISIBLE);
			}else{
				tvTips.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.btn_submit:
			submit();
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
					Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.png"));
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(openCameraIntent, TAKE_PICTURE);
				} else {
					
					Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, CHOOSE_PICTURE);
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
				bitmapCard = bitmap_tack;
				imgCard.setImageBitmap(newBitmap);
				imgCard.setVisibility(View.VISIBLE);
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
					bitmapCard = bitmap_choose;
					imgCard.setImageBitmap(smallBitmap);
					imgCard.setVisibility(View.VISIBLE);
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
			if(!json.getString("PicUrl").isEmpty()){
				ImageLoaderUtil.displayImage(json.getString("PicUrl"), imgCard);
				imgCard.setVisibility(View.VISIBLE);
				tvState.setText(json.optString("AuditStatusName"));
			}
			etCard.setText(json.optString("IdentityNo"));
			etName.setText(json.optString("UserRealName"));
		}else if(task == taskSubmit){
			getActivity().finish();
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
