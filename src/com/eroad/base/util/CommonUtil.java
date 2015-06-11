package com.eroad.base.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.eroad.base.SHApplication;

/**
 * 工具类
 * 
 * @author skypan
 * 
 */
public class CommonUtil {
	/**
	 * 字符拆分字符 返回list
	 * 
	 * @return list
	 */
	public static List<String> getListByString(String string, String speter) {
		List<String> list = new ArrayList<String>();
		if (string.isEmpty())
			return list;
		String[] s = string.split(speter);
		for (String temp : s) {
			list.add(temp);
		}
		return list;

	}

	/**
	 * 合并JSONArray
	 * 
	 * @param oldArray
	 *            原来的array
	 * @param tempArray
	 *            要加入的array
	 * @return
	 */
	public static JSONArray combineArray(JSONArray oldArray, JSONArray tempArray) {
		if (oldArray == null) {
			oldArray = new JSONArray();
		}
		if (tempArray.length() != 0) {
			for (int i = 0; i < tempArray.length(); i++) {
				oldArray.put(tempArray.opt(i));
			}
		}

		return oldArray;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 *            入参
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.length() <= 0 || "".equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * 文件转base64
	 * 
	 * @param path
	 *            路径
	 * @return
	 */
	public static String file2Base64(String path) {
		File file = new File(path);
		FileInputStream inputFile;
		byte[] buffer = null;
		try {
			inputFile = new FileInputStream(file);
			buffer = new byte[(int) file.length()];
			inputFile.read(buffer);
			inputFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Base64.encodeToString(buffer, Base64.DEFAULT);
	}

	/**
	 * bitmap转base64
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmap2Base64(Bitmap bitmap) {
		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 检查摄像头 是否存在
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			// 摄像头存在

			return isCameraCanUse();
		} else {
			// 摄像头不存在
			return false;
		}
	}

	/**
	 * 测试当前摄像头能否被使用
	 * 
	 * @return
	 */
	public static boolean isCameraCanUse() {
		boolean canUse = true;
		Camera mCamera = null;
		try {
			// TODO camera驱动挂掉,处理??
			mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
		} catch (Exception e) {
			try {
				mCamera = Camera.open();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				canUse = false;
			}
		}
		if (canUse) {
			mCamera.release();
			mCamera = null;
		}

		return canUse;
	}

	public static boolean isMobile(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9]*[-_.]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 密码由6- 20位 数字、英文或字符
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isSuitPassword(String passwrod) {
		if (passwrod.isEmpty()) {
			return false;
		}
		// String
		// str="^(?![0-9]*$)(?![a-zA-Z]*$)[a-zA-Z0-9~!@#$%^&*()_+;',./\\:\"<>?|]{6,18}$";
		String str = "^[a-zA-Z0-9~!@#$%^&*()_+;',./\\:\"<>?|]{6,18}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(passwrod);
		return m.matches();
	}

	/**
	 * dp转px
	 * 
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	/**
	 * px转dp
	 * 
	 * @param context
	 * @param px
	 * @return
	 */
	public static int px2dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/**
	 * sp*ppi/160 =px
	 * 
	 * @param ctx
	 * @param dip
	 * @return
	 */
	public static int sp2pX(final Context ctx, float sp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, ctx.getResources().getDisplayMetrics());
	}

	/**
	 * JSONArry 删除 index 索引元素
	 * 
	 * @param arry
	 * @param index
	 * @return
	 */
	public static JSONArray removeJSONArray(JSONArray arry, int index) {
		try {
			JSONArray newArray = new JSONArray();
			for (int i = 0; i < arry.length(); i++) {
				if (i != index)
					newArray.put(arry.getJSONObject(i));
			}
			arry = newArray;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arry;
	}

	public static JSONArray removeObj(JSONArray arry, int obj) {
		try {
			JSONArray newArray = new JSONArray();
			for (int i = 0; i < arry.length(); i++) {
				if(arry.getInt(i) != obj){
					newArray.put(arry.get(i));
				}
			}
			arry = newArray;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arry;
	}

	
	/**
	 * jsonarry删除 字段key == value的 jsonarry
	 * 
	 * @param arry
	 * @param key
	 * @param value
	 * @return
	 */
	public static JSONArray removeJSONArray(JSONArray arry, String key, String value) {
		try {
			JSONArray newArray = new JSONArray();
			for (int i = 0; i < arry.length(); i++) {
				if (!arry.getJSONObject(i).getString(key).equalsIgnoreCase(value))
					newArray.put(arry.getJSONObject(i));
			}
			arry = newArray;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arry;
	}

	/**
	 * 判断 jsonarry 是否存在该值
	 * 
	 * @param arry
	 * @param key
	 *            比较的字段
	 * @param value
	 * @return
	 */
	public static boolean containsJSONArray(JSONArray arry, String key, String value) {
		try {
			for (int i = 0; i < arry.length(); i++) {
				if (arry.getJSONObject(i).getString(key).equalsIgnoreCase(value))
					return true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

	public static boolean containsObj(JSONArray jsonArray,Object obj){
		for(int i=0;i<jsonArray.length();i++){
			try {
				if(obj.equals(jsonArray.get(i))){
					return true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 日期格式转换类
	 * 
	 * @author skypan
	 * 
	 */
	public static class Date {
		/**
		 * 转换成 2015-01－01格式
		 * 
		 * @param time
		 * @return
		 */
		public static String toYMR(String time) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date;
			String str = null;
			try {
				date = sdf.parse(time);
				str = sdf.format(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return str;
		}
	}

	public static class FileUtils {
		/**
		 * 读取表情配置文件
		 * 
		 * @param context
		 * @return
		 */
		public static List<String> getEmojiFile(Context context) {
			try {
				List<String> list = new ArrayList<String>();
				InputStream in = context.getResources().getAssets().open("emoji");
				BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				String str = null;
				while ((str = br.readLine()) != null) {
					list.add(str);
				}

				return list;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public static class InputTools {
		// 隐藏虚拟键盘
		public static void HideKeyboard(View v) {
			InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

			}
		}

		// 显示虚拟键盘
		public static void ShowKeyboard(View v) {
			InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

			imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

		}
	}
	
	/**
	 * 窗口类
	 * @author skypan
	 *
	 */
	public static class Window {
		/**
		 * 获取屏幕宽度
		 * @return px
		 */
		public static int getWidth() {
			WindowManager wm = (WindowManager) SHApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
			int width = wm.getDefaultDisplay().getWidth();
			return width;
		}

		/**
		 * 获取屏幕高度
		 * @return px
		 */
		public static int getHeight() {
			WindowManager wm = (WindowManager) SHApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
			int height = wm.getDefaultDisplay().getHeight();
			return height;
		}
	}
	
	public static class JSONUtil{
		public static void clear(JSONObject json){
			Iterator it = json.keys();  
			while(it.hasNext()){
				String key = (String) it.next();
				it.remove();
			}
		}
		
		public static void copyJson(JSONObject json,JSONObject secJson){
			Iterator it = secJson.keys();  
			while(it.hasNext()){
				String key = (String) it.next();
				try {
					json.put(key, secJson.get(key));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	public final static String encodeMD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
