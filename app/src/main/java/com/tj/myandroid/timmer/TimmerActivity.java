package com.tj.myandroid.timmer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tj.myandroid.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimmerActivity extends AppCompatActivity {
    LinearLayout container;
    LinearLayout container1;
    Timmer timmer;
    Timmer  timmer1;
    Map<String, Timmer> timmerMap = new HashMap<>();
    TextView tvDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timmer);
        tvDate= findViewById(R.id.tv_date);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long times = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                tvDate.setText(sdf.format(new Date(times)));
            }
        });

        container = findViewById(R.id.timmer_container);
        container1 = findViewById(R.id.timmer_container1);

        timmer = new Timmer(this,container,"dd",20000000);
        timmer.init();
        timmerMap.put(timmer.getId(),timmer);

        timmer1 = new Timmer(this,container1,"ss",1000000);
        timmer1.init();
        timmerMap.put(timmer1.getId(),timmer1);

        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveAlarm(TimmerInfo info){
        if(timmerMap.containsKey(info.getId())){
            Timmer timmer = timmerMap.get(info.getId());
            if(info.getTotalTime()>0){
                timmer.setViews(timmer.getState(),info.getTotalTime());
            }else {
                timmer.setViews(Timmer.TIMER_UNSTART,timmer.getTotalTime());
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for(String key : timmerMap.keySet()){
            TimmerInfo info = new TimmerInfo(key, timmerMap.get(key).getLastTime(),timmerMap.get(key).getState());
            TimmerUtil.timmerInfoMap.put(key,info);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
