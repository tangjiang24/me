package com.tj.myandroid.wifi;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tj.myandroid.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class WifiView extends FrameLayout implements LifeCircle , CompoundButton.OnCheckedChangeListener,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    private static final String TAG = "WifiView";
    private Context context;
    private RecyclerView rcWifi;
    private Switch sv;
    private WifiAdapter wifiAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<WifiItem> wifiItems = new ArrayList<>();
    ProgressDialog dialog;
    private WifiBroadcastReceiver wifiReceiver;
    WifiConnector wifiConnector;
    public WifiView(@NonNull Context context) {
        this(context,null);
    }

    public WifiView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WifiView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        wifiConnector = new WifiConnector(context);
        initView();
    }

    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.wifi_list_view,this);
        sv = findViewById(R.id.sv);
        rcWifi = findViewById(R.id.rc_wifi);
        sv.setOnCheckedChangeListener(this);

        dialog = new ProgressDialog(context);
        dialog.setTitle("");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);

        linearLayoutManager = new LinearLayoutManager(context);
        wifiAdapter = new WifiAdapter();
        rcWifi.setLayoutManager(linearLayoutManager);
        rcWifi.setAdapter(wifiAdapter);
        wifiAdapter.setOnItemClickListener(this);
        wifiAdapter.setOnItemChildClickListener(this);

        sv.setChecked(WifiAdmin.isWifiEnable(context));
        if(sv.isChecked()){
            getScanResult();
        }
    }

    @Override
    public void onResume() {
        //注册广播
        wifiReceiver = new WifiBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifi连接状态广播,是否连接了一个有效路由
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
        context.registerReceiver(wifiReceiver, filter);
    }

    @Override
    public void onPause() {
        context.unregisterReceiver(wifiReceiver);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            WifiAdmin.openWifi(context);
        }else {
            wifiAdapter.getData().clear();
            wifiAdapter.notifyDataSetChanged();
            WifiAdmin.closeWifi(context);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        WifiItem wifiItem = wifiItems.get(position);
        dialog.show();
        wifiConnector.connect(wifiItem, new WifiConnector.WifiConnectCallBack() {
            @Override
            public void onConnectSucess() {
                dialog.dismiss();
            }

            @Override
            public void onConnectFail(String msg) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 扫描网络并排序
     */
    private void getScanResult() {
        List<ScanResult> scanResults = WifiAdmin.getScanResults(context);
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
            wifiItem.setLelve(WifiAdmin.getWifiLevle(scanResult.level,4));
            wifiItem.setConnected(WifiAdmin.isConnectedBySsid(scanResult.SSID,context));
            wifiItem.setPwdType(WifiAdmin.getPwdType(scanResult.capabilities));
            tmpResult.put(scanResult.SSID, wifiItem);
        }
        wifiItems.addAll(tmpResult.values());
        Collections.sort(wifiItems,new WifiComparator());
        this.wifiItems = wifiItems;
        wifiAdapter.setNewData(wifiItems);
    }

    //监听wifi状态
    public class WifiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (state){
                    case WifiManager.WIFI_STATE_DISABLED:{
                        Log.d(TAG,"已经关闭");
                        Toast.makeText(context,"WIFI处于关闭状态",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case WifiManager.WIFI_STATE_DISABLING:{
                        Log.d(TAG,"正在关闭");
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLED:{
                        Log.d(TAG,"已经打开");
                        getScanResult();
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLING:{
                        Log.d(TAG,"正在打开");
                        break;
                    }
                    case WifiManager.WIFI_STATE_UNKNOWN:{
                        Log.d(TAG,"未知状态");
                        break;
                    }
                }
            }else if(WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())){
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                Log.d(TAG, "--NetworkInfo--" + info.toString());
                if(NetworkInfo.State.DISCONNECTED == info.getState()){//wifi没连接上
                    Log.d(TAG,"wifi没连接上");
                    for(int i = 0;i < wifiItems.size();i++){//没连接上将 所有的连接状态都置为“未连接”
                        wifiItems.get(i).setConnected(false);
                    }
                    wifiAdapter.notifyDataSetChanged();
                }else if(NetworkInfo.State.CONNECTED == info.getState()){//wifi连接上了
                    Log.d(TAG,"wifi连接上了");
                    notifyTopWifi();
                }else if(NetworkInfo.State.CONNECTING == info.getState()){//正在连接
                    Log.d(TAG,"wifi正在连接");
                }
            }else if(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())){
                Log.d(TAG,"网络列表变化了");
                getScanResult();
            }
        }
    }

    /**
     * 将已连网络接置顶
     */
    private void notifyTopWifi() {
        if(wifiItems.size() == 0){
            return;
        }
        String connectedSsid = WifiAdmin.getSSID(context);
        WifiItem wifiItem = null;
        for(int i = 0;i < wifiItems.size();i++){
            if(("\"" + wifiItems.get(i).getSsid() + "\"").equals(connectedSsid)){
                wifiItem = wifiItems.get(i);
                wifiItem.setConnected(true);
            }else {
                wifiItems.get(i).setConnected(false);
            }
        }
        if(wifiItem!=null){
            wifiItems.remove(wifiItem);
            wifiItems.add(0, wifiItem);
            wifiAdapter.notifyDataSetChanged();
        }
    }
}
