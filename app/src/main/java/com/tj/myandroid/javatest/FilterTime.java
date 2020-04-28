package com.tj.myandroid.javatest;

public class FilterTime {
    private static final String[] NUM_BIG = new String[]{"十","一","二","三","四","五","六","七","八","九",};
    private static final String[] NUM_SMALL = new String[]{"0","1","2","3","4","5","6","7","8","9",};
    private  static final String NUM_ALL = "十一二三四五六七八九0123456789";

    /**
     * 获取字符中需要时间的时长  如：蒸煮25分钟 返回 25*60*1000
     * @param content
     * @return
     */
    public  int filterContent(String content){
        String time = "";
        int indextMin = content.indexOf("分钟");
        int indextHour = content.indexOf("小时");
        int indextSeconds = content.indexOf("秒");
        try {
            if(indextHour != -1){
                time = parse(content,indextHour);
                return Integer.parseInt(time)*3600*1000;
            }else if(indextMin != -1){
                time = parse(content,indextMin);
                return Integer.parseInt(time)*60*1000;
            } else if(indextSeconds != -1){
                time = parse(content,indextSeconds);
                return Integer.parseInt(time)*1000;
            }else {
                return 0;
            }
        }catch (NumberFormatException e){
            return 0;
        }
    }
    private  String parse(String content,int indextMin){
        String time = "";
        while (indextMin>0 && NUM_ALL.contains(String.valueOf(content.charAt(indextMin-1)))){
            time =content.charAt(indextMin-1)+time;
            indextMin--;
        }
        System.out.println(time);
        if(time.startsWith("十")){
            if(time.length()==1){
                time = tolower(time.replace("十","10"));
            }else if(time.length() == 2){
                time = tolower(time.replace("十","1"));
            }
        }else if(time.endsWith("十")){
            time = tolower(time.replace("十","0"));
        }else {
            time = tolower(time.replace("十",""));
        }
        return time;
    }
    /**
     * 将大写转换为小写
     * @param str
     * @return
     */
    private  String tolower(String str){
        str = str.replace("一","1")
                .replace("二","2")
                .replace("三","3")
                .replace("四","4")
                .replace("五","5")
                .replace("六","6")
                .replace("七","7")
                .replace("八","8")
                .replace("九","9");
        return str;
    }
}
