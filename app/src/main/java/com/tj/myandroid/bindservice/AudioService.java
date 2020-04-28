package com.tj.myandroid.bindservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AudioService extends Service {
    public static final String TAG = AudioService.class.getSimpleName();
    private List<String> steps = new ArrayList<>();
    private int currentPostion = 0;

    private Handler handler = new Handler();
    private BroadCastRunnable broadCastRunnable;

    public AudioService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setRecipeSteps(List<String> steps){
        this.steps = steps;
    }

    public int getCurrentPostion(){
        return currentPostion;
    }

    public void startBroadCast(final BroadCastListner listner){
        handler.removeCallbacks(broadCastRunnable);
        broadCastRunnable = new BroadCastRunnable(listner);
        listner.onStartBroadCast();
        currentPostion = listner.getPostion();
        handler.postDelayed(broadCastRunnable,3000);
    }

    public void pauseBroadCast(){
        handler.removeCallbacks(broadCastRunnable);
        Log.e(TAG,"pause:postion="+ currentPostion);
    }

    public void reSetBroadCast(){
        currentPostion = 0;
        handler.removeCallbacks(broadCastRunnable);
        Log.e(TAG,"reSetBroadCast"+ currentPostion);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new AudioBinder();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"destory");
        super.onDestroy();
    }

    public class AudioBinder extends Binder {
        public AudioService getService(){
            return AudioService.this;
        }
    }

    public interface BroadCastListner{
        public int getPostion();
        public void onStartBroadCast();
        public void onFinishBroadCast();
        public void onAllFinish();
    }

    class BroadCastRunnable implements Runnable{
        private BroadCastListner listner;

        BroadCastRunnable(BroadCastListner listner) {
            this.listner = listner;
        }

        @Override
        public void run() {
            Log.e(TAG,"startbroadcast"+steps.get(currentPostion));
            if(steps.size()> currentPostion+1){
                currentPostion++;
                listner.onFinishBroadCast();
            }else {
                currentPostion = 0;
                listner.onAllFinish();
            }
        }
    }
}
