package com.study.myqlive.presenters.viewinface;


/**
 *  直播界面回调
 */
public interface LiveView extends MvpView {
    void refreshText(String text, String name);
}
