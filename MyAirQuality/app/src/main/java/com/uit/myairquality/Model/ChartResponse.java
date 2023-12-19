package com.uit.myairquality.Model;

public class ChartResponse {
    private long x;
    private float y;
    public ChartResponse(long x, float y){
        this.x = x;
        this.y = y;
    }
    public long getX() {
        return x;
    }
    public float getY() {
        return y;
    }
}
