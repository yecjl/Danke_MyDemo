package com.study.friendcircledemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.study.friendcircledemo.adapter.FriendLogAdapter;
import com.study.friendcircledemo.bean.FriendLogBean;
import com.study.friendcircledemo.bean.ReplyBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private Context mContext;
    private ArrayList<FriendLogBean> list;
    private ListView listView;
    private View headView;
    private EditText articleEdit;
    private RelativeLayout articleReplyBox;
    private RelativeLayout titleBox;
    private InputMethodManager inputManager;
    private FriendLogAdapter pyqAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mContext = this;
        list = new ArrayList<>();
        listView = (ListView) findViewById(R.id.circle_list);

        headView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.colleagues_circle_head_layout, null);
        listView.addHeaderView(headView);
        articleEdit = (EditText) findViewById(R.id.articleEdit);
        articleReplyBox = (RelativeLayout) findViewById(R.id.articleReplyBox);
        titleBox = (RelativeLayout) findViewById(R.id.titleBox);
        inputManager = (InputMethodManager) articleEdit.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        setData();
        pyqAdapter = new FriendLogAdapter(mContext, aHandler, list);
        listView.setAdapter(pyqAdapter);
    }

    private Handler aHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private void setData() {
        for (int i = 0; i < 20; i++) {
            FriendLogBean bean = new FriendLogBean();
            bean.setArticleId(i);
            bean.setArticleContent("这是朋友圈发的内容");
            bean.setArticleTime("01-23 18:" + i);
            bean.setArticleTitile("好友：" + i);
            if (i > 0 && i % 3 == 0) {
                List<ReplyBean> rbList = new ArrayList<>();
                ReplyBean rb = new ReplyBean();
                rb.setrUser("回复:" + i + "号");
                rb.setrCotent("  重庆欢迎你" + i);
                rbList.add(rb);
                Log.i("info", rb.getrCotent());
                bean.setReplyList(rbList);
            }
            list.add(bean);
        }
    }
}
