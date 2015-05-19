package com.eroad.base.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CityUtil {
    
    
    public static final JSONArray getBrandList(JSONArray array, String filterFlg, boolean ifFilter) throws JSONException {
    	JSONArray newArray = new JSONArray();
    	for(int i=0;i<array.length();i++){
    		JSONObject json = array.optJSONObject(i);
    		JSONArray tempArray = json.optJSONArray("letterCities");
    		if(tempArray == null){
    			continue;
    		}
    		for(int j = 0;j<tempArray.length();j++){
    			if(ifFilter){
    				if(tempArray.optJSONObject(j).getString("cityName").contains(filterFlg)){
    					newArray.put(tempArray.optJSONObject(j));
    				}
    			}else{
    				if(j==0){
        				JSONObject tempJson = new JSONObject();
        				tempJson.put("sort", json.optString("firstLetter"));
        				tempJson.put("carcategoryname", "-1");
        				newArray.put(tempJson);
        			}
        			newArray.put(tempArray.optJSONObject(j).put("sort", json.optString("firstLetter")));
    			}
    		}
   
    	}
    	return newArray;
    }
}
