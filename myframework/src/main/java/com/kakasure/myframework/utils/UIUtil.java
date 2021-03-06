package com.kakasure.myframework.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.kakasure.myframework.MyFramework;
import com.kakasure.myframework.view.LoadingDialog;
import com.kakasure.myframework.view.LoadingView;

/**
 * View 工具类
 *
 * @author danke
 */
public class UIUtil {

    private static Handler handler;

    private UIUtil() {
    }

    /**
     * 从资源中获取一个Drawable对象
     *
     * @param resId 资源ID
     * @return Drawable对象
     */
    public static Drawable getDrawable(int resId) {
        return MyFramework.getAppContext().getResources().getDrawable(resId);
    }

    /**
     * 从资源中获取一个Bitmap对象
     *
     * @param resId
     * @return
     */
    public static Bitmap decodeResource(int resId) {
        TypedValue value = new TypedValue();
        Resources resources = MyFramework.getAppContext().getResources();
        resources.openRawResource(resId, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, resId, opts);
    }

    /**
     * 显示软键盘
     *
     * @param activity
     */
    public static void showSoftKeyboard(Activity activity) {
        View v = activity.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!imm.isActive()) {
                imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
            }
        }
    }

    /**
     * 显示软键盘
     */
    public static void showSoftKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示软键盘
     */
    public static void showSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 多少时间后显示软键盘
     */
    public static void showSoftKeyboard(final View view, long delayMillis) {
        // 显示输入法
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                UIUtil.showSoftKeyboard(view);
            }
        }, delayMillis);
    }

    /**
     * 显示软键盘
     *
     * @param dialog
     */
    public static void showSoftKeyboard(Dialog dialog) {
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * 隐藏软键盘
     *
     * @param dialog
     */
    public static void hideSoftKeyboard(Context context, Dialog dialog) {
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //隐藏输入法
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        View v = activity.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }

    /**
     * 获取MotionEvent的Action名称
     *
     * @param action
     * @return
     */
    public static String getAction(int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return "ACTION_DOWN";
            case MotionEvent.ACTION_CANCEL:
                return "ACTION_CANCEL";
            case MotionEvent.ACTION_HOVER_ENTER:
                return "ACTION_HOVER_ENTER";
            case MotionEvent.ACTION_HOVER_EXIT:
                return "ACTION_HOVER_EXIT";
            case MotionEvent.ACTION_HOVER_MOVE:
                return "ACTION_HOVER_MOVE";
            case MotionEvent.ACTION_MASK:
                return "ACTION_MASK";
            case MotionEvent.ACTION_MOVE:
                return "ACTION_MOVE";
            case MotionEvent.ACTION_OUTSIDE:
                return "ACTION_OUTSIDE";
            case MotionEvent.ACTION_POINTER_DOWN:
                return "ACTION_POINTER_DOWN";
            case MotionEvent.ACTION_POINTER_INDEX_MASK:
                return "ACTION_POINTER_INDEX_MASK";
            case MotionEvent.ACTION_POINTER_UP:
                return "ACTION_POINTER_UP";
            case MotionEvent.ACTION_SCROLL:
                return "ACTION_SCROLL";
            case MotionEvent.ACTION_UP:
                return "ACTION_UP";
            default:
                return "UNKNOWN_ACTION";
        }
    }

    /**
     * 创建一个“加载中”视图对象
     *
     * @param context
     * @return
     */
    public static LoadingView createLoadingView(Context context) {
        return new LoadingDialog(context);
    }

    /**
     * 发起一个在UI线程执行的任务
     *
     * @param runnable
     */
    public static void runOnUIThread(Runnable runnable) {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        handler.post(runnable);
    }

    /**
     * 发起一个在UI线程执行的任务
     *
     * @param runnable
     * @param delay
     */
    public static void runOnUIThread(Runnable runnable, int delay) {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        handler.postDelayed(runnable, delay);
    }


    public static int Dp2Px(float dp) {
        final float scale = MyFramework.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取屏幕尺寸(宽度)
     *
     * @return
     */
    public static DisplayMetrics getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) MyFramework.getAppContext()
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 显示密码
     *
     * @param editText
     */
    public static void showPassWord(EditText editText) {
        editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }

    /**
     * 隐藏密码
     *
     * @param editText
     */
    public static void hidePassWord(EditText editText) {
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    /**
     * 动态计算图片的宽度
     *
     * @param context
     * @param spanCount 一行的个数
     * @param spacing   图片的边距
     * @return
     */
    public static int operateImageSize(Context context, int spanCount, int spacing) {
        return (getScreenWidth(context) - spacing * (spanCount + 1)) / spanCount;
    }

    /**
     * 判断点击的位置是不是 不在 指定的view上
     *
     * @param view
     * @param ev
     * @return false: 在  true:在
     */
    public static boolean outRangeOfView(View view, MotionEvent ev) {
        /**
         * 获取输入框当前的location位置，保存到leftTop数组中
         */
        int[] leftTop = {0, 0};
        view.getLocationInWindow(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int right = left + view.getWidth();
        int bottom = top + view.getHeight();
        float evX = ev.getX();
        float evY = ev.getY();
        // 点击的是输入框区域，保留点击事件
        if (evX > left && evX < right && evY > top && evY < bottom) {
            return false;
        } else {
            return true;
        }
    }
}
