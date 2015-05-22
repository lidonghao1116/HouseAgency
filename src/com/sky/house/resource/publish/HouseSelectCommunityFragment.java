package com.sky.house.resource.publish;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.eroad.base.BaseFragment;
import com.eroad.base.util.ViewInit;
import com.eroad.base.util.location.Location;
import com.sky.house.R;
import com.sky.house.adapter.AddressAdapter;
import com.sky.widget.SHEditText;

/**
 * 选择小区
 * 
 * @author skypan
 * 
 */
public class HouseSelectCommunityFragment extends BaseFragment implements OnGetSuggestionResultListener ,OnGetPoiSearchResultListener{

	@ViewInit(id = R.id.et_content)
	private SHEditText mEtContent;

	@ViewInit(id = R.id.lv_address)
	private ListView mLvAddress;

	private AddressAdapter adapter;
	private SuggestionSearch mSuggestionSearch = null;
	private PoiSearch mPoiSearch = null;

	private List<SuggestionResult.SuggestionInfo> list = new ArrayList<SuggestionResult.SuggestionInfo>();
	
//	private List<PoiInfo> list = new ArrayList<PoiInfo>();

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mDetailTitlebar.setTitle("选择小区");
		adapter = new AddressAdapter(getActivity());
		adapter.setList(list);

		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);

		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		// OnGetPoiSearchResultListener poiListener = new
		// OnGetPoiSearchResultListener(){
		// public void onGetPoiResult(PoiResult result){
		// //获取POI检索结果
		// if(result != null && result.getAllPoi() != null){
		// for(PoiInfo p:result.getAllPoi()){
		// if(p.type == PoiInfo.POITYPE.POINT){
		// p.
		// list.add(p);
		// }
		// }
		// adapter.setList(result.getAllPoi());
		// adapter.notifyDataSetChanged();
		// }
		// }
		// public void onGetPoiDetailResult(PoiDetailResult result){
		// //获取Place详情页检索结果
		// }
		// };
		mEtContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String text = mEtContent.getText().toString().trim();
				if (text != null && !"".equals(text)) {
					/**
					 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
					 */
					mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(text).city(Location.getInstance().getCity()));
//					mPoiSearch.searchInCity((new PoiCitySearchOption())
//							.city(Location.getInstance().getCity())
//							.keyword(text)
//							.pageNum(1));
				}
			}
		});
		adapter = new AddressAdapter(getActivity());
		adapter.setList(list);
		mLvAddress.setAdapter(adapter);
		mLvAddress.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("city", list.get(position).city);
				intent.putExtra("district", list.get(position).district);
				intent.putExtra("houseZoneName", list.get(position).key);
				getActivity().setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_select_xiaoqu, container, false);
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mSuggestionSearch.destroy();
		mPoiSearch.destroy();
	}

	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		// TODO Auto-generated method stub
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		list.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			if (info.key != null)
				System.out.println(info.city + "," + info.district + "," + info.key);
			list.add(info);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		// TODO Auto-generated method stub

		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(getActivity(), "未找到结果", Toast.LENGTH_LONG)
			.show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			list.clear();
			for(PoiInfo p:result.getAllPoi()){
//				if(p.type == PoiInfo.POITYPE.POINT){
					System.out.println(p.address+","+p.city+","+p.name);
//					list.add(p);
//				}
			}
			adapter.notifyDataSetChanged();
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
			String strInfo = "在";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "找到结果";
			Toast.makeText(getActivity(), strInfo, Toast.LENGTH_LONG)
					.show();
		}
	
	}
}
