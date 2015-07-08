package com.eroad.base.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ViewUtil {
	public static void setListViewHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)

		{
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++)

		{
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static void setExpandableListViewHeight(ExpandableListView listView) {
		ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getGroupCount(); i++) {
			View listItem = listAdapter.getGroupView(i, true, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
			System.out.println(listItem.getMeasuredHeight());
			for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
				View childItem = listAdapter.getChildView(i, j, false, null, listView);
				// getView(i, null, listView);
				childItem.measure(0, 0);
				totalHeight += childItem.getMeasuredHeight();
			}
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight+(listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
		listView.setLayoutParams(params);
	}
	
}
