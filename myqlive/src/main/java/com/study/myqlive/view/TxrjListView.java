package com.study.myqlive.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

public class TxrjListView extends ListView {
    public int count = 0;

    public TxrjListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("txrjsms " + count++, "=>TxrjListView.onSizeChanged called! w=" + w
                + ",h=" + h + ",oldw=" + oldw + ",oldh=" + oldh);
//        setVisibility(h <= 0 ? View.INVISIBLE : View.VISIBLE); // 高度小于等于0不可见，高度大于0可见。
    }

}