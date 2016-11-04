package com.kakasure.myframework.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：RecyclerView的adapter
 * 只需实现getViewHolder()的适配器
 * Created by danke on 2016/5/17.
 */
public abstract class SimpleRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Context context;
    protected int layoutId;
    protected List<T> mList;

    public SimpleRecyclerAdapter(Context context) {
        this(context, 0);
    }

    public SimpleRecyclerAdapter(Context context, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return getViewHolder(view);
    }

    /**
     * 获取视图保持器
     *
     * @return
     */
    public abstract RecyclerViewHolder<T> getViewHolder(View view);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
        recyclerViewHolder.onPositionChange(position);
        if (mList != null) {
            recyclerViewHolder.onBindData(mList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        }
        return 0;
    }

    /**
     * 传入数据集合
     *
     * @param data 集合数据
     */
    public void setList(List<T> data) {
        if (data != null) {
            this.mList = data;
        }
        notifyDataSetChanged();
    }

    /**
     * 传入数据集合，如果传递的是数组，转换为集合数据
     *
     * @param data 数组
     */
    public void setList(T[] data) {
        if (data != null) {
            List<T> list = new ArrayList<>();
            for (T d : data) {
                if (d != null) {
                    list.add(d);
                }
            }
            setList(list);
        }
    }

    /**
     * 获取数据集合
     *
     * @return
     */
    public List<T> getList() {
        return mList;
    }

    /**
     * 清空所有数据
     */
    public void clear() {
        if (mList != null && !mList.isEmpty()) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public Context getContext() {
        return context;
    }
}
