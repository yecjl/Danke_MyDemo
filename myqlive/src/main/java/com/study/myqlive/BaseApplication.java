package com.study.myqlive;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.kakasure.myframework.MyFramework;
import com.study.myqlive.presenters.InitBusinessHelper;

/**
 * 功能：
 * Created by danke on 2016/10/31.
 */
public class BaseApplication extends Application {
    // 是否为测试
    public static final Boolean DEBUG = true;
    //获取主线程的上下文
    private static BaseApplication mContext = null;
    //获取主线程的handler
    private static Handler mMainThreadHandler = null;
    //获取主线程
    private static Thread mMainThread = null;
    //获取主线程id
    private static int mMainThreadId;
    //获取主线程的looper
    private static Looper mMainThreadLooper = null;
    //获取Application
    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        // 初始化框架
        MyFramework.init(mContext);
        MyFramework.DEBUG = DEBUG;

        //初始化APP
        InitBusinessHelper.initApp(mContext);
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public static BaseApplication getApplication() {
        return mContext;
    }

    /**
     * 获取主线程的Handler
     *
     * @return
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 获取主线程
     *
     * @return
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 获取主线程ID
     *
     * @return
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 获取主线程的looper
     *
     * @return
     */
    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }
}
