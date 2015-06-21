package com.sky.house.entity;

public class LeftTime {
	 //小时
    private int hours;
     
    //分钟
    private int min;
     
    //秒
    private int second;
     
    public LeftTime(int hours,int min,int second) {
        this.hours = hours;
        this.min = min;
        this.second = second;
    }
 
    public int getHours() {
        return hours;
    }
 
    public void setHours(int hours) {
        this.hours = hours;
    }
 
    public int getMin() {
        return min;
    }
 
    public void setMin(int min) {
        this.min = min;
    }
    
    public int getSecond() {
        return second;
    }
 
    public void setSecond(int second) {
        this.second = second;
    }
}
