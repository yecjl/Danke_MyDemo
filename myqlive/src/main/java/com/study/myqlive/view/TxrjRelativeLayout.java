package com.study.myqlive.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

public class TxrjRelativeLayout extends RelativeLayout {
    public int count = 0;

    public TxrjRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("txrjsms " + count++, "=>TxrjRelativeLayout.onSizeChanged called! w=" + w
                + ",h=" + h + ",oldw=" + oldw + ",oldh=" + oldh);
    }
}