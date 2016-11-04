package com.study.mydemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import com.nineoldandroids.view.ViewHelper;

/**
 * 功能：商品详情的ScrollView
 * Created by 佳露 on 2016/7/2.
 */

public class ProductScrollView extends ParallaxScrollView {
    private int lastTranslationY;
    private int deltaTranslationY;
    private int mHeaderHeight;
    private OnTouchEventMoveListener mOnTouchEventMoveListener;
    private static final float MAX_ALPHA = 255.00000f;
    private int mAlpha = 0; // 当前的透明度
    private int titleHeight; // 标题的高度
    private int alphaMax = 0xFF; // 255


    public ProductScrollView(Context context) {
        super(context);
        mHeaderHeight = originalHeight;
    }

    public interface OnTouchEventMoveListener {

        void onSlideUp(int mOriginalHeaderHeight, int mHeaderHeight);

        void onSlideDown(int mOriginalHeaderHeight, int mHeaderHeight);

        void onSlide(int alpha);
    }

    public void setOnTouchEventMoveListener(OnTouchEventMoveListener l) {
        mOnTouchEventMoveListener = l;
    }

    public ProductScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置视觉特效改变的View
     *
     * @param mHeadView
     * @param maxHeight
     */
    public void setParallaxView(ViewGroup mHeadView, int maxHeight, int titleHeight) {
        super.setParallaxView(mHeadView, maxHeight);
        this.titleHeight = titleHeight;
    }

    @Override
    protected void onScrollChanged(int left, int top, int oldl, int oldt) {
        super.onScrollChanged(left, top, oldl, oldt);
        if (top > originalHeight) {
            return;
        }
        float percent = animateScroll(top);
        deltaTranslationY = top - lastTranslationY;
        lastTranslationY = top;

        scrollListen(percent);
        Log.d("ScrollView top", top + ", originalHeight：" + originalHeight + ", percent：" + percent);
    }

    /**
     * 设置滑动监听
     * @param percent
     */
    private void scrollListen(float percent) {
        mHeaderHeight -= deltaTranslationY;
        if (mOnTouchEventMoveListener != null) {
            mAlpha = (int) (percent * MAX_ALPHA);
            if (deltaTranslationY < 0) {
                // 下滑
                mOnTouchEventMoveListener.onSlideDown(originalHeight, mHeaderHeight);
            } else if (deltaTranslationY > 0) {
                // 上滑
                mOnTouchEventMoveListener.onSlideUp(originalHeight, mHeaderHeight);
            }

            if (mHeaderHeight == originalHeight - titleHeight) {
                mAlpha = 0;
            }
            if (mAlpha > alphaMax) {
                mAlpha = alphaMax;
            }
            if (mAlpha < 0) {
                mAlpha = 0;
            }

            mOnTouchEventMoveListener.onSlide(mAlpha);
        }
    }

    /**
     * 上滑时Header view被吃掉的效果
     * 滑动速度比之前慢2倍即可
     *
     * @param top
     * @return header增量部分占header高度的百分比
     */
    private float animateScroll(int top) {
        float percent = (float) (top * 1.0 / (originalHeight - titleHeight));
        if (percent > 1) {
            percent = 1;
        }
        // mHeadView不断的向下移动，营造View被吃掉的效果
        ViewHelper.setTranslationY(mHeadView, top / 2);
        Log.d("ScrollView mHeadView.Y", mHeadView.getY() + "");
        return percent;
    }
}
