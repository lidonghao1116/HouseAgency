package com.sky.house.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.sky.house.interfaces.ScrollViewListener;

public class ObservableScrollView extends ScrollView {
	private ScrollViewListener scrollViewListener = null;  
	  
    public ObservableScrollView(Context context) {  
        super(context);  
    }  
  
    public ObservableScrollView(Context context, AttributeSet attrs,  
            int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
    public ObservableScrollView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public void setOnScrollListener(ScrollViewListener scrollViewListener) {  
        this.scrollViewListener = scrollViewListener;  
    }  
  
    @Override  
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {  
        super.onScrollChanged(x, y, oldx, oldy);  
        if (scrollViewListener != null) {  
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);  
        }  
    }

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		int eventAction = event.getAction();
//	    int y = (int) getY();handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 5);
//        
//        break;
//	    switch (eventAction) {
//        case MotionEvent.ACTION_MOVE:
//        	scrollViewListener.onScrollChanged(this, 0, y, 0, 0);  
//        	break;
//	    }
//		return super.onTouchEvent(event);
//	}  
    
}
