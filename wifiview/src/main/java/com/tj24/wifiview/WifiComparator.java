package com.tj24.wifiview;

import java.util.Comparator;
/**
 * @Description: wifi列表排序
 * @Author: 19018090 2020/8/28 19:25
 * @Version: 1.0
 */
public class WifiComparator implements Comparator<WifiItem> {
    @Override
    public int compare(WifiItem o1, WifiItem o2) {
        if(o1.isConnected() && !o2.isConnected()){
            return -1;
        }else if(!o1.isConnected() && o2.isConnected()){
            return 1;
        }else {
            return comareByIsSaved(o1,o2);
        }
    }

    /**
     * Description: 将已保存的排到前面
     * Author: 19018090 2020/8/31 17:46
     */
    private int comareByIsSaved(WifiItem o1, WifiItem o2) {
        if(o1.isSaved() && !o2.isSaved()){
            return -1;
        }else if(!o1.isSaved() && o2.isSaved()){
            return 1;
        }
        return  compareByLeve(o1.getLelve(),o2.getLelve());
    }

    /**
     * Description: 按照信号强度排序
     * Author: 19018090 2020/8/31 17:47
     */
    private int compareByLeve(int lelve, int lelve1) {
        if(lelve>lelve1){
            return -1;
        }else if(lelve<lelve1){
            return 1;
        }
        return 0;
    }
}
