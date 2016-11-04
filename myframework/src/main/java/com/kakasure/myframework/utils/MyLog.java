package com.kakasure.myframework.utils;

import android.util.Log;

import com.kakasure.myframework.MyFramework;

/**
 * 调试用的Log
 *
 * @author pan
 */
public class MyLog {

    private MyLog() {
    }

    public static boolean v = true & MyFramework.DEBUG;
    public static boolean d = true & MyFramework.DEBUG;
    public static boolean i = true & MyFramework.DEBUG;
    public static boolean w = true & MyFramework.DEBUG;
    public static boolean e = true & MyFramework.DEBUG;

    public static void v(String msg) {
        if (msg == null) {
            e(null);
        }
        if (v)
            Log.v("===Verbose===", msg);
    }

    public static void d(String msg) {
        if (msg == null) {
            e(null);
        }
        if (d)
            Log.d("===Debug===", msg);
    }

    public static void i(String msg) {
        if (msg == null) {
            e(null);
        }
        if (i)
            Log.i("===Info===", msg);
    }

    public static void w(String msg) {
        if (msg == null) {
            e(null);
        }
        if (w)
            Log.w("===Warn===", msg);
    }

    public static void e(String msg) {
        if (msg == null)
            msg = "Log message can not be null";
        if (e)
            Log.e("===Error===", msg);
    }
}
