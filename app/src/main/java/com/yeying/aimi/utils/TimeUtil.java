package com.yeying.aimi.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 时间Util
 *
 * @author weixiang.qin
 */
public class TimeUtil {
    /**
     * yyyy-MM-dd HH:mm:ss格式
     */
    public static final String FORMAT_COMMON = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd格式
     */
    public static final String FORMAT_YMD = "yyyy-MM-dd";
    /**
     * MM-dd HH:mm格式
     */
    public static final String FORMAT_MMDDHHMM = "MM-dd HH:mm";
    /**
     * 小时
     */
    public static final String KEY_HOUR = "hour";
    /**
     * 分钟
     */
    public static final String KEY_MINUTE = "minute";
    /**
     * 秒
     */
    public static final String KEY_SECOND = "second";
    final static String daynames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    /**
     * 将String时间格式pattern转换成Date类型
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date parseStringToDate(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Date类型时间转换成String格式
     *
     * @param date
     * @return
     */
    public static String parseDateToString(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 计算相差秒时间
     *
     * @param date
     * @param second
     * @return
     */
    public static Date subSecondToDate(Date date, Integer second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, second);
        return cal.getTime();
    }

    /**
     * 计算时间
     *
     * @param src
     * @param des
     * @return
     */
    public static Map<String, Long> calTime(Date src, Date des) {
        long time = des.getTime() - src.getTime();
        if (time < 0) {
            return null;
        }
        long ts = time / 1000;
        long hour = ts / 3600;
        long minute = ts % 3600 / 60;
        long second = ts % 3600 % 60;
        Map<String, Long> result = new HashMap<String, Long>();
        result.put(KEY_HOUR, hour);
        result.put(KEY_MINUTE, minute);
        result.put(KEY_SECOND, second);
        return result;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_YMD);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_COMMON);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getWeekDate(String date) {
        StringBuffer sb = new StringBuffer();

        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YMD);
        sb.append(date);
        try {
            Date date1 = sdf.parse(date);
            Calendar cl = Calendar.getInstance();
            cl.setTime(date1);
            int days = cl.get(Calendar.DAY_OF_WEEK);
            sb.append("(");
            sb.append(daynames[days - 1]);
            sb.append(")");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return sb.toString();
    }
}
