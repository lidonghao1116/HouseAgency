package com.eroad.base;

import java.util.HashMap;


public class SHArgsContent<K,V> extends HashMap<K, Object>{

	private String mTarget;
	/**
	 * 目标
	 * @param target
	 */
	public void setTarget(String target)	{
		mTarget = target;
	}
	
	public String getTarget(){
		return mTarget;
	}
	private Object mDelegate;
	/**
	 * 目标
	 * @param target
	 */
	public void setDelegate(Object target)	{
		mDelegate = target;
	}
	
	public Object getDelegate(){
		return mDelegate;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 取int
	 * @param key
	 * @return
	 */
	public  int getInt(K key){
		Integer integer = (Integer) this.get(key);
		if(integer != null){
			return integer.intValue();
		}else{
			return 0;
		}
		
	}
	/**
	 * 取Double
	 * @param key
	 * @return
	 */
	public  double getDouble(K key){
		Double dou = (Double) this.get(key);
		if(dou != null){
			return dou.doubleValue();
		}else{
			return 0.0;
		}
	}
	/**
	 * setDouble
	 * @param key
	 * @param value
	 * @return
	 */
	public  Object setDouble(K key,double value ){
		return this.put(key,(Object) new Double(value));
	}
	/**
	 * setInt
	 * @param key
	 * @param value
	 * @return
	 */
	public  Object setInt(K key,int value){
		return this.put(key,(Object) new Integer(value));
	}
}
