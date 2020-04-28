package com.tj.myandroid.timmer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tj.myandroid.R;
/**
 * @Description:定时器组件
 * @Createdtime:2019/6/6 16:11
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class Timmer implements View.OnClickListener {

    private Context mContext;

    private LinearLayout container;
    private ImageView ivAlarm;
    private ImageView ivStart;
    private ImageView ivPause;
    private ImageView ivCancle;
    private TextView tvTimes;

    public static final int TIMER_UNSTART = 0 ;
    public static final int TIMER_START = 1 ;
    public static final int TIMER_PAUSE = 2 ;
    /**
     * timmer的状态
     */
    private int state = TIMER_UNSTART;
    /**
     * timmer 的ID
     */
    private String id;
    /**
     * 剩余的时间
     */
    private int lastTime;
    /**
     * 总时间
     */
    private int totalTime;

    public Timmer(Context mContext, LinearLayout container,String id,int totalTimes) {
        this.totalTime = totalTimes;
        this.mContext = mContext;
        this.container = container;
        this.id = id;
    }

    public void init(){
        TimmerInfo info = TimmerUtil.timmerInfoMap.get(id);
        if(info!=null ){
            if(info.getState()== Timmer.TIMER_PAUSE){
                initView(Timmer.TIMER_PAUSE,info.getTotalTime());
            }else {
                initView(info.getState(),info.getTotalTime()==0?totalTime:info.getTotalTime());
            }
        }else {
            initView(Timmer.TIMER_UNSTART,totalTime);
        }
    }

    private void initView(int state,int currentTime) {
        container.removeAllViews();
        this.state = state;
        View view = LayoutInflater.from(mContext).inflate(R.layout.timer_layout,null);
        container.addView(view);
        tvTimes = view.findViewById(R.id.tv_times);
        ivStart = view.findViewById(R.id.iv_start);
        ivPause = view.findViewById(R.id.iv_pause);
        ivCancle = view.findViewById(R.id.iv_cancle);
        ivAlarm = view.findViewById(R.id.iv_clock);

        ivStart.setOnClickListener(this);
        ivPause.setOnClickListener(this);
        ivCancle.setOnClickListener(this);
        setViews(state,currentTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_start:
                start(lastTime);
                break;
            case R.id.iv_pause:
                if(state == TIMER_PAUSE){
                    restart(lastTime);
                }else {
                    pause(lastTime);
                }
                break;
            case R.id.iv_cancle:
                cancle(totalTime);
                break;
        }
    }

    /**
     * 取消定时
     * @param times
     */
    public void cancle(int times) {
        MyService.startService(mContext,id, 0);
        state = TIMER_UNSTART;
        setViews(state,times);
    }

    /**
     * 暂停定时
     * @param times
     */
    private void pause(int times) {
        MyService.startService(mContext,id, 0);
        state = TIMER_PAUSE;
        setViews(state,times);
    }

    /**
     * 开始倒计时
     * @param times
     */
    public void start(int times){
        MyService.startService(mContext,id, totalTime);
        state = TIMER_START;
        setViews(state,times);
    }

    /**
     * 重新开启定时
     * @param times
     */
    private void restart(int times) {
        MyService.startService(mContext,id, lastTime);
        state = TIMER_START;
        setViews(state,times);
    }

    /**
     * 获取状态
     * @return
     */
    public int getState() {
        return state;
    }

    /**
     * 获取剩余时间
     * @return
     */
    public int getLastTime() {
        return lastTime;
    }

    /**
     * 获取总时间
     * @return
     */
    public int getTotalTime() {
        return totalTime;
    }

    /**
     * 获取ID
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * 设置控件信息
     * @param state
     * @param times
     */
    @SuppressLint("ResourceType")
    public void setViews(int state, int times) {
        this.lastTime =times;
        this.state = state;
        tvTimes.setText(TimmerUtil.convertTime(times));
        switch (state){
            case TIMER_UNSTART:
                ivAlarm.setVisibility(View.VISIBLE);
                ivStart.setVisibility(View.VISIBLE);
                ivCancle.setVisibility(View.GONE);
                ivPause.setVisibility(View.GONE);
                break;
            case TIMER_START:
                ivAlarm.setVisibility(View.GONE);
                ivStart.setVisibility(View.GONE);
                ivCancle.setVisibility(View.VISIBLE);
                ivPause.setVisibility(View.VISIBLE);
                ivPause.setImageResource(R.drawable.pause);
                break;
            case  TIMER_PAUSE:
                ivAlarm.setVisibility(View.GONE);
                ivStart.setVisibility(View.GONE);
                ivCancle.setVisibility(View.VISIBLE);
                ivPause.setVisibility(View.VISIBLE);
                ivPause.setImageResource(R.drawable.start);
                break;
        }
    }
}
