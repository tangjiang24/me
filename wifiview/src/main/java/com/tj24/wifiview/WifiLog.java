package com.tj24.wifiview;

import android.util.Log;

/**
 * Describtion  : log工具
 * Author  : 19018090 2020/8/29 12:04
 * version : 4.2
 */
public class WifiLog {
    //是否打印标记
    public static boolean isPrint = false;

    public static void v(String tag,String log){
        if(isPrint){
            Log.v(tag,log);
        }
    }

    public static void d(String tag,String log){
        if(isPrint){
            Log.d(tag,log);
        }
    }

    public static void i(String tag,String log){
        if(isPrint){
            Log.i(tag,log);
        }
    }

    public static void w(String tag,String log){
        if(isPrint){
            Log.w(tag,log);
        }
    }

    public static void e(String tag,String log){
        if(isPrint){
            Log.e(tag,log);
        }
    }
}
