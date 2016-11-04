package com.study.marqueetext;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 功能：
 * Created by danke on 2016/10/21.
 */
public class MyActivity extends Activity {
    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_main);
        ButterKnife.bind(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setVisibility(View.VISIBLE);
                TranslateAnimation myAnimation_Translate = new TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT, 1,
                        Animation.RELATIVE_TO_PARENT, -1,
                        Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, 0);
                myAnimation_Translate.setDuration(5000);
                myAnimation_Translate.setFillAfter(true);
//                myAnimation_Translate.setInterpolator(AnimationUtils
//                        .loadInterpolator(MyActivity.this,
//                                android.R.anim.accelerate_decelerate_interpolator));
                tv.startAnimation(myAnimation_Translate);
            }
        });
    }
}
