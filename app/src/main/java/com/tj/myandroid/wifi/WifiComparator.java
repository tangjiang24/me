package com.tj.myandroid.wifi;

import java.util.Comparator;

public class WifiComparator implements Comparator<WifiItem> {
    @Override
    public int compare(WifiItem o1, WifiItem o2) {
        if(o1.isConnected() && !o2.isConnected()){
            return -1;
        }else if(!o1.isConnected() && o2.isConnected()){
            return 1;
        }else {
            if(o1.getLelve()>o2.getLelve()){
                return -1;
            }else if(o1.getLelve()<o2.getLelve()){
                return 1;
            }
        }
        return 0;
    }
}
