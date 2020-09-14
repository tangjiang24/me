package com.tj24.wifiview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tj24.wifiview.dialog.WifiCallBack;
import com.tj24.wifiview.dialog.WifiConnectDialog;
import com.tj24.wifiview.dialog.WifiEditDialog;
import com.unilife.common.timer.UMTimer;
import com.unilife.common.ui.dialog.LoadProgress;
import com.unilife.common.utils.ToastMng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Description: wifi连接 自定义view
 * @Author: 19018090 2020/7/28 19:25
 * @Version: 1.0
 */
public abstract class AbstractWifiView extends FrameLayout implements IWifiWidget, CompoundButton.OnCheckedChangeListener,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, View.OnClickListener {

    private static final String TAG = "WifiView";
    private static final String WIFI_SCAN = "wifiScan";
    private Context context;
    private RecyclerView rcWifi;
    private CheckBox cbWifi;
    private ImageView ivRefresh;
    private WifiAdapter wifiAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<WifiItem> wifiItems = new ArrayList<>();
    //连接弹出框
    private WifiConnectDialog wifiConnectDialog;
    //广播
    private WifiBroadcastReceiver wifiReceiver;
    //刷新按钮的动画
    private ObjectAnimator mWifiRefreshAnimator;
    //wifi连接器
    private WifiConnector wifiConnector;
    //wifi建造器
    public WifiViewBuilder wifiViewBuilder;

    public AbstractWifiView(@NonNull Context context) {
        this(context,null);
    }

