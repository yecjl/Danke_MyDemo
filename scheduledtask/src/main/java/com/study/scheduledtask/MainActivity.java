package com.study.scheduledtask;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends AppCompatActivity {
    private static long start;
    private ScheduledExecutorService newScheduledThreadPool;
    private ScheduledCounter scheduledCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_timer)
    public void timer(View view) {
        Timer timer = new Timer();
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                Log.d("invoked", "task1: " + (System.currentTimeMillis() - start));
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                Log.d("invoked", "task2: " + (System.currentTimeMillis() - start));
            }
        };
        start = System.currentTimeMillis();
        timer.schedule(task1, 1000);
        timer.schedule(task2, 3000);
    }

    @OnClick(R.id.btn_schedule)
    public void schedule(View view) {
        /**
         * 使用工厂方法初始化一个ScheduledThreadPool
         */
        newScheduledThreadPool = Executors.newScheduledThreadPool(2);

        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                Log.d("invoked", "task1: " + (System.currentTimeMillis() - start));
            }
        };

        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                Log.d("invoked", "task2: " + (System.currentTimeMillis() - start));
            }
        };
        start = System.currentTimeMillis();
        newScheduledThreadPool.schedule(task1, 1000, TimeUnit.MILLISECONDS);
        newScheduledThreadPool.scheduleAtFixedRate(task2, 0, 1000, TimeUnit.MILLISECONDS);

    }

    @OnClick(R.id.btn_schedule_close)
    public void scheduleClose(View view) {
        if (newScheduledThreadPool != null) {
            newScheduledThreadPool.shutdown();
        }
    }

    @OnClick(R.id.btn_schedule_single)
    public void scheduleSingle(View view) {
        if (scheduledCounter == null) {
            scheduledCounter = new ScheduledCounter(3);
        }

        scheduledCounter.schedule(10 * 1000, new ScheduledCounter.CountListener() {
            @Override
            public void onCount(int currentCount) {
                Toast.makeText(getApplicationContext(), "延迟10秒执行一次，只执行一次", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCountOver() {

            }
        });

    }

    @OnClick(R.id.btn_schedule_end)
    public void scheduleEnd(View view) {
        if (scheduledCounter == null) {
            scheduledCounter = new ScheduledCounter(3);
        }

        scheduledCounter.schedule(0, 5 * 1000, 1000, new ScheduledCounter.CountListener() {
            @Override
            public void onCount(int currentCount) {
                Toast.makeText(getApplicationContext(), (currentCount + 1) + "每过一秒执行一次，执行60秒", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCountOver() {
                Toast.makeText(getApplicationContext(), "Ending 每过一秒执行一次，执行60秒", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btn_schedule_recycle)
    public void scheduleRecycle(View view) {
        if (scheduledCounter == null) {
            scheduledCounter = new ScheduledCounter(3);
        }

        scheduledCounter.schedule(0, 2 * 1000, new ScheduledCounter.CountListener() {
            @Override
            public void onCount(int currentCount) {
                Toast.makeText(getApplicationContext(), (currentCount + 1) + "每10秒执行一次，无限执行", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCountOver() {
            }
        });
    }

    int count = 0;

    @OnClick(R.id.btn_handler)
    public void handler(View view) {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                count++;
                if (count <= 60) {
                    Toast.makeText(getApplicationContext(), count + "每过一秒执行一次，执行60秒", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

}
