package com.kakasure.myframework.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import com.kakasure.myframework.view.ProgressDialogAlert;

/**
 * Created by danke on 16/1/08.
 */
public class ViewUtils {

    public static void changeRoundShapeGradient(View view, int colorId, Context context) {
        GradientDrawable tv1gradient = (GradientDrawable) view.getBackground();
        tv1gradient.setColor(context.getResources().getColor(colorId));
    }

    /**
     * 显示删除对话框
     */
    public static void showMessageDialog(Context context, String message) {
        final ProgressDialogAlert delDialog = new ProgressDialogAlert(context, "", message, "确定");
        delDialog.show();

        delDialog.setClickListener(new ProgressDialogAlert.ClickListenerInterface() {

                                       @Override
                                       public void doLeft() {
                                       }

                                       @Override
                                       public void doRight() {
                                           delDialog.dismiss();
                                       }
                                   }
        );
    }
}
