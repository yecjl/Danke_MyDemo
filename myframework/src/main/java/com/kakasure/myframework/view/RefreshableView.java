package com.kakasure.myframework.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kakasure.myframework.R;
/**
 * 可进行下拉刷新的自定义控件。
 * 用于商品详情
 *
 * @author danke
 */
public class RefreshableView extends RelativeLayout implements OnTouchListener {

    /**
     * 下拉状态
     */
    public static final int STATUS_PULL_TO_REFRESH = 0;

    /**
     * 释放立即刷新状态
     */
    public static final int STATUS_RELEASE_TO_REFRESH = 1;

    /**
     * 正在刷新状态
     */
    public static final int STATUS_REFRESHING = 2;

    /**
     * 刷新完成或未刷新状态
     */
    public static final int STATUS_REFRESH_FINISHED = 3;

    /**
     * 下拉头部回滚的速度
     */
    public static final int SCROLL_SPEED = -20;

    /**
     * 一分钟的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_MINUTE = 60 * 1000;

    /**
     * 一小时的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_HOUR = 60 * ONE_MINUTE;

    /**
     * 一天的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_DAY = 24 * ONE_HOUR;

    /**
     * 一月的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_MONTH = 30 * ONE_DAY;

    /**
     * 一年的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_YEAR = 12 * ONE_MONTH;

    /**
     * 上次更新时间的字符串常量，用于作为SharedPreferences的键值
     */
    private static final String UPDATED_AT = "updated_at";

    /**
     * 下拉刷新的回调接口
     */
    private PullToRefreshListener mListener;

    /**
     * 下拉头的View
     */
    private View header;

    /**
     * 需要去下拉刷新的ListView
     */
    private ViewGroup viewGroup;

    /**
     * 指示下拉和释放的箭头
     */
    private ImageView arrow;

    /**
     * 指示下拉和释放的文字描述
     */
    private TextView description;

    /**
     * 指示下拉和释放的箭头的背景
     */
    private Drawable arrowBgDrawable;

    /**
     * 指示下拉和释放的文字描述的背景
     */
    private Drawable descBgDrawable;

    /**
     * 下拉头的布局参数
     */
    private MarginLayoutParams headerLayoutParams;

    /**
     * 内容的布局参数
     */
    private MarginLayoutParams viewGroupLayoutParams;

    /**
     * 下拉头的高度
     */
    private int headerHeight;

    /**
     * 下拉头的显示位置
     */
    private int mTopMargin;

    /**
     * 当前处理什么状态，可选值有STATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
     * STATUS_REFRESHING 和 STATUS_REFRESH_FINISHED
     */
    private int currentStatus = STATUS_REFRESH_FINISHED;
    ;

    /**
     * 记录上一次的状态是什么，避免进行重复操作
     */
    private int lastStatus = currentStatus;

    /**
     * 手指按下时的屏幕纵坐标
     */
    private float downY;

    /**
     * 在被判定为滚动之前用户手指可以移动的最大值。
     */
    private int touchSlop;

    /**
     * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
     */
    private boolean loadOnce;

    /**
     * 当前是否可以下拉，只有ListView滚动到头的时候才允许下拉
     */
    private boolean ableToPull;

    /**
     * 最大的透明度
     */
    private int alphaMax = 0xFF;
    private int mAlpha;

    /**
     * 下拉刷新控件的构造函数，会在运行时动态添加一个下拉头的布局。
     *
     * @param context
     * @param attrs
     */
    public RefreshableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置刷新功能的HeadView
     *
     * @param header
     */
    public void setHeadView(View header) {
        this.header = header;
        arrow = (ImageView) header.findViewById(R.id.arrow);
        description = (TextView) header.findViewById(R.id.description);
        arrowBgDrawable = arrow.getBackground();
        arrowBgDrawable.setAlpha(0);
        description.setTextColor(Color.TRANSPARENT);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        // 进行一些关键性的初始化操作，比如：将下拉头向上偏移进行隐藏，给ViewGroup注册touch事件。
        if (!loadOnce) {
            headerHeight = header.getHeight();
            mTopMargin = 12;
            headerLayoutParams = (MarginLayoutParams) header.getLayoutParams();
            headerLayoutParams.topMargin = mTopMargin;
            viewGroup = (ViewGroup) getChildAt(0);
            viewGroupLayoutParams = ((MarginLayoutParams) viewGroup.getLayoutParams());
            viewGroup.setOnTouchListener(this);
            loadOnce = true;
        }
    }

