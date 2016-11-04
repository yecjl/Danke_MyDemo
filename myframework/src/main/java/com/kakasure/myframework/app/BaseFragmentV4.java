package com.kakasure.myframework.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kakasure.myframework.data.RequestManager;
import com.kakasure.myframework.view.LoadingView;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * 功能：支持V4包的 基本Fragment
 * Created by danke on 2016/1/11.
 */
public abstract class BaseFragmentV4 extends Fragment {
    protected String TAG;
    protected LoadingView loadingView;
    protected View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = super.onCreateView(inflater, container, savedInstanceState);
        if (root == null) {
            int layoutResource = getLayoutRes();
            if (layoutResource == 0) {
                return null;
            }
            root = inflater.inflate(layoutResource, container, false);
        }
        ButterKnife.bind(this, root);
        TAG = getClass().getName();
        afterInject(savedInstanceState);
        return root;
    }

    /**
     * 初始化基本数据，子类可重写这个方法
     * @param savedInstanceState
     */
    protected void afterInject(Bundle savedInstanceState) {
    }


    /**
     * 加载布局文件，子类必须重写这个方法
     * @return
     */
    protected abstract int getLayoutRes();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 结束还没有返回的网络请求
        RequestManager.cancelAll(this);
        ButterKnife.unbind(this);
    }

    /**
     * 获取Fragment的Activity的加载中视图，如需自定义可重写此方法
     * @return
     */
    public LoadingView getLoadingView() {
        return ((BaseFragmentActivity) getActivity()).getLoadingView();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，TAG为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); //统计页面，TAG为页面名称，可自定义
    }
}
