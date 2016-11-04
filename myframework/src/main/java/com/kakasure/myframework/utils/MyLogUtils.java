package com.kakasure.myframework.utils;

import com.kakasure.myframework.MyFramework;
import com.lidroid.xutils.util.LogUtils;

/**
 * 封装了log
 * Created by danke on 2015/12/30.
 */
public class MyLogUtils {


    public static void i(String content) {
        if (MyFramework.DEBUG) {
            LogUtils.i(content);
        }
    }

    public static void e(String content) {
        if (MyFramework.DEBUG) {
            LogUtils.e(content);
        }
    }

    public static void w(String content) {
        if (MyFramework.DEBUG) {
            LogUtils.w(content);
        }
    }

    public static void d(String content) {
        if (MyFramework.DEBUG) {
            LogUtils.d(content);
        }
    }

    public static void v(String content) {
        if (MyFramework.DEBUG) {
            LogUtils.v(content);
        }
    }


}
