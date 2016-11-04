package com.study.marqueetext;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    @Bind(R.id.marquee)
    MarqueeText marquee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        marquee.setText("这才是真正的文字跑马灯效果这才是真正的字跑马灯效果这才是真正的");
    }

    public void start(View v) {
        marquee.startScroll();
    }
    public void stop(View v) {
        marquee.stopScroll();
    }
    public void startFor0(View v){
        marquee.startFor0();
    }
}
