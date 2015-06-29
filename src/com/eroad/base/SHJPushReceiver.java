package com.eroad.base;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.eroad.base.util.UserInfoManager;
import com.next.app.StandardApplication;
import com.next.message.SHMsgManager;
import com.sky.house.home.HouseLoginFragment;
import com.sky.house.home.HouseMainActivity;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class SHJPushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	private static SHJPushReel reel;
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		if(reel == null){
			reel = new SHJPushReel();
			SHMsgManager.getInstance().setReel(reel);
		}

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			//send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Log.e("push", extras);
			ComponentName componetName = new ComponentName("com.sky.house", "com.sky.house.HouseMainActivity");  
			try {  
			    Intent intent_open = new Intent();  
			    intent_open.setComponent(componetName);  
			    SHApplication.getInstance().startActivity(intent_open);  
			} catch (Exception e) {  
			}  
//			try {
//				JSONObject extraJson = new JSONObject(extras);
//				if(extraJson != null){
//					JSONObject jsonObject = new JSONObject(extraJson.getString("data"));
//					if (TextUtils.isEmpty(SHEnvironment.getInstance().getSession())) {
//						SHApplication.getInstance().open(ConfigDefinition.SCHEME+"login");
//					} else {
//						//eHR://login?id=12&a=1
//						SHApplication.getInstance().open(jsonObject.optString("action"));
//					}
//				}
//
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
			//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
			System.out.println(intent);
		} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} 
			else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		//	if (MainActivity.isForeground) {
		//String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		if(extras != null){
			try {
				JSONObject extraJson = new JSONObject(extras);
				if(extraJson.getInt("code") == -5){
					SHApplication.getInstance().exitApplication();
					Intent intent = new Intent(SHApplication.getInstance(),SHContainerActivity.class);
					intent.putExtra("class", HouseLoginFragment.class.getName());
					SHApplication.getInstance().startActivity(intent);
					UserInfoManager.getInstance().setSession("");
					UserInfoManager.getInstance().sync(SHApplication.getInstance(), true);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.i("push", extras);
		Intent intent = new Intent("JPUSH_MSG");
		StandardApplication.getInstance().sendBroadcast(intent); 
//		SHResMsgM msg = new SHResMsgM();
//		reel.processPackage(msg);
//		try {
//			JSONObject extraJson = new JSONObject(extras);
//			if(extraJson != null){
//				SHResMsgM msg = new SHResMsgM();
//				msg.setResult(extraJson.getString("data"));
//				System.out.println(extraJson.getString("data"));
//				msg.setRespinfo(new SHRespinfo(extraJson.getInt("code"),extraJson.getString("message") ));
//				msg.setResponse(extraJson.getString("response"));
//				reel.processPackage(msg);
//			}
//
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
}
