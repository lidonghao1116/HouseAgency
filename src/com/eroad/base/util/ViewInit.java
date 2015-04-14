package com.eroad.base.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 注解
 * @author skypan
 * 运行时有效
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInit {

	/**
	 * 默认id
	 */
	public static int DEFAULT_ID = -1;
	
	/**
	 * 默认method
	 */
	public static String DEFAULT_METHOD = "";
	
	/**
	 * 功能:接收控件ID
	 * @return 返回设置ID
	 */
	public int id() default DEFAULT_ID;
	
	/**
	 * 功能:接收控件点击方法名
	 * @return 返回设置方法名
	 */
	public String onClick() default DEFAULT_METHOD;
}
