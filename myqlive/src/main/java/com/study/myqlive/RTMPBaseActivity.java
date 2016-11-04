package com.study.myqlive;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kakasure.myframework.app.BaseFragmentActivity;
import com.kakasure.myframework.utils.MyLog;
import com.tencent.rtmp.TXLiveConstants;

import java.text.SimpleDateFormat;

/**
 * 功能：腾讯云直播，主播的基础类
 * Created by danke on 2016/10/31.
 */
public abstract class RTMPBaseActivity extends BaseFragmentActivity {
    private static final String TAG = RTMPBaseActivity.class.getSimpleName();
    public static final int ACTIVITY_TYPE_PUBLISH = 1;
    public static final int ACTIVITY_TYPE_LIVE_PLAY = 2;
    public static final int ACTIVITY_TYPE_VOD_PLAY = 3;
    protected int mActivityType;
    public TextView mLogViewStatus; // 状态
    public TextView mLogViewEvent; // 事件
    protected StringBuffer mLogMsg = new StringBuffer("");
    private int mLogMsgLenLimit = 3000;

    protected void initLogView(View view) {
        mLogViewEvent = (TextView) view.findViewById(R.id.logViewEvent);
        mLogViewStatus = (TextView) view.findViewById(R.id.logViewStatus);
    }

    /**
     * 公用打印辅助函数
     *
     * @param event
     * @param message
     */
    protected void appendEventLog(int event, String message) {
        String str = "receive event: " + event + ", " + message;
        MyLog.d(str);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String date = sdf.format(System.currentTimeMillis());
        while (mLogMsg.length() > mLogMsgLenLimit) {
            int idx = mLogMsg.indexOf("\n");
            if (idx == 0)
                idx = 1;
            mLogMsg = mLogMsg.delete(0, idx);
        }
        mLogMsg = mLogMsg.append("\n" + "[" + date + "]" + message);
    }

    /**
     * 公用打印辅助函数
     *
     * @param status
     * @return
     */
    protected String getNetStatusString(Bundle status) {
        String str = String.format("%-14s %-14s %-12s\n%-14s %-14s %-12s\n%-14s %-14s %-12s\n%-14s",
                "CPU:" + status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE),
                "RES:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT),
                "SPD:" + status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps",
                "JIT:" + status.getInt(TXLiveConstants.NET_STATUS_NET_JITTER),
                "FPS:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS),
                "ARA:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps",
                "QUE:" + status.getInt(TXLiveConstants.NET_STATUS_CODEC_CACHE) + "|" + status.getInt(TXLiveConstants.NET_STATUS_CACHE_SIZE),
                "DRP:" + status.getInt(TXLiveConstants.NET_STATUS_CODEC_DROP_CNT) + "|" + status.getInt(TXLiveConstants.NET_STATUS_DROP_SIZE),
                "VRA:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps",
                "SVR:" + status.getString(TXLiveConstants.NET_STATUS_SERVER_IP));
        return str;
    }

    /**
     * 清空日志
     */
    protected void clearLog() {
        mLogMsg.setLength(0);
        mLogViewEvent.setText("");
        mLogViewStatus.setText("");
    }

    /**
     * 实现EVENT VIEW的滚动显示
     *
     * @param scroll
     * @param inner
     */
    public static void scroll2Bottom(final ScrollView scroll, final View inner) {
        if (scroll == null || inner == null) {
            return;
        }
        int offset = inner.getMeasuredHeight() - scroll.getMeasuredHeight();
        if (offset < 0) {
            offset = 0;
        }
        scroll.scrollTo(0, offset);
    }

    public void setActivityType(int type) {
        mActivityType = type;
    }
}
