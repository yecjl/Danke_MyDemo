package com.study.friendcircledemo.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.study.friendcircledemo.R;
import com.study.friendcircledemo.bean.FriendLogBean;
import com.study.friendcircledemo.view.MyWrapListView;

import java.util.ArrayList;

/**
 * 功能：
 * Created by danke on 2016/8/12.
 */
public class FriendLogAdapter extends BaseAdapter {

    private Context mContext;
    private Handler aHandler;
    private ArrayList<FriendLogBean> list;

    public FriendLogAdapter(Context mContext, Handler aHandler, ArrayList<FriendLogBean> list) {
        this.mContext = mContext;
        this.aHandler = aHandler;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.colleagues_circle_item, null);
            holderView = new HolderView();
            holderView.circle_comment = (ImageView) convertView
                    .findViewById(R.id.circle_comment);
            holderView.circleImg = (ImageView) convertView
                    .findViewById(R.id.circle_img);
            holderView.circleTitle = (TextView) convertView
                    .findViewById(R.id.circle_title);
            holderView.circleContent = (TextView) convertView
                    .findViewById(R.id.circle_content);
            holderView.circleTime = (TextView) convertView
                    .findViewById(R.id.circle_time);
            holderView.logImageBox = (LinearLayout) convertView
                    .findViewById(R.id.logImageBox);

            holderView.commentBox = (LinearLayout) convertView
                    .findViewById(R.id.commentBox);
            holderView.circle_comment_list = (MyWrapListView) convertView
                    .findViewById(R.id.circle_comment_list);
            holderView.listAdapter = new ReplyAdapter(mContext);
            holderView.circle_comment_list.setAdapter(holderView.listAdapter);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }

        FriendLogBean pyqBean = list.get(position);

        holderView.circleContent.setText(pyqBean.getArticleContent());
        holderView.circleTime.setText(pyqBean.getArticleTime());
        holderView.circleTitle.setText(pyqBean.getArticleTitile());
        final int mPosition = position;
        return convertView;
    }

    static class HolderView {
        ImageView circleImg, circle_comment;
        TextView circleTitle;
        TextView circleContent;
        TextView circleTime;
        LinearLayout logImageBox;
        LinearLayout commentBox;
        ReplyAdapter listAdapter;
        MyWrapListView circle_comment_list;
    }
}
