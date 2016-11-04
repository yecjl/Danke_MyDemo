package com.study.mydispatchevent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    @Bind(R.id.my_btn)
    Button mBtn;
    @Bind(R.id.mylayout)
    LinearLayout mlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mlayout.setOnTouchListener(onTouchListener);
        mlayout.setOnClickListener(onClickListener);

        mBtn.setOnTouchListener(onTouchListener);
        mBtn.setOnClickListener(onClickListener);
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d("MainActivity", "OnTouchListener--onTouch-- action=" + event.getAction() + " --" + v);
            return false;
//            return true; // 拦截
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("MainActivity", "OnClickListener--onClick--" + v);
        }
    };
}
