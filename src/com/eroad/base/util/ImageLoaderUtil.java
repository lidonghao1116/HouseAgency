package com.eroad.base.util;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * 图片加载工具类
 * @author jiema
 *
 */
public class ImageLoaderUtil {

	/*
	 * 图片显示选项
	 */
	public static DisplayImageOptions options = new DisplayImageOptions.Builder()
//	.showStubImage(R.drawable.ic_family_leave)			// 设置图片下载期间显示的图片
//	.showImageForEmptyUri(R.drawable.ic_family_leave)	// 设置图片Uri为空或是错误的时候显示的图片
//	.showImageOnFail(R.drawable.ic_family_leave)		// 设置图片加载或解码过程中发生错误显示的图片	
//	.displayer(new RoundedBitmapDisplayer(20))	// 设置成圆角图片 20
	.cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
	.cacheOnDisc(true)							// 设置下载的图片是否缓存在SD卡中
	.build();
	
	private static ImageLoader imageLoader = ImageLoader.getInstance();
	
//	/**
//	 * 初始化图片加载器
//	 * @param context
//	 */
//	public static void initImageLoader(Context context) {
//		// This configuration tuning is custom. You can tune every option, you may tune some of them,
//		// or you can create default configuration by
//		//  ImageLoaderConfiguration.createDefault(this);
//		// method.
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//		.threadPriority(Thread.NORM_PRIORITY - 2)
//		.denyCacheImageMultipleSizesInMemory()
//		.discCacheFileNameGenerator(new Md5FileNameGenerator())
//		.tasksProcessingOrder(QueueProcessingType.LIFO)
//		.writeDebugLogs() // Remove for release app
//		.build();
//		// Initialize ImageLoader with configuration.
//		ImageLoader.getInstance().init(config);
//	}
	public static void displayImage(String uri, ImageView imageView) {
		imageLoader.displayImage(uri, imageView,options);
	}
}
