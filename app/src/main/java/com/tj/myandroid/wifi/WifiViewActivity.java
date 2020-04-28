package com.tj.myandroid.wifi;

import android.app.ProgressDialog;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tj.myandroid.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WifiViewActivity extends AppCompatActivity {
    private RecyclerView rvWifi;
    private Switch aSwitch;
    private TextView tvBack;
    private WifiAdmin wifiAdmin;
    private WifiAdapter wifiAdapter;
    private LinearLayoutManager linearLayoutManager;
    private WifiHelper wifiHelper;
    private List<WifiItem> wifiItems = new ArrayList<>();
    private boolean isInput;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_view);
        dialog = new ProgressDialog(this);
        dialog.setTitle("dfdfdfdf");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        wifiAdmin = new WifiAdmin(this);
        wifiHelper = new WifiHelper(this);
        initView();
        wifiHelper.setWifiScanCallBack(new WifiHelper.WifiScanCallBack() {
            @Override
            public void onScanWifi(List<WifiItem> scanResults) {
                if(isInput){
                    return;
                }
                wifiItems = scanResults;
                wifiAdapter.setNewData(wifiItems);
            }

            @Override
            public void onWifiConnected(String ssId) {
                wifiHelper.onScanResult();
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);

        wifiAdapter = new WifiAdapter();
        rvWifi.setLayoutManager(linearLayoutManager);
        rvWifi.setAdapter(wifiAdapter);
        wifiAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.btn_connect){
                    WifiItem item = wifiItems.get(position);

                    item.setPwdType(WifiAdmin.TYPE_WPA);
                    connectWifi(item);
                }
            }
        });
        wifiAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                final WifiItem item = wifiItems.get(position);
                if(item.isNoPwd()){
                    item.setPwdType(WifiAdmin.TYPE_NO_PWD);
                    connectWifi(item);
                }else {
                    WifiConfiguration configuration = wifiAdmin.isExsits(item.getSsid());
                    if(configuration == null){
                        RelativeLayout rlPwd = view.findViewById(R.id.rl_pwd);
                        if(rlPwd.getVisibility() == View.GONE){
                            setInputing(item,position);
                            rlPwd.setVisibility(View.VISIBLE);
                        }else {
                            exsitInputing();
                            rlPwd.setVisibility(View.GONE);
                        }
                    }else {
                        connectWifi(item);
                    }
                }
            }
        });
    }

    private void initView() {
        rvWifi = findViewById(R.id.rv_wifi);
        aSwitch = findViewById(R.id.sv);
        tvBack = findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiViewActivity.super.onBackPressed();
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isInput = false;
                    wifiAdmin.openWifi();
                    wifiAdmin.startScan();
                }else {
                    wifiAdapter.getData().clear();
                    wifiAdapter.notifyDataSetChanged();
                    wifiAdmin.closeWifi();
                }
            }
        });

        if(wifiAdmin.isWifiEnable()){
            aSwitch.setChecked(true);
            wifiAdmin.startScan();
            wifiHelper.onScanResult();
        }else {
            aSwitch.setChecked(false);
        }
    }

    public void connectWifi(final WifiItem item){
        exsitInputing();
        dialog.show();
        wifiHelper.connectWifi(item, new WifiHelper.WifiConnectCallBack() {
            @Override
            public void onConnectSucess() {
                dialog.dismiss();
                item.setConnected(true);
                Collections.sort(wifiItems,new WifiComparator());
                wifiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onConnectFail(String msg) {
                dialog.dismiss();
                wifiAdmin.removeWifiBySsid(item.getSsid());
                Toast.makeText(WifiViewActivity.this,msg+"连接失败ssid---"+item.getSsid(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setInputing(WifiItem wifiItem,int position){
        for(int i = 0; i< wifiItems.size();i++){
            if(wifiItems.get(i).isInputing()){
                wifiItems.get(i).setInputing(false);
                wifiAdapter.notifyItemChanged(i);
                break;
            }
        }
        wifiItem.setInputing(true);
        wifiAdapter.notifyItemChanged(position);
        isInput = true;
    }

    private void exsitInputing(){
        for(int i = 0; i<wifiItems.size();i++){
            if(wifiItems.get(i).isInputing()){
                wifiItems.get(i).setInputing(false);
                wifiAdapter.notifyItemChanged(i);
                break;
            }
        }
        isInput = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wifiHelper.unRegistWifiReceiver();
    }
}
