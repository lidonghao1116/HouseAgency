package com.sky.house.resource.publish;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.eroad.base.BaseFragment;
import com.eroad.base.util.ImageTools;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;
import com.sky.house.adapter.ImageGridAdapter;
import com.sky.house.widget.ExampleDialog;
import com.sky.house.widget.ExampleDialog.ExampleDialogOnClick;
import com.sky.house.widget.MyGridView;
import com.sky.widget.SHDialog;
import com.sky.widget.SHDialog.DialogItemClickListener;
import com.sky.widget.SHToast;

/**
 * 发布房源 下一步
 * 
 * @author skypan
 * 
 */
public class HousePublishNextFragment extends BaseFragment {

	@ViewInit(id = R.id.gv_house)
	private MyGridView mGvHouse;

	@ViewInit(id = R.id.tv_add_picture, onClick = "onClick")
	private TextView mTvAddPicture;

	private final int TAKE_PICTURE = 0;// 拍照
	private final int CHOOSE_PICTURE = 1;// 相册
	private final int MAX_HOUSE_PICTURE = 10;//最大房源图片数量

	private List<Bitmap> houseImageList = new ArrayList<Bitmap>();
	
	private ImageGridAdapter imageAdapter;
	
	@ViewInit(id = R.id.iv_example_house,onClick = "onClick")
	private ImageView mIvExampleHouse;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("发布房源");
		imageAdapter = new ImageGridAdapter(getActivity(), houseImageList);
		mGvHouse.setAdapter(imageAdapter);
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
			if(houseImageList.size() >= MAX_HOUSE_PICTURE){
				SHToast.showToast(getActivity(), "最多上传"+MAX_HOUSE_PICTURE+"张", 1000);
			}else{
				selectPicture();
			}
			break;
		case R.id.iv_example_house:
			new ExampleDialog(getActivity(), R.drawable.test,new ExampleDialogOnClick() {
				
				@Override
				public void exampleOnClick(Dialog d) {
					// TODO Auto-generated method stub
					d.dismiss();
				}
			}).show(); 
			break;
		}
	}
	
	private void selectPicture(){
		final String[] items = new String[] { "拍照", "相册" };
		SHDialog.showActionSheet(getActivity(), "上传房源图片", items, new DialogItemClickListener() {
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
				houseImageList.add(newBitmap);
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
					houseImageList.add(smallBitmap);
					bitmap_choose.recycle();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			imageAdapter.notifyDataSetChanged();
		}
	}

}
