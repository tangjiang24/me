package com.tj.myandroid.timmer;

public class TimmerInfo {
    private String id;
    private  int totalTime;
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public TimmerInfo(String id, int totalTime, int state) {
        this.id = id;
        this.totalTime = totalTime;
        this.state = state;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }
}
