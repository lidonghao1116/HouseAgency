package com.sky.house.me;

import java.io.File;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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
import com.eroad.base.util.UserInfoManager;
import com.eroad.base.util.Utils;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.house.widget.ExampleDialog;
import com.sky.house.widget.ExampleDialog.ExampleDialogOnClick;
import com.sky.widget.SHDialog;
import com.sky.widget.SHDialog.DialogItemClickListener;
import com.sky.widget.SHToast;
import com.sky.widget.sweetdialog.SweetDialog;

/**
 * @author yebaohua
 * 
 */
public class HouseAuthenticationFragment extends BaseFragment implements ITaskListener, OnClickListener {
	@ViewInit(id = R.id.iv_auth, onClick = "onClick")
	private ImageView imgState;

	@ViewInit(id = R.id.et_name)
	private EditText etName;

	@ViewInit(id = R.id.et_card)
	private EditText etCard;

	@ViewInit(id = R.id.rl_upload, onClick = "onClick")
	private RelativeLayout rlUpload;

	@ViewInit(id = R.id.tv_upload, onClick = "onClick")
	private TextView tvStateName;

	@ViewInit(id = R.id.tv_state)
	private TextView tvState;

	@ViewInit(id = R.id.rl_submit)
	private RelativeLayout rlSubmit;

	@ViewInit(id = R.id.btn_submit, onClick = "onClick")
	private Button btnSubmit;

	@ViewInit(id = R.id.iv_question, onClick = "onClick")
	private ImageView imgQues;

	@ViewInit(id = R.id.iv_card)
	private ImageView imgCard;

	@ViewInit(id = R.id.tv_tips)
	private TextView tvTips;

	private final int TAKE_PICTURE = 0;// 拍照
	private final int CHOOSE_PICTURE_LESS = 1;// 相册 4.4以下
	private final int CHOOSE_PICTURE_ABOVE = 5;// 相册 4.4以上（包含4.4）

