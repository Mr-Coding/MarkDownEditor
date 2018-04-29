package com.frank.markdowneditor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Tools {
    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = simpleDateFormat.parse(time);
        long ts = date.getTime();
        return String.valueOf(ts);
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long timeMillis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    public static void sortByTime(List<Article> articlesList){
        Collections.sort(articlesList, new Comparator<Article>() {
            @Override
            public int compare(Article o1, Article o2) {
                if (o1.getTime() > o2.getTime()) {
                    return -1;
                } else if (o1.getTime() < o2.getTime()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }
}
