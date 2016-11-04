package com.kakasure.myframework.widget;

import android.view.View;

/**
 * 功能：适配器视图保持器
 * Created by danke on 2016/1/12.
 *
 * @param <T>
 */
public abstract class ViewHolder<T> {
    protected int position;
    protected int childPosition;

    /**
     * 保持器的位置发生变化时，使用此Holder对象的Adapter将自动调用此方法
     *
     * @param position
     */
    public void onPositionChange(int position) {
        onPositionChange(position, 0);
    }


    /**
     * 如果有子孩子的话。
     * 保持器的位置发生变化时，使用此Holder对象的Adapter将自动调用此方法
     * @param position
     * @param childPosition
     */
    public void onPositionChange(int position, int childPosition) {
        this.position = position;
        this.childPosition = childPosition;
    }

    /**
     * 需要加载子控件时，使用此Holder对象的Adapter将自动调用此方法
     *
     * @param root 根布局
     */
    public abstract void onFindView(View root);

    /**
     * 加载布局中控件的数据时，使用此Holder对象的Adapter将自动调用此方法绑定控件数据
     *
     * @param data
     */
    public abstract void onBindData(T data);
}