    /**
     * 当ListView被触摸时调用，其中处理了各种下拉刷新的具体逻辑。
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        setIsAbleToPull(event);
        if (ableToPull) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float yMove = event.getRawY();
                    int distance = (int) (yMove - downY);
                    // 如果手指是下滑状态，并且下拉头是完全隐藏的，就屏蔽下拉事件
                    if (distance <= 0 && viewGroupLayoutParams.topMargin <= mTopMargin) {
                        return false;
                    }
                    if (distance < touchSlop) {
                        return false;
                    }
                    // 下拉设置
                    if (currentStatus != STATUS_REFRESHING) {
                        if (viewGroupLayoutParams.topMargin > mTopMargin + headerHeight) {
                            // 释放立即刷新状态
                            currentStatus = STATUS_RELEASE_TO_REFRESH;
                        } else {
                            // 下拉状态
                            currentStatus = STATUS_PULL_TO_REFRESH;
                        }
                        /***
                         * 通过偏移下拉头的topMargin值，来实现下拉效果
                         * 以下操作是为了让header滑动平稳
                         */
                        // 记录上一次ViewGroup的margin值 -- 释放立即刷新状态
                        int topMarginTemp = 0;
                        if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
                            topMarginTemp = viewGroupLayoutParams.topMargin;
                        }
                        // 设置这一次ViewGroup的margin值
                        viewGroupLayoutParams.topMargin = distance / 3;
                        viewGroup.setLayoutParams(viewGroupLayoutParams);

                        // 计算ViewGroup的margin之差，设置给head -- 释放立即刷新状态
                        if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
                            headerLayoutParams.topMargin += (viewGroupLayoutParams.topMargin - topMarginTemp) / 2;
                            if (headerLayoutParams.topMargin < mTopMargin) {
                                headerLayoutParams.topMargin = mTopMargin;
                            }
                            header.setLayoutParams(headerLayoutParams);
                        }

                        setHeadViewAlpha();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
                        // 松手时如果是释放立即刷新状态，就去调用正在刷新的任务
                        refreshingTask();
                    } else if (currentStatus == STATUS_PULL_TO_REFRESH) {
                        // 松手时如果是下拉状态，就去调用隐藏下拉头的任务
                        hideHeaderTask();
                    }
                    break;
            }
            // 时刻记得更新下拉头中的信息
            if (currentStatus == STATUS_PULL_TO_REFRESH
                    || currentStatus == STATUS_RELEASE_TO_REFRESH) {
                updateHeaderView();
                // 当前正处于下拉或释放状态，要让ViewGroup失去焦点，否则被点击的那一项会一直处于选中状态
                viewGroup.setPressed(false);
                viewGroup.setFocusable(false);
                viewGroup.setFocusableInTouchMode(false);
                lastStatus = currentStatus;
                // 当前正处于下拉或释放状态，通过返回true屏蔽掉ViewGroup的滚动事件
                return true;
            }
        }
        return false;
    }

    /**
     * 设置HeadView 的透明度
     */
    private void setHeadViewAlpha() {
        int topMargin = viewGroupLayoutParams.topMargin;
        int alphaHeight = headerHeight;
        if (topMargin > alphaHeight) {
            mAlpha = alphaMax;
            arrowBgDrawable.setAlpha(alphaMax);
            description.setTextColor(Color.argb(alphaMax, 63, 57, 51));
        } else {
            mAlpha = (int) (topMargin * 1.0 / alphaHeight * alphaMax);
            arrowBgDrawable.setAlpha(mAlpha);
            description.setTextColor(Color.argb(mAlpha, 63, 57, 51));
            if (topMargin <= 0) {
                arrowBgDrawable.setAlpha(0);
                description.setTextColor(Color.argb(0, 63, 57, 51));
            }
        }
    }

    /**
     * 根据当前ListView的滚动状态来设定 {@link #ableToPull}
     * 的值，每次都需要在onTouch中第一个执行，这样可以判断出当前应该是滚动ListView，还是应该进行下拉。
     *
     * @param event
     */
    private void setIsAbleToPull(MotionEvent event) {
        if (viewGroup.getScrollY() == 0) {
            if (!ableToPull) {
                downY = event.getRawY();
            }
            // 如果首个元素的上边缘，距离父布局值为0，就说明ViewGroup滚动到了最顶部，此时应该允许下拉刷新
            ableToPull = true;
        } else {
            if (headerLayoutParams.topMargin != mTopMargin) {
                headerLayoutParams.topMargin = mTopMargin;
                header.setLayoutParams(headerLayoutParams);
            }
            if (viewGroupLayoutParams.topMargin != 0) {
                viewGroupLayoutParams.topMargin = 0;
                viewGroup.setLayoutParams(viewGroupLayoutParams);
            }
            if (mAlpha != 0) {
                mAlpha = 0;
                arrowBgDrawable.setAlpha(0);
                description.setTextColor(Color.argb(0, 63, 57, 51));
            }
            ableToPull = false;
        }
    }

    /**
     * 更新下拉头中的信息。
     * 如果状态不同的话，需要更新
     */
    private void updateHeaderView() {
        if (lastStatus != currentStatus) {
            if (currentStatus == STATUS_PULL_TO_REFRESH) {
                description.setText(getResources().getString(R.string.pull_to_down));
                arrow.setVisibility(View.VISIBLE);
                rotateArrow();
            } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
                description.setText(getResources().getString(R.string.pull_to_release));
                arrow.setVisibility(View.VISIBLE);
                rotateArrow();
            }
        }
    }

    /**
     * 根据当前的状态来旋转箭头。
     */
    private void rotateArrow() {
        float pivotX = arrow.getWidth() / 2f;
        float pivotY = arrow.getHeight() / 2f;
        float fromDegrees = 0f;
        float toDegrees = 0f;
        if (currentStatus == STATUS_PULL_TO_REFRESH) {
            fromDegrees = 180f;
            toDegrees = 360f;
        } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
            fromDegrees = 0f;
            toDegrees = 180f;
        }
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
        animation.setDuration(200);
        animation.setFillAfter(true);
        arrow.startAnimation(animation);
    }

    /**
     * 正在刷新的任务，在此任务中会去回调注册进来的下拉刷新监听器。
     *
     * @author guolin
     */
    private void refreshingTask() {
        updateHeaderView();
        currentStatus = STATUS_REFRESHING;
        setResetAnimator();
        if (mListener != null) {
            mListener.onRefresh();
        }
    }


    /**
     * 隐藏下拉头的任务，当未进行下拉刷新或下拉刷新完成后，此任务将会使下拉头重新隐藏。
     *
     * @author guolin
     */
    private void hideHeaderTask() {
        currentStatus = STATUS_REFRESH_FINISHED;
        setResetAnimator();
    }

    /**
     * 设置HeadView 重置动画
     */
    private void setResetAnimator() {
        int topMargin = viewGroupLayoutParams.topMargin;

        ValueAnimator animator = ValueAnimator.ofInt(topMargin, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer animatedValue = (Integer) animation.getAnimatedValue();
                // 分度值是动画执行的百分比。区别于AnimatedValue。
                float animatedFraction = animation.getAnimatedFraction();
                headerLayoutParams.topMargin -= (int) ((headerLayoutParams.topMargin - mTopMargin) * animatedFraction);
                header.setLayoutParams(headerLayoutParams);
                viewGroupLayoutParams.topMargin = animatedValue;
                viewGroup.setLayoutParams(viewGroupLayoutParams);

                int alpha = (int) (mAlpha - mAlpha * animatedFraction);
                arrowBgDrawable.setAlpha(alpha);
                description.setTextColor(Color.argb(alpha, 63, 57, 51));
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    /**
     * 当所有的刷新逻辑完成后，记录调用一下，否则你的ListView将一直处于正在刷新状态。
     */
    public void finishRefreshing() {
        currentStatus = STATUS_REFRESH_FINISHED;
        hideHeaderTask();
    }

    /**
     * 给下拉刷新控件注册一个监听器。
     *
     * @param listener 监听器的实现。
     */
    public void setOnRefreshListener(PullToRefreshListener listener) {
        mListener = listener;
    }

    /**
     * 下拉刷新的监听器，使用下拉刷新的地方应该注册此监听器来获取刷新回调。
     *
     * @author guolin
     */
    public interface PullToRefreshListener {

        /**
         * 刷新时会去回调此方法，在方法内编写具体的刷新逻辑。注意此方法是在子线程中调用的， 你可以不必另开线程来进行耗时操作。
         */
        void onRefresh();

    }
}
