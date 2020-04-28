package com.tj.myandroid.recyclertest;

public class AppBean {
    private String name;
    private String describe;

    public AppBean() {
    }

    public AppBean(String name, String describe) {
        this.name = name;
        this.describe = describe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
