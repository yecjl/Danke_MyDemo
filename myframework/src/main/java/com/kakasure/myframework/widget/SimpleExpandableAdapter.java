package com.kakasure.myframework.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.kakasure.myframework.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：只需实现getViewHolder()的适配器
 * Created by danke on 2016/1/12.
 */
public abstract class SimpleExpandableAdapter<T> extends BaseExpandableListAdapter {
    protected Context context;
    /**
     * 组布局
     */
    protected int groupLayoutId;
    /**
     * 组孩子布局
     */
    protected int childLayoutId;
    protected List<T> mList;
    private View mNoDataView;
    // 显示默认没有数据的界面
    private boolean showDefaultNoDataView;
    private boolean dataInitialized;
    private String defaultNoDataText;

    public SimpleExpandableAdapter(Context context, int groupLayoutId, int childLayoutId) {
        this.context = context;
        this.groupLayoutId = groupLayoutId;
        this.childLayoutId = childLayoutId;
        this.showDefaultNoDataView = true;
    }

    /**
     * 设置集合数据
     *
     * @param data
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

    /**
     * 获取Group视图保持器
     * @return
     */
    public abstract ViewHolder<T> getGroupViewHolder();

    /**
     * 获取Child视图保持器
     * @return
     */
    public abstract ViewHolder<T> getChildViewHolder();

    /**
     * 总共的组数
     * @return
     */
    @Override
    public int getGroupCount() {
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
     * 获取组对象
     * @param groupPosition
     * @return
     */
    @Override
    public Object getGroup(int groupPosition) {
        if (mList == null || mList.size() < 1) {
            return null;
        }
        return mList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (mList == null || mList.size() < 1) {
            return mNoDataView;
        } else if (convertView == mNoDataView) {
            convertView = null;
        }
        ViewHolder<T> holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(groupLayoutId, parent, false);
            holder = getGroupViewHolder();
            holder.onFindView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder<T>) convertView.getTag();
        }
        holder.onPositionChange(groupPosition);
        if (mList != null) {
            holder.onBindData(mList.get(groupPosition));
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (mList == null || mList.size() < 1) {
            return mNoDataView;
        } else if (convertView == mNoDataView) {
            convertView = null;
        }
        ViewHolder<T> holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(childLayoutId, parent, false);
            holder = getChildViewHolder();
            holder.onFindView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder<T>) convertView.getTag();
        }
        holder.onPositionChange(groupPosition, childPosition);
        if (mList != null) {
            holder.onBindData(mList.get(groupPosition));
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
