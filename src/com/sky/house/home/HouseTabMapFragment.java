package com.sky.house.home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ConfigDefinition;
import com.eroad.base.util.ViewInit;
import com.eroad.base.util.location.Location;
import com.next.intf.ITaskListener;
import com.next.net.SHPostTaskM;
import com.next.net.SHTask;
import com.sky.house.R;
import com.sky.house.resource.HouseListFragment;
import com.sky.widget.SHDialog;
import com.sky.widget.sweetdialog.SweetDialog;

/**
 * 地图
 * 
 * @author skypan
 * 
 */
public class HouseTabMapFragment extends BaseFragment implements ITaskListener {

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LatLng currentLatLng;
	OverlayOptions ooCurrent;
//	private Marker mCenterMarker;
	private Marker mMarkerShop;

	private SHPostTaskM houseTask;
	private JSONArray jsonArray;
	
	@ViewInit(id = R.id.iv_current,onClick = "onClick")
	private ImageView mIvCurrent;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("地图找房");
		mDetailTitlebar.setLeftButton(null, null);
		mMapView = (MapView) view.findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		initOverlay(null);
		requestHouse(Location.getInstance().getLat(),Location.getInstance().getLng());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_map, container, false);
		return view;
	}

	private void requestHouse(double lat, double lng) {
		SHDialog.ShowProgressDiaolg(getActivity(), null);
		houseTask = new SHPostTaskM();
		houseTask.setListener(this);
		houseTask.setUrl(ConfigDefinition.URL + "SearchHouseByMap");
		houseTask.getTaskArgs().put("searchType", 2);
		houseTask.getTaskArgs().put("lat", lat);
		houseTask.getTaskArgs().put("lng", lng);
		houseTask.getTaskArgs().put("distance", 10);
//		houseTask.getTaskArgs().put("cityId", Location.getInstance().getCityId());
		houseTask.start();
	}
	
	private void onClick(View v){
		switch(v.getId()){
		case R.id.iv_current:
			initOverlay(null);
			requestHouse(Location.getInstance().getLat(),Location.getInstance().getLng());
			break;
		}
	}

	public void initOverlay(LatLng ll) {
		// add marker overlay
		if (ll != null) {
			currentLatLng = ll;
		} else {
			currentLatLng = new LatLng(Location.getInstance().getLat(), Location.getInstance().getLng());
		}
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(currentLatLng);
		mBaiduMap.animateMapStatus(u);//
		ooCurrent = new MarkerOptions().position(currentLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding)).zIndex(5);
//		mCenterMarker = (Marker) mBaiduMap.addOverlay(ooCurrent);//
		addOverlay();
	}

	private void addOverlay() {
		mBaiduMap.clear();//
		if (jsonArray != null && jsonArray.length() > 0) {
			// InfoWindow mInfoWindow;
			OverlayOptions ooShop;
			View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_map, null);
			TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
			for (int i = 0; i < jsonArray.length(); i++) {
				LatLng point = null;
				try {
					point = new LatLng(jsonArray.getJSONObject(i).optDouble("lat"), jsonArray.getJSONObject(i).optDouble("lng"));
					tv_count.setText(jsonArray.getJSONObject(i).optString("houseDetailCount")+"套");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ooShop = new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromView(view)).zIndex(i);
				mMarkerShop = (Marker) (mBaiduMap.addOverlay(ooShop));
			}
		}
		setListener();
	}

	private void setListener() {
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),SHContainerActivity.class);
				intent.putExtra("class", HouseListFragment.class.getName());
				intent.putExtra("from", "map");
				try {
					intent.putExtra("zoneId", jsonArray.getJSONObject(marker.getZIndex()).optInt("houseZoneId"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivity(intent);
				return true;
			}
		});

//		SHLocationManager.getInstance().setReverseGeoListener(new OnGetGeoCoderResultListener() {
//
//			@Override
//			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onGetGeoCodeResult(GeoCodeResult arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});
		
		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

			@Override
			public void onMapStatusChangeStart(MapStatus arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onMapStatusChangeFinish(MapStatus arg0) {
				// TODO Auto-generated method stub
//				mBaiduMap.clear();//
//				SHLocationManager.getInstance().reverseGeoCode(arg0.target);
//				mCenterMarker.remove();
//				ooCurrent = new MarkerOptions().position(arg0.target).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding)).zIndex(5);
//				mCenterMarker = (Marker) mBaiduMap.addOverlay(ooCurrent);//
				requestHouse(arg0.target.latitude, arg0.target.longitude);
			}

			@Override
			public void onMapStatusChange(MapStatus arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onTaskFinished(SHTask task) throws Exception {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		JSONObject json = (JSONObject) task.getResult();
		jsonArray = json.getJSONArray("houseZoneList");
		addOverlay();
	}

	@Override
	public void onTaskFailed(SHTask task) {
		// TODO Auto-generated method stub
		SHDialog.dismissProgressDiaolg();
		new SweetDialog(getActivity(), SweetDialog.ERROR_TYPE).setTitleText("提示").setContentText(task.getRespInfo().getMessage()).show();
	}

	@Override
	public void onTaskUpdateProgress(SHTask task, int count, int total) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTaskTry(SHTask task) {
		// TODO Auto-generated method stub

	}

}
