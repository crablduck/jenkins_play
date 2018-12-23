package cn.wlh.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /*根据今天日期的天数数字向前后推移多少天*/
    public static String getDateString(Integer dateNum) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();

        //过去七天
        c.setTime(new Date());
        c.add(Calendar.DATE, dateNum);
        Date d = c.getTime();
        String day = simpleDateFormat.format(d);
        return day;
    }
}
