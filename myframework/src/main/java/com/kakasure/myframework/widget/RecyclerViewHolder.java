package com.kakasure.myframework.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 功能：适配器视图保持器
 * Created by danke on 2016/1/12.
 *
 * @param <T>
 */
public abstract class RecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    protected int position;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * 保持器的位置发生变化时，使用此Holder对象的Adapter将自动调用此方法
     *
     * @param position
     */
    public void onPositionChange(int position) {
        this.position = position;
    }

    /**
     * 加载布局中控件的数据时，使用此Holder对象的Adapter将自动调用此方法绑定控件数据
     *
     * @param data
     */
    public abstract void onBindData(T data);
}
