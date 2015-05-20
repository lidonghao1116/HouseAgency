package com.eroad.base.util.location;

import java.io.Serializable;

/**
 * 
 * @author skypan
 * 
 */
public class Location implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Location instance;

	public static Location getInstance() {
		if (instance == null) {
			instance = new Location();
		}
		return instance;
	}

	private int cityId;
	
	private int selectedCityId;
	
	public int getSelectedCityId() {
		return selectedCityId;
	}

	public void setSelectedCityId(int selectedCityId) {
		this.selectedCityId = selectedCityId;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
		this.selectedCityId = cityId;
	}

	/**
	 * 详细地址
	 */
	private String address;

	/**
	 * 城市
	 */
	private String city;
	/**
	 * 城市编码
	 */
	private String cityCode;
	/**
	 * 省份
	 */
	private String pro;

	/**
	 * 县、区
	 */
	private String block;
	/**
	 * 经度
	 */
	private double lng;
	/**
	 * 纬度
	 */
	private double lat;
	
	private String selectedArea;
	
	private String selectedCode;

	public String getSelectedArea() {
		return selectedArea;
	}

	public String getSelectedCode() {
		return selectedCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
		this.selectedArea = city;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public String getPro() {
		return pro;
	}

	public void setPro(String pro) {
		this.pro = pro;
	}

	public String getBlock() {
		return block == null?getCity():block;//区县为空则返回城市
	}

	public void setBlock(String block) {
		this.block = block;
		this.selectedArea = block;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
		this.selectedCode = cityCode;
	}
//
//	@Override
//	public String toString() {
//		return "Location [lng=" + lng + ", lat=" + lat + "]";
//	}

	@Override
	public String toString() {
		return "Location [city=" + city + ", cityCode=" + cityCode + ", block="
				+ block + "]";
	}

}
