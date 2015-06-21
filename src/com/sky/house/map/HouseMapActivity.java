package com.sky.house.map;

import android.os.Bundle;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.eroad.base.BaseActivity;
import com.eroad.base.DetailTitlebar;
import com.sky.house.R;

public class HouseMapActivity extends BaseActivity {

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Marker mMarkerB;
	private InfoWindow mInfoWindow;
	private double lng, lat;
	BitmapDescriptor bdB = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
	private DetailTitlebar titleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showmap);
		titleBar = (DetailTitlebar) findViewById(R.id.detailTitlebar);
		titleBar.setTitle("地图");
		mMapView = (MapView) findViewById(R.id.bmap_view);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		lng = getIntent().getDoubleExtra("Lng", 116.369199);// 经度
		lat = getIntent().getDoubleExtra("Lat", 39.942821);// 纬度
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(lat, lng));
		mBaiduMap.animateMapStatus(u);

		initOverlay();
	}

	public void initOverlay() {
		// add marker overlay
		LatLng llB = new LatLng(lat, lng);
		OverlayOptions ooB = new MarkerOptions().position(llB).icon(bdB).zIndex(5);
		mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));
		Button button = new Button(getApplicationContext());
		button.setText(getIntent().getStringExtra("name"));
		button.setTextColor(getResources().getColor(R.color.color_white));
		button.setPadding(20, 0, 20, 0);
		button.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_orange_round));
		mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), mMarkerB.getPosition(), -120, null);
		mBaiduMap.showInfoWindow(mInfoWindow);
	}

	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
		// 回收 bitmap 资源
		bdB.recycle();
	}
}
