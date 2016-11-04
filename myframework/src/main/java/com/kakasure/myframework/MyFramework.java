package com.kakasure.myframework;

import android.content.Context;

import com.android.volley.toolbox.ImageLoader;
import com.kakasure.myframework.data.RequestManager;
import com.kakasure.myframework.view.MyToast;

/**
 * Created by danke on 2015/12/30.
 */
public class MyFramework {
    private static Context appContext;
    public static boolean DEBUG = true;

    private MyFramework() {}

    /**
     * 初始化Framework
     * @param appContext
     */
    public static void init(Context appContext) {
        init(appContext, null);
    }

    /**
     * 初始化Framework
     * @param appContext
     * @param imageCache 自定义图片缓存
     */
    public static void init(Context appContext, ImageLoader.ImageCache imageCache) {
        MyFramework.appContext = appContext;
        RequestManager.init(appContext, imageCache);
        MyToast.init(appContext);
    }

    /**
     * 获取上下文对象
     * @return
     */
    public static Context getAppContext() {
        if (appContext == null) {
            throw new IllegalStateException("MyFramework is not initialized");
        }
        return appContext;
    }

}
