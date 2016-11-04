package com.study.myqlive.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * 功能：
 * Created by danke on 2016/10/31.
 */
public class TxrjTXCloudVideoView extends TXCloudVideoView {
    public int count = 0;
    public TxrjTXCloudVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("txrjsms " + count++, "=>TxrjTXCloudVideoView.onSizeChanged called! w=" + w
                + ",h=" + h + ",oldw=" + oldw + ",oldh=" + oldh);
    }
}
