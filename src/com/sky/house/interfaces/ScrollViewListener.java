package com.sky.house.interfaces;

import com.sky.house.widget.ObservableScrollView;

public interface ScrollViewListener {  
	  
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);  
  
}  
