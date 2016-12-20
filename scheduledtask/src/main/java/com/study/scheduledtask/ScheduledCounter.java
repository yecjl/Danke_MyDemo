package com.study.scheduledtask;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by danke on 2016/11/17.
 */

public class ScheduledCounter {
    private static final int TASK_TYPE_SINGLE = 0; // 单次任务
    private static final int TASK_TYPE_END = 1; // 有结束的循环任务
    private static final int TASK_TYPE_RECYCLE = 2; // 无限循环的任务

    private final ScheduledExecutorService newScheduledThreadPool;

    /**
     * 使用工厂方法初始化一个ScheduledThreadPool
     */

    public ScheduledCounter(int corePoolSize) {
        this.newScheduledThreadPool = Executors.newScheduledThreadPool(corePoolSize);
    }

    /**
     * 执行单次任务，延迟多长时间执行
     *
     * @param delay
     */
    public void schedule(long delay, CountListener listener) {
        newScheduledThreadPool.schedule(
                new CountTask(listener, TASK_TYPE_SINGLE, delay),
                delay, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行周期性任务, 有结束时间点
     *
     * @param startDate 起始执行时间
     * @param endDate   结束执行时间
     * @param delay     延迟执行时间
     * @param listener
     */
    public void schedule(long startDate, long endDate, long delay, CountListener listener) {
        newScheduledThreadPool.scheduleAtFixedRate(
                new CountTask(listener, TASK_TYPE_END, startDate, endDate, delay),
                startDate, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行周期性任务 - 没有结束点
     *
     * @param startDate 起始执行时间
     * @param delay     延迟执行时间
     */
    public void schedule(long startDate, long delay, CountListener listener) {
        newScheduledThreadPool.scheduleAtFixedRate(
                new CountTask(listener, TASK_TYPE_RECYCLE, startDate, delay),
                startDate, delay, TimeUnit.MILLISECONDS);
    }

    class CountTask extends TimerTask {
        private int count = 0;
        private CountListener mListener;
        private long mStartDate;
        private long mEndDate;
        private long mDelay;
        private int mCurrentType;

        private Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        mListener.onCount((int) msg.obj);
                        break;
                    case 1:
                        mListener.onCountOver();
                        break;
                }
            }
        };

        public CountTask(CountListener listener, int currentType, long delay) {
            this(listener, currentType, 0, 0, delay);
        }

        public CountTask(CountListener listener, int currentType, long startDate, long delay) {
            this(listener, currentType, startDate, 0, delay);
        }

        public CountTask(CountListener listener, int currentType, long startDate, long endDate, long delay) {
            this.mListener = listener;
            this.mCurrentType = currentType;
            this.mStartDate = startDate;
            this.mEndDate = endDate;
            this.mDelay = delay;
        }

        @Override
        public void run() {
            Log.d("invoked", "count: " + count);
            switch (mCurrentType) {
                case TASK_TYPE_SINGLE:
                    handler.obtainMessage(0, count++).sendToTarget();
                    break;
                case TASK_TYPE_END:
                    if (mStartDate + ((count + 1) * mDelay) >= mEndDate) {
                        handler.sendEmptyMessage(1);
                        cancel();
                        throw new RuntimeException("Stub!");
                    } else {
                        handler.obtainMessage(0, count++).sendToTarget();
                    }
                    break;
                case TASK_TYPE_RECYCLE:
                    handler.obtainMessage(0, count++).sendToTarget();
                    break;
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
}
