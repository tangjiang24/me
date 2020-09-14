package com.tj.myandroid.download;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.tj.myandroid.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import okhttp3.Call;

public class DownLoadActivity extends AppCompatActivity {
    String url = "https://raw.githubusercontent.com/tangjiang24/tangjiang24.github.io/master/app-portrait-market-release_81390.apk";

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        requestPermission();
        findViewById(R.id.tv_executor).setOnClickListener(new MyOnclick());
        findViewById(R.id.tv_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpUtils.get().url(url).build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),"miao.apk") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("tj24", "onError :" + e.getMessage());
                    }

                    @Override
                    public void onResponse(File file, int id) {
                        Log.e("tj24", "onResponse :" + file.getAbsolutePath());
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        Log.e("tj24",100*progress+"%");
                    }
                });
            }
        });
    }

    private void requestPermission() {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    List<Future<String>> futures = new ArrayList<>();
    class MyOnclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {

//        new Thread(runnable).start();
//            Test.getInstance().log();
//            CacheThreadPool.getInstance().execute(runnable);
            Future<String> submit = (Future<String>) CacheThreadPool.getInstance().submit(callable);
            futures.add(submit);

            for(Future<String> future :futures){
                try {
                    Log.e("tj","futures="+future.get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            }
    }
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for(int i=0;i<3;i++) {
                final int index = i;
                try {
                    Thread.sleep(1000);
                    Log.e("tj","indext="+index);
                    Log.e("tj","thread="+Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Callable callable = new Callable() {

        @Override
        public String call() throws Exception {
            for(int i=0;i<3;i++) {
                final int index = i;
                try {
                    Thread.sleep(1000);
                    Log.e("tj","indext="+index);
                    Log.e("tj","thread="+Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "hello";
        }
    };
}
