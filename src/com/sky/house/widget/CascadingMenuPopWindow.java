package com.sky.house.widget;
import java.util.ArrayList;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.sky.house.R;
import com.sky.house.entity.MenuItem;
import com.sky.house.interfaces.CascadingMenuViewOnSelectListener;
/**
 * @author skypan
 * 
 */
public class CascadingMenuPopWindow extends PopupWindow{

	private Context context;
	private CascadingMenuView cascadingMenuView;
	private ArrayList<MenuItem> menuItems=null;
    //提供给外的接口
	private CascadingMenuViewOnSelectListener menuViewOnSelectListener;
	
	public void setMenuItems(ArrayList<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}
	public void setMenuViewOnSelectListener(
			CascadingMenuViewOnSelectListener menuViewOnSelectListener) {
		this.menuViewOnSelectListener = menuViewOnSelectListener;
	}
     
	public CascadingMenuPopWindow(Context context,ArrayList<MenuItem> list) {
		super(context);
		this.context=context;
		this.menuItems=list;
		init();
	}
	
	public void init(){
		//实例化级联菜单
		cascadingMenuView=new CascadingMenuView(context,menuItems);
		setContentView(cascadingMenuView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		//设置回调接口
		cascadingMenuView.setCascadingMenuViewOnSelectListener(new MCascadingMenuViewOnSelectListener());
	}
	//级联菜单选择回调接口
	class MCascadingMenuViewOnSelectListener implements CascadingMenuViewOnSelectListener{

		@Override
		public void getValue(MenuItem menuItem) {
			// TODO Auto-generated method stub
			if(menuViewOnSelectListener!=null){
				menuViewOnSelectListener.getValue(menuItem);
				dismiss();
			}
		}

		
		
	}
}
