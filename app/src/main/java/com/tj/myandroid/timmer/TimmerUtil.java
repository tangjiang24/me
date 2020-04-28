package com.tj.myandroid.timmer;

import java.util.HashMap;
import java.util.Map;

public class TimmerUtil {

    /**
     * 全局保存timmer的状态
     */
    public static Map<String,TimmerInfo> timmerInfoMap = new HashMap<>();

    public static String convertTime(int t){
        if(t<60000){
            return (t % 60000 )/1000+"";
        }else if((t>=60000)&&(t<3600000)){
            return getString((t % 3600000)/60000)+":"+getString((t % 60000 )/1000);
        }else {
            return getString(t / 3600000)+":"+getString((t % 3600000)/60000)+":"+getString((t % 60000 )/1000);
        }
    }


    private static String getString(int t){
        String m="";
        if(t>0){
            if(t<10){
                m="0"+t;
            }else{
                m=t+"";
            }
        }else{
            m="00";
        }
        return m;
    }
}
