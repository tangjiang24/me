package com.tj.myandroid.javatest;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.parsers.ParserConfigurationException;

public class Test {
    public static void main(String[] args){
        Integer i = Integer.valueOf(127);
        Integer j = Integer.valueOf(127);
       if(i==j){
           System.out.println("true");
       }else {
           System.out.println("false");
       }
    }

    private static void domParse() {
        DomParser parser = new DomParser();
        try {
            parser.parseDom();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private static void filtContent() {
        FilterTime filterTime = new FilterTime();
        int filterContent = filterTime.filterContent("蒸煮1小时");
        System.out.println(filterContent);
    }

    private static void  parseTime(long timestamp){
        Date d = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(d));
    }

    private static void getTodayTime(){
        long current=System.currentTimeMillis();//当前时间毫秒数
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
        long yesterday=System.currentTimeMillis()-24*60*60*1000;//昨天的这一时间的毫秒数
        System.out.println(new Timestamp(current).toString()+current);//当前时间
        System.out.println(new Timestamp(yesterday).toString()+yesterday);//昨天这一时间点
        System.out.println(new Timestamp(zero).toString()+zero);//今天零点零分零秒
        System.out.println(new Timestamp(twelve));//今天23点59分59秒
    }
}
