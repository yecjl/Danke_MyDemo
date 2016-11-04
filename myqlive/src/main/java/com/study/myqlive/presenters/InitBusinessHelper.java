package com.study.myqlive.presenters;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kakasure.myframework.utils.MyLog;
import com.study.myqlive.constant.Constant;
import com.study.myqlive.R;
import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;
import com.tencent.TIMUserStatusListener;

import tencent.tls.platform.TLSAccountHelper;
import tencent.tls.platform.TLSLoginHelper;


/**
 * 初始化
 * 包括imsdk等
 */
public class InitBusinessHelper {
    private static String TAG = "InitBusinessHelper";

    private InitBusinessHelper() {
    }

    public static TLSLoginHelper getmLoginHelper() {
        return mLoginHelper;
    }

    public static TLSAccountHelper getmAccountHelper() {
        return mAccountHelper;
    }

    private static TLSLoginHelper mLoginHelper;
    private static TLSAccountHelper mAccountHelper;
    private static String appVer = "1.0";

    /**
     * 初始化App
     */
    public static void initApp(final Context context) {
        //初始化 imsdk
        TIMManager.getInstance().disableBeaconReport();
        TIMManager.getInstance().setLogLevel(TIMLogLevel.INFO);
//        TIMManager.getInstance().setEnv(1);
        TIMManager.getInstance().init(context);

        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                MyLog.d(TAG + "onForceOffline->entered!");
//                MyLog.d(TAG + LogConstants.ACTION_HOST_KICK + LogConstants.DIV + MySelfInfo.getInstance().getId() + LogConstants.DIV + "on force off line");
                Toast.makeText(context, context.getString(R.string.tip_force_offline), Toast.LENGTH_SHORT).show();
                context.sendBroadcast(new Intent(Constant.BD_EXIT_APP));
            }

            @Override
            public void onUserSigExpired() {
                MyLog.d(TAG + "onUserSigExpired->entered!");
//                refreshSig();
            }
        });
    }

//    /**
//     * 重新登陆IM
//     * @param identify
//     * @param userSig
//     */
//    private static void reLoginIM(String identify, String userSig){
//        TIMUser user = new TIMUser();
//        user.setAccountType(String.valueOf(Constants.ACCOUNT_TYPE));
//        user.setAppIdAt3rd(String.valueOf(Constants.SDK_APPID));
//        user.setIdentifier(identify);
//        //发起登录请求
//        TIMManager.getInstance().login(
//                Constants.SDK_APPID,
//                user,
//                userSig,                    //用户帐号签名，由私钥加密获得，具体请参考文档
//                new TIMCallBack() {
//                    @Override
//                    public void onError(int i, String s) {
//                        SxbLog.e(TAG, "reLoginIM fail ：" + i + "|" + s);
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        SxbLog.i(TAG, "reLoginIM IMLogin succ !");
//                    }
//                });
//    }
//
//    /**
//     * 更新票据
//     */
//    private static void refreshSig(){
//        final String userId = MySelfInfo.getInstance().getId();
//        if (TextUtils.isEmpty(userId)){
//            SxbLog.w(TAG, "refreshSig->with empty identifier");
//            return;
//        }
//
//        // 更新票据
//        mLoginHelper.TLSRefreshUserSig(MySelfInfo.getInstance().getId(), new TLSRefreshUserSigListener() {
//            @Override
//            public void OnRefreshUserSigSuccess(TLSUserInfo tlsUserInfo) {
//                reLoginIM(userId, mLoginHelper.getUserSig(userId));
//            }
//
//            @Override
//            public void OnRefreshUserSigFail(TLSErrInfo tlsErrInfo) {
//                SxbLog.w(TAG, "OnRefreshUserSigFail->"+tlsErrInfo.ErrCode+"|"+tlsErrInfo.Msg);
//            }
//
//            @Override
//            public void OnRefreshUserSigTimeout(TLSErrInfo tlsErrInfo) {
//                SxbLog.w(TAG, "OnRefreshUserSigTimeout->"+tlsErrInfo.ErrCode+"|"+tlsErrInfo.Msg);
//            }
//        });
//    }
}
