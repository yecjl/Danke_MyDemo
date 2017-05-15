package danke.utils;

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

    /**
     * 根据时间获取当文件名
     *
     * @return
     */
    public static String getFileName() {
        return new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
    }

    /**
     * 根据时间获取当文件名
     *
     * @return
     */
    public static String getFileNameNomal() {
        return new Date().getTime() + ".jpg";
    }

}
