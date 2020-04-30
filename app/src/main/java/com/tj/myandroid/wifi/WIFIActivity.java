package com.tj.myandroid.wifi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tj.myandroid.R;

public class WIFIActivity extends AppCompatActivity {

    WifiView wifiView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_w_i_f_i);
        wifiView = findViewById(R.id.wifi_view);
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
}
