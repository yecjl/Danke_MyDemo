package com.kakasure.checkupdateservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

/**
 * 功能：
 * Created by danke on 2016/7/12.
 */
public class DownloadService extends Service {
    private MyApp app;
    private NotificationManager notificationManager;
    private boolean isStart;
    private DownloadActivity.ICallbackResult callbackResult;

    @Override
    public void onCreate() {
        super.onCreate();
        app = (MyApp) super.getApplication();
        notificationManager = (NotificationManager) super.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isStart = true;
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        isStart = true;
        return new DownloadBind();
    }

    public class DownloadBind extends Binder {

        public void start() {
            startupNotification();//在信息栏显示下载通知信息
            startDownload();//执行下载任务
        }

        public void setOnBackResult(DownloadActivity.ICallbackResult iCallbackResult) {
            callbackResult = iCallbackResult;
        }

        public void cancelNotification() {
            mNotification.tickerText = "已经取消下载";
            notificationManager.cancel(NOTIFY_ID);//取消通知栏信息
            stopDownload = true;
        }
    }

    private final int NOTIFY_ID = 0;
    private Notification mNotification;

    private void startupNotification() {
        mNotification = new NotificationCompat.Builder(this).build();
        mNotification.icon = R.mipmap.ic_launcher;
        mNotification.tickerText = "正在下载";
        mNotification.defaults = Notification.DEFAULT_SOUND;
        RemoteViews view = new RemoteViews(super.getPackageName(), R.layout.notification_download);
        mNotification.contentView = view;
        Intent intent = new Intent(this, DownloadActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent
                .getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        mNotification.contentIntent = contentIntent;
        notificationManager.notify(NOTIFY_ID, mNotification);
    }

    private Thread downloadThread;

    private void startDownload() {
        downloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                downApk();//这里模拟实现网络下载功能
            }
        });
        downloadThread.start();
    }

    // downApk()实现连接网络并下载并保存文件，这里模拟实现网络下载功能
    private int progress = 0;
    private int lastLength = 10 * 1024 * 1024;//剩余文件的大小，初始值为文件总尺寸
    private int count = 0;//已经下载的数据大小
    private boolean stopDownload = false;

    private void downApk() {
        while (lastLength > 0 && !stopDownload) {
            try {
                Thread.sleep(2000);
                count += 1 * 1024 * 1024;
                lastLength = lastLength - 1 * 1024 * 1024;
                progress = (int) ((float) count / (10 * 1024 * 1024) * 100);
                if (progress > 1) {
                    Message msg = mhandler.obtainMessage();
                    msg.what = 1;
                    msg.arg1 = progress;
                    mhandler.sendMessage(msg);
                    callbackResult.OnBackResult(progress);//通过ICallBackResult回调OnBackResult（），实现更改DownloadActivity中进度条的值
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mhandler.sendEmptyMessage(0);//表示下载完成
        app.setIsDownloadIng(false);
        callbackResult.OnBackResult("finish");
    }

    private Handler mhandler = new Handler() {
        /**
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://表示下载完成
                    stopSelf();//停止当前服务
                    notificationManager.cancel(NOTIFY_ID);//关闭下载提示栏
                    isStart = false;
                    break;
                case 1://表示产生>1的百分比的变化
                    int progress = (Integer) msg.arg1;
                    RemoteViews contentView = mNotification.contentView;
                    contentView.setTextViewText(R.id.tvProgress, progress + "%");//显示百分比数据
                    contentView.setProgressBar(R.id.pbUpdate, 100, progress, false);//改变进度条
                    mNotification.defaults = 0;//取消声音提示
                    notificationManager.notify(NOTIFY_ID, mNotification);//通知下载提示栏的更新
                    break;

            }
        }
    };
}
