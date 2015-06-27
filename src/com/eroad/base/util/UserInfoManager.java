package com.eroad.base.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.next.app.StandardApplication;
import com.next.util.Log;

/**
 * 
 * 
 * @author skypan
 * 
 */
public class UserInfoManager {
	/**
	 * 
	 */
	private String userId;
	private int uid;

	private boolean personInfoState;
	private boolean firstInstall;//  
	private boolean autoLogin;// 
	private boolean isAuth;

	public boolean isAuth() {
		return isAuth;
	}

	public void setAuth(boolean isAuth) {
		this.isAuth = isAuth;
	}

	private String email;
	private String moblie;
	private String displayName;
	private String session;


	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private String password;

	private static UserInfoManager _instance;

	public static synchronized UserInfoManager getInstance() {
		if (_instance == null) {
			_instance = new UserInfoManager();
			_instance.sync(StandardApplication.getInstance(),false);
		}
		return _instance;
	}

	/**
	 * 
	 * 
	 * @param context
	 * @param isWrite
	 */
	public void sync(Context context, boolean isWrite) {
		SharedPreferences pref = context.getSharedPreferences(ConfigDefinition.PREFS_DATA, Context.MODE_PRIVATE);
		if (isWrite) {
			SharedPreferences.Editor editor = pref.edit();
			if (userId != null) {
				editor.putString("userId", userId);
			}
			if (name != null) {
				editor.putString("name", name);
			}
			if (password != null) {
				editor.putString("password", password);
			}
			if (moblie != null) {
				editor.putString("moblie", moblie);
			}
			if (email != null) {
				editor.putString("email", email);
			}
			if (displayName != null) {
				editor.putString("displayName", displayName);
			}
			if(session != null){
				editor.putString("session", session);
			}
			editor.putBoolean("personInfoState", personInfoState);
			editor.putBoolean("firstInstall", firstInstall);
			editor.putBoolean("autoLogin", autoLogin);
			editor.putBoolean("isAuth", isAuth);
			editor.commit();
			
		} else {
			this.userId = pref.getString("userId", null);
			this.name = pref.getString("name", null);
			this.password = pref.getString("password", null);
			this.moblie = pref.getString("moblie", null);
			this.email = pref.getString("email", null);
			this.displayName = pref.getString("displayName", null);
			this.personInfoState = pref.getBoolean("personInfoState", false);
			this.firstInstall = pref.getBoolean("firstInstall", true);//  
			this.autoLogin = pref.getBoolean("autoLogin", true);// 
			this.session = pref.getString("session", null);
			this.isAuth = pref.getBoolean("isAuth", true);// 
		}
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isPersonInfoState() {
		return personInfoState;
	}

	public void setPersonInfoState(boolean personInfoState) {
		this.personInfoState = personInfoState;
	}
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMoblie() {
		return moblie;
	}

	public void setMoblie(String moblie) {
		this.moblie = moblie;
	}
	

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isFirstInstall() {
		return firstInstall;
	}

	public void setFirstInstall(boolean firstInstall) {
		this.firstInstall = firstInstall;
	}

	public boolean isAutoLogin() {
		return autoLogin;
	}

	public void setAutoLogin(boolean autoLogin) {
		this.autoLogin = autoLogin;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public void print() {
		Log.i("userInfo","userId="+userId+" pwd="+password+" name="+name+" moblie="+moblie+"emial="+email);
	}

}
