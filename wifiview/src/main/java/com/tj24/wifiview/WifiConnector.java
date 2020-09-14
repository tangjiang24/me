package com.tj24.wifiview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

/**
 * @Description: wifi连接器
 * @Author: 19018090 2020/8/04 18:22
 * @Version: 1.0
 */
public class WifiConnector {
    private static final String TAG = "WifiConnector";
    private WifiItem wifiItem;
    private Context context;
    private WifiConnectReceiver wifiConnectReceiver;
    private WifiConnectCallBack callBack;
    //广播接受者标识
    private boolean mReceiverTag = false;
    //连接成功
    public static final int CONNECT_WIFI_SUCESS = 1;
    //连接失败
    public static final int CONNECT_WIFI_FAIL = 2;
    //连接超时
    public static final int CONNECT_TIME_OUT = 3;
    //错误连接次数
    private int reConnectNum = 0;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            unRegistWifiReceiver();
            if(msg.what==CONNECT_WIFI_SUCESS){
                WifiLog.i(TAG,"连接成功！");
                handler.removeMessages(CONNECT_WIFI_FAIL);
                handler.removeMessages(CONNECT_TIME_OUT);
                reConnectNum = 0;
                if(callBack!=null){
                    callBack.onConnectSucess();
                }
            }else if(msg.what==CONNECT_WIFI_FAIL){
                reConnectNum++;
                WifiLog.i(TAG,"失败次数："+reConnectNum+"------失败原因："+msg.obj);
                String failMsg = (String) msg.obj;
                if(reConnectNum <= 2){
                    handler.removeMessages(CONNECT_WIFI_FAIL);
                    connect(wifiItem,callBack);
                }else if(callBack!=null) {
                    handler.removeMessages(CONNECT_WIFI_FAIL);
                    handler.removeMessages(CONNECT_TIME_OUT);
                    reConnectNum = 0;
                    callBack.onConnectFail(failMsg);
                }
            } else if(msg.what == CONNECT_TIME_OUT){
                WifiLog.i(TAG,"连接超时！");
                handler.removeMessages(CONNECT_WIFI_FAIL);
                reConnectNum = 0;
                if(callBack != null){
                    callBack.onConnectFail((String) msg.obj);
                }
            }
        }
    };


    public WifiConnector(Context context) {
        this.context = context;
    }

    public void connect(WifiItem wifiItem,WifiConnectCallBack callBack){
        this.wifiItem = wifiItem;
        this.callBack = callBack;
        registWifiConnectReceiver();
        if(reConnectNum == 0){
            sendTimeOutMessage(context.getString(R.string.wifiview_connect_timeout));
        }
        if(WifiHelper.isConnectedBySsid(wifiItem.getSsid(),context)){
            handler.sendEmptyMessage(CONNECT_WIFI_SUCESS);
            return;
        }

        boolean isConnected = WifiHelper.connectWifi(wifiItem,context);
        if(!isConnected) {
            sendFailMessage(context.getString(R.string.wifiview_connect_fail));
        }
    }

    private void sendFailMessage(String failMsg){
        Message msg = handler.obtainMessage();
        msg.obj = failMsg;
        msg.what = CONNECT_WIFI_FAIL;
        handler.sendMessage(msg);
    }

    private void sendTimeOutMessage(String failMsg){
        Message msg = handler.obtainMessage();
        msg.obj = failMsg;
        msg.what = CONNECT_TIME_OUT;
        handler.sendMessageDelayed(msg,24000);
    }


    private void registWifiConnectReceiver() {
        if (!mReceiverTag) {
            wifiConnectReceiver = new WifiConnectReceiver();
            mReceiverTag = true;    //标识值 赋值为 true 表示广播已被注册
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
            context.registerReceiver(wifiConnectReceiver, intentFilter);
        }
    }

    private void unRegistWifiReceiver(){
        if (mReceiverTag) {   //判断广播是否注册
            mReceiverTag = false;   //Tag值 赋值为false 表示该广播已被注销
            try {
                context.unregisterReceiver(wifiConnectReceiver);   //注销广播 增加异常判断
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doWifiStateChanged(Context context, NetworkInfo info) {
        if(!("\""+wifiItem.getSsid()+"\"").equals(info.getExtraInfo())){
            Log.e(TAG,"wifiItemssid"+wifiItem.getSsid()+"____info:"+info.getExtraInfo());
            return;
        }
        if(info.getState().equals(NetworkInfo.State.CONNECTED)){
            WifiInfo wifiInfo = WifiHelper.getWifiInfo(context);
            String ssid = wifiInfo.getSSID();  //连接到的网络的ssid
            WifiLog.i(TAG, "已连接的网络的ssid="+ssid);
            if (!TextUtils.isEmpty(ssid)) {
                if (("\""+wifiItem.getSsid()+"\"").equals(ssid)) {
                    handler.removeMessages(CONNECT_TIME_OUT);
                    handler.removeMessages(CONNECT_WIFI_FAIL);
                    handler.sendEmptyMessageDelayed(CONNECT_WIFI_SUCESS,100);
                } else {
                    WifiLog.i(TAG,"未连接到指定网络！");
                    sendFailMessage(context.getString(R.string.wifiview_not_connected_appoint_net));
                }
            }
        }
    }

    class WifiConnectReceiver extends BroadcastReceiver {
        boolean isFirst = true;
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(action.equals( WifiManager.NETWORK_STATE_CHANGED_ACTION)){
                WifiLog.i(TAG, "接受到网络连接变化的广播，当前网络状态为："+info.getState()+"----info:"+info.getExtraInfo());
                //第一次会返回上次连接的 已连接状态，因此需要过滤掉。
                if(isFirst){
                    Log.e(TAG,"first:"+info.getExtraInfo());
                    isFirst = !isFirst;
                    return;
                }
                doWifiStateChanged(context,info);
                Log.e(TAG,"not first:"+info.getExtraInfo());
            }else if(action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)){
                WifiLog.i(TAG, "接受到 SUPPLICANT_ERROR 广播");
                if(info == null){
                    Log.e(TAG,"SUPPLICANT_ERROR: info == null");
                }else {
                    Log.e(TAG,"SUPPLICANT_ERROR: info ="+info.getExtraInfo());
                }
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf((SupplicantState)intent.getParcelableExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED));
                int code = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                if(code == WifiManager.ERROR_AUTHENTICATING){
                    sendFailMessage(context.getString(R.string.wifiview_pwd_error));
                }
            }
        }
    }

    public interface WifiConnectCallBack{
        public void onConnectSucess();
        public void onConnectFail(String msg);
    }
}
