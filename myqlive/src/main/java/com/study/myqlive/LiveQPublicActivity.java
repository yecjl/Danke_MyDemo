package com.study.myqlive;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kakasure.myframework.utils.MyLogUtils;
import com.kakasure.myframework.utils.UIUtil;
import com.kakasure.myframework.view.MarqueeView;
import com.kakasure.myframework.view.MyToast;
import com.makeramen.roundedimageview.RoundedImageView;
import com.study.myqlive.constant.Constant;
import com.study.myqlive.view.TxrjFrameLayout;
import com.study.myqlive.view.TxrjLinearLayout;
import com.study.myqlive.view.TxrjListView;
import com.study.myqlive.view.TxrjRelativeLayout;
import com.study.myqlive.view.TxrjTXCloudVideoView;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 功能：推流 -- 主播
 * Created by danke on 2016/11/3.
 */

public class LiveQPublicActivity extends RTMPBaseActivity implements ITXLivePushListener, View.OnClickListener {
    @Bind(R.id.video_view)
    TxrjTXCloudVideoView mCaptureView;
    @Bind(R.id.ri_madia_icon)
    RoundedImageView riMadiaIcon;
    @Bind(R.id.tv_madia_name)
    TextView tvMadiaName;
    @Bind(R.id.tv_guanzhu)
    TextView tvGuanzhu;
    @Bind(R.id.fl_guanzhu)
    FrameLayout flGuanzhu;
    @Bind(R.id.ll_guanzhu)
    LinearLayout llGuanzhu;
    @Bind(R.id.tv_online)
    TextView tvOnline;
    @Bind(R.id.ll_people)
    LinearLayout llPeople;
    @Bind(R.id.ivClose)
    ImageView ivClose;
    @Bind(R.id.rl_title)
    TxrjRelativeLayout rlTitle;
    @Bind(R.id.marquee_view)
    MarqueeView marqueeView;
    @Bind(R.id.ll_gonggao)
    LinearLayout llGonggao;
    @Bind(R.id.tv_reply)
    TextView tvReply;
    @Bind(R.id.iv_gift)
    ImageView ivGift;
    @Bind(R.id.iv_goods)
    ImageView ivGoods;
    @Bind(R.id.ll_goods)
    FrameLayout llGoods;
    @Bind(R.id.ll_bottom)
    TxrjLinearLayout llBottom;
    @Bind(R.id.chat_context_listview)
    TxrjListView chatContextListview;
    @Bind(R.id.ll_video)
    TxrjRelativeLayout llVideo;
    @Bind(R.id.logViewStatus)
    TextView logViewStatus;
    @Bind(R.id.logViewEvent)
    TextView logViewEvent;
    @Bind(R.id.scrollview)
    ScrollView mLogScrollView;
    @Bind(R.id.layout_main)
    TxrjFrameLayout layoutMain;
    @Bind(R.id.iv_log)
    ImageView ivLog;
    private TXLivePusher mLivePusher;
    private TXLivePushConfig mLivePushConfig;
    private TXLivePlayer mLivePlayer;
    private boolean mVideoPublish;

    public static final int VIDEO_RESOLUTION_TYPE_360_640 = 0;
    public static final int VIDEO_RESOLUTION_TYPE_540_960 = 1;
    public static final int VIDEO_RESOLUTION_TYPE_720_1280 = 2;
    public static final int VIDEO_RESOLUTION_TYPE_AUTO = 3;
    private String playUrl;

