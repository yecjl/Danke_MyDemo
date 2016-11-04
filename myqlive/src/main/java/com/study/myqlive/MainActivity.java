package com.study.myqlive;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kakasure.myframework.app.BaseFragmentActivity;
import com.kakasure.myframework.utils.MyLog;
import com.kakasure.myframework.view.MyToast;
import com.study.myqlive.model.CurLiveInfo;
import com.study.myqlive.presenters.LoginHelper;
import com.study.myqlive.presenters.viewinface.LoginView;
import com.study.myqlive.view.LiveQIMPublicActivity;
import com.tencent.rtmp.TXLivePusher;

import butterknife.Bind;

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener, LoginView {

    @Bind(R.id.btn_zhibo)
    Button btnZhibo;
    @Bind(R.id.btn_zhubo)
    Button btnZhubo;
    @Bind(R.id.btn_zhibo_im)
    Button btnZhiboIm;
    @Bind(R.id.btn_zhubo_im)
    Button btnZhuboIm;
    private LoginHelper mLoginHelper;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void afterInject(Bundle savedInstanceState) {
        super.afterInject(savedInstanceState);
        int[] sdkver = TXLivePusher.getSDKVersion();
        if (sdkver != null && sdkver.length >= 3) {
            MyLog.d("rtmpsdk" + "rtmp sdk version is:" + sdkver[0] + "." + sdkver[1] + "." + sdkver[2]);
        }

        btnZhibo.setOnClickListener(this);
        btnZhubo.setOnClickListener(this);
        btnZhiboIm.setOnClickListener(this);
        btnZhuboIm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_zhibo: // 观看直播
                LiveQActivity.start(this, "rtmp://4894.liveplay.myqcloud.com/live/4894_20d265e4976611e69776e435c87f075e");
                break;
            case R.id.btn_zhubo: // 主播
                LiveQPublicActivity.start(this, "rtmp://4894.livepush.myqcloud.com/live/4894_20d265e4976611e69776e435c87f075e?bizid=4894");
                break;
            case R.id.btn_zhibo_im: // 观看直播 IM
                mLoginHelper = new LoginHelper(this, MainActivity.this);
                mLoginHelper.imLogin("13456789190", "eJxlzkFPgzAUwPE7n6LhitGW0nV4M2yawaZszi2cCIwWaifraMecxu*uokaM7-r-5b33agEA7OX0-jzbbHaH2qTmpJgNLoEN7bPfqJQo0sykuCn*RfasRMPSjBvWdBERQlwI*0YUrDaCix*BPTKgQx-5faQLmXaXvoz3sQJR7P4houzibJwEk3mQ3ez9-GRuW0cUMlAPMCS5qmYy5ImzPWTXakVR9OjqI5tPqqtYllOdHyPpRS8mXsd6XTrxohqFq4sELfZ8u0zyZlzdtSPeO2nEE-t*iA4hxQOMe7VljRa7ugMuRAS5GH6Obb1Z75xqXpI_");
                break;
            case R.id.btn_zhubo_im: // 主播 IM
                mLoginHelper = new LoginHelper(this, MainActivity.this);
                mLoginHelper.imLogin("kakasure", "eJxljsFOg0AURfd8BWFtzMAAU0y6QIvFWJoixXTckCkM5JUA4zCoxPjvKjYpiW97zr33fWq6rhv7TXLN8rwbWpWpUXBDv9ENZFxdoBBQZExlWBb-IP8QIHnGSsXlBE3HcSyE5g4UvFVQwtmoWc36QfKZ0Rd1Ns38Vdg-eZNga17SQzXBKEjvHgJ6cqvch9Vx26*60Per9THc7jzaPJpdFR5cQES4*12xjn0IfGmnaQzeuHmu3uuGvBxwImiU0Ngb49vw-sTK6PWJjkPs1cvZpIKGnx8iC8teYERm9I3LHrp2EixkOqaF0e8Z2pf2DXypXk0_");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoginHelper != null)
            mLoginHelper.onDestory();
    }

    @Override
    public void loginSucc() {
        MyToast.showBottom("登录成功");
        CurLiveInfo.setTitle("danke 测试");
        CurLiveInfo.setHostID("kakasure");
        CurLiveInfo.setGroupNum("@TGS#a6TF4BHEC");
        LiveQIMPublicActivity.start(this, "rtmp://4894.livepush.myqcloud.com/live/4894_20d265e4976611e69776e435c87f075e?bizid=4894");
    }

    @Override
    public void loginFail() {
        MyToast.showBottom("登录失败");
    }
}
