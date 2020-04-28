package com.tj.myandroid.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by energy on 2018/11/9.
 */

public class WifiReceiver extends BroadcastReceiver {
    private final String TAG = "WifiConnectReceiver:";
    boolean isFirst = true;
    private WifiReceiverListner mListner;

    public WifiReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals( WifiManager.NETWORK_STATE_CHANGED_ACTION)){
            if(isFirst){
                isFirst = !isFirst;
                return;
            }
            doWifiStateChanged(context,intent);
        }else if(action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)){
            NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf((SupplicantState)intent.getParcelableExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED));
            int code = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
            if(code == WifiManager.ERROR_AUTHENTICATING){
               if(mListner != null){
                   mListner.connectError("密码错误");
               }
            }
        }else if(action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
            if(mListner != null){
                mListner.onScanResult( );
            }
        }
    }

    private void doWifiStateChanged(Context context, Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        Log.i(TAG, "接受到网络连接变化的广播，当前网络状态为："+info.getState());
        if(info.getState().equals(NetworkInfo.State.CONNECTED)){
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();  //连接到的网络的ssid
            Log.i(TAG, "接受到网络连接变化的广播，已连接的网络的ssid="+ssid);
            if(mListner!=null){
                mListner.connectedWifi(ssid);
            }
        }
    }

    public interface WifiReceiverListner {
        public void connectedWifi(String ssid);
        public void connectError(String error);
        public void onScanResult();
    }
    public void setOnWifiConnetListner(WifiReceiverListner listner){
        this.mListner = listner;
    }
}