    private boolean isShowLog = false;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_live_new;
    }

    @Override
    protected void afterInject(Bundle savedInstanceState) {
        mLivePusher = new TXLivePusher(this);
        mLivePushConfig = new TXLivePushConfig();

        initLogView(root); // 初始化日志
        setActivityType(RTMPBaseActivity.ACTIVITY_TYPE_PUBLISH); // 设置Log类型

        mLogViewStatus.setMovementMethod(new ScrollingMovementMethod());
        mLogViewEvent.setMovementMethod(new ScrollingMovementMethod());

        if (mLivePlayer == null) {
            mLivePlayer = new TXLivePlayer(this);
        }

        mVideoPublish = false;

        // 获取出传递的数据
        playUrl = getIntent().getStringExtra("playUrl");

        ivGift.setOnClickListener(this);

        // 如果是Debug模式， 可以显示log 按钮
        ivLog.setVisibility(BaseApplication.DEBUG ? View.VISIBLE: View.GONE);
        ivLog.setOnClickListener(BaseApplication.DEBUG ? this: null);

        getPublishPermission();

        if (mVideoPublish) {
            stopPublishRtmp();
            mVideoPublish = false;
        } else {
            fixOrAdjustBitrate(VIDEO_RESOLUTION_TYPE_AUTO); // 根据设置确定是“固定”还是“自动”码率
            mVideoPublish = startPublishRtmp();
        }
    }

    /**
     * 获取直播权限
     */
    private void getPublishPermission() {
        // 编译版本23的时候需要这么调用权限
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) { // 允许启动摄像头
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAPTURE_AUDIO_OUTPUT)) { // 允许音频输出
                permissions.add(Manifest.permission.CAPTURE_AUDIO_OUTPUT);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAPTURE_VIDEO_OUTPUT)) { // 允许视频输出
                permissions.add(Manifest.permission.CAPTURE_VIDEO_OUTPUT);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) { // 允许录制
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this,
                        (String[]) permissions.toArray(new String[0]),
                        Constant.WRITE_PERMISSION_REQ_CODE);
            }
        }
    }

    /**
     * 设置清晰度
     *
     * @param mode
     */
    public void fixOrAdjustBitrate(int mode) {
        if (mLivePushConfig == null || mLivePusher == null) {
            return;
        }
        switch (mode) {
            case VIDEO_RESOLUTION_TYPE_720_1280: /*720p*/
                if (mLivePusher != null) {
                    // 设置分辨率：摄像头直播有三种 9:16 的常规分辨率可供选择：360*640，540*960，720*1280。
                    mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_720_1280);
                    mLivePushConfig.setAutoAdjustBitrate(false);
                    // 设置码率：编码器每秒编出的数据大小，单位是kbps，比如800kbps代表编码器每秒产生800kb（或100KB）的数据。
                    mLivePushConfig.setVideoBitrate(1500);
                    mLivePusher.setConfig(mLivePushConfig);
                }
                break;
            case VIDEO_RESOLUTION_TYPE_540_960: /*540p*/
                if (mLivePusher != null) {
                    mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960);
                    mLivePushConfig.setAutoAdjustBitrate(false);
                    mLivePushConfig.setVideoBitrate(1000);
                    mLivePusher.setConfig(mLivePushConfig);
                }
                break;
            case VIDEO_RESOLUTION_TYPE_360_640: /*360p*/
                if (mLivePusher != null) {
                    mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640);
                    mLivePushConfig.setAutoAdjustBitrate(false);
                    mLivePushConfig.setVideoBitrate(700);
                    mLivePusher.setConfig(mLivePushConfig);
                }
                break;

            case VIDEO_RESOLUTION_TYPE_AUTO: /*自动*/
                if (mLivePusher != null) {
                    mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640);
                    mLivePushConfig.setAutoAdjustBitrate(true);
                    mLivePushConfig.setMaxVideoBitrate(1000);
                    mLivePushConfig.setMinVideoBitrate(500);
                    mLivePushConfig.setVideoBitrate(700);
                    mLivePusher.setConfig(mLivePushConfig);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 启动推流
     *
     * @return
     */
    private boolean startPublishRtmp() {
        if (TextUtils.isEmpty(playUrl) || (!playUrl.trim().toLowerCase().startsWith("rtmp://"))) {
            MyToast.showBottom("推流地址不合法，目前支持rtmp推流!");
            return false;
        }

        // 设置水印
//        mLivePushConfig.setWatermark(mBitmap, 10, 10);

        // 帧率：FPS <=10 会明显感觉到卡顿，摄像头直播推荐设置 20 FPS。
        mLivePushConfig.setVideoFPS(25);

        int customModeType = 0;
        mLivePushConfig.setCustomModeType(customModeType);
        // 设置一张等待图片，图片含义推荐为“主播暂时离开一下下，稍后回来”
        mLivePushConfig.setPauseImg(300, 10);
        Bitmap bitmap = UIUtil.decodeResource(R.mipmap.pause_publish);
        mLivePushConfig.setPauseImg(bitmap);

        mLivePusher.setConfig(mLivePushConfig);

        // 设置监听器
        mLivePusher.setPushListener(this);

        mLivePusher.startPusher(playUrl); // 告诉 RTMP SDK 音视频流要推到哪个推流URL上去
        mLivePusher.startCameraPreview(mCaptureView);
        mLivePusher.setLogLevel(TXLiveConstants.LOG_LEVEL_DEBUG);
        clearLog();
        int[] ver = TXLivePusher.getSDKVersion();
        if (ver != null && ver.length >= 3) {
            mLogMsg.append(String.format("rtmp sdk version:%d.%d.%d ", ver[0], ver[1], ver[2]));
            mLogViewEvent.setText(mLogMsg);
        }

        appendEventLog(0, "点击推流按钮！");
        return true;
    }

    /**
     * 结束推流，注意做好清理工作
     */
    private void stopPublishRtmp() {
        mLivePusher.stopCameraPreview(true); //停止摄像头预览
        mLivePusher.stopScreenCapture();
        mLivePusher.setPushListener(null); //解绑 listener
        mLivePusher.stopPusher();  //停止推流

        if (mLivePushConfig != null) {
            mLivePushConfig.setPauseImg(null);
        }
    }

    @Override
    public void onPushEvent(int event, Bundle param) {
        String msg = param.getString(TXLiveConstants.EVT_DESCRIPTION);
        appendEventLog(event, msg);
        if (mLogScrollView.getVisibility() == View.VISIBLE) {
            mLogViewEvent.setText(mLogMsg);
            scroll2Bottom(mLogScrollView, mLogViewEvent);
        }
        //错误还是要明确的报一下
        if (event < 0) {
            MyToast.showBottom(msg);
        }

        switch (event) {
            case TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL: // 打开摄像头失败
                break;
            case TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL: // 打开麦克风失败
                break;
            case TXLiveConstants.PUSH_WARNING_HW_ACCELERATION_FAIL: // 硬编码启动失败，采用软编码
                break;
            case TXLiveConstants.PUSH_ERR_NET_DISCONNECT: // 网络断连,且经三次抢救无效,可以放弃治疗,更多重试请自行重启推流
            case TXLiveConstants.PUSH_ERR_SCREEN_CAPTURE_UNSURPORT:
            case TXLiveConstants.PUSH_ERR_SCREEN_CAPTURE_START_FAILED:
                stopPublishRtmp();
                mVideoPublish = false;
                break;
        }
    }

    @Override
    public void onNetStatus(Bundle status) {
        String str = getNetStatusString(status);
        mLogViewStatus.setText(str);
        MyLogUtils.d("Current status: " + status.toString());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCaptureView != null) {
            mCaptureView.onResume();
        }
        if (mVideoPublish && mLivePusher != null) {
            // 将界面元素和Pusher对象关联起来，从而能够将手机摄像头采集到的画面渲染到屏幕上
            mLivePusher.resumePusher();
            mLivePusher.startCameraPreview(mCaptureView);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCaptureView != null) {
            mCaptureView.onPause();
        }
        if (mVideoPublish && mLivePusher != null) {
            mLivePusher.stopCameraPreview(false);
            mLivePusher.pausePusher();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPublishRtmp();
        if (mCaptureView != null) {
            mCaptureView.onDestroy();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_log: // 设置显示Log
                isShowLog = !isShowLog;
                mLogScrollView.setVisibility(isShowLog ? View.VISIBLE : View.GONE);
                logViewStatus.setVisibility(isShowLog ? View.VISIBLE : View.GONE);
                break;
            case R.id.iv_gift:
                break;
        }
    }

    public static void start(Context context, String playUrl) {
        Intent intent = new Intent(context, LiveQPublicActivity.class);
        intent.putExtra("playUrl", playUrl);
        ((Activity) context).startActivityForResult(intent, 0); // 导致了打开摄像头失败
    }

}
