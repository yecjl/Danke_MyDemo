package com.study.scheduledtask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class timerTask extends Activity {
    private int recLen = 0;
    private TextView txtView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.timertask);
        txtView = (TextView) findViewById(R.id.txttime);

        handler.postDelayed(runnable, 1000);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen++;
            txtView.setText("" + recLen);
            handler.postDelayed(this, 1000);
        }
    };
}  