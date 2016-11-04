package com.kakasure.checkupdateservice;

import android.app.Application;

/**
 * 功能：
 * Created by danke on 2016/7/12.
 */
public class MyApp extends Application {
    private boolean isDownloadIng = false;//记录是否正在下载应用

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public boolean isDownloadIng() {
        return isDownloadIng;
    }

    public void setIsDownloadIng(boolean isDownloadIng) {
        this.isDownloadIng = isDownloadIng;
    }
}
