package com.tj24.wifiview;

import android.content.Context;

/**
 * Describtion  :wifiView 生命周期接口
 * Author  : 19018090 2020/8/29 11:02
 * version : 4.2
 */
public interface IWifiWidget {
    void onCreate(Context var1);

    void onDestroy(Context var1);

    void onResume();

    void onPause();
}
