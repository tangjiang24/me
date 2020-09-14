package com.tj.myandroid;

import android.app.Application;

import com.amitshekhar.DebugDB;
import com.facebook.stetho.Stetho;
import com.tj.myandroid.greendao.GreenDaoManager;
import com.unilife.kernel.UmKernel;

public class MyApplication extends Application {
    private static MyApplication context;
    public static MyApplication getContext(){
        return context;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Stetho.initializeWithDefaults(this);
        DebugDB.getAddressLog();
        GreenDaoManager.init();
        UmKernel.getInstance().init(this);
}
}
