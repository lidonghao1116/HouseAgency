package com.sky.house.resource;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eroad.base.BaseFragment;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;
/**
 * H5  合同
 * @author skypan
 *
 */
public class HTMLFragment extends BaseFragment {

	@ViewInit(id = R.id.web_html)
	private WebView webView;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		if(getActivity().getIntent().getStringExtra("title") != null){
			mDetailTitlebar.setTitle(getActivity().getIntent().getStringExtra("title"));
		}else{
			mDetailTitlebar.setTitle("合同");
		}
		webView.getSettings().setJavaScriptEnabled(true);//支持js脚本
		webView.getSettings().setLoadsImagesAutomatically(true);
		// 自适应屏幕
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		// 缩放
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
		webView.loadUrl(getActivity().getIntent().getStringExtra("url"));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_html, container, false);
		return view;
	}

}
