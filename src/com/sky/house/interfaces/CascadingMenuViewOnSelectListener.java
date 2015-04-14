package com.sky.house.interfaces;

import com.sky.house.entity.MenuItem;

/**
 * 级联回调
 * @author skypan
 *
 */
public interface CascadingMenuViewOnSelectListener {
	public void getValue(MenuItem menuItem);
}
