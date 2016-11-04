package com.kakasure.myframework.view;

import android.animation.Animator;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kakasure.myframework.R;

/**
 * 功能：从底部滑上来的popup
 * Created by danke on 2016/1/19.
 */
public abstract class SlideFromBottomPopup extends BasePopupWindow {
    private View popupView;
    private View view;
    private int height;
    private LinearLayout popup_anima;
    private View dismissView;

    public SlideFromBottomPopup(Activity context) {
        super(context);
    }

    @Override
    public View getPopupView() {
        popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_slide_from_bottom, null);
        popup_anima = (LinearLayout) popupView.findViewById(R.id.popup_anima);
        dismissView = popupView.findViewById(R.id.click_to_dismiss);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) popup_anima.getLayoutParams();
        layoutParams.height = (int) (mContext.getWindowManager().getDefaultDisplay().getHeight() * 0.6);
        popup_anima.setLayoutParams(layoutParams);
        popup_anima.removeAllViews();
        view = setChildView(popup_anima);
        if (view != null) {
            popup_anima.addView(view);
        }
        return popupView;
    }

    @Override
    public View getAnimaView() {
        return popup_anima;
    }

    @Override
    public View getDismissView() {
        return dismissView;
    }

    @Override
    public View getInputView() {
        return null;
    }
    @Override
    public Animation getAnimation() {
        return getTranslateAnimation(250 * 2, 0, 200);
    }

    @Override
    public Animator getAnimator() {
        return null;
    }

    /**
     * 设置自定义子孩子
     * 注意： 这个方法在父类初始化的时候执行的，而不是子类，所以定义在成员变量上的值都为空
     *
     * @return
     */
    public abstract View setChildView(ViewGroup mPopupView);
}
