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
                mLoginHelper.imLogin("9896587163619414027|主播", "eJxlzk9Og0AYBfA9pyBsa2Rm*DeYuJhQbKotpUil6YYgDO1AOkUYCY16Ce-h3sTrGM9hQ5pI4vr33ve*F0mWZSWc3V8maXp45iIWx4oq8pWsAOXiD6uKZXEiYq3O-iHtKlbTOMkFrXvUwJBZRrlgOTujjW3TwBY0NRPaOtQBsl6-P79*3j8GnSYr436zr5xCAEBLQ8OzDdv2OHdXznScjij3wg0y2hu-yIPKbbxl4O1K9bZgWzuYwsdotJzsnnhEmEuKvVP73UOGiDdz1lk3nm88qB6xf*eTFi2i1XrCVULUsEyvB5OC7en5IQtjXTexOdCW1g078D6AADQgAvbpb6BIb9IvGRdiPg__");
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
        CurLiveInfo.setHostID("9896587163619414027|主播");
        CurLiveInfo.setGroupNum("@TGS#aOHYKWHEQ");
        LiveQIMPublicActivity.start(this, "rtmp://4894.livepush.myqcloud.com/live/4894_6f3c29bba70b11e69776e435c87f075e?bizid=4894");
    }

    @Override
    public void loginFail() {
        MyToast.showBottom("登录失败");
    }
}
