package com.sky.house.resource.publish;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHApplication;
import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ImageTools;
import com.eroad.base.util.ViewInit;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.house.adapter.ImageGridAdapter;
import com.sky.house.widget.ExampleDialog;
import com.sky.house.widget.ExampleDialog.ExampleDialogOnClick;
import com.sky.house.widget.MyGridView;
import com.sky.widget.SHDialog;
import com.sky.widget.SHDialog.DialogItemClickListener;
import com.sky.widget.sweetdialog.SweetDialog;
import com.sky.widget.SHToast;

/**
 * 发布房源 下一步
 * 
 * @author skypan
 * 
 */
public class HousePublishNextFragment extends BaseFragment implements ITaskListener {

	@ViewInit(id = R.id.btn_publish, onClick = "onClick")
	private Button mBtnPublish;

	@ViewInit(id = R.id.gv_house)
	private MyGridView mGvHouse;

	@ViewInit(id = R.id.tv_add_picture, onClick = "onClick")
	private TextView mTvAddPicture;

	@ViewInit(id = R.id.ll_agreement)
	private LinearLayout mLlAgreement;

	@ViewInit(id = R.id.tv_add_agreement, onClick = "onClick")
	private TextView mTvAddAgreement;

	@ViewInit(id = R.id.gv_agreement)
	private MyGridView mGvAgreement;

	@ViewInit(id = R.id.iv_identification)
	private ImageView mIvIdenti;

	@ViewInit(id = R.id.iv_property)
	private ImageView mIvProperty;

	@ViewInit(id = R.id.ll_add_identification, onClick = "onClick")
	private LinearLayout mLlAddIdenti;

	@ViewInit(id = R.id.tv_add_identification, onClick = "onClick")
	private TextView mTvAddIdenti;

	@ViewInit(id = R.id.ll_add_property, onClick = "onClick")
	private LinearLayout mLlAddProperty;

	private final int TAKE_PICTURE = 0;// 拍照
	private final int CHOOSE_PICTURE = 1;// 相册
	private final int MAX_HOUSE_PICTURE = 10;// 最大房源图片数量
	private final int MAX_AGREEMENT_PICTURE = 5;// 最大合同图片数量
	private int currentSelect = 0;

	private List<Bitmap> houseImageList = new ArrayList<Bitmap>();// 房源图片
	private List<Bitmap> agreementImageList = new ArrayList<Bitmap>();// 合同图片
	private Bitmap bmIdenti;// 身份证图片
	private Bitmap bmProperty;// 房产图片

	private ImageGridAdapter houseImageAdapter, agreeImageAdapter;

	@ViewInit(id = R.id.iv_example_house, onClick = "onClick")
	private ImageView mIvExampleHouse;

	@ViewInit(id = R.id.iv_agreement, onClick = "onClick")
	private ImageView mIvAgreement;

	@ViewInit(id = R.id.iv_example_identi, onClick = "onClick")
	private ImageView mIvExampleIdenti;

	private SHPostTaskM uploadTask;

