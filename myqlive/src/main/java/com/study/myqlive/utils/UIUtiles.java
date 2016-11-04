package com.study.myqlive.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.study.myqlive.BaseApplication;

/**
 * 封装了UI的工具类
 * Created by danke on 2015/12/30.
 */
public class UIUtiles {
    /**
     * 获取上下文对象
     * @return
     */
    public static Context getContext() {
        return BaseApplication.getApplication();
    }

    /**
     * 获取主线程的Handler
     *
     * @return
     */
    public static Handler getHandler() {
        return BaseApplication.getMainThreadHandler();
    }

    /**
     * 获取主线程
     *
     * @return
     */
    public static Thread getMainThread() {
        return BaseApplication.getMainThread();
    }

    /**
     * 获取主线程ID
     *
     * @return
     */
    public static int getMainThreadId() {
        return BaseApplication.getMainThreadId();
    }

    /**
     * 获取主线程的looper
     * @return
     */
    public static Looper getMainThreadLooper() {
        return BaseApplication.getMainThreadLooper();
    }

    /**
     * 延时在主线程执行runnable
     * @param runnable
     * @param delayMillis
     * @return
     */
    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }

    /**
     * 在主线程执行runnable
     * @param runnable
     * @return
     */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    /**
     * 填充布局
     * @param resId
     * @return
     */
    public static View inflate(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null);
    }

    /**
     * 填充布局
     * @param resId
     * @return
     */
    public static View inflate(int resId, ViewGroup parent) {
        return LayoutInflater.from(getContext()).inflate(resId, parent, false);
    }


    /**
     * 获取资源
     * @return
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取文字
     * @param resId
     * @return
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取文字数组
     * @param resId
     * @return
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取资源数组
     * @param resId
     * @return
     */
    public static int[] getIntegerArray(int resId) {
        return getResources().getIntArray(resId);
    }

    /**
     * 获取dimens
     * @param resId
     * @return
     */
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取Drawable
     * @param resId
     * @return
     */
    public static Drawable getDrawable(int resId) {
        //getDrawable(resId) 已经过时
//        return getResources().getDrawable(resId, null);
        return ContextCompat.getDrawable(getContext(), resId);
    }

    /**
     * 获取颜色
     * @param resId
     * @return
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取颜色选择器
     * @param resId
     * @return
     */
    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }

    /**
     * 判断当前线程是否为主线程
     * @return
     */
    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }

    /**
     * 在主线程执行任务
     * @param runnable
     */
    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }

    public static void startActivity(Intent intent) {
    }


}
