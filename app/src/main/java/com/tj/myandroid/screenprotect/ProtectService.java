package com.tj.myandroid.screenprotect;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class ProtectService extends Service {

    public ProtectService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        ScreenProtectHelper.getInstance(this).registScreenProtect();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        ScreenProtectHelper.getInstance(this).unRegistScreenProtect();
        super.onDestroy();
    }

    public static void initScreenProtect(Context context){
//        Intent i = new Intent(context,ProtectService.class);
//        context.startService(i);
    }
}
