package com.kakasure.myframework.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 计数器
 *
 * @author danke
 */
public class Counter extends Timer {

    private int start;
    private int end;
    private int interval;
    private final boolean countPlus;
    private final CountListener listener;
    private Handler handler;

    /**
     * 初始化计数器，开始值大于结束值则倒数反之正数
     *
     * @param start    开始值
     * @param end      结束值
     * @param interval 计数间隔
     */
    public Counter(int start, int end, int interval, final CountListener listener) {
        this.countPlus = start < end;
        this.start = start;
        this.end = end;
        this.interval = interval;
        this.listener = listener;
        this.handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        listener.onCount((int) msg.obj);
                        break;
                    case 1:
                        listener.onCountOver();
                        break;
                }
            }
        };
        schedule(new CountTask(), 0, interval);
    }

    /**
     * 以1秒钟单位，从开始值倒数到0
     *
     * @param start
     */
    public Counter(int start, CountListener listener) {
        this(start, 0, 1000, listener);
    }

    /**
     * 每割多少时间计时，有终点
     * @param start
     * @param interval
     * @param listener
     */
    public Counter(int start, int interval, CountListener listener) {
        this(start, 0, interval, listener);

    }

    class CountTask extends TimerTask {
        private int count = 0;

        @Override
        public void run() {
            if (countPlus) {
                if (start >= end) {
                    handler.sendEmptyMessage(1);
                    cancel();
                    Counter.this.cancel();
                    return;
                }

                start += interval;
                handler.obtainMessage(0, count++).sendToTarget();
            } else {
                if (start == -1 && end == -1) {
                    // 特殊情况，如果没有结束时间，一直计时
                    handler.obtainMessage(0, count++).sendToTarget();

                } else {
                    if (start <= end) {
                        handler.sendEmptyMessage(1);
                        cancel();
                        Counter.this.cancel();
                        return;
                    }

                    start -= interval;
                    handler.obtainMessage(0, count++).sendToTarget();
                }
            }
        }
    }

    /**
     * 计数监听器
     *
     * @author pan
     */
    public interface CountListener {
        void onCount(int currentCount);

        void onCountOver();
    }

    /**
     * 简单计数监听器
     *
     * @author pan
     */
    public static class SimpleCountListener implements CountListener {
        @Override
        public void onCount(int currentCount) {
        }

        @Override
        public void onCountOver() {
        }
    }
}
