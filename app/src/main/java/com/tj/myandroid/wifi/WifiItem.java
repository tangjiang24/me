package com.tj.myandroid.wifi;

public class WifiItem {
    private String ssid;
    private String pwd;
    private boolean isNoPwd;
    private int pwdType;
    private boolean isConnected;
    private int lelve;
    private boolean isInputing;

    public boolean isInputing() {
        return isInputing;
    }

    public void setInputing(boolean inputing) {
        isInputing = inputing;
    }

    public boolean isNoPwd() {
        return isNoPwd;
    }

    public void setNoPwd(boolean noPwd) {
        isNoPwd = noPwd;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getPwdType() {
        return pwdType;
    }

    public void setPwdType(int pwdType) {
        this.pwdType = pwdType;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public int getLelve() {
        return lelve;
    }

    public void setLelve(int lelve) {
        this.lelve = lelve;
    }
}
