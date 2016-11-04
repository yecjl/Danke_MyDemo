package com.kakasure.myframework.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.kakasure.myframework.R;

/**
 * 加载中Dialog， 可以以后设置的， 现在设置和自定义的ProgressDialog一样，所以展示不扩展这个类
 *
 * @author danke
 */
public class LoadingDialog extends Dialog implements LoadingView {

    private AnimationDrawable spinner;

    public LoadingDialog(Context context) {
        super(context, R.style.ProgressDialog);
        setContentView(R.layout.progress_dialog);
        findViewById(R.id.message).setVisibility(View.GONE);
        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        spinner = (AnimationDrawable) imageView.getBackground();

        // 设置是否可以取消
        setCancelable(true);
        // 获取布局参数
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        // 设置居中
        layoutParams.gravity = Gravity.CENTER;
        // 设置黑暗度，在0.0f和1.0f之间，0.0f完全不暗，1.0f全暗
        layoutParams.dimAmount = 0.2f;
        // 重新设置布局
        getWindow().setAttributes(layoutParams);
    }

//    /**
//     * 焦点改变的时候被调用，开启动画
//     * @param hasFocus
//     */
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
//        spinner = (AnimationDrawable) imageView.getBackground();
//        spinner.start();
//    }

    @Override
    public void showLoadingView() {
        spinner.start();
        super.show();
    }

    @Override
    public void hideLoadingView() {
        if (spinner != null) {
            spinner.stop();
        }
        super.dismiss();
    }

    @Override
    public void errorLoadingView() {
        if (spinner != null) {
            spinner.stop();
        }
        super.dismiss();
    }
}
