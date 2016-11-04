package com.kakasure.checkupdateservice;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ProgressBar;

/**
 * 功能：
 * Created by danke on 2016/7/12.
 */
public class DownloadActivity extends Activity {
    private ProgressBar pbDownload;
    private MyApp app;
    private boolean isCancel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        pbDownload = (ProgressBar) super.findViewById(R.id.pbDownload);
        app = (MyApp) super.getApplication();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startDownload();
    }

    private void startDownload() {
        if (!app.isDownloadIng()) {
            Intent intent = new Intent(DownloadActivity.this, DownloadService.class);
            super.startService(intent);//启动服务下载服务，可以确保应用结束下载服务仍然执行
            super.bindService(intent, conn, Service.BIND_AUTO_CREATE);//同时绑定服务，可通过ServiceConnection获得Binder实现与Service的通信
            app.setIsDownloadIng(true);
        }
    }

    private DownloadService.DownloadBind bind;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bind = (DownloadService.DownloadBind) service;//获得binder
            bind.start();//调用DownloadService实例中的start()，执行后台下载任务
            bind.setOnBackResult(result);//调用DownloadService实例中的setOnBackResult()
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bind = null;
        }
    };

    public interface ICallbackResult {
        void OnBackResult(Object Result);
    }

    private boolean isFinished;
    private ICallbackResult result = new ICallbackResult() {
        @Override
        public void OnBackResult(Object result) {
            if ("finish".equals(result)) {//如果传入“finish”表示下载完成
                finish();
                isFinished = true;
                return;
            }
            int progress = (Integer) result;
            pbDownload.setProgress(progress);//改变进度条
        }
    };

    public void onCancel(View view) {
        isCancel = true;
        app.setIsDownloadIng(false);
        bind.cancelNotification();
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (isFinished || isCancel) {//如果下载完成或用户取消下载停止后台服务，否则服务仍在后台运行
            stopService(new Intent(this, DownloadService.class));//停止后台服务
        }
        if (bind != null) {
            unbindService(conn);//取消服务绑定
        }
    }
}
