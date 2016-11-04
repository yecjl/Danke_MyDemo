package com.kakasure.myframework.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.kakasure.myframework.R;
import com.kakasure.myframework.utils.UIUtil;

/**
 * 功能：自定义虚线
 * Created by danke on 2016/5/4.
 */
public class DashedLineView extends View {
    private Paint paint = null;
    private Path path = null;
    private PathEffect pe = null;

    public DashedLineView(Context context) {
        this(context, null);
    }

    public DashedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //通过R.styleable.dashedline获得我们在attrs.xml中定义的TypedArray
        //<declare-styleable name="dashedline">
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.dashedline);
        //我们在attrs.xml中<declare-styleable name="dashedline">节点下
        //添加了<attr name="lineColor" format="color" />
        //表示这个属性名为lineColor类型为color。当用户在布局文件中对它有设定值时
        //可通过TypedArray获得它的值当用户无设置值是采用默认值0XFF00000
        int lineColor = a.getColor(R.styleable.dashedline_lineColor, 0XFF000000);
        a.recycle();
        this.paint = new Paint();
        this.path = new Path();
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setColor(lineColor);
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(UIUtil.Dp2Px(getContext(), 6.0F));
        float[] arrayOfFloat = new float[2];
        arrayOfFloat[0] = UIUtil.Dp2Px(getContext(), 6.0F);
        arrayOfFloat[1] = UIUtil.Dp2Px(getContext(), 6.0F);
        this.pe = new DashPathEffect(arrayOfFloat, UIUtil.Dp2Px(getContext(), 1.0F)); //设置虚线的间隔和点的长度
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        this.path.moveTo(0.0F, 0.0F); //起始坐标
        this.path.lineTo(getMeasuredWidth(), 0.0F); //终点坐标
        this.paint.setPathEffect(this.pe);
        canvas.drawPath(this.path, this.paint);
    }
}
