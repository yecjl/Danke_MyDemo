package com.kakasure.myframework.utils;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author danke
 */
public class TimeUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat();

    public static String getTime() {
        sdf.applyPattern("HH:mm:ss");
        return sdf.format(new Date());
    }

    public static Integer[] getMillisDatas() {
        Integer[] millisDatas = new Integer[30];
        for (int i = 0; i < millisDatas.length; i++) {
            millisDatas[i] = 7 * 60 + 30 * (i + 1);
        }
        return millisDatas;
    }

    public static String[] getMillisStr(Integer[] millisDatas) {
        String[] millisStr = new String[millisDatas.length];
        for (int i = 0; i < millisDatas.length; i++) {
            millisStr[i] = StringUtil.getFormatTwoNums(millisDatas[i] / 60) + ":" + StringUtil.getFormatMillis(millisDatas[i] % 60);
        }
        return millisStr;
    }

    public static String parseTimeToDate(long time) {
        sdf.applyPattern("yyyy年MM月dd日 HH:mm:ss");
        return sdf.format(time);
    }

    public static String parseTime(long time) {
        sdf.applyPattern("HH:mm");
        return sdf.format(time);
    }

    /**
     * 将一段时间转换为订单时间格式 MM-DD HH:mm-HH:mm
     *
     * @param start 订单开始时间
     * @param end   订单结束时间
     * @return
     */
    public static String parseToOrderTime(long start, long end) {
        String time = "";
        sdf.applyPattern("MM-dd HH:mm");
        time += sdf.format(new Date(start));
        sdf.applyPattern("-HH:mm");
        time += sdf.format(new Date(end));
        return time;
    }

    /**
     * 将时间长整型格式化成  yyyy-MM-dd HH:mm:ss 格式
     *
     * @param time
     * @return
     */
    public static String[] parseTimeToDateArrayAll(long time) {
        String[] dateStr = new String[2];
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        dateStr[0] = sdf.format(time);
        dateStr[1] = sdf2.format(time);
        return dateStr;
    }

    /**
     * 将时间长整型格式化成  yyyy-MM-dd HH:mm:ss 格式
     *
     * @param time
     * @return
     */
    public static String parseTimeToDateStringAll(long time) {
        String[] dateStr = new String[2];
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        dateStr[0] = sdf.format(time);
        dateStr[1] = sdf2.format(time);
        return dateStr[0] + " " + dateStr[1];
    }

    /**
     * 根据时间获取当文件名
     *
     * @return
     */
    public static String getFileName() {
        return new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
    }

    /**
     * 将时间长整型格式化成 yy.MM HH:mm格式
     *
     * @param time
     * @return
     */
    public static String[] parseTimeToDateArrayDot(long time) {
        String[] dateStr = new String[2];
        sdf = new SimpleDateFormat("MM.dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        dateStr[0] = sdf.format(time);
        dateStr[1] = sdf2.format(time);
        return dateStr;
    }

    /**
     * 将时间长整型格式化成   yyyyy-MM-dd HH:mm   格式
     *
     * @param time
     * @return
     */
    public static String[] parseTimeToDateArrayYear(long time) {
        String[] dateStr = new String[2];
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        dateStr[0] = sdf.format(time);
        dateStr[1] = sdf2.format(time);
        return dateStr;
    }

    /**
     * 获取格式化事件
     *
     * @param date
     * @return
     */
    public static String getTime(Date date) {
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 判断当前时间是否在起始时间和结束时间之内
     *
     * @param strStart 起始时间
     * @param strEnd   结束时间
     * @return
     */
    public static boolean isRightDate(String strStart, String strEnd) {
        //String转换为java.util.Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Date dateStart = null; //初始化date
        Date dateEnd = null; //初始化date
        try {
            dateStart = sdf.parse(strStart); //Mon Jan 14 00:00:00 CST 2013
            dateEnd = sdf.parse(strEnd); //Mon Jan 14 00:00:00 CST 2013
            long timeStart = dateStart.getTime();
            long timeEnd = dateEnd.getTime();
            long timeNow = new Date().getTime(); // 当前时间
            if (timeNow >= timeStart && timeNow <= timeEnd) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 还剩多少天
     * @param strEnd
     * @return
     */
    public static int getDateLeft(String strEnd) {
        // String转换为java.util.Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Date date = null; //初始化date
        try {
            date = sdf.parse(strEnd); //Mon Jan 14 00:00:00 CST 2013
            long time = date.getTime();
            long timeNow = System.currentTimeMillis(); // 当前时间
            return (int) ((time - timeNow) / 1000 / 60 / 60 / 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