	private int progress = 0;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("发布房源");
		if (getActivity().getIntent().getIntExtra("lordType", 1) == 2) {
			mLlAgreement.setVisibility(View.VISIBLE);
		}
		;
		houseImageAdapter = new ImageGridAdapter(getActivity(), houseImageList);
		agreeImageAdapter = new ImageGridAdapter(getActivity(), agreementImageList);
		mGvHouse.setAdapter(houseImageAdapter);
		mGvAgreement.setAdapter(agreeImageAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_publish_next, container, false);
		return view;
	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_add_picture:
			currentSelect = 0;
			if (houseImageList.size() >= MAX_HOUSE_PICTURE) {
				SHToast.showToast(getActivity(), "最多上传" + MAX_HOUSE_PICTURE + "张", 1000);
			} else {
				selectPicture();
			}
			break;
		case R.id.tv_add_agreement:
			currentSelect = 1;
			if (agreementImageList.size() >= MAX_AGREEMENT_PICTURE) {
				SHToast.showToast(getActivity(), "最多上传" + MAX_AGREEMENT_PICTURE + "张", 1000);
			} else {
				selectPicture();
			}
			break;
		case R.id.ll_add_identification:
			currentSelect = 2;
			selectPicture();
			break;
		case R.id.ll_add_property:
			currentSelect = 3;
			selectPicture();
			break;
		case R.id.iv_example_house:
			new ExampleDialog(getActivity(), R.drawable.test, new ExampleDialogOnClick() {

				@Override
				public void exampleOnClick(Dialog d) {
					// TODO Auto-generated method stub
					d.dismiss();
				}
			}).show();
			break;
		case R.id.iv_agreement:
			new ExampleDialog(getActivity(), R.drawable.test, new ExampleDialogOnClick() {

				@Override
				public void exampleOnClick(Dialog d) {
					// TODO Auto-generated method stub
					d.dismiss();
				}
			}).show();
			break;
		case R.id.iv_example_identi:
			new ExampleDialog(getActivity(), R.drawable.test, new ExampleDialogOnClick() {

				@Override
				public void exampleOnClick(Dialog d) {
					// TODO Auto-generated method stub
					d.dismiss();
				}
			}).show();
			break;
		case R.id.btn_publish:
			final ProgressDialog mProgressdialog = new ProgressDialog(getActivity());
			mProgressdialog.setMessage("正在上传");
			mProgressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressdialog.setIndeterminate(true);
			mProgressdialog.setMax(100);
			mProgressdialog.setCancelable(false);
			mProgressdialog.show();
			uploadTask = new SHPostTaskM();
			uploadTask.setUrl(ConfigDefinition.URL + "PublishHouseImage");
			uploadTask.setListener(this);
			uploadTask.getTaskArgs().put("houseId", getActivity().getIntent().getIntExtra("houseId", -1));
			JSONArray imageArray = new JSONArray();
			for (Bitmap b : houseImageList) {
				JSONObject json = new JSONObject();
				try {
					json.put("type", 1);
					json.put("picString", CommonUtil.bitmap2Base64(b));
					imageArray.put(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			for (Bitmap b : agreementImageList) {
				JSONObject json = new JSONObject();
				try {
					json.put("type", 3);
					json.put("picString", CommonUtil.bitmap2Base64(b));
					imageArray.put(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			JSONObject json = new JSONObject();
			try {
				json.put("type", 4);
				json.put("picString", CommonUtil.bitmap2Base64(bmIdenti));
				imageArray.put(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			json = new JSONObject();
			try {
				json.put("type", 5);
				json.put("picString", CommonUtil.bitmap2Base64(bmProperty));
				imageArray.put(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			uploadTask.getTaskArgs().put("imageList", imageArray);
			uploadTask.start();
			// 打造假进度条
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (progress < 90) {
						progress += 5;
						mProgressdialog.setProgress(progress);
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			}).start();
			break;
		}
	}

	private void selectPicture() {
		final String[] items = new String[] { "拍照", "相册" };
		SHDialog.showActionSheet(getActivity(), "选择", items, new DialogItemClickListener() {
			@Override
			public void onSelected(String result) {
				// TODO Auto-generated method stub
				if (result.equals(items[0])) {
					Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "house_temp.png"));
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(openCameraIntent, TAKE_PICTURE);
				} else {
					Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
				Bitmap bitmap_tack = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/house_temp.png", bitmapOptions);
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap_tack, bitmap_tack.getWidth() / 6, bitmap_tack.getHeight() / 6);
				switch (currentSelect) {
				case 0:
					houseImageList.add(newBitmap);
					houseImageAdapter.notifyDataSetChanged();
					break;
				case 1:
					agreementImageList.add(newBitmap);
					agreeImageAdapter.notifyDataSetChanged();
					break;
				case 2:
					bmIdenti = newBitmap;
					mIvIdenti.setImageBitmap(bmIdenti);
					break;
				case 3:
					bmProperty = newBitmap;
					mIvProperty.setImageBitmap(bmProperty);
					break;
				}
				bitmap_tack.recycle();
				break;
			case CHOOSE_PICTURE:
				Uri originalUri = data.getData();
				String[] filePathColumns = { MediaStore.Images.Media.DATA };
				Cursor c = getActivity().getContentResolver().query(originalUri, filePathColumns, null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePathColumns[0]);
				String picturePath = c.getString(columnIndex);
				c.close();
				try {
					Bitmap bitmap_choose = BitmapFactory.decodeFile(picturePath, bitmapOptions);
					Bitmap smallBitmap = ImageTools.zoomBitmap(bitmap_choose, bitmap_choose.getWidth() / 6, bitmap_choose.getHeight() / 6);
					switch (currentSelect) {
					case 0:
						houseImageList.add(smallBitmap);
						houseImageAdapter.notifyDataSetChanged();
						break;
					case 1:
						agreementImageList.add(smallBitmap);
						agreeImageAdapter.notifyDataSetChanged();
						break;
					case 2:
						bmIdenti = smallBitmap;
						mIvIdenti.setImageBitmap(bmIdenti);
						break;
					case 3:
						bmProperty = smallBitmap;
						mIvProperty.setImageBitmap(bmIdenti);
						break;
					}
					bitmap_choose.recycle();
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
		finish();
	}

	@Override
	public void onTaskFailed(SHTask task) {
		// TODO Auto-generated method stub
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
