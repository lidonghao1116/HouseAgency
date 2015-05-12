package com.sky.house.adapter;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eroad.base.util.CommonUtil;
import com.eroad.base.util.ImageLoaderUtil;
import com.sky.house.R;

public class TopAdvertPagerAdapter extends PagerAdapter {

	private LayoutInflater mInflater;
	private HashMap<Integer, View> mCacheView = new HashMap<Integer, View>();
	private OnClickListener mItemClickListener;
	private Context mContext;
	private JSONArray mJsonArray;

	public TopAdvertPagerAdapter(Context context, JSONArray jsonArray) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mJsonArray = jsonArray;
		mContext = context;
		if (mJsonArray == null) {
			mJsonArray = new JSONArray();
		}

		mItemClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ViewHolder holder = (ViewHolder) v.getTag();
				String url = holder.ulr;
			}
		};
	}

	@Override
	public int getCount() {
		return mJsonArray.length();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (mCacheView.containsKey(position)) {
			View view = mCacheView.get(position);
			if (container.indexOfChild(view) != -1) {
				container.removeView(view);
			}
		}
		// container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		ViewHolder holder = null;
		View view = mInflater.inflate(R.layout.top_advert_item, null);
		holder = new ViewHolder();
		holder.iv = (ImageView) view.findViewById(R.id.iv_advert);
//		holder.iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtil.Window.getWidth()*(2/5)));
		view.setTag(holder);

		try {
			ImageLoaderUtil.displayImage(mJsonArray.getJSONObject(position).getString("bannerImgUrl"), holder.iv);
			holder.ulr = mJsonArray.getJSONObject(position).getString("bannerUrl");
			holder.index = position;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		view.setOnClickListener(mItemClickListener);
		container.addView(view);
		mCacheView.put(position, view);
		// view.setOnClickListener(mItemClickListener);
		return view;

	}

	static class ViewHolder {
		ImageView iv;
		String ulr, title;
		int index;
	}
}
