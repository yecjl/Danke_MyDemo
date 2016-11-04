package com.study.myqlive.utils;

import com.kakasure.myframework.utils.MyLog;
import com.study.myqlive.constant.Constant;
import com.study.myqlive.constant.LogConstants;

/**
 * 日志输出
 */
public class SxbLog {
    public enum SxbLogLevel {
        OFF,
        ERROR,
        WARN,
        DEBUG,
        INFO
    }


    public static String ACTION_HOST_CREATE_ROOM = "clogs.host.createRoom";
    public static String ACTION_VIEWER_ENTER_ROOM = "clogs.viewer.enterRoom";
    public static String ACTION_VIEWER_QUIT_ROOM = "clogs.viewer.quitRoom";
    public static String ACTION_HOST_QUIT_ROOM = "clogs.host.quitRoom";
    public static String ACTION_VIEWER_SHOW = "clogs.viewer.upShow";
    public static String ACTION_VIEWER_UNSHOW = "clogs.viewer.unShow";
    public static String ACTION_HOST_KICK = "clogs.host.kick";


    static private SxbLogLevel level = SxbLogLevel.INFO;

    static public String[] getStringValues() {
        SxbLogLevel[] levels = SxbLogLevel.values();
        String[] stringValuse = new String[levels.length];
        for (int i = 0; i < levels.length; i++) {
            stringValuse[i] = levels[i].toString();
        }
        return stringValuse;
    }

//    static public void setLogLevel(SxbLogLevel newLevel) {
//        level = newLevel;
//        w("Log", "change log level: " + newLevel);
//    }
//
//    public static void v(String strTag, String strInfo) {
//        MyLog.v(strTag + strInfo);
//        if (level.ordinal() >= SxbLogLevel.INFO.ordinal()) {
//            SxbLogImpl.writeLog("I", strTag, strInfo, null);
//        }
//    }
//
//    public static void i(String strTag, String strInfo) {
//        v(strTag, strInfo);
//    }
//
//    public static void d(String strTag, String strInfo) {
//        MyLog.d(strTag + strInfo);
//        if (level.ordinal() >= SxbLogLevel.DEBUG.ordinal()) {
//            SxbLogImpl.writeLog("D", strTag, strInfo, null);
//        }
//    }
//
//
//    public static void w(String strTag, String strInfo) {
//        MyLog.w(strTag + strInfo);
//        if (level.ordinal() >= SxbLogLevel.WARN.ordinal()) {
//            SxbLogImpl.writeLog("W", strTag, strInfo, null);
//        }
//    }
//
//    public static void e(String strTag, String strInfo) {
//        MyLog.e(strTag + strInfo);
//        if (level.ordinal() >= SxbLogLevel.ERROR.ordinal()) {
//            SxbLogImpl.writeLog("E", strTag, strInfo, null);
//        }
//    }

    public static void writeException(String strTag, String strInfo, Exception tr) {
        SxbLogImpl.writeLog("C", strTag, strInfo, tr);
    }

    public static void standardEnterRoomLog(String TAG, String info, String success, String info2, int idStatus, String id) {
        if (idStatus == Constant.HOST) {
            MyLog.d(TAG + LogConstants.ACTION_HOST_CREATE_ROOM + LogConstants.DIV + id + LogConstants.DIV + info
                    + LogConstants.DIV + success + LogConstants.DIV + info2);
        } else {
            MyLog.d(TAG + LogConstants.ACTION_VIEWER_ENTER_ROOM + LogConstants.DIV + id + LogConstants.DIV + info +
                    LogConstants.DIV + success + LogConstants.DIV + info2);
        }
    }


    public static void standardQuiteRoomLog(String TAG, String info, String success, String info2, int idStatus, String id) {
        if (idStatus == Constant.HOST) {
            MyLog.d(TAG + LogConstants.ACTION_HOST_QUIT_ROOM + LogConstants.DIV + id + LogConstants.DIV + info
                    + LogConstants.DIV + success + LogConstants.DIV + info2);
        } else {
            MyLog.d(TAG + LogConstants.ACTION_VIEWER_QUIT_ROOM + LogConstants.DIV + id + LogConstants.DIV + info +
                    LogConstants.DIV + success + LogConstants.DIV + info2);
        }
    }

    /**
     * 上麦LOG
     *
     * @param TAG
     * @param info
     * @param success
     * @param info2
     */
    public static void standardMemberShowLog(String TAG, String info, String success, String info2, String id) {
        MyLog.d(TAG + LogConstants.ACTION_VIEWER_UNSHOW + LogConstants.DIV + id + LogConstants.DIV + info
                + LogConstants.DIV + success + LogConstants.DIV + info2);
    }

    /**
     * 下麦LOG
     *
     * @param TAG
     * @param info
     * @param success 成功与否
     * @param info2
     */
    public static void standardMemberUnShowLog(String TAG, String info, String success, String info2, String id) {
        MyLog.d(TAG + LogConstants.ACTION_VIEWER_UNSHOW + LogConstants.DIV + id + LogConstants.DIV + info
                + LogConstants.DIV + success + LogConstants.DIV + info2);
    }


    public static void standardLog(String TAG, String type, String info, String success, String info2, String id) {
        MyLog.d(TAG + LogConstants.ACTION_HOST_CREATE_ROOM + LogConstants.DIV + id + LogConstants.DIV + info
                + LogConstants.DIV + success + LogConstants.DIV + info2);
    }

}
