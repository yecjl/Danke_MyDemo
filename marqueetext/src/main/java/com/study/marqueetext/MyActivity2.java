package com.study.marqueetext;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 功能：
 * Created by danke on 2016/10/21.
 */
public class MyActivity2 extends Activity {

    private MarqueeView marqueeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_main2);
        marqueeView = (MarqueeView) findViewById(R.id.marquee_view);
        initMarqueeView();
    }

    private void initMarqueeView() {
//        ImageView iv1 = new ImageView(this);
//        iv1.setImageResource(R.mipmap.pic1);
//        marqueeView.addViewInQueue(iv1);
//        ImageView iv2 = new ImageView(this);
//        iv2.setImageResource(R.mipmap.pic2);
//        marqueeView.addViewInQueue(iv2);
//        ImageView iv3 = new ImageView(this);
//        iv3.setImageResource(R.mipmap.pic3);
//        marqueeView.addViewInQueue(iv3);
//        ImageView iv4 = new ImageView(this);
//        iv4.setImageResource(R.mipmap.pic4);
//        marqueeView.addViewInQueue(iv4);
//        ImageView iv5 = new ImageView(this);
//        iv5.setImageResource(R.mipmap.pic5);
//        marqueeView.addViewInQueue(iv5);
//        ImageView iv6 = new ImageView(this);
//        iv6.setImageResource(R.mipmap.pic6);
//        marqueeView.addViewInQueue(iv6);

        TextView textView = new TextView(this);
        textView.setText("dasdasfasfaffffffffffffff");
        marqueeView.addViewInQueue(textView);

        marqueeView.setScrollSpeed(8);
        marqueeView.setScrollDirection(MarqueeView.RIGHT_TO_LEFT);
        marqueeView.setViewMargin(15);
        marqueeView.startScroll();
    }
}