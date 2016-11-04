package com.study.myqlive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kakasure.myframework.utils.MyLog;
import com.kakasure.myframework.utils.StringUtil;
import com.kakasure.myframework.utils.UIUtil;
import com.kakasure.myframework.view.MarqueeView;
import com.kakasure.myframework.view.MyToast;
import com.kakasure.myframework.view.ProgressDialog;
import com.makeramen.roundedimageview.RoundedImageView;
import com.study.myqlive.utils.UIUtiles;
import com.study.myqlive.utils.WindowUtil;
import com.study.myqlive.view.TxrjFrameLayout;
import com.study.myqlive.view.TxrjLinearLayout;
import com.study.myqlive.view.TxrjListView;
import com.study.myqlive.view.TxrjRelativeLayout;
import com.study.myqlive.view.TxrjTXCloudVideoView;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

/**
 * 功能：腾讯云直播
 * Created by danke on 2016/10/31.
 */
public class LiveQActivity extends RTMPBaseActivity implements ITXLivePlayListener, View.OnClickListener {

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
    @Bind(R.id.video_view)
    TxrjTXCloudVideoView mPlayerView;
    @Bind(R.id.marquee_view)
    MarqueeView marqueeView;
    @Bind(R.id.ll_gonggao)
    LinearLayout llGonggao;
    @Bind(R.id.ll_video)
    TxrjRelativeLayout llVideo;
    @Bind(R.id.chat_context_listview)
    TxrjListView mChatListView;
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
    @Bind(R.id.logViewStatus)
    TextView logViewStatus;
    @Bind(R.id.logViewEvent)
    TextView logViewEvent;
    @Bind(R.id.scrollview)
    ScrollView mScrollView;
    @Bind(R.id.layout_main)
    TxrjFrameLayout layoutMain;
    private TXLivePlayer mLivePlayer;
    private int mCurrentRenderMode;
    private int mCurrentRenderRotation;
    private TXLivePlayConfig mPlayConfig;
    private boolean mVideoPlay; // 是否播放
    private boolean mVideoPause = false; // 是否暂停
    private int mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
    private String playUrl; // 视频直播的流地址
    private boolean mHWDecode = false;
    private long mStartPlayTS = 0;
    private long mTrackingTouchTS = 0;
    private ProgressDialog mLoadDialog;
    private boolean isSendChat = false;
    private PopupWindow mChatPopupWindow;
    private EditText etReply;
    private Button btnReply;
    private RelativeLayout rlReplyBox;
    private boolean canShowPop = true; // 可以显示商品气泡
    private boolean shouldShowPop = false; // 是否需要显示商品的气泡
    private ChatAdapter chatAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_live_new;
    }

    @Override
    protected void afterInject(Bundle savedInstanceState) {
        super.afterInject(savedInstanceState);

        // 设置内容区域的大小，固定大小，软键盘就不会将内容顶上去
        llVideo.getLayoutParams().height = WindowUtil.getWindowHeight(this) - WindowUtil.getStatusHeight(this);

        // RENDER_MODE_ADJUST_RESOLUTION: 将图像等比例缩放，适配最长边，缩放后的宽和高都不会超过显示区域，居中显示，画面可能会留有黑边。
        // RENDER_MODE_FULL_FILL_SCREEN	将图像等比例铺满整个屏幕，多余部分裁剪掉，此模式下画面不会留黑边，但可能因为部分区域被裁剪而显示不全。
        mCurrentRenderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
        // 0	正常播放（Home键在画面正下方）
        // 90	画面逆时针旋转90度
        // 180	画面逆时针旋转180度
        // 270	画面逆时针旋转270度
        mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT;

        // 初始化播放配置
        mPlayConfig = new TXLivePlayConfig();

        if (mLivePlayer == null) {
            mLivePlayer = new TXLivePlayer(this);
        }

        mVideoPlay = false;
        setActivityType(RTMPBaseActivity.ACTIVITY_TYPE_LIVE_PLAY);
        initLogView(root);

        // 初始化消息记录
        chatAdapter = new ChatAdapter(this, null);
        chatAdapter.init(10001, "outa");
        mChatListView.setAdapter(chatAdapter);

        // 初始化监听
        tvReply.setOnClickListener(this);

        // 获取出传递的数据
        playUrl = getIntent().getStringExtra("playUrl");
        MyLog.d("click playbtn isplay:" + mVideoPlay + " ispause:" + mVideoPause + " playtype:" + mPlayType);
        if (mVideoPlay) {
            if (mPlayType == TXLivePlayer.PLAY_TYPE_VOD_FLV || mPlayType == TXLivePlayer.PLAY_TYPE_VOD_HLS || mPlayType == TXLivePlayer.PLAY_TYPE_VOD_MP4) {
                if (mVideoPause) {
                    mLivePlayer.resume();
                } else {
                    mLivePlayer.pause();
                }
                mVideoPause = !mVideoPause;

            } else {
                stopPlayRtmp();
                mVideoPlay = !mVideoPlay;
            }

        } else {
            if (startPlayRtmp()) {
                mVideoPlay = !mVideoPlay;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLivePlayer != null) {
            mLivePlayer.stopPlay(true);
        }
        if (mPlayerView != null) {
            mPlayerView.onDestroy();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mPlayType == TXLivePlayer.PLAY_TYPE_VOD_FLV || mPlayType == TXLivePlayer.PLAY_TYPE_VOD_HLS || mPlayType == TXLivePlayer.PLAY_TYPE_VOD_MP4) {
            if (mLivePlayer != null) {
                mLivePlayer.pause();
            }
        } else if (Build.VERSION.SDK_INT >= 23) { //目前android6.0以上暂不支持后台播放
            stopPlayRtmp();
        }

        if (mPlayerView != null) {
            mPlayerView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mVideoPlay && !mVideoPause) {
            if (mPlayType == TXLivePlayer.PLAY_TYPE_VOD_FLV || mPlayType == TXLivePlayer.PLAY_TYPE_VOD_HLS || mPlayType == TXLivePlayer.PLAY_TYPE_VOD_MP4) {
                if (mLivePlayer != null) {
                    mLivePlayer.resume();
                }
            } else if (Build.VERSION.SDK_INT >= 23) { //目前android6.0以上暂不支持后台播放
                startPlayRtmp();
            }
        }

        if (mPlayerView != null) {
            mPlayerView.onResume();
        }
    }

    /**
     * 检查Url的格式
     *
     * @param playUrl
     * @return
     */
    private boolean checkPlayUrl(final String playUrl) {
        if (TextUtils.isEmpty(playUrl) || (!playUrl.startsWith("http://") && !playUrl.startsWith("https://") && !playUrl.startsWith("rtmp://"))) {
            MyToast.showBottom("播放地址不合法，目前仅支持rtmp,flv,hls,mp4播放方式!");
            return false;
        }

        switch (mActivityType) {
            case RTMPBaseActivity.ACTIVITY_TYPE_LIVE_PLAY: {
                if (playUrl.startsWith("rtmp://")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
                } else if ((playUrl.startsWith("http://") || playUrl.startsWith("https://")) && playUrl.contains(".flv")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_FLV;
                } else {
                    MyToast.showBottom("播放地址不合法，直播目前仅支持rtmp,flv播放方式!");
                    return false;
                }
            }
            break;
            case RTMPBaseActivity.ACTIVITY_TYPE_VOD_PLAY: {
                if (playUrl.startsWith("http://") || playUrl.startsWith("https://")) {
                    if (playUrl.contains(".flv")) {
                        mPlayType = TXLivePlayer.PLAY_TYPE_VOD_FLV;
                    } else if (playUrl.contains(".m3u8")) {
                        mPlayType = TXLivePlayer.PLAY_TYPE_VOD_HLS;
                    } else if (playUrl.toLowerCase().contains(".mp4")) {
                        mPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
                    } else {
                        MyToast.showBottom("播放地址不合法，点播目前仅支持flv,hls,mp4播放方式!");
                        return false;
                    }
                } else {
                    MyToast.showBottom("播放地址不合法，点播目前仅支持flv,hls,mp4播放方式!");
                    return false;
                }
            }
            break;
            default:
                MyToast.showBottom("播放地址不合法，目前仅支持rtmp,flv,hls,mp4播放方式!");
                return false;
        }
        return true;
    }

    /**
     * 启动播放
     *
     * @return
     */
    private boolean startPlayRtmp() {

        if (!checkPlayUrl(playUrl)) {
            return false;
        }

        clearLog();

        int[] ver = TXLivePlayer.getSDKVersion();
        if (ver != null && ver.length >= 3) {
            mLogMsg.append(String.format("rtmp sdk version:%d.%d.%d ", ver[0], ver[1], ver[2]));
            mLogViewEvent.setText(mLogMsg);
        }

        //将这个TXLivePlayer对象与TXCloudVideoView控件进行关联
        mLivePlayer.setPlayerView(mPlayerView);
        mLivePlayer.setPlayListener(this);

        // 硬件加速在1080p解码场景下效果显著，但细节之处并不如想象的那么美好：
        // (1) 只有 4.3 以上android系统才支持
        // (2) 兼容性我们目前还仅过了小米华为等常见机型，故这里的返回值您先不要太当真
        // 对于蓝光级别（1080p）的画质，简单采用软件解码的方式很难获得较为流畅的播放体验，所以如果您的场景是以游戏直播为主，建议打开硬件加速开关。
        mLivePlayer.enableHardwareDecode(mHWDecode);
        mLivePlayer.setRenderRotation(mCurrentRenderRotation);
        mLivePlayer.setRenderMode(mCurrentRenderMode);
        //设置播放器缓存策略
        //这里将播放器的策略设置为自动调整，调整的范围设定为1到4s，您也可以通过setCacheTime将播放器策略设置为采用
        //固定缓存时间。如果您什么都不调用，播放器将采用默认的策略（默认策略为自动调整，调整范围为1到4s）
        //mLivePlayer.setCacheTime(5);
        mLivePlayer.setConfig(mPlayConfig);

        int result = mLivePlayer.startPlay(playUrl, mPlayType); // result返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;
        if (result == -2) {
            MyToast.showBottom("非腾讯云链接地址，若要放开限制，请联系腾讯云商务团队");
        }
        if (result != 0) {
            return false;
        }

        mLivePlayer.setLogLevel(TXLiveConstants.LOG_LEVEL_DEBUG);
        appendEventLog(0, "点击播放按钮！播放类型：" + mPlayType);

        startLoadingAnimation("正在进入直播...");

        mStartPlayTS = System.currentTimeMillis();
        return true;
    }

    /**
     * 关闭直播
     */
    private void stopPlayRtmp() {
        stopLoadingAnimation();
        if (mLivePlayer != null) {
            mLivePlayer.setPlayListener(null);
            mLivePlayer.stopPlay(true);
        }
    }

    /**
     * 播放事件回调
     *
     * @param event
     * @param param
     */
    @Override
    public void onPlayEvent(int event, Bundle param) {
        switch (event) {
            case TXLiveConstants.PLAY_EVT_PLAY_BEGIN: // 视频播放开始，如果有转菊花什么的这个时候该停了
                stopLoadingAnimation();
                MyLog.d("AutoMonitor: " + "PlayFirstRender, cost=" + (System.currentTimeMillis() - mStartPlayTS));
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS: // 视频播放进度，会通知当前进度和总体进度:
                int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
                int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION);
                long curTS = System.currentTimeMillis();

                // 避免滑动进度条松开的瞬间可能出现滑动条瞬间跳到上一个位置
                if (Math.abs(curTS - mTrackingTouchTS) < 500) {
                    return;
                }
                mTrackingTouchTS = curTS;

                MyLog.d("progress: " + String.format("%02d:%02d", progress / 60, progress % 60));
                MyLog.d("duration: " + String.format("%02d:%02d", duration / 60, duration % 60));
                return;
            case TXLiveConstants.PLAY_EVT_PLAY_LOADING: // 视频播放loading，如果能够恢复，之后会有BEGIN事件:
//                startLoadingAnimation("加载中...");
                break;
            case TXLiveConstants.PLAY_ERR_NET_DISCONNECT: // 网络断连,且经多次重连抢救无效,可以放弃治疗,更多重试请自行重启播放
            case TXLiveConstants.PLAY_EVT_PLAY_END: // 视频播放结束
                stopPlayRtmp();
                mVideoPlay = false;
                mVideoPause = false;
                MyLog.d("progress: 00:00");
                break;
        }

        String msg = param.getString(TXLiveConstants.EVT_DESCRIPTION);
        appendEventLog(event, msg);
        if (mScrollView.getVisibility() == View.VISIBLE) {
            mLogViewEvent.setText(mLogMsg);
            scroll2Bottom(mScrollView, mLogViewEvent);
        }
//        if(mLivePlayer != null){
//            mLivePlayer.onLogRecord("[event:"+event+"]"+msg+"\n");
//        }
        if (event < 0) {
            // step1：网络断连,且经多次抢救无效,可以放弃治疗!
            MyToast.showBottom(msg);
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            stopLoadingAnimation();
        }
    }

    /**
     * 连接状态回调
     *
     * @param status
     */
    @Override
    public void onNetStatus(Bundle status) {
        String str = getNetStatusString(status);
        mLogViewStatus.setText(str);
        MyLog.d("Current status: " + status.toString());
//        if (mLivePlayer != null){
//            mLivePlayer.onLogRecord("[net state]:\n"+str+"\n");
//        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reply: // 聊天框
                if (!isSendChat) {
                    isSendChat = true;
                }

                showChatPopwindow();
                break;
            case R.id.btn_reply: // 聊天
                sendReply();
                break;
        }
    }

    /**
     * etReply 设置输入监听
     */
    TextWatcher etReplyWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;
        private final int charMaxNum = 200;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = etReply.getSelectionStart();
            editEnd = etReply.getSelectionEnd();
            if (temp.length() > charMaxNum) {
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                etReply.setText(s);
                etReply.setSelection(tempSelection);
            }
        }
    };

    /**
     * 显示聊天对话框
     */
    private void showChatPopwindow() {
        if (mChatPopupWindow == null) {
            View contentView = UIUtiles.inflate(R.layout.item_popup_chat);
            etReply = ((EditText) contentView.findViewById(R.id.et_reply));
            btnReply = ((Button) contentView.findViewById(R.id.btn_reply));
            rlReplyBox = ((RelativeLayout) contentView.findViewById(R.id.rl_replyBox));

            mChatPopupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, UIUtil.Dp2Px(40));
        }
        // 设置最多输入200
        etReply.addTextChangedListener(etReplyWatcher);

        mChatPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mChatPopupWindow.setFocusable(true);

        //设置弹出窗体需要软键盘
        mChatPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        //设置模式，和Activity的一样，覆盖，调整大小。
        mChatPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 显示的位置为
        mChatPopupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);

        etReply.setFocusable(true);
        etReply.setFocusableInTouchMode(true);
        etReply.requestFocus();

        //警告：对于刚跳到一个新的界面就要弹出软键盘的情况上述代码可能由于界面未加载完全而无法弹出软键盘。此时应该适当的延迟弹出软键盘如998毫秒（保证界面的数据加载完成）
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager) etReply.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etReply, 0);
            }
        }, 200);
        btnReply.setOnClickListener(this);

        canShowPop = false;
    }

    /**
     * 发送消息
     */
    public void sendReply() {
        String chatText = StringUtil.getInput(etReply);
        if (!StringUtil.isNull(chatText)) {

            etReply.setFocusable(false);
            etReply.setFocusableInTouchMode(false);
            // 清空
            etReply.setText("");
            // 发送聊天
            mChatPopupWindow.dismiss();
        } else {
            MyToast.showBottom("发送内容不能为空");
        }
    }

    private void startLoadingAnimation(String msg) {
        if (mLoadDialog == null) {
            mLoadDialog = ProgressDialog.show(this, msg, false);
        } else {
            mLoadDialog.setMessage(msg);
        }
    }

    private void stopLoadingAnimation() {
        if (mLoadDialog != null) {
            mLoadDialog.dismiss();
            mLoadDialog = null;
        }
    }

    public static void start(Context context, String playUrl) {
        Intent intent = new Intent(context, LiveQActivity.class);
        intent.putExtra("playUrl", playUrl);
        ((Activity) context).startActivityForResult(intent, 0);
    }

}
