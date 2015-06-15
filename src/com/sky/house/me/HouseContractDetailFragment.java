package com.sky.house.me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.eroad.base.BaseFragment;
import com.eroad.base.SHContainerActivity;
import com.eroad.base.util.ViewInit;
import com.sky.house.R;
import com.sky.widget.SHDialog;

public class HouseContractDetailFragment extends BaseFragment implements OnClickListener{

	@ViewInit(id = R.id.btn_email, onClick = "onClick")
	private Button btnEmail;

	private WebView webView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_contract_detail,container, false);
		webView = (WebView) view.findViewById(R.id.webview);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mDetailTitlebar.setTitle("合同详情");
		SHDialog.ShowProgressDiaolg(getActivity(), null);
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
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url)
			{
				//开始
				super.onPageFinished(view, url);
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				//结束
				super.onPageStarted(view, url, favicon);
				SHDialog.dismissProgressDiaolg();

			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent  = new Intent(getActivity(),SHContainerActivity.class);
		switch (v.getId()) {
		case R.id.btn_email:
			intent.putExtra("class", HouseContractEmailFragment.class.getName());
			intent.putExtra("orderId", getActivity().getIntent().getIntExtra("orderId", -1));
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
