package com.tj.myandroid.timmer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class MyService extends Service {
    private Handler handler = new Handler();

    private static final String TIMER_ID = "id";
    private static final String TIMER_TIME = "time";
    public static final String TIMER_ACTION = "timer_action";
    /**
     * 管理runnable的map
     */
    Map<String, Runnable> map = new HashMap<>();

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initData(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 获取传递过来的timmer的ID 和需要定时的时间；
     * 若map中包含有此ID 的 任务，需要先将其移除掉，重新加入后启动
     * @param intent
     */
    private void initData(Intent intent) {
        int totalTimes = intent.getIntExtra(TIMER_TIME,0);
        String id = intent.getStringExtra(TIMER_ID);

        if(map.containsKey(id)){
            handler.removeCallbacks(map.get(id));
        }
        map.put(id,new AlarmRunnable(id,totalTimes));
        handler.postDelayed(map.get(id),1000);

    }

    /**
     * 启动service
     * @param mContext
     * @param id
     * @param times
     */
    public static void startService(Context mContext,String id, int times){
        Intent intent = new Intent();
        intent.putExtra(TIMER_ID,id);
        intent.putExtra(TIMER_TIME,times);
        intent.setAction(TIMER_ACTION);
        intent.setPackage("com.tj.myandroid");
        mContext.startService(intent);
    }


    class AlarmRunnable implements Runnable{
        private String id;
        private int totalTimes;
        public AlarmRunnable(String id,int totalTimes) {
            this.id = id;
            this.totalTimes = totalTimes;
        }

        @Override
        public void run() {
            if(totalTimes>=0){
                totalTimes -= 1000;
                if(totalTimes>0){ //正在倒计时
                    TimmerInfo info1 = new TimmerInfo(id, totalTimes, Timmer.TIMER_START);
                    TimmerUtil.timmerInfoMap.put(id,info1);
                }else if(totalTimes ==0){ //刚好倒计时完成
                    TimmerInfo info = new TimmerInfo(id, totalTimes, Timmer.TIMER_UNSTART);
                    TimmerUtil.timmerInfoMap.put(id,info);
                    Log.e("alarm","service已经完成了定时");
                }else {
                    return;
                }
                handler.postDelayed(this,1000);
                EventBus.getDefault().post(new TimmerInfo(id,totalTimes,Timmer.TIMER_START));
            }
        }
    }
}
