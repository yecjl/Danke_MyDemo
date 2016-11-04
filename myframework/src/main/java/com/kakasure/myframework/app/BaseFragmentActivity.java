package com.kakasure.myframework.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.kakasure.myframework.data.RequestManager;
import com.kakasure.myframework.utils.UIUtil;
import com.kakasure.myframework.view.LoadingView;

import butterknife.ButterKnife;

/**
 * 功能：基本Activity，编译MinSdk<11时继承此Activity，以实现Fragment布局支持
 * Created by danke on 2016/1/11.
 */
public abstract class BaseFragmentActivity extends FragmentActivity {
    protected String TAG;
    protected LoadingView loadingView;
    /**
     * root view
     */
    protected ViewGroup root;

    /**
     * 获取加载中视图，如需自定义可重写此方法
     *
     * @return
     */
    public LoadingView getLoadingView() {
        if (loadingView == null) {
            loadingView = UIUtil.createLoadingView(this);
        }
        return loadingView;
    }

    /**
     * Activity 创建调用的方法
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getName();
        // 将此Activity添加到Activity集合管理
        ActivityManager.onActivityCreate(this);
        // 将布局文件转换成view对象
        root = (ViewGroup) LayoutInflater.from(this).inflate(getLayoutRes(), null);
        setContentView(root);
        ButterKnife.bind(this);
        afterInject(savedInstanceState);
    }

    /**
     * Activity 销毁调用的方法
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束还没有返回的网络请求
        RequestManager.cancelAll(this);
        // 将此Activity取消Activity集合管理
        ActivityManager.onActivityDestroy(this);
    }

    /**
     * 初始化基本数据，子类可重写这个方法
     *
     * @param savedInstanceState
     */
    protected void afterInject(Bundle savedInstanceState) {
    }

    /**
     * 加载布局文件，子类必须重写这个方法
     *
     * @return
     */
    protected abstract int getLayoutRes();

    /**
     * 处理事件分发调用的方法
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 当前获取焦点的View对象
            View view = getCurrentFocus();
            if (isShouldHideInput(view, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                    hideInputAfter(ev);
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 判断是否需要隐藏软键盘输入：如果点击的是除了EditText之外的，需要隐藏键盘
     *
     * @param view
     * @param ev
     * @return 需要隐藏键盘的返回true，点击EditText区域返回false
     */
    private boolean isShouldHideInput(View view, MotionEvent ev) {
        if (view != null && (view instanceof EditText)) {
            return UIUtil.outRangeOfView(view, ev);
        }
        return false;
    }

    /**
     * 隐藏软键盘之后的处理
     */
    protected void hideInputAfter(MotionEvent ev) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
