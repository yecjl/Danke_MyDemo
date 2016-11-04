package com.study.mydispatchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * 功能：
 * Created by danke on 2016/10/19.
 */
public class TestButton extends Button {
    private float mLastMotionY;

    public TestButton(Context context) {
        super(context);
    }

    public TestButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("MainActivity", "dispatchTouchEvent-- action=" + event.getAction());
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mLastMotionY = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                final float y = event.getY();
//                if (Math.abs(y - mLastMotionY) > 1f) {
//                    return true;
//                }
//                mLastMotionY = y;
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("MainActivity", "onTouchEvent-- action=" + event.getAction());
        return super.onTouchEvent(event);
    }
}
