package com.study.myqlive.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

import com.kakasure.myframework.utils.MyLogUtils;

import java.lang.reflect.Field;

/**
 * Created by xgy on 2015/12/22.
 * 屏幕尺寸相关操作工具类
 */
public class WindowUtil {
    /**
     * 获取状态栏高度
     *
     * @param mContext
     * @return
     */
//    public static int getStatusHeight(Context mContext) {
//        Rect frame = new Rect();
//        ((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        int statusBarHeight = frame.top;
//        return statusBarHeight;
//    }

    /**
     * 获取状态栏高度
     *
     * @param mContext
     * @return
     */
    public static int getStatusHeight(Context mContext) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = mContext.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            MyLogUtils.d("get status bar height fail");
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获取底部导航条的高度
     *
     * @param mContext
     * @return
     */
    public static int getNavigationBarHeight(Context mContext) {
        Resources resources = mContext.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;
    }

    /**
     * 检查是否有虚拟键盘
     *
     * @param context
     * @return
     */
    @SuppressLint("NewApi")
    public static boolean checkDeviceHasNavigationBar(Context context) {

        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(context)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

    /**
     * 获取屏幕高度的px值
     *
     * @param mContext
     * @return
     */
    public static int getWindowHeight(Context mContext) {
        int height = 0;
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        return height;
    }

    /**
     * 获取屏幕宽度的px值
     *
     * @param mContext
     * @return
     */
    public static int getWindowWidth(Context mContext) {
        int width = 0;
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        return width;
    }

    /**
     * dp值转换成px值
     *
     * @param mContext
     * @param dpSize
     * @return
     */
    public static int dpToPx(Context mContext, float dpSize) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float mDensity = metrics.density;
        return (int) (dpSize * mDensity + 0.5f);
    }

    /**
     * px值转换成dp值
     *
     * @param mContext
     * @param pxSize
     * @return
     */
    public static int pxToDp(Context mContext, float pxSize) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float mDensity = metrics.density;
        return (int) (pxSize / mDensity + 0.5f);
    }
}
