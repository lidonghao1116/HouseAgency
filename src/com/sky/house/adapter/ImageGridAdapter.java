package com.sky.house.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sky.house.R;

/**
 * 
 * @author skypan
 */

public class ImageGridAdapter extends BaseAdapter {

	private Activity mActivity;
	private List<Bitmap> imageList;

	public ImageGridAdapter(Activity a, List<Bitmap> imageList) {
		this.mActivity = a;
		this.imageList = imageList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageList.size();
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
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_picture, null);
			holder = new ViewHolder();
			holder.ivImage = (ImageView) convertView.findViewById(R.id.iv_item);
			holder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.ivImage.setImageBitmap(imageList.get(arg0));
		holder.ivDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				imageList.remove(arg0);
				notifyDataSetChanged();
			}
		});

		return convertView;
	}

	private static class ViewHolder {
		private ImageView ivImage,ivDelete;
	}

}
