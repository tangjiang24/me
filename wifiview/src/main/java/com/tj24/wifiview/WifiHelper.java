package com.tj24.wifiview;

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
 * @Description: wifi连接帮助类
 * @Author: 19018090 2020/8/31 19:25
 * @Version: 1.0
 */
public class WifiHelper {
    private final String TAG = "WifiAdmin";
    //密码类型
    public static final int TYPE_NO_PWD = 1;
    public static final int TYPE_WEB = 2;
    public static final int TYPE_WPA = 3;

    public static WifiManager getWifiManager(Context context){
        return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }
    /**
     * wifi 是否打开
     * @return
     */
    public static boolean isWifiEnable(Context context) {
        return getWifiManager(context).isWifiEnabled();
    }

    /**
     * 打开WiFi
     * @return true 打开成功  false 打开失败
     */
    @SuppressLint("WrongConstant")
    public static boolean openWifi(Context context) {
        boolean sucess = true;
        if (!getWifiManager(context).isWifiEnabled()) {
            sucess =  getWifiManager(context).setWifiEnabled(true);
        }
        return sucess;
    }

    /**
     * 关闭WiFi
     */
    public static boolean closeWifi(Context context) {
        boolean sucess = true;
        if (getWifiManager(context).isWifiEnabled()) {
            sucess =  getWifiManager(context).setWifiEnabled(false);
        }
        return sucess;
    }

    /**
     * 获取当前的WiFi状态
     * @return
     */
    public int getWifiState(Context context){
        return getWifiManager(context).getWifiState();
    }

