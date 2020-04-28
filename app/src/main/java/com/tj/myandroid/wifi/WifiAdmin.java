package com.tj.myandroid.wifi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.util.List;

/**
 * Created by energy on 2017/11/16.
 */

public class WifiAdmin {
    private final String TAG = "WifiAdmin";
    //密码类型
    public static final int TYPE_NO_PWD = 1;
    public static final int TYPE_WEB = 2;
    public static final int TYPE_WPA = 3;

    private Context context;
    // 定义WifiManager对象
    private WifiManager mWifiManager;

    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    // 网络连接列表
    private List<WifiConfiguration> mWifiConfiguration;
    // 定义一个WifiLock
    WifiManager.WifiLock mWifiLock;

    // 构造器
    public WifiAdmin(Context context) {
        this.context = context;
        // 取得WifiManager对象
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * wifi 是否打开
     * @return
     */
    public boolean isWifiEnable() {
        return mWifiManager.isWifiEnabled();
    }

    /**
     * 打开WiFi
     * @return true 打开成功  false 打开失败
     */
    @SuppressLint("WrongConstant")
    public boolean openWifi() {
        boolean sucess = true;
        if (!mWifiManager.isWifiEnabled()) {
            sucess =  mWifiManager.setWifiEnabled(true);
        }
        return sucess;
    }

    /**
     * 关闭WiFi
     */
    public boolean closeWifi() {
        boolean sucess = true;
        if (mWifiManager.isWifiEnabled()) {
            sucess =  mWifiManager.setWifiEnabled(false);
        }
        return sucess;
    }

    /**
     * 获取当前的WiFi状态
     * @return
     */
    public int getWifiState(){
        return mWifiManager.getWifiState();
    }
    // 检查当前WIFI状态
    private void checkState(Context context) {
        if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
            Toast.makeText(context,"Wifi正在关闭", Toast.LENGTH_SHORT).show();
        } else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
            Toast.makeText(context,"Wifi已经关闭", Toast.LENGTH_SHORT).show();
        } else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            Toast.makeText(context,"Wifi正在开启", Toast.LENGTH_SHORT).show();
        } else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            Toast.makeText(context,"Wifi已经开启", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"没有获取到WiFi状态", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 扫描WiFi网络
     */
    public boolean startScan() {
        return mWifiManager.startScan();
    }

    // 得到网络列表
    public List<ScanResult> getScanResults() {
        mWifiList = mWifiManager.getScanResults();
        return mWifiList;
    }

    // 得到WifiInfo的所有信息包
    public WifiInfo getWifiInfo() {
         return mWifiManager.getConnectionInfo();
    }

    /**
     *  得到WifiInfo的所有信息包 8.0必须要开启gps定位
     * @return
     */
    public String getSSID() {
        return (getWifiInfo() == null) ? "NULL" : getWifiInfo().getSSID();
    }

    // 添加一个网络并连接
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private boolean addNetwork(WifiConfiguration wcg) {
        boolean ret = false;
        if(wcg==null){
            return false;
        }
        int wcgID = mWifiManager.addNetwork(wcg);
        if(wcgID != -1){
            ret = mWifiManager.enableNetwork(wcgID, true);
        }
        ret = ret && mWifiManager.saveConfiguration();
        return ret;
    }

    /**
     * 通过ssid pwd type连接wifi
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean connectWifi(WifiItem wifiItem){
        if (!this.openWifi()) {
            return false;
        }
        long timeMills = System.currentTimeMillis();
        //避免wifi刚开启还不稳定，因此给2.5秒的开启时间
        while (mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
            try {
                if(System.currentTimeMillis()-timeMills>2500){
                    break;
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        WifiConfiguration wifiConfiguration = createWifiInfo(wifiItem);
        return addNetwork(wifiConfiguration);
    }

    /**
     * 没有密码的情况下 连接
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean connectWifiWithNoPwd(String ssId){
        if (!this.openWifi()) {
            return false;
        }
        long timeMills = System.currentTimeMillis();
        //避免wifi刚开启还不稳定，因此给2.5秒的开启时间
        while (mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
            try {
                if(System.currentTimeMillis()-timeMills>2500){
                    break;
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"" + ssId + "\"";
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        return addNetwork(config);
    }

    /**
     * 忘记某一个wifi密码
     *
     * @param targetSsid
     */
    public  void removeWifiBySsid( String targetSsid) {
        List<WifiConfiguration> wifiConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration wifiConfig : wifiConfigs) {
            String ssid = wifiConfig.SSID;
            if (ssid.equals("\""+targetSsid+"\"")) {
                removeWifi(wifiConfig.networkId);
            }
        }
    }

    private void removeWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.removeNetwork(netId);
        mWifiManager.saveConfiguration();
    }

    /**
     * 断开指定的WiFi
     * @param targetSsid
     */
    public void disconnectWifi(String targetSsid ){
        List<WifiConfiguration> wifiConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration wifiConfig : wifiConfigs) {
            String ssid = wifiConfig.SSID;
            if (ssid.equals("\""+targetSsid+"\"")) {
                disconnectWifi(wifiConfig.networkId);
            }
        }
    }
    // 断开指定ID的网络
    private void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }


    /**
     * 创建一个wifiConfiguration
     * @return
     */
    private WifiConfiguration createWifiInfo(WifiItem wifiItem) {
        String SSID = wifiItem.getSsid();
        String Password = wifiItem.getPwd();
        int Type = wifiItem.getPwdType();

        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.isExsits(SSID);
//        List<WifiConfiguration> beforeConfig = mWifiManager.getConfiguredNetworks();
//        if(tempConfig!=null) {
//            removeWifi(tempConfig.networkId);
//        }
//        List<WifiConfiguration> afterConfig = mWifiManager.getConfiguredNetworks();
        if( tempConfig!=null){
            return tempConfig;
        }else {
            if(Type == TYPE_NO_PWD) {  //WIFICIPHER_NOPASS
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            }
            if(Type == TYPE_WEB){ //WIFICIPHER_WEP
                config.hiddenSSID = true;
                config.wepKeys[0]= "\""+Password+"\"";
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
            }
            if(Type == TYPE_WPA){ //WIFICIPHER_WPA
                config.preSharedKey = "\""+Password+"\"";
                config.hiddenSSID = true;
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                config.status = WifiConfiguration.Status.ENABLED;
            }
            return config;
        }
    }

    /**
     * 是否存在同名的ssid网络，有则直接返回
     * @param SSID
     * @return
     */
    public WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        if(existingConfigs!=null ){
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals("\""+SSID+"\"")) {
                    return existingConfig;
                }
            }
        }
        return null;
    }

    /**
     * 判断当前网络是否是wifi连接
     * @return
     */
    public boolean isWiFiConnected(){
        ConnectivityManager connectManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(networkInfo.isConnected()){
            return true;
        } else{
            return false;
        }
    }

    /**
     * 获取信号强度
     * @param rssi
     * @param numLevels
     * @return
     */
    public int getWifiLevle(int rssi, int numLevels) {
       return WifiManager.calculateSignalLevel(rssi, numLevels);
    }

}
