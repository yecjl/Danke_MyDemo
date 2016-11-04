package com.kakasure.myframework.view;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.widget.TextView;

import com.kakasure.myframework.utils.MyLogUtils;
import com.kakasure.myframework.utils.UIUtil;

/**
 * 功能：计算line高度的TextView
 * Created by danke on 2016/5/24.
 */
public class TextViewOperateHeight extends TextView {
    private Context context;

    public TextViewOperateHeight(Context context) {
        super(context);
        this.context = context;
    }

    public TextViewOperateHeight(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public TextViewOperateHeight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Layout layout = getLayout();
        if (layout != null) {
            int height = (int) Math.ceil(getMaxLineHeight(this.getText()
                    .toString()))
                    + getCompoundPaddingTop()
                    + getCompoundPaddingBottom();
            int width = getMeasuredWidth();
            setMeasuredDimension(width, height);
        }
    }

    private float getMaxLineHeight(String str) {
        float height = 0.0f;
        float screenW = UIUtil.getScreenWidth(context);
        float paddingLeft = this.getPaddingLeft(); // ((LinearLayout) this.getParent()).getPaddingLeft(); 获取的值为0
        float paddingReft = this.getPaddingRight();
        // 这里具体this.getPaint()要注意使用，要看你的TextView在什么位置，这个是拿TextView父控件的Padding的，为了更准确的算出换行
        int line = (int) Math.ceil((this.getPaint().measureText(str) / (screenW
                - paddingLeft - paddingReft)));
        MyLogUtils.d("line: " + line);
        height = (this.getPaint().getFontMetrics().descent - this.getPaint()
                .getFontMetrics().ascent) * line; // 如果TextView设置了行高，这个计算就不准确了，没有找到比较好的计算，this.getLineSpacingExtra()
        return height;
    }

}