package com.kakasure.myframework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.kakasure.myframework.R;
import com.kakasure.myframework.utils.MyLog;

/**
 * 跑马灯View
 * Created by danke on 2016/10/21.
 */
public class MarqueeView extends HorizontalScrollView implements Runnable {

    private Context context;
    private LinearLayout mainLayout;//跑马灯滚动部分
    private int scrollSpeed = 5;//滚动速度
    private int scrollDirection = LEFT_TO_RIGHT;//滚动方向
    private int currentX;//当前x坐标
    private int viewMargin = 0;//View间距
    private int viewWidth;//View总宽度
    private int screenWidth;//屏幕宽度
    private boolean isLoop = false; //是否循环滚动
    private boolean isPlaying = false; //是否正在播放
    private boolean isPause = false; //是否停止

    public static final int LEFT_TO_RIGHT = 1;
    public static final int RIGHT_TO_LEFT = 2;
    private OnMarqueeListener onMarqueeListener;

    public MarqueeView(Context context) {
        this(context, null);
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    void initView() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        mainLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.scroll_content, null);
        this.addView(mainLayout);
    }

    public void addViewInQueue(View view) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(viewMargin, 0, 0, 0);
        view.setLayoutParams(lp);
        mainLayout.addView(view);
        view.measure(0, 0);//测量view
        viewWidth = viewWidth + view.getMeasuredWidth() + viewMargin;
    }

    public void removeAllView() {
        mainLayout.removeAllViews();
        viewWidth = 0;
    }

    /**
     * 开始滚动
     */
    public void startScroll() {
        currentX = (scrollDirection == LEFT_TO_RIGHT ? viewWidth : -screenWidth);
        startFromStopScroll();
    }

    /**
     * 暂停后播放
     */
    public void startFromStopScroll() {
        removeCallbacks(this);
        isPlaying = true;
        post(this);
    }

    /**
     * 停止滚动
     */
    public void stopScroll() {
        isPlaying = false;
        isPause = false;
        removeCallbacks(this);
    }

    /**
     * 暂停滚动
     */
    public void pauseScroll() {
        isPause = true;
        removeCallbacks(this);
    }

    public boolean isLoop() {
        return isLoop;
    }

    public void setLoop(boolean isLoop) {
        this.isLoop = isLoop;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean isPause) {
        this.isPause = isPause;
    }

    /**
     * 设置View间距
     *
     * @param viewMargin
     */
    public void setViewMargin(int viewMargin) {
        this.viewMargin = viewMargin;
    }

    /**
     * 设置滚动速度
     *
     * @param scrollSpeed
     */
    public void setScrollSpeed(int scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    /**
     * 设置滚动方向 默认从左向右
     *
     * @param scrollDirection
     */
    public void setScrollDirection(int scrollDirection) {
        this.scrollDirection = scrollDirection;
    }

    @Override
    public void run() {
        switch (scrollDirection) {
            case LEFT_TO_RIGHT:
                mainLayout.scrollTo(currentX, 0);
                currentX--;

                if (-currentX >= screenWidth) {
                    if (!isLoop) {
                        isPlaying = false;
                        isPause = false;
                        if (onMarqueeListener != null) {
                            onMarqueeListener.onFinished();
                        }
                        return;
                    }
                    int max = viewWidth > screenWidth ? viewWidth : screenWidth;
                    mainLayout.scrollTo(max, 0);
                    currentX = max;
                }
                break;
            case RIGHT_TO_LEFT:
                mainLayout.scrollTo(currentX, 0);
                currentX++;

                if (currentX >= viewWidth) {
                    if (!isLoop) {
                        isPlaying = false;
                        isPause = false;
                        if (onMarqueeListener != null) {
                            onMarqueeListener.onFinished();
                        }
                        return;
                    }

                    mainLayout.scrollTo(-screenWidth, 0);
                    currentX = -screenWidth;
                }
                break;
            default:
                break;
        }

        MyLog.d("currentX: " + currentX);

        postDelayed(this, 50 / scrollSpeed);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    public void setOnMarqueeListener(OnMarqueeListener onMarqueeListener) {
        this.onMarqueeListener = onMarqueeListener;
    }

    public interface OnMarqueeListener {
        void onFinished();
    }
}