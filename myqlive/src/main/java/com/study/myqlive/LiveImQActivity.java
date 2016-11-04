package com.study.myqlive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.kakasure.myframework.app.BaseFragmentActivity;

/**
 * 功能：
 * Created by danke on 2016/11/2.
 */

public class LiveImQActivity extends BaseFragmentActivity {

    private int idStatus;
    private String identify;
    private String playUrl;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_live_new;
    }

    @Override
    protected void afterInject(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 不锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 去掉信息栏

        // 获取传递的数据
        Intent intent = getIntent();
        if (intent != null) {
            playUrl = intent.getStringExtra("playUrl");
            identify = intent.getStringExtra("identify");
            idStatus = intent.getIntExtra("IdStatus", 0);
        }
    }

    public static void start(Context context, String playUrl, String identify, int IdStatus) {
        Intent intent = new Intent(context, LiveImQActivity.class);
        intent.putExtra("playUrl", playUrl);
        intent.putExtra("identify", identify);
        intent.putExtra("IdStatus", IdStatus);
        ((Activity) context).startActivityForResult(intent, 0);
    }
}