    // 检查当前WIFI状态
    private void checkState(Context context) {
        if (getWifiManager(context).getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
            Toast.makeText(context,"Wifi正在关闭", Toast.LENGTH_SHORT).show();
        } else if (getWifiManager(context).getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
            Toast.makeText(context,"Wifi已经关闭", Toast.LENGTH_SHORT).show();
        } else if (getWifiManager(context).getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            Toast.makeText(context,"Wifi正在开启", Toast.LENGTH_SHORT).show();
        } else if (getWifiManager(context).getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            Toast.makeText(context,"Wifi已经开启", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"没有获取到WiFi状态", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 扫描WiFi网络
     */
    public static boolean startScan(Context context) {
        return getWifiManager(context).startScan();
    }

    // 得到网络列表
    public static List<ScanResult> getScanResults(Context context) {
        return getWifiManager(context).getScanResults();
    }

    // 得到WifiInfo的所有信息包
    public static WifiInfo getWifiInfo(Context context) {
        return getWifiManager(context).getConnectionInfo();
    }

    /**
     *  得到WifiInfo的所有信息包 8.0必须要开启gps定位
     * @return
     */
    public static String getSSID(Context context) {
        return (getWifiInfo(context) == null) ? "" : getWifiInfo(context).getSSID();
    }

    // 添加一个网络并连接
    private static boolean addNetwork(WifiConfiguration wcg, Context context) {
        boolean ret = false;
        if(wcg==null){
            return false;
        }
        int wcgID = getWifiManager(context).addNetwork(wcg);
        if(wcgID != -1){
            ret = getWifiManager(context).enableNetwork(wcgID, true);
        }
        ret = ret && getWifiManager(context).saveConfiguration();
        return ret;
    }

    /**
     * 通过ssid pwd type连接wifi
     */
    public static boolean connectWifi(WifiItem wifiItem, Context context){
        if (!openWifi(context)) {
            return false;
        }
        long timeMills = System.currentTimeMillis();
        //避免wifi刚开启还不稳定，因此给2.5秒的开启时间
        while (getWifiManager(context).getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
            try {
                if(System.currentTimeMillis()-timeMills>2500){
                    break;
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        WifiConfiguration wifiConfiguration = isExsist(wifiItem.getSsid(),context);
        if(wifiConfiguration == null || wifiItem.getPwdType() == WifiHelper.TYPE_NO_PWD){
            wifiConfiguration = createWifiInfo(wifiItem);
        }
        return addNetwork(wifiConfiguration,context);
    }

    /**
     * 没有密码的情况下 连接
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean connectWifiWithNoPwd(WifiItem wifiItem, Context context){
        if (!this.openWifi(context)) {
            return false;
        }
        long timeMills = System.currentTimeMillis();
        //避免wifi刚开启还不稳定，因此给2.5秒的开启时间
        while (getWifiManager(context).getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
            try {
                if(System.currentTimeMillis()-timeMills>2500){
                    break;
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        WifiConfiguration config = createWifiInfo(wifiItem);
        return addNetwork(config,context);
    }

    /**
     * 忘记某一个wifi密码
     *
     * @param targetSsid
     */
    public static void removeWifiBySsid(String targetSsid, Context context) {
        List<WifiConfiguration> wifiConfigs = getWifiManager(context).getConfiguredNetworks();
        for (WifiConfiguration wifiConfig : wifiConfigs) {
            String ssid = wifiConfig.SSID;
            if (ssid.equals("\""+targetSsid+"\"")) {
                removeWifi(wifiConfig.networkId,context);
            }
        }
    }

    private static void removeWifi(int netId, Context context) {
        getWifiManager(context).disableNetwork(netId);
        getWifiManager(context).removeNetwork(netId);
        getWifiManager(context).saveConfiguration();
    }

    /**
     * 断开指定的WiFi
     * @param targetSsid
     */
    public static void disconnectWifi(String targetSsid, Context context ){
        List<WifiConfiguration> wifiConfigs = getWifiManager(context).getConfiguredNetworks();
        for (WifiConfiguration wifiConfig : wifiConfigs) {
            String ssid = wifiConfig.SSID;
            if (ssid.equals("\""+targetSsid+"\"")) {
                disconnectWifi(wifiConfig.networkId,context);
            }
        }
    }

    // 断开指定ID的网络
    private static void disconnectWifi(int netId, Context context) {
        getWifiManager(context).disableNetwork(netId);
        getWifiManager(context).disconnect();
    }

    /**
     * 创建一个wifiConfiguration
     * @return
     */
    private static WifiConfiguration createWifiInfo(WifiItem wifiItem) {
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

    /**
     * 是否存在同名的ssid网络，有则直接返回
     * @param SSID
     * @return
     */
    public static WifiConfiguration isExsist(String SSID, Context context) {
        List<WifiConfiguration> existingConfigs = getWifiManager(context).getConfiguredNetworks();
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
     * 判断wifi 是否保存
     * @param context
     * @param ssid
     * @return
     */
    public static boolean isWifiSaved(Context context,String ssid){
        List<WifiConfiguration> existingConfigs = getWifiManager(context).getConfiguredNetworks();
        if(existingConfigs != null){
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals("\""+ssid+"\"")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断当前网络是否是wifi连接
     * @return
     */
    public static boolean isWiFiConnected(Context context){
        ConnectivityManager connectManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(networkInfo.isConnected()){
            return true;
        } else{
            return false;
        }
    }

    /**
     * 指定 ssid 的网络 是否连接
     * @param ssid
     * @return
     */
    public static boolean isConnectedBySsid(String ssid, Context context) {
        boolean isConnected = false;
        ssid = "\"" + ssid + "\"";
        if(ssid.equals(getSSID(context)) && isWiFiConnected(context)){
            isConnected = true;
        }
        return isConnected;
    }

    /**
     * 获取信号强度
     * @param rssi
     * @param numLevels
     * @return
     */
    public static int getWifiLevle(int rssi, int numLevels) {
        return WifiManager.calculateSignalLevel(rssi, numLevels);
    }

    /**
     * 获取密码类型
     *
     * @param capabilities
     * @return
     */
    public static int getPwdType(String capabilities) {
        if (capabilities.contains("WEP")) {
            return TYPE_WEB;
        } else if (capabilities.contains("WPA") || capabilities.contains("WPA2") || capabilities.contains("WPS")) {
            return TYPE_WPA;
        } else {
            return TYPE_NO_PWD;
        }
    }

    /**
     * 根据ssid获取wifi对应的密码
     * @param ssid
     * @return
     */
    public static String getPwd(Context context,String ssid){
        WifiConfiguration exsist = isExsist(ssid, context);
        if(exsist != null){
            return exsist.preSharedKey;
        }
        return "";
    }
}
