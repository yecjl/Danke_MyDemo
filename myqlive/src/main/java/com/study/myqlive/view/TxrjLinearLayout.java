package com.study.myqlive.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class TxrjLinearLayout extends LinearLayout {
    public int count = 0;

    public TxrjLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("txrjsms " + count++, "=>TxrjLinearLayout.onSizeChanged called! w=" + w
                + ",h=" + h + ",oldw=" + oldw + ",oldh=" + oldh);
    }
}