package com.tj24.wifiview;
/**
 * @Description: wifi实体类
 * @Author: 19018090 2020/8/31 19:25
 * @Version: 1.0
 */
public class WifiItem {
    //名称
    private String ssid;
    //密码
    private String pwd;
    //密码类型
    private int pwdType;
    //是否连接
    private boolean isConnected;
    //信号强度
    private int lelve;
    //是否保存
    private boolean isSaved;

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
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
