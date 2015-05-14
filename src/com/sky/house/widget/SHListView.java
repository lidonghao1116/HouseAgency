package com.sky.house.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sky.house.R;



/**
 * 
 * @author skypan 2014-12-31 12:56 V1.0.0
 */
public class SHListView extends ListView implements OnScrollListener {

	private String TAG = "SHListView";
	private LayoutInflater mInflater;
	private View footView,newFootView;
	private TextView tvNoDataTips;
	
	private LinearLayout mFootLayout;
	private OnLoadMoreListener mOnLoadMoreListener;
	private int totalNum = -99;
	private int lastVisibleItem;
	private boolean isLoading = true;
	private boolean canLoad = true;

	public String tipsMessage;
	public SHListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public SHListView(Context context, AttributeSet pAttrs) {
		super(context, pAttrs);
		init(context);
	}

	public SHListView(Context context, AttributeSet pAttrs, int pDefStyle) {
		super(context, pAttrs, pDefStyle);
		init(context);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
		if(totalNum == getCount() - 1){
			mFootLayout.setVisibility(View.INVISIBLE);
			canLoad = false;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
			if(getLastVisiblePosition() == totalNum){
				mFootLayout.setVisibility(View.INVISIBLE);
			}else{
				if(getLastVisiblePosition() == getCount() - 1  &&  canLoad){
					mFootLayout.setVisibility(View.VISIBLE);
					if(mOnLoadMoreListener != null){
						if(!isLoading){
							mOnLoadMoreListener.onLoadMore();
							isLoading = true;
						}
					}else{
						try {
							throw new Exception("OnLoadMoreListener is Null");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			
		}
	}

	private void init(Context mContext) {
		setCacheColorHint(mContext.getResources().getColor(R.color.full_transparent));
		mInflater = LayoutInflater.from(mContext);
		addFootView();
		setOnScrollListener(this);
	}
	
	private void addFootView(){
		footView = (LinearLayout) mInflater.inflate(R.layout.lv_load_more, null);
		mFootLayout = (LinearLayout) footView.findViewById(R.id.ll_footlayout);
		addFooterView(footView,null,false);
	}
	
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
		this.isLoading = false;
		if(totalNum == 0){
			canLoad = false;
			removeFooterView(footView);
			if(newFootView != null){
				removeFooterView(newFootView);
			}else{
				newFootView = (LinearLayout) mInflater.inflate(R.layout.lv_no_data, null);
				tvNoDataTips = (TextView) newFootView.findViewById(R.id.tv_nodata_tips);
				if(tipsMessage!=null && !tipsMessage.isEmpty()){
					tvNoDataTips.setText(tipsMessage);
				}
			}
			addFooterView(newFootView, null, false);
		}else{
			canLoad = true;
			if(newFootView != null){
				removeFooterView(newFootView);
			}
			removeFooterView(footView);
			addFooterView(footView, null, false);
		}
	}
	public void setTipsMessage(String message){
		this.tipsMessage = message;
	}
	
	public interface OnLoadMoreListener {
		public void onLoadMore();
	}
	
	public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
		this.mOnLoadMoreListener = mOnLoadMoreListener;
	}
}