	private SHPostTaskM taskSubmit, taskAuthinfo;
	private Bitmap bitmapCard;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_authentication, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mDetailTitlebar.setTitle("认证信息");
		requestAuthInfo();
	}

	private void requestAuthInfo() {
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		taskAuthinfo = new SHPostTaskM();
		taskAuthinfo.setUrl(ConfigDefinition.URL + "GetUserAuthInfo");
		taskAuthinfo.setListener(this);
		taskAuthinfo.start();
	}

	private void submit() {
		String realName = etName.getText().toString().trim();
		String card = etCard.getText().toString().trim();
		if (realName.isEmpty()) {
			SHToast.showToast(getActivity(), "请填写您的真实姓名", Toast.LENGTH_SHORT);
			return;
		}
		if (card.isEmpty() || card.length() != 18) {
			SHToast.showToast(getActivity(), "请正确填写您的证件号", Toast.LENGTH_SHORT);
			return;
		}
		if (bitmapCard == null) {
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
		case R.id.iv_auth:
			modifyPhoto();
			break;
		case R.id.rl_upload:
			modifyPhoto();
			break;
		case R.id.tv_upload:
			new ExampleDialog(getActivity(), R.drawable.img_card_example, new ExampleDialogOnClick() {

				@Override
				public void exampleOnClick(Dialog d) {
					// TODO Auto-generated method stub
					d.dismiss();
				}
			}).show();
			break;
		case R.id.iv_question:
			if (tvTips.getVisibility() == View.INVISIBLE) {
				tvTips.setVisibility(View.VISIBLE);
			} else {
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

					if (Build.VERSION.SDK_INT < 19) {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						// 由于startActivityForResult()的第二个参数"requestCode"为常量，
						// 个人喜好把常量用一个类全部装起来，不知道各位大神对这种做法有异议没？
						startActivityForResult(intent, CHOOSE_PICTURE_LESS);
					} else {
						Intent intent = new Intent();
						intent.setType("image/*");
						// 由于Intent.ACTION_OPEN_DOCUMENT的版本是4.4以上的内容
						// 所以注意这个方法的最上面添加了@SuppressLint("InlinedApi")
						// 如果客户使用的不是4.4以上的版本，因为前面有判断，所以根本不会走else，
						// 也就不会出现任何因为这句代码引发的错误
						intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
						startActivityForResult(intent, CHOOSE_PICTURE_ABOVE);
					}
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
				Bitmap bitmap_tack = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/temp.png", bitmapOptions);
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap_tack, bitmap_tack.getWidth() / 6, bitmap_tack.getHeight() / 6);
				// bitmap_tack.recycle();
				bitmapCard = bitmap_tack;
				imgCard.setImageBitmap(bitmap_tack);
				imgCard.setVisibility(View.VISIBLE);
				break;
			case CHOOSE_PICTURE_LESS:
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
					imgCard.setImageBitmap(bitmap_choose);
					imgCard.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case CHOOSE_PICTURE_ABOVE:
				Uri uri = data.getData();
				ContentResolver cr = getActivity().getContentResolver();  
				// 先将这个uri转换为path，然后再转换为uri
				String thePath = Utils.getInstance().getPath(getActivity(), uri);
				try {
					Bitmap bitmap_choose = BitmapFactory.decodeFile(thePath, bitmapOptions);
					Bitmap smallBitmap = ImageTools.zoomBitmap(bitmap_choose, bitmap_choose.getWidth() / 6, bitmap_choose.getHeight() / 6);
					// bitmap_choose.recycle();
					bitmapCard = bitmap_choose;
					imgCard.setImageBitmap(bitmap_choose);
					imgCard.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}

		}
	}

	private void checkStatus(boolean enable) {
		if (enable) {
			imgState.setEnabled(true);
			etName.setEnabled(true);
			etCard.setEnabled(true);
			rlUpload.setEnabled(true);
			rlSubmit.setVisibility(View.VISIBLE);
		} else {
			imgState.setEnabled(false);
			etName.setEnabled(false);
			etName.setFocusable(false);

			etCard.setEnabled(false);
			etCard.setFocusable(false);

			rlUpload.setEnabled(false);
			rlSubmit.setVisibility(View.GONE);
		}
	}

	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		if (task == taskAuthinfo) {
			JSONObject json = (JSONObject) task.getResult();
			//status=-1 未认证 status=0 等待认证 status=1 认证通过 status=2认证失败(可编辑)
			if (json.getString("auditStatus").equalsIgnoreCase("-1")) {
				return;
			}else if (json.getString("auditStatus").equalsIgnoreCase("0")) {
				imgState.setBackgroundResource(R.drawable.ic_auth_fali);
				checkStatus(false);
			} else if (json.getString("auditStatus").equalsIgnoreCase("1")) {
				imgState.setBackgroundResource(R.drawable.ic_auth_success);
				checkStatus(false);
				ConfigDefinition.isAuth = true;
				UserInfoManager.getInstance().setAuth(true);
				UserInfoManager.getInstance().sync(getActivity(), true);
			} else {
				imgState.setBackgroundResource(R.drawable.ic_auth_fali);
				checkStatus(true);
			}
			if (!json.getString("picUrl").isEmpty()) {
				ImageLoaderUtil.displayImage(json.getString("picUrl"), imgCard);
//				imgCard.setVisibility(View.VISIBLE);
				tvStateName.setText("证件审核");
				tvStateName.setEnabled(false);
				tvStateName.setCompoundDrawables(null, null, null, null);
				tvState.setText(json.optString("auditStatusName") == null ? "" : json.optString("auditStatusName"));
				etCard.setText(json.optString("identityNo") == null ? "" : json.optString("identityNo"));
				etName.setText(json.optString("userRealName") == null ? "" : json.optString("userRealName"));
			}
		} else if (task == taskSubmit) {
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
