package com.tj.myandroid.accesbilityservice;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class MyAccessbilityService extends AccessibilityService {
    private static final String TAG = "MyAccessbilityService";
    public MyAccessbilityService() {
    }


    /**
     * 发生用户界面事件回调此事件
     * @param event
     */
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e(TAG,"event:"+event.toString());
//        String packageName = event.getPackageName().toString();
//        String className = event.getClassName().toString();
//        Log.e(TAG,"packageName:"+packageName);
//        Log.e(TAG,"className:"+className);
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if(nodeInfo!=null){
            List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByViewId("com.tj24.module_appmanager.dao:id/iv_addItem");
            if(nodeInfos!=null && nodeInfos.size()>0){
                nodeInfos.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }

        int eventType = event.getEventType();
        switch (eventType){
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.e(TAG,"TYPE_VIEW_CLICKED:"+event.getText());
                break;
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
                Log.e(TAG,"TYPE_VIEW_ACCESSIBILITY_FOCUSED:"+event.getText());
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                Log.e(TAG,"TYPE_VIEW_LONG_CLICKED:"+event.getText());
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                Log.e(TAG,"TYPE_VIEW_FOCUSED:"+event.getText());
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED :
                Log.e(TAG,"TYPE_WINDOW_STATE_CHANGED:"+event.getText());
                break;
            case AccessibilityEvent.TYPE_WINDOWS_CHANGED :
                Log.e(TAG,"TYPE_WINDOWS_CHANGED:"+event.getText());
                break;
        }
    }

    /**
     * 中断可访问性反馈
     */
    @Override
    public void onInterrupt() {
        Log.e(TAG,"onInterrupt");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

    }
}
