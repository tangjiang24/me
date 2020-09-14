package com.tj.myandroid.wifinew.wifi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tj.myandroid.R;
import com.tj24.wifiview.WifiView;

public class WIFIActivity extends AppCompatActivity {

    WifiView wifiView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_w_i_f_i);
        wifiView = findViewById(R.id.wifi_view);
        wifiView.onCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wifiView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wifiView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wifiView.onDestroy(this);
    }
}
