package com.sky.house.resource;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;
import com.sky.house.adapter.OptionAdapter;
import com.sky.house.entity.MenuItem;
import com.sky.house.interfaces.CascadingMenuViewOnSelectListener;
import com.sky.house.widget.CascadingMenuPopWindow;

/**
 * 房源
 * 
 * @author skypan
 * 
 */
public class HouseListFragment extends BaseFragment {

	private ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
	private PopupWindow mPopRent;
	private OptionAdapter mAdapterRent;

	private CascadingMenuPopWindow cascadingMenuPopWindow = null;

	@ViewInit(id = R.id.tv_rent, onClick = "onClick")
	private TextView mTvRent;

	@ViewInit(id = R.id.tv_area, onClick = "onClick")
	private TextView mTvArea;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setRightButton1("发布房源", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SHContainerActivity.class);
				intent.putExtra("class", HousePublishFragment.class.getName());
				startActivity(intent);
			}
		});
		//假数据
		ArrayList<MenuItem> tempMenuItems = null;
		for (int j = 0; j < 7; j++) {
			tempMenuItems = new ArrayList<MenuItem>();
			for (int i = 0; i < 15; i++) {
				tempMenuItems.add(new MenuItem(false, "子菜单" + j + "" + i, null));
			}
			menuItems.add(new MenuItem(true, "主菜单" + j, tempMenuItems));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_source, container, false);
		return view;
	}

	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_rent:
			showRentPopupWindow();
			break;
		case R.id.tv_area:
			showPopMenu();
			break;
		}
	}

	private void showPopMenu() {
		mTvArea.setTextAppearance(getActivity(), R.style.TextMidGreen);
		if (cascadingMenuPopWindow == null) {
			cascadingMenuPopWindow = new CascadingMenuPopWindow(getActivity(), menuItems);
			cascadingMenuPopWindow.setMenuViewOnSelectListener(new NMCascadingMenuViewOnSelectListener());
		}
		cascadingMenuPopWindow.setBackgroundDrawable(getResources().getDrawable(R.color.color_full_transparent));
		cascadingMenuPopWindow.setFocusable(true);
		// 设置允许在外点击消失
		cascadingMenuPopWindow.setOutsideTouchable(true);
		// 设置显示动画
		cascadingMenuPopWindow.setAnimationStyle(R.style.TypeSelAnimationFade);
		cascadingMenuPopWindow.showAsDropDown(mTvArea, 0, 1);
		cascadingMenuPopWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				mTvArea.setTextAppearance(getActivity(), R.style.TextMidBlack);
			}
		});
	}

	// 级联菜单选择回调接口
	class NMCascadingMenuViewOnSelectListener implements CascadingMenuViewOnSelectListener {

		@Override
		public void getValue(MenuItem menuItem) {
			Toast.makeText(getActivity(), "" + menuItem.toString(), 1000).show();
		}

	}

	private void showRentPopupWindow() {
		mTvRent.setTextAppearance(getActivity(), R.style.TextMidGreen);
		int[] location = new int[2];
		mTvRent.getLocationInWindow(location);
		if (mPopRent == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = layoutInflater.inflate(R.layout.pop_option, null);
			ListView lv = (ListView) view.findViewById(R.id.lv_option);
			mAdapterRent = new OptionAdapter(getActivity());
			lv.setAdapter(mAdapterRent);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					mPopRent.dismiss();
					mTvRent.setText("1000-3000");
				}
			});
			// 创建一个PopuWidow对象
			mPopRent = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		// 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		mPopRent.setBackgroundDrawable(getResources().getDrawable(R.color.color_full_transparent));
		// 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
		mPopRent.setFocusable(true);
		// 设置允许在外点击消失
		mPopRent.setOutsideTouchable(true);
		// 设置显示动画
		mPopRent.setAnimationStyle(R.style.TypeSelAnimationFade);
		// 设置菜单显示的位置
		mPopRent.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.NO_GRAVITY, location[0], location[1] + mTvRent.getHeight() + 1);
		mPopRent.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				mTvRent.setTextAppearance(getActivity(), R.style.TextMidBlack);
			}
		});
	}

}
