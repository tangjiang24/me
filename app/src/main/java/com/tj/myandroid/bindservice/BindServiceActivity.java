package com.tj.myandroid.bindservice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tj.myandroid.R;
import com.tj.myandroid.SwipeBackFinishActivity;

import java.util.ArrayList;
import java.util.List;

public class BindServiceActivity extends SwipeBackFinishActivity implements View.OnClickListener {

    private TextView tvContent;
    private Button btnStart;
    private Button btnPause;
    private Button btnReset;

    private boolean isBind;
    private AudioService.AudioBinder mBinder;
    private AudioService mService;
    private List<String> steps = new ArrayList<>();

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            isBind = true;
                mBinder = (AudioService.AudioBinder) iBinder;
            mService = mBinder.getService();
            mService.setRecipeSteps(steps);
            mService.startBroadCast(new MyBroadCastListner());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_service);
        tvContent = findViewById(R.id.tv_content);
        btnPause = findViewById(R.id.btn_pause);
        btnStart = findViewById(R.id.btn_start);
        btnReset = findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        steps.add("111111111111");
        steps.add("222222222222");
        steps.add("333333333333");
        steps.add("444444444444");
        bindService(new Intent(this,AudioService.class),serviceConnection, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                btnPause.setText("暂停");
                btnStart.setText("开始");
                mService.startBroadCast(new MyBroadCastListner());
                break;
            case R.id.btn_reset:
                tvContent.setText(steps.get(0));
                mService.reSetBroadCast();
                break;
            case R.id.btn_pause:
                btnPause.setText("已暂停");
                mService.pauseBroadCast();
                break;
        }
    }

    class MyBroadCastListner implements AudioService.BroadCastListner{
        @Override
        public int getPostion() {
            return mService.getCurrentPostion();
        }

        @Override
        public void onStartBroadCast() {
            tvContent.setText(steps.get(mService.getCurrentPostion()));
        }

        @Override
        public void onFinishBroadCast() {
            mService.startBroadCast(new MyBroadCastListner());
        }

        @Override
        public void onAllFinish() {
            btnStart.setText("再次播放");
        }
    }
}
