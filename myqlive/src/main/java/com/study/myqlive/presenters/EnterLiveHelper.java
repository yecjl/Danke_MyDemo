package com.study.myqlive.presenters;

import android.content.Context;
import android.os.AsyncTask;

import com.kakasure.myframework.utils.MyLogUtils;
import com.kakasure.myframework.view.MyToast;
import com.study.myqlive.constant.Constant;
import com.study.myqlive.constant.LogConstants;
import com.study.myqlive.model.CurLiveInfo;
import com.study.myqlive.model.LiveInfoJson;
import com.study.myqlive.presenters.viewinface.EnterQuiteRoomView;
import com.study.myqlive.utils.SxbLog;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupManager;
import com.tencent.TIMManager;

import java.util.ArrayList;


/**
 * 进出房间Presenter
 */
public class EnterLiveHelper extends Presenter {
    private EnterQuiteRoomView mStepInOutView;
    private String mId; // id
    private int mIdStatus; // HOST MEMBER
    private Context mContext;
    private static final String TAG = EnterLiveHelper.class.getSimpleName();
    private static boolean isInChatRoom = false;
    private ArrayList<String> video_ids = new ArrayList<String>();

    private static final int TYPE_MEMBER_CHANGE_IN = 1;//进入房间事件。
    private static final int TYPE_MEMBER_CHANGE_OUT = 2;//退出房间事件。
    private static final int TYPE_MEMBER_CHANGE_HAS_CAMERA_VIDEO = 3;//有发摄像头视频事件。
    private static final int TYPE_MEMBER_CHANGE_NO_CAMERA_VIDEO = 4;//无发摄像头视频事件。
    private static final int TYPE_MEMBER_CHANGE_HAS_AUDIO = 5;//有发语音事件。
    private static final int TYPE_MEMBER_CHANGE_NO_AUDIO = 6;//无发语音事件。
    private static final int TYPE_MEMBER_CHANGE_HAS_SCREEN_VIDEO = 7;//有发屏幕视频事件。
    private static final int TYPE_MEMBER_CHANGE_NO_SCREEN_VIDEO = 8;//无发屏幕视频事件。


    public EnterLiveHelper(Context context, EnterQuiteRoomView view, int idStatus, String id) {
        mContext = context;
        mStepInOutView = view;
        mIdStatus = idStatus;
        mId = id;
    }


    /**
     * 进入一个直播房间流程
     */
    public void startEnterRoom() {
        if (mIdStatus == Constant.HOST) { // 1_1 创建一个直播 1_2创建一个IM聊天室 -- 这条线后台处理
            mStepInOutView.enterRoomComplete(mIdStatus, true);
        } else {
            MyLogUtils.i(TAG + " joinLiveRoom startEnterRoom ");
            joinLive(CurLiveInfo.getGroupNum());
        }
    }

    /**
     * 2 加入一个房间
     */
    private void joinLive(String groupNum) {
        joinIMChatRoom(groupNum);
    }

    /**
     * 2_2加入一个聊天室
     */
    private void joinIMChatRoom(final String groupId) {
        SxbLog.standardEnterRoomLog(TAG, "join im chat group", "", "group id " + groupId, mIdStatus, mId);
        // groupId 群组id, reason 申请理由（选填）, 回调
        TIMGroupManager.getInstance().applyJoinGroup(groupId, Constant.APPLY_CHATROOM + groupId, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                //已经在是成员了
                if (i == Constant.IS_ALREADY_MEMBER) {
                    MyLogUtils.i(TAG + " joinLiveRoom joinIMChatRoom callback succ ");
                    isInChatRoom = true;
                    mStepInOutView.enterRoomComplete(mIdStatus, true);
                } else {
                    SxbLog.standardEnterRoomLog(TAG, "join im chat group", "" + LogConstants.STATUS.FAILED, "code:" + i + " msg:" + s, mIdStatus, mId);
                    if (mContext != null) {
                        MyToast.showBottom("join IM group fail " + s + " " + i);
                    }
                    //退出IM房间
                    quiteIMChatRoom();
                }
            }

            @Override
            public void onSuccess() {
                SxbLog.standardEnterRoomLog(TAG, "join im chat group", "" + LogConstants.STATUS.FAILED, "group id " + groupId, mIdStatus, mId);
                isInChatRoom = true;
                mStepInOutView.enterRoomComplete(mIdStatus, true);
            }
        });

    }


    private NotifyServerLiveEnd liveEndTask;

    /**
     * 通知用户UserServer房间
     */
    private void notifyServerLiveEnd() {
        liveEndTask = new NotifyServerLiveEnd();
        liveEndTask.execute(mId);
    }

    @Override
    public void onDestory() {
        if (isInChatRoom) {
            quiteIMChatRoom();
        }
        mContext = null;
    }

    class NotifyServerLiveEnd extends AsyncTask<String, Integer, LiveInfoJson> {

        @Override
        protected LiveInfoJson doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(LiveInfoJson result) {
        }
    }

    /**
     * 退出IM房间
     */
    private void quiteIMChatRoom() {
        if ((isInChatRoom == true)) {
            if (mIdStatus == Constant.HOST) {
                // 主播解散群
                TIMGroupManager.getInstance().deleteGroup(CurLiveInfo.getGroupNum(), new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        SxbLog.standardQuiteRoomLog(TAG, "delete im room", "" + LogConstants.STATUS.FAILED, "code:" + i + " msg:" + s, mIdStatus, mId);
                    }

                    @Override
                    public void onSuccess() {
                        SxbLog.standardQuiteRoomLog(TAG, "delete im room", "" + LogConstants.STATUS.SUCCEED, "group id " + CurLiveInfo.getGroupNum(), mIdStatus, mId);
                        isInChatRoom = false;
                    }
                });
                TIMManager.getInstance().deleteConversation(TIMConversationType.Group, CurLiveInfo.getGroupNum());
            } else {
                // 成员退出群 groupId	群组Id, cb回调
                TIMGroupManager.getInstance().quitGroup(CurLiveInfo.getGroupNum(), new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        SxbLog.standardQuiteRoomLog(TAG, "quite im room", "" + LogConstants.STATUS.FAILED, "code:" + i + " msg:" + s, mIdStatus, mId);
                    }

                    @Override
                    public void onSuccess() {
                        SxbLog.standardQuiteRoomLog(TAG, "quite im room", "" + LogConstants.STATUS.SUCCEED, "room id " + CurLiveInfo.getGroupNum(), mIdStatus, mId);
                        isInChatRoom = false;
                    }
                });
            }
        }
    }
}
