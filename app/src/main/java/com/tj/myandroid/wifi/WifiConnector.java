package com.tj.myandroid.wifi;

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

public class WifiConnector {
    private static final String TAG = "WifiConnector";
    private WifiItem wifiItem;
    private Context context;
    private WifiConnectReceiver wifiConnectReceiver;
    private WifiConnectCallBack callBack;
    private boolean mReceiverTag = false;   //广播接受者标识
    public static final int CONNECT_WIFI_SUCESS = 1;
    public static final int CONNECT_WIFI_FAIL = 2;
    private boolean isFirstFail = true;//是否是第一次连接失败  某些条件下需要重连

   private Handler handler = new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           unRegistWifiReceiver();
           if(msg.what==CONNECT_WIFI_SUCESS){
               Log.i(TAG,"连接成功！");
               if(callBack!=null){
                   callBack.onConnectSucess();
               }
           }else if(msg.what==CONNECT_WIFI_FAIL){
               Log.i(TAG,"连接失败！原因："+msg.obj);
               String failMsg = (String) msg.obj;
               if(callBack!=null){
                   callBack.onConnectFail(failMsg);
               }
               handler.removeMessages(CONNECT_WIFI_FAIL);
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
        if(WifiAdmin.isConnectedBySsid(wifiItem.getSsid(),context)){
            handler.sendEmptyMessage(CONNECT_WIFI_SUCESS);
            return;
        }
        sendFailMessage("连接超时！",15000);
        boolean isConnected = WifiAdmin.connectWifi(wifiItem,context);
        if(!isConnected) {
            sendFailMessage("连接失败！");
        }
    }

    private void sendFailMessage(String failMsg){
        if(isFirstFail){
            isFirstFail = !isFirstFail;
            Log.i(TAG,"第一次连接失败，开始第二次连接");
            connect(wifiItem,callBack);
            return;
        }

        Message msg = handler.obtainMessage();
        msg.obj = failMsg;
        msg.what = CONNECT_WIFI_FAIL;
        handler.sendMessageDelayed(msg,100);
    }

    private void sendFailMessage(String failMsg,long delay){
        Message msg = handler.obtainMessage();
        msg.obj = failMsg;
        msg.what = CONNECT_WIFI_FAIL;
        handler.sendMessageDelayed(msg,delay);
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
            context.unregisterReceiver(wifiConnectReceiver);   //注销广播
        }
    }

    private void doWifiStateChanged(Context context,Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        Log.i(TAG, "接受到网络连接变化的广播，当前网络状态为："+info.getState());
        if(info.getState().equals(NetworkInfo.State.CONNECTED)){
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();  //连接到的网络的ssid
            Log.i(TAG, "接受到网络连接变化的广播，已连接的网络的ssid="+ssid);

            if (!TextUtils.isEmpty(ssid)) {
                if (wifiItem.getSsid().equals("\"" + ssid + "\"")) {
                    handler.removeMessages(CONNECT_WIFI_SUCESS);
                    handler.removeMessages(CONNECT_WIFI_FAIL);
                    handler.sendEmptyMessageDelayed(CONNECT_WIFI_SUCESS,1000);
                } else {
                    Log.i(TAG,"未连接到指定网络！");
                    sendFailMessage("未连接到指定网络！",1);
                }
            }
        }
    }

    class WifiConnectReceiver extends BroadcastReceiver{
        boolean isFirst = true;
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
                    sendFailMessage("密码错误",0);
                }
            }
        }
    }

    public interface WifiConnectCallBack{
        public void onConnectSucess();
        public void onConnectFail(String msg);
    }
}
