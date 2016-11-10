package com.study.myqlive.presenters;

import android.content.Context;
import android.widget.Toast;

import com.kakasure.myframework.utils.MyLog;
import com.study.myqlive.constant.Constant;
import com.study.myqlive.constant.LogConstants;
import com.study.myqlive.presenters.viewinface.LoginView;
import com.study.myqlive.presenters.viewinface.LogoutView;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;

/**
 * 登录的数据处理类
 */
public class LoginHelper extends Presenter {
    private Context mContext;
    private static final String TAG = LoginHelper.class.getSimpleName();
    private LoginView mLoginView;
    private LogoutView mLogoutView;
    private int RoomId = -1;

    public LoginHelper(Context context) {
        mContext = context;
    }

    public LoginHelper(Context context, LoginView loginView) {
        mContext = context;
        mLoginView = loginView;
    }

    public LoginHelper(Context context, LogoutView logoutView) {
        mContext = context;
        mLogoutView = logoutView;
    }

    /**
     * 登录imsdk
     *
     * @param identify 用户id
     * @param userSig  用户签名
     */
    public void imLogin(final String identify, final String userSig) {
        TIMUser user = new TIMUser();
        user.setAccountType("8177");
        user.setAppIdAt3rd("1400017320");
        user.setIdentifier("kakasure|主播");
        //发起登录请求
        TIMManager.getInstance().login(
                1400017320,
                user,
                "eJxlz9FKwzAUBuD7PkXo7USTtDWr4EWd62yZU*w6xJuQtpnGbl2WZmVDfQnfw3vB1xGfwxEGBjy338-5z3lxAADudJwds7JcbRpN9U5yF5wBF7pHfyilqCjT1FPVP*RbKRSnbK65MuhBm0XFGy3m4oA1q1m7Ufz1*-Pr5-3DCrZVTU2RySEfQoiIh*1drXg0eD3MB0k88WFPr-MsRvfFA5yxwe2O*JMQp3G6GLFt8bQcXWTjEhVXkRhGMg8WvUSzmxbDriAx6vwqYtks5f7dNOHytIwuO*Gtn0-guVWpxZIfDiJ9EoRh37O046oVq8YEMEQBwvvn9*M6b84vnE9gwA__",                    //用户帐号签名，由私钥加密获得，具体请参考文档
                new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        MyLog.d(TAG +  "IMLogin fail ：" + i + " msg " + s);
                        Toast.makeText(mContext, "IMLogin fail ：" + i + " msg " + s, Toast.LENGTH_SHORT).show();
                        if (mLoginView != null) {
                            mLoginView.loginFail();
                        }
                    }

                    @Override
                    public void onSuccess() {
                        MyLog.d(TAG + "keypath IMLogin succ !");
                        MyLog.d(TAG + LogConstants.ACTION_HOST_CREATE_ROOM + LogConstants.DIV + identify + LogConstants.DIV + "request room id");
                        if (mLoginView != null)
                            mLoginView.loginSucc();
                    }
                });
    }


    /**
     * 退出imsdk
     * <p>
     * 退出成功会调用退出AVSDK
     */
    public void imLogout() {
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                MyLog.d(TAG + "IMLogout fail ：" + i + " msg " + s);
            }

            @Override
            public void onSuccess() {
                MyLog.d(TAG + "IMLogout succ !");
                if (mLogoutView != null) {
                    mLogoutView.logoutSucc();
                }
            }
        });

    }

    @Override
    public void onDestory() {
        mLoginView = null;
        mLogoutView = null;
        mContext = null;
    }
}
