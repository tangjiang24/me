package com.tj.myandroid.download;

import android.util.Log;

public class TjThread implements LOG{
    public static TjThread get() {
        return new TjThread();
    }

    public void log(){
        Log.e("tj","TjThread.log()");
    }
}
