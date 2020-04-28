package com.tj.myandroid.screenprotect;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class ScreenProtectHelper {
    private static ScreenProtectHelper screenProtectHelper;
    private Context context;
    private ScreenProtectHelper(Context context) {
        this.context = context;
        //关闭系统屏保
        KeyguardManager mKeyguardManager=(KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock mKeyguardLock= mKeyguardManager.newKeyguardLock("");
        mKeyguardLock.disableKeyguard();
    }

    public static ScreenProtectHelper getInstance(Context context){
        if(screenProtectHelper == null){
            screenProtectHelper = new ScreenProtectHelper(context);
        }
        return screenProtectHelper;
    }

    public void registScreenProtect(){
        IntentFilter intentFilters = new IntentFilter();
        intentFilters.addAction(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(protectReceiver,intentFilters);
    }

    public void unRegistScreenProtect(){
        context.unregisterReceiver(protectReceiver);
    }

    static BroadcastReceiver protectReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent){
            Log.e("screenoff","熄灭了");
            try{
                Intent i = new Intent();
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setClass(context, ProtectActivity.class);
                context.startActivity(i);
            }catch(Exception e){
                Log.i("Output:", e.toString());
            }
        }
    };
}
