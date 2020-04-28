package com.tj.myandroid.wifi;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by energy on 2018/11/29.
 */

public class WifiHelper implements WifiReceiver.WifiReceiverListner {
    private final String TAG = "WifiConnector";
    public static final String WIFI_AUTH_OPEN = "";
    public static final String WIFI_AUTH_ROAM = "[ESS]";
    private WifiItem wifiItem;
    private Context context;
    private WifiReceiver wifiConnectReceiver;
    private WifiAdmin mWifiAdmin;
    private WifiConnectCallBack connectCallBack;
    private WifiScanCallBack wifiScanCallBack;
    private boolean mReceiverTag = false;   //广播接受者标识
    public static final int CONNECT_WIFI_SUCESS = 1;
    public static final int CONNECT_WIFI_FAIL = 2;
    private boolean isFirstFail = true;//是否是第一次连接失败  某些条件下需要重连

    public WifiHelper(Context context) {
        this.context = context;
        registWifiConnectReceiver();
        mWifiAdmin = new WifiAdmin(context);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==CONNECT_WIFI_SUCESS){
                Log.i(TAG,"连接成功！");
                if(connectCallBack!=null){
                    connectCallBack.onConnectSucess();
                }
            }else if(msg.what==CONNECT_WIFI_FAIL){
                handler.removeMessages(CONNECT_WIFI_FAIL);
                Log.i(TAG,"连接失败！原因："+msg.obj);
                String failMsg = (String) msg.obj;
                if(connectCallBack!=null){
                    connectCallBack.onConnectFail(failMsg);
                }
            }
        }
    };

    @Override
    public void connectedWifi(String s) {
        if(wifiScanCallBack != null){
            wifiScanCallBack.onWifiConnected(s);
        }
        if (wifiItem!=null &&!TextUtils.isEmpty(wifiItem.getSsid())) {
            if (s.equals("\"" + wifiItem.getSsid() + "\"")) {
                handler.removeMessages(CONNECT_WIFI_SUCESS);
                handler.removeMessages(CONNECT_WIFI_FAIL);
                handler.sendEmptyMessageDelayed(CONNECT_WIFI_SUCESS,1000);
            } else {
                Log.i(TAG,"未连接到指定网络！");
//                sendFailMessage("未连接到指定网络！",1);
            }
        }
    }

    @Override
    public void connectError(String error) {
        sendFailMessage(error,0);
    }

    @Override
    public void onScanResult() {
        if(wifiScanCallBack != null){
            List<ScanResult> scanResults = mWifiAdmin.getScanResults();
            wifiScanCallBack.onScanWifi(filterResult(scanResults));
        }
    }

    public void connectWifi(WifiItem wifiItem, WifiConnectCallBack callBack){
        handler.removeMessages(CONNECT_WIFI_SUCESS);
        handler.removeMessages(CONNECT_WIFI_FAIL);

        this.wifiItem = wifiItem;
        this.connectCallBack = callBack;

        Log.i(TAG,"是否已经是WiFi连接："+ mWifiAdmin.isWiFiConnected()+"    已连接的ssid:"+ mWifiAdmin.getSSID());
        if(mWifiAdmin.isWiFiConnected() && ("\"" + wifiItem.getSsid() + "\"").equals(mWifiAdmin.getSSID())){
            handler.sendEmptyMessage(CONNECT_WIFI_SUCESS);
            return;
        }
        startConnect();
    }

    private void startConnect() {
        boolean isConnected = mWifiAdmin.connectWifi(wifiItem);
        sendFailMessage("连接超时！",15000);
        if(!isConnected) {
            sendFailMessage("连接失败");
        }
    }

    private void registWifiConnectReceiver() {
        if (!mReceiverTag) {
            wifiConnectReceiver = new WifiReceiver();
            mReceiverTag = true;    //标识值 赋值为 true 表示广播已被注册
            wifiConnectReceiver.setOnWifiConnetListner(this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            context.registerReceiver(wifiConnectReceiver, intentFilter);
        }
    }

    public void unRegistWifiReceiver(){
        if (mReceiverTag) {   //判断广播是否注册
            mReceiverTag = false;   //Tag值 赋值为false 表示该广播已被注销
            context.unregisterReceiver(wifiConnectReceiver);   //注销广播
        }
    }

    private void sendFailMessage(String failMsg){
        if(isFirstFail){
            isFirstFail = !isFirstFail;
            Log.i(TAG,"第一次连接失败，开始第二次连接");
            connectWifi(wifiItem, connectCallBack);
            return;
        }

        Message msg = handler.obtainMessage();
        msg.obj = failMsg;
        msg.what = CONNECT_WIFI_FAIL;
        handler.sendMessageDelayed(msg,100);
    }

    private void sendFailMessage(String failMsg, long delay){
        Message msg = handler.obtainMessage();
        msg.obj = failMsg;
        msg.what = CONNECT_WIFI_FAIL;
        handler.sendMessageDelayed(msg,delay);
    }

    private List<WifiItem> filterResult(List<ScanResult> scanResults) {
        List<WifiItem> wifiItems = new ArrayList<>();
        //去重开始
        LinkedHashMap<String, WifiItem> tmpResult = new LinkedHashMap<>();
        for (ScanResult scanResult : scanResults) {
            //去掉空白的
            if ("".equals(scanResult.SSID)) {
                continue;
            }
            //重名的选信号强的显示
            if (tmpResult.containsKey(scanResult.SSID)) {
                if (tmpResult.get(scanResult.SSID).getLelve() > scanResult.level) {
                    continue;
                }
            }

            WifiItem wifiItem = new WifiItem();
            wifiItem.setSsid(scanResult.SSID);
            wifiItem.setLelve(mWifiAdmin.getWifiLevle(scanResult.level,4));
            wifiItem.setConnected(isItemConnected(scanResult));
            if (scanResult.capabilities != null && (scanResult.capabilities.equals(WIFI_AUTH_OPEN) || scanResult.capabilities.equals(WIFI_AUTH_ROAM))) {
                wifiItem.setNoPwd(true);
            }

            tmpResult.put(scanResult.SSID, wifiItem);
        }
        wifiItems.addAll(tmpResult.values());
        Collections.sort(wifiItems,new WifiComparator());
        return wifiItems;
    }

    private boolean isItemConnected(ScanResult item) {
        boolean isConnected = false;
        if(mWifiAdmin.getWifiInfo()!=null){
            String connectSSid = mWifiAdmin.getWifiInfo().getSSID();
            String ssid = "\"" + item.SSID + "\"";
            if(ssid.equals(connectSSid) && mWifiAdmin.isWiFiConnected()){
                isConnected = true;
            }
        }
        return isConnected;
    }

    public interface WifiConnectCallBack {
        public void onConnectSucess();
        public void onConnectFail(String msg);
    }

    public void setWifiScanCallBack(WifiScanCallBack wifiScanCallBack){
        this.wifiScanCallBack = wifiScanCallBack;
    }
    public interface WifiScanCallBack {
        public void onScanWifi(List<WifiItem> scanResults);
        public void onWifiConnected(String ssId);
    }
}
