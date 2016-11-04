package com.kakasure.myframework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：容器，可以根据一行宽度对里面的子view换行排版
 * Created by danke on 2016/1/21.
 */
public class MyViewGroup extends ViewGroup{

    public static final int DEFAULT_SPACING = 0;
    /** 横向间隔 */
    private int mHorizontalSpacing = DEFAULT_SPACING;
    /** 纵向间隔 */
    private int mVerticalSpacing = DEFAULT_SPACING;
    /** 是否需要布局，只用于第一次 */
    boolean mNeedLayout = true;
    /** 当前行已用的宽度，由子View宽度加上横向间隔 */
    private int mUsedWidth = 0;
    /** 代表每一行的集合 */
    private final List<Line> mLines = new ArrayList<Line>();
    private Line mLine = null;
    /** 最大的行数 */
    private int mMaxLinesCount = Integer.MAX_VALUE;
    /** 是否想要把剩余空间平均到每个View上 **/
    private boolean isSplitSpacing = true;

    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
                - getPaddingRight() - getPaddingLeft();
        // 如果父类的父类是ScrollView，就获取不到sizeHeight为0，所有还是不要用这样的方式，测量的时候measure直接传递0即可
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec)
                - getPaddingTop() - getPaddingBottom();

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        restoreLine();// 还原数据，以便重新记录
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            // sizeWidth - mHorizontalSpacing 解决孩子宽度的最大值
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(sizeWidth - mHorizontalSpacing,
                    MeasureSpec.AT_MOST);
//            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
//                    sizeHeight, MeasureSpec.AT_MOST);
            // 测量child
            child.measure(childWidthMeasureSpec, 0);

            if (mLine == null) {
                mLine = new Line();
            }
            int childWidth = child.getMeasuredWidth();
            // 增加使用的宽度, 需要加上横隔距离
            mUsedWidth += childWidth + mHorizontalSpacing;
            if (mUsedWidth <= sizeWidth) {// 使用宽度小于总宽度，该child属于这一行。
                mLine.addView(child);// 添加child

            } else {// 使用宽度大于总宽度。需要换行
                mLines.add(mLine);
                mLine = new Line();
                mUsedWidth = 0;
                // // 在新的一行，不管是否超过长度，先加上去，因为这一行一个child都没有，所以必须满足每行至少有一个child
                mLine.addView(child);
                // 增加使用的宽度, 需要加上横隔距离
                mUsedWidth += childWidth + mHorizontalSpacing;
            }
        }

        mLines.add(mLine);

        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int totalHeight = 0;
        final int linesCount = mLines.size();
        for (int i = 0; i < linesCount; i++) {// 加上所有行的高度
            totalHeight += mLines.get(i).mHeight;
        }
        totalHeight += mVerticalSpacing * (linesCount - 1);// 加上所有间隔的高度
        totalHeight += getPaddingTop() + getPaddingBottom();// 加上padding
        // 设置布局的宽高，宽度直接采用父view传递过来的最大宽度，而不用考虑子view是否填满宽度，因为该布局的特性就是填满一行后，再换行
        // 高度根据设置的模式来决定采用所有子View的高度之和还是采用父view传递过来的高度
        setMeasuredDimension(totalWidth,
                resolveSize(totalHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!mNeedLayout || changed) {// 没有发生改变就不重新布局
            mNeedLayout = false;
            int left = getPaddingLeft();// 获取最初的左上点
            int top = getPaddingTop();
            final int linesCount = mLines.size();
            for (int i = 0; i < linesCount; i++) {
                final Line oneLine = mLines.get(i);
                oneLine.layoutView(left, top);// 布局每一行
                top += oneLine.mHeight + mVerticalSpacing;// 为下一行的top赋值
            }
        }
    }

    /** 还原所有数据 */
    private void restoreLine() {
        mLines.clear();
        mLine = new Line();
        mUsedWidth = 0;
    }

    /** 新增加一行 */
    private boolean newLine() {
        mLines.add(mLine);
        if (mLines.size() < mMaxLinesCount) {
            mLine = new Line();
            mUsedWidth = 0;
            return true;
        }
        return false;
    }

    /**
     * 代表着一行，封装了一行所占高度，该行子View的集合，以及所有View的宽度总和
     */
    class Line {
        int mWidth = 0;// 该行中所有的子View累加的宽度
        int mHeight = 0;// 该行中所有的子View中高度的那个子View的高度
        List<View> views = new ArrayList<View>();

        public void addView(View view) {// 往该行中添加一个
            views.add(view);
            mWidth += view.getMeasuredWidth();
            int childHeight = view.getMeasuredHeight();
            mHeight = mHeight < childHeight ? childHeight : mHeight;// 高度等于一行中最高的View
        }

        public int getViewCount() {
            return views.size();
        }

        public void layoutView(int l, int t) {// 布局
            int left = l;
            int top = t;
            int count = getViewCount();
            // 总宽度
            int layoutWidth = getMeasuredWidth() - getPaddingLeft()
                    - getPaddingRight();
            // 剩余的宽度，是除了View和间隙的剩余空间
            int surplusWidth = layoutWidth - mWidth - mHorizontalSpacing
                    * (count - 1);
            if (surplusWidth >= 0) {// 剩余空间
                // 采用float类型数据计算后四舍五入能减少int类型计算带来的误差
                int splitSpacing = (int) (surplusWidth / count + 0.5);
                for (int i = 0; i < count; i++) {
                    final View view = views.get(i);
                    int childWidth = view.getMeasuredWidth();
                    int childHeight = view.getMeasuredHeight();
                    // 计算出每个View的顶点，是由最高的View和该View高度的差值除以2
                    int topOffset = (int) ((mHeight - childHeight) / 2.0 + 0.5);
                    if (topOffset < 0) {
                        topOffset = 0;
                    }
                    // 判断是否想要把剩余空间平均到每个View上
                    if (isSplitSpacing) {
                        childWidth = childWidth + splitSpacing;
                    }
                    view.getLayoutParams().width = childWidth;
                    if (splitSpacing > 0) {// View的长度改变了，需要重新measure
                        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                                childWidth, MeasureSpec.EXACTLY);
                        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                                childHeight, MeasureSpec.EXACTLY);
                        view.measure(widthMeasureSpec, heightMeasureSpec);
                    }
                    // 布局View
                    view.layout(left, top + topOffset, left + childWidth, top
                            + topOffset + childHeight);
                    left += childWidth + mHorizontalSpacing; // 为下一个View的left赋值
                }
            } else {
                if (count == 1) {
                    View view = views.get(0);
                    view.layout(left, top, left + view.getMeasuredWidth(), top
                            + view.getMeasuredHeight());
                } else {
                    // 走到这里来，应该是代码出问题了，目前按照逻辑来看，是不可能走到这一步
                }
            }
        }
    }

    /**
     * 设置横隔距离
     * @param mHorizontalSpacing
     */
    public void setHorizontalSpacing(int mHorizontalSpacing) {
        this.mHorizontalSpacing = mHorizontalSpacing;
    }

    /**
     * 设置纵隔距离
     * @param mVerticalSpacing
     */
    public void setVerticalSpacing(int mVerticalSpacing) {
        this.mVerticalSpacing = mVerticalSpacing;
    }
    /**
     * 判断是否想要把剩余空间平均到每个View上
     * @param isSplitSpacing
     */
    public void setSplitSpacing(boolean isSplitSpacing) {
        this.isSplitSpacing = isSplitSpacing;
    }
}
