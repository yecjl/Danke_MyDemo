package com.study.bluetooth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：只需实现getViewHolder()的适配器
 * Created by danke on 2016/1/12.
 */
public abstract class SimpleAdapter<T> extends BaseAdapter {
    protected Context context;
    protected int layoutId;
    protected List<T> mList;
    protected View mNoDataView;
    // 显示默认没有数据的界面
    private boolean showDefaultNoDataView;
    protected boolean dataInitialized;
    private String defaultNoDataText;

    public SimpleAdapter(Context context) {
        this.context = context;
    }

    public SimpleAdapter(Context context, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
        this.showDefaultNoDataView = true;
    }

    /**
     * 获取条目
     *
     * @return
     */
    @Override
    public int getCount() {
        if (mList == null || mList.size() < 1) {
            if (mNoDataView == null && showDefaultNoDataView) {
                mNoDataView = getDefaultNoDataView();
            }
            if (dataInitialized && mNoDataView != null) {
                return 1;
            }
            return 0;
        }
        return mList.size();
    }

    /**
     * 获取单条数据
     *
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        if (mList == null || mList.size() < 1) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 获取条目显示的View
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mList == null || mList.size() < 1) {
            return mNoDataView;
        } else if (convertView == mNoDataView) {
            convertView = null;
        }
        ViewHolder<T> holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            holder = getViewHolder();
            holder.onFindView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder<T>) convertView.getTag();
        }
        holder.onPositionChange(position);
        if (mList != null) {
            holder.onBindData(mList.get(position));
        }

        return convertView;
    }

    /**
     * 获取视图保持器
     *
     * @return
     */
    public abstract ViewHolder<T> getViewHolder();

    /**
     * 传入数据集合
     *
     * @param data 集合数据
     */
    public void setList(List<T> data) {
        if (data != null) {
            this.mList = data;
        }
        dataInitialized = true;
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
            setList(data);
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
     * 当没有数据时，显示没有数据提示（显示在第一个item的位置）。
     *
     * @param noDataView
     */
    public void setNoDataView(View noDataView) {
        this.mNoDataView = noDataView;
    }

    public View getNoDataView() {
        return mNoDataView;
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public Context getContext() {
        return context;
    }

    /**
     * 设置默认无数据提示文本
     *
     * @param defaultNoDataText
     */
    public void setDefaultNoDataText(String defaultNoDataText) {
        this.defaultNoDataText = defaultNoDataText;
    }

    /**
     * 显示默认无数据视图(默认true)
     *
     * @param showDefaultNoDataView
     */
    public void showDefaultNoDataView(boolean showDefaultNoDataView) {
        this.showDefaultNoDataView = showDefaultNoDataView;
    }

    /**
     * 初始化默认无数据视图
     *
     * @return
     */
    private View getDefaultNoDataView() {
        View view = LayoutInflater.from(context).inflate(R.layout.my_no_data, null);
        if (defaultNoDataText != null) {
            ((TextView) view.findViewById(R.id.my_no_data_text)).setText(defaultNoDataText);
        }
        return view;
    }
}
