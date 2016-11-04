package com.study.mydispatchevent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 功能：
 * Created by danke on 2016/10/19.
 */
public class TestLinearLayoutActivity extends Activity {

    @Bind(R.id.my_btn)
    TestButton mBtn;
    @Bind(R.id.mylayout)
    TestLinearLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_linearlayout);
        ButterKnife.bind(this);

        mLayout.setOnTouchListener(onTouchListener);
        mLayout.setOnClickListener(onClickListener);
//
//        mBtn.setOnTouchListener(onTouchListener);
//        mBtn.setOnClickListener(onClickListener);
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d("MainActivity", "OnTouchListener--onTouch-- action=" + event.getAction() + " --" + v);
            return false;
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("MainActivity", "OnClickListener--onClick--" + v);
        }
    };
}
