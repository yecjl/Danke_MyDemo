package com.study.mydemo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ScrollView;

import com.nineoldandroids.animation.ValueAnimator;

/**
 * 功能：视觉特效的ScrollView
 * Created by 佳露 on 2016/6/22.
 */

public class ParallaxScrollView extends ScrollView {
    protected ViewGroup mHeadView; // 被放大缩小的ViewGroup
    protected View mHeadChild; // 被放大缩小的View子类 -- ImageView
    protected int originalHeight; // View初始高度
    protected int maxHeight;
    protected float downY = -1; // 按下的Y坐标
    protected float deltaY = -1; // 按下和移动的Y坐标之差

    public ParallaxScrollView(Context context) {
        super(context);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置视觉特效改变的View
     *
     * @param mHeadView
     * @param maxHeight
     */
    public void setParallaxView(ViewGroup mHeadView, int maxHeight) {
        this.mHeadView = mHeadView;
        if (mHeadView != null) {
            mHeadChild = mHeadView.getChildAt(0);
            originalHeight = mHeadView.getHeight();
        }
        this.maxHeight = maxHeight;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                Log.d("getScrollY(): ", getScrollY() + "");
                if (getScrollY() != 0) {
                    deltaY = 0;
                    downY = ev.getY();
                } else {
                    // 已经是头部了，还下拉的时候，getScrollY()=0
                    float moveY = ev.getY();
                    deltaY = downY - moveY;
                    if (deltaY < 0) {
                        // 增加高度
                        updateViewHeight((int) (originalHeight - deltaY / 5));
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // 恢复HeadView
                if (getScrollY() < originalHeight) {
                    if (deltaY != 0) {
                        reset();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 更新ImageView的高度 -- 发送handler
     *
     * @param newHeight
     */
    private void updateViewHeight(int newHeight) {
        Message msg = mAnimatePullHandler.obtainMessage();
        msg.arg1 = newHeight;
        mAnimatePullHandler.sendMessage(msg);
    }

    /**
     * 具体设置图片大小
     */
    private Handler mAnimatePullHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int newHeight = msg.arg1;
            if (mHeadView != null) {
                // 设置高度
                mHeadView.getLayoutParams().height = newHeight;
                mHeadChild.getLayoutParams().height = newHeight;
                // 重新布局
                mHeadChild.requestLayout();
            }
        }
    };

    /**
     * 重置高度
     */
    private void reset() {
        int currentHeight = mHeadView.getHeight();
        // currentHeight --> originalHeight 不断改变高度
        ValueAnimator animator = ValueAnimator.ofInt(currentHeight, originalHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer animatedValue = (Integer) animation.getAnimatedValue();
                updateViewHeight(animatedValue);
            }
        });
        // 回弹效果
        animator.setInterpolator(new OvershootInterpolator(2));
        animator.setDuration(500);
        animator.start();
    }
}
