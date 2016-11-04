package com.kakasure.myframework.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kakasure.myframework.R;
import com.kakasure.myframework.utils.UIUtil;

/**
 * 自定义Toast
 *
 * @author danke
 */
public class MyToast {

    private static Context context;
    private static Toast toast;
    private static Holder holder;

    public static void hide() {
        if (toast != null)
            toast.cancel();
    }

    public static void init(Context context) {
        MyToast.context = context;
        holder = new Holder();
    }

    /**
     * 在底部显示一个短（时间）Toast
     *
     * @param msg
     */
    public static void showBottom(final String msg) {
        UIUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                show(msg, 0, false);
            }
        });
    }

    /**
     * 在底部显示一个长（时间）Toast
     *
     * @param msg
     */
    public static void showBottomL(final String msg) {
        UIUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                show(msg, 1, false);
            }
        });
    }

    /**
     * 在中部显示一个短（时间）Toast
     *
     * @param msg
     */
    public static void showMiddle(final String msg) {
        UIUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                show(msg, 2, false);
            }
        });
    }

    /**
     * 在中部显示一个长（时间）Toast
     *
     * @param msg
     */
    public static void showMiddleL(final String msg) {
        UIUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                show(msg, 3, false);
            }
        });
    }

    /**
     * 在底部显示一个短（时间）Toast
     *
     * @param msg
     */
    public static void errorBottom(final String msg) {
        UIUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                show(msg, 4, true);
            }
        });
    }

    /**
     * 在底部显示一个长（时间）Toast
     *
     * @param msg
     */
    public static void errorBottomL(final String msg) {
        UIUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                show(msg, 5, true);
            }
        });
    }

    /**
     * 在中部显示一个短（时间）Toast
     *
     * @param msg
     */
    public static void errorMiddle(final String msg) {
        UIUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                show(msg, 6, true);
            }
        });
    }

    /**
     * 在中部显示一个长（时间）Toast
     *
     * @param msg
     */
    public static void errorMiddleL(final String msg) {
        UIUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                show(msg, 7, true);
            }
        });
    }

    /**
     * 显示Toast
     *
     * @param msg
     * @param type
     * @param errorStyle
     */
    @SuppressLint("InflateParams")
    private static void show(String msg, int type, boolean errorStyle) {
//        if (toast != null) {
//            hide();
//        }

        if (toast == null) {
            toast = new Toast(context);
        }
        toast.setView(holder.root);
        holder.text.setText(msg);
        holder.errorStyle(errorStyle);
        if (type % 4 >= 2) {
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.setDuration(type % 2 == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Holder查找id
     */
    static class Holder {
        View root;
        TextView text;

        public Holder() {
            root = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
            text = (TextView) root.findViewById(R.id.my_toast_text);
        }

        public void errorStyle(boolean errorStyle) {
            if (errorStyle) {
                root.setBackgroundResource(R.drawable.toast_error_bg);
            } else {
                root.setBackgroundResource(R.drawable.toast_black_bg);
            }
        }
    }

    /**
     * 显示Toast
     * @param context
     * @param msg
     */
    public static void showToast(Context context, String msg) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.my_toast,
                null);
        TextView tvMsg = (TextView) layout.findViewById(R.id.my_toast_text);
        tvMsg.setText(msg);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