    public AbstractWifiView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AbstractWifiView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        wifiViewBuilder = getWifiViewBuilder()!=null?getWifiViewBuilder():new WifiViewBuilder();
        wifiConnector = new WifiConnector(context);
        initView();
    }

    /**
     * Description: wifiView的构建方法抽象
     * Author: 19018090 2020/8/31 17:34
     */
    protected abstract WifiViewBuilder getWifiViewBuilder();


    private void initView() {
        LayoutInflater.from(context).inflate(wifiViewBuilder.getWifiViewLayoutId(),this);
        cbWifi = findViewById(R.id.cb_wifi);
        rcWifi = findViewById(R.id.rc_wifi);
        ivRefresh = findViewById(R.id.iv_refresh);
        ivRefresh.setOnClickListener(this);
        cbWifi.setOnCheckedChangeListener(this);

        linearLayoutManager = new LinearLayoutManager(context);
        wifiAdapter = new WifiAdapter(wifiViewBuilder);
        rcWifi.setLayoutManager(linearLayoutManager);
        rcWifi.setAdapter(wifiAdapter);
        wifiAdapter.setOnItemClickListener(this);
        wifiAdapter.setOnItemChildClickListener(this);

        cbWifi.setChecked(WifiHelper.isWifiEnable(context));
        if(cbWifi.isChecked()){
            getScanResult();
        }
    }

    @Override
    public void onCreate(Context context) {
        //注册广播
        wifiReceiver = new WifiBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifi连接状态广播,是否连接了一个有效路由
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
        context.registerReceiver(wifiReceiver, filter);
    }

    @Override
    public void onResume() {
        //周期刷新
        UMTimer.getInstance().startTimer(WIFI_SCAN, wifiViewBuilder.getRefreshCircle(), new UMTimer.UMTimerOutListener() {
            @Override
            public void UMTimeOut(String s) {
                WifiHelper.startScan(context);
            }
        });
    }

    @Override
    public void onPause() {
        UMTimer.getInstance().stopTimer(WIFI_SCAN);
        cancleRefreshAnmator();
    }

    @Override
    public void onDestroy(Context context) {
        context.unregisterReceiver(wifiReceiver);
    }


    /**
     * Description: 设置是否可以开关wifi
     * Author: 19018090 2020/8/31 17:39
     */
    public void setEnableSwitch(boolean enable){
        cbWifi.setVisibility(enable?VISIBLE:GONE);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            WifiHelper.openWifi(context);
        }else {
            wifiAdapter.getData().clear();
            wifiAdapter.notifyDataSetChanged();
            WifiHelper.closeWifi(context);
            cancleRefreshAnmator();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.iv_refresh){
            if(!WifiHelper.isWifiEnable(context)){
                return;
            }
            WifiHelper.startScan(context);
            if(mWifiRefreshAnimator == null){
                mWifiRefreshAnimator = ObjectAnimator.ofFloat(ivRefresh, "rotation", 0f, 360f);
            }
            if (mWifiRefreshAnimator.isRunning()) {
                return;
            }
            mWifiRefreshAnimator.setDuration(900);
            mWifiRefreshAnimator.setRepeatCount(-1);
            mWifiRefreshAnimator.setRepeatMode(ValueAnimator.RESTART);
            mWifiRefreshAnimator.setInterpolator(new LinearInterpolator());
            mWifiRefreshAnimator.start();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        WifiItem wifiItem = wifiItems.get(position);
        if(WifiHelper.isConnectedBySsid(wifiItem.getSsid(),context)){
            return;
        }
        if(wifiItem.getPwdType() == WifiHelper.TYPE_NO_PWD){
            connect(wifiItem);
        }else {
            if(WifiHelper.isExsist(wifiItem.getSsid(),context) == null){
                showConnectDialog(wifiItem, WifiConnectDialog.TYPE_CONNECT);
            }else {
                connect(wifiItem);
            }
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        final WifiItem wifiItem = wifiItems.get(position);
        if(view.getId() == R.id.iv_arrow){
            if(wifiItem.isConnected()||wifiItem.isSaved()){
                showEditDialog(wifiItem);
            }else {
                showConnectDialog(wifiItem, WifiConnectDialog.TYPE_CONNECT);
            }
        }
    }

    /**
     * Description: 连接网络
     * Author: 19018090 2020/8/31 17:42
     */
    private void connect(final WifiItem wifiItem){
        showLoadProgress(context.getString(R.string.wifiview_connecting));
        wifiConnector.connect(wifiItem, new WifiConnector.WifiConnectCallBack() {
            @Override
            public void onConnectSucess() {
                dismissLoadProgress();
                if(wifiConnectDialog !=null){
                    wifiConnectDialog.dismiss();
                }
                ToastMng.toastShow(context.getString(R.string.wifiview_connected_to,wifiItem.getSsid()));
            }

            @Override
            public void onConnectFail(String msg) {
                dismissLoadProgress();
                if(context.getString(R.string.wifiview_pwd_error).equals(msg)){
                    WifiHelper.removeWifiBySsid(wifiItem.getSsid(),context);
                }
                getScanResult();
                ToastMng.toastShow(msg);
            }
        });
    }

    /**
     * Description: 获取网络并排序
     * Author: 19018090 2020/8/31 17:43
     */
    private void getScanResult() {
        List<ScanResult> scanResults = WifiHelper.getScanResults(context);
        if(scanResults == null){
            return;
        }
        WifiLog.d(TAG,"扫描到网络数量scanResults:"+scanResults.size());
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
            wifiItem.setLelve(WifiHelper.getWifiLevle(scanResult.level,4));
            wifiItem.setConnected(WifiHelper.isConnectedBySsid(scanResult.SSID,context));
            wifiItem.setPwdType(WifiHelper.getPwdType(scanResult.capabilities));
            wifiItem.setSaved(WifiHelper.isWifiSaved(context,scanResult.SSID));
            wifiItem.setPwd(WifiHelper.getPwd(context,scanResult.SSID));
            tmpResult.put(scanResult.SSID, wifiItem);
        }
        wifiItems.addAll(tmpResult.values());
        Collections.sort(wifiItems,new WifiComparator());
        this.wifiItems = wifiItems;
        wifiAdapter.setNewData(wifiItems);
    }


    /**
     * Description: 监听wifi状态
     * Author: 19018090 2020/8/31 17:43
     */
    public class WifiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (state){
                    case WifiManager.WIFI_STATE_DISABLED:{
                        WifiLog.d(TAG,"已经关闭");
                        //刷新checkbox
                        cbWifi.setChecked(false);
                        break;
                    }
                    case WifiManager.WIFI_STATE_DISABLING:{
                        WifiLog.d(TAG,"正在关闭");
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLED:{
                        WifiLog.d(TAG,"已经打开");
                        //刷新checkbox
                        cbWifi.setChecked(true);
                        getScanResult();
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLING:{
                        WifiLog.d(TAG,"正在打开");
                        break;
                    }
                    case WifiManager.WIFI_STATE_UNKNOWN:{
                        WifiLog.d(TAG,"未知状态");
                        break;
                    }
                }
            }else if(WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())){
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                WifiLog.d(TAG, "--NetworkInfo--" + info.toString());
                if(NetworkInfo.State.DISCONNECTED == info.getState()){//wifi没连接上
                    WifiLog.d(TAG,"wifi没连接上");
                    for(int i = 0;i < wifiItems.size();i++){//没连接上将 所有的连接状态都置为“未连接”
                        wifiItems.get(i).setConnected(false);
                    }
                    wifiAdapter.notifyDataSetChanged();
                }else if(NetworkInfo.State.CONNECTED == info.getState()){//wifi连接上了
                    WifiLog.d(TAG,"wifi连接上了");
                    notifyTopWifi();
                }else if(NetworkInfo.State.CONNECTING == info.getState()){//正在连接
                    WifiLog.d(TAG,"wifi正在连接");
                }
            }else if(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())){
                WifiLog.d(TAG,"网络列表变化了");
                getScanResult();
                cancleRefreshAnmator();
            }
        }
    }

    /**
     * Description: 将已连接网络置顶
     * Author: 19018090 2020/8/31 17:43
     */
    private void notifyTopWifi() {
        if(wifiItems.size() == 0){
            return;
        }
        String connectedSsid = WifiHelper.getSSID(context);
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

    /**
     * Description: 停止动画
     * Author: 19018090 2020/8/31 17:43
     */
    private void  cancleRefreshAnmator(){
        if(mWifiRefreshAnimator != null && mWifiRefreshAnimator.isRunning()){
            mWifiRefreshAnimator.cancel();
        }
    }

    /**
     * Description: 显示忽略网络 修改密码 dialog
     * Author: 19018090 2020/8/31 17:44
     */
    private void showEditDialog(final WifiItem wifiItem) {
        WifiEditDialog editDialog = new WifiEditDialog(context,wifiItem,wifiViewBuilder);
        editDialog.setWifiCallBack(new WifiEditDialog.CallBack() {
            @Override
            public void onUpdatePwd() {
                showConnectDialog(wifiItem, WifiConnectDialog.TYPE_UPDATE);
            }

            @Override
            public void onIgnor() {
                WifiHelper.removeWifiBySsid(wifiItem.getSsid(), context);
                getScanResult();
            }
        });
        editDialog.show();
    }

    /**
     * Description: 显示输入密码连接wifi dialog
     * Author: 19018090 2020/8/31 17:44
     */
    private void showConnectDialog(final WifiItem wifiItem, int type) {
        wifiConnectDialog = new WifiConnectDialog(context,wifiItem, type,wifiViewBuilder);
        wifiConnectDialog.setCallBack(new WifiCallBack() {
            @Override
            public void onConfirm() {
                connect(wifiItem);
            }
        });
        wifiConnectDialog.show();
    }

    //显示loading
    public void showLoadProgress(String msg) {
        LoadProgress.get().getDialog(context).setLoadingTip(msg);
        if (!LoadProgress.get().getDialog(context).isShowing()) {
            LoadProgress.get().getDialog(context).show();
        }
    }

    //隐藏loading
    public void dismissLoadProgress() {
        LoadProgress.get().dismissDialog(context);
        LoadProgress.get().cleanUpTrash();
    }
}
