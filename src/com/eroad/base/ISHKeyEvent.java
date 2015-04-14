package com.eroad.base;

import android.view.KeyEvent;

public interface ISHKeyEvent {
	/**
	 * 
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) ;
	/**
	 * 
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public boolean onKeyLongPress(int keyCode, KeyEvent event) ;
	/**
	 * 
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event) ;
	/**
	 * 
	 * @param keyCode
	 * @param repeatCount
	 * @param event
	 * @return
	 */
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) ;
}
