package com.kakasure.myframework.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * 垂直居中的ImageSpan
 *
 * @author KenChung
 */
public class VerticalImageSpan extends ImageSpan {

    public VerticalImageSpan(Drawable drawable) {
        super(drawable);
    }

    public VerticalImageSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public VerticalImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();
        int transY = bottom - drawable.getBounds().bottom;
        // 获得将要显示的文本高度-图片高度除2等居中位置+top(换行情况)
//        transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
        transY -= paint.getFontMetricsInt().descent - 3;
        // 偏移画布后开始绘制
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}