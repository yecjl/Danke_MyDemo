package com.study.myqlive.presenters;


import android.content.Context;
import android.content.Intent;

import com.kakasure.myframework.utils.MyLogUtils;
import com.kakasure.myframework.view.MyToast;
import com.study.myqlive.constant.Constant;
import com.study.myqlive.model.CurLiveInfo;
import com.study.myqlive.presenters.viewinface.LiveView;
import com.study.myqlive.presenters.viewinface.MembersDialogView;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMGroupSystemElem;
import com.tencent.TIMGroupSystemElemType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMTextElem;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.av.TIMAvManager;
import com.tencent.av.sdk.AVView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 直播的控制类presenter
 */
public class LiveHelper extends Presenter {
    private LiveView mLiveView;
    private String mId;
    private String mNickName;
    private MembersDialogView mMembersDialogView;
    public Context mContext;
    private static final String TAG = LiveHelper.class.getSimpleName();
    private static final int CAMERA_NONE = -1;
    private static final int FRONT_CAMERA = 0;
    private static final int BACK_CAMERA = 1;
    private static final int MAX_REQUEST_VIEW_COUNT = 4; //当前最大支持请求画面个数
    private static final boolean LOCAL = true;
    private static final boolean REMOTE = false;
    private TIMConversation mGroupConversation;
    private TIMConversation mC2CConversation;
    private boolean isMicOpen = false;
    private static final String UNREAD = "0";
    private AVView mRequestViewList[] = new AVView[MAX_REQUEST_VIEW_COUNT];
    private String mRequestIdentifierList[] = new String[MAX_REQUEST_VIEW_COUNT];
    private Boolean isOpenCamera = false;
    private boolean isBakCameraOpen, isBakMicOpen;      // 切后时备份当前camera及mic状态


    public LiveHelper(Context context, LiveView liveview, String id, String nickName) {
        mContext = context;
        mLiveView = liveview;
        mId = id;
        mNickName = nickName;
    }

    /**
     * 初始化聊天室  设置监听器
     */
    public void initTIMListener(String chatGroupId) {
        MyLogUtils.v(TAG + "initTIMListener->current group id: " + chatGroupId);
        // 获取群聊会话:会话类型：群组, 群组Id
        mGroupConversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, chatGroupId);
        // 获取单聊会话
        mC2CConversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, chatGroupId);
        // 注册新消息通知回调
        TIMManager.getInstance().addMessageListener(msgListener);
    }

    /**
     * 发送群消息
     *
     * @param cmd
     * @param param
     * @param callback 发送消息回调
     */
    public void sendGroupMessage(int cmd, String param, TIMValueCallBack<TIMMessage> callback) {
        JSONObject inviteCmd = new JSONObject();
        try {
            inviteCmd.put(Constant.CMD_KEY, cmd);
            inviteCmd.put(Constant.CMD_PARAM, param);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String cmds = inviteCmd.toString();
        MyLogUtils.i(TAG + "send cmd : " + cmd + "|" + cmds);

        // 构造一条消息
        TIMMessage gmsg = new TIMMessage();
        // 构造一条消息
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(cmds.getBytes());
        elem.setDesc("");
        // 将elem添加到消息
        gmsg.addElement(elem);

        if (mGroupConversation != null)
            // 发送消息
            mGroupConversation.sendMessage(gmsg, callback);
    }

    /**
     * 发送群消息
     *
     * @param cmd
     * @param param
     */
    public void sendGroupMessage(int cmd, String param) {
        sendGroupMessage(cmd, param, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                if (i == 85) { //消息体太长
                    MyToast.showBottom("您发的内容过长");
                } else if (i == 6011) {//群主不存在
                    MyToast.showBottom("主播不在线");
                }
                // 错误码code和错误描述desc，可用于定位请求失败原因
                // 错误码code含义请参见错误码表
                MyLogUtils.e(TAG + "send message failed. code: " + i + " errmsg: " + s);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                MyLogUtils.i(TAG + "onSuccess ");
            }
        });
    }

    public void sendGroupText(TIMMessage Nmsg) {
        if (mGroupConversation != null)
            mGroupConversation.sendMessage(Nmsg, new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(int i, String s) {
                    if (i == 85) { //消息体太长
                        MyToast.showBottom("您发的内容过长");
                    } else if (i == 6011) {//群主不存在
                        MyToast.showBottom("主播不在线");
                    }
                    MyLogUtils.e(TAG + "send message failed. code: " + i + " errmsg: " + s);
                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    //发送成回显示消息内容
                    for (int j = 0; j < timMessage.getElementCount(); j++) {
                        TIMElem elem = (TIMElem) timMessage.getElement(0);
                        if (timMessage.isSelf()) {
                            handleTextMessage(elem, mNickName);
                        } else {
                            TIMUserProfile sendUser = timMessage.getSenderProfile();
                            String name;
                            if (sendUser != null) {
                                name = sendUser.getNickName();
                            } else {
                                name = timMessage.getSender();
                            }
                            //String sendId = timMessage.getSender();
                            handleTextMessage(elem, name);
                        }
                    }
                    MyLogUtils.i(TAG + "Send text Msg ok");
                }
            });
    }

    /**
     * 群消息回调
     */
    private TIMMessageListener msgListener = new TIMMessageListener() {
        /**
         * 收到新消息
         * @param list
         * @return
         */
        @Override
        public boolean onNewMessages(List<TIMMessage> list) {
            //MyLogUtils.d(TAG, "onNewMessages readMessage " + list.size());
            //解析TIM推送消息
            parseIMMessage(list);
            return false; // 返回true将终止回调链，不再调用下一个新消息监听器
        }
    };

    /**
     * 解析消息回调
     *
     * @param list 消息列表
     */
    private void parseIMMessage(List<TIMMessage> list) {
        List<TIMMessage> tlist = list;

        if (tlist.size() > 0) {
            if (mGroupConversation != null)
                mGroupConversation.setReadMessage(tlist.get(0));
            // timestamp 消息在服务端生成的时间戳
            MyLogUtils.d(TAG + "parseIMMessage readMessage " + tlist.get(0).timestamp());
        }

        for (int i = tlist.size() - 1; i >= 0; i--) {
            TIMMessage currMsg = tlist.get(i);

            // getElementCount 获取元素个数
            for (int j = 0; j < currMsg.getElementCount(); j++) {
                if (currMsg.getElement(j) == null)
                    continue;

                // 获取消息元素
                TIMElem elem = currMsg.getElement(j);
                // 获取当前元素的类型
                TIMElemType type = elem.getType();
                // 获取消息发送方
                String sendId = currMsg.getSender();

                //系统消息
                if (type == TIMElemType.GroupSystem) {
                    if (TIMGroupSystemElemType.TIM_GROUP_SYSTEM_DELETE_GROUP_TYPE == ((TIMGroupSystemElem) elem).getSubtype()) {
                        if (null != mContext) {
                            mContext.sendBroadcast(new Intent(Constant.ACTION_HOST_LEAVE));
                        }
                    }
                }
                //定制消息
                if (type == TIMElemType.Custom) {
                    String id, nickname;
                    if (currMsg.getSenderProfile() != null) {
                        id = currMsg.getSenderProfile().getIdentifier();
                        nickname = currMsg.getSenderProfile().getNickName();
                    } else {
                        id = sendId;
                        nickname = sendId;
                    }
//                    handleCustomMsg(elem, id, nickname);
                    continue;
                }
                //其他群消息过滤
                if (currMsg.getConversation() != null && currMsg.getConversation().getPeer() != null) {
                    if (!CurLiveInfo.getGroupNum().equals(currMsg.getConversation().getPeer())) {
                        continue;
                    }
                }
                //最后处理文本消息
                if (type == TIMElemType.Text) {
                    if (currMsg.isSelf()) { // 可以判断消息是否是自己发出的消息，界面显示时可用
                        handleTextMessage(elem, mNickName);
                    } else {
                        String nickname;
                        // getSenderProfile 获取发送者个人资料
                        if (currMsg.getSenderProfile() != null && (!currMsg.getSenderProfile().getNickName().equals(""))) {
                            nickname = currMsg.getSenderProfile().getNickName();
                        } else {
                            nickname = sendId;
                        }
                        handleTextMessage(elem, nickname);
                    }
                }

            }
        }
    }

    /**
     * 处理文本消息解析
     *
     * @param elem
     * @param name
     */
    private void handleTextMessage(TIMElem elem, String name) {
        TIMTextElem textElem = (TIMTextElem) elem;
//        Toast.makeText(mContext, "" + textElem.getText(), Toast.LENGTH_SHORT).show();
        mLiveView.refreshText(textElem.getText(), name);
//        sendToUIThread(REFRESH_TEXT, textElem.getText(), sendId);
        MyToast.showBottom(name + ": " + textElem.getText());
    }

    /**
     * 已经发完退出消息了
     */
    private void notifyQuitReady() {
        // 删除一个消息监听器，消息监听器被删除后，将不再被调用。
        TIMManager.getInstance().removeMessageListener(msgListener);
    }

    public void perpareQuitRoom(boolean bPurpose) {
        if (bPurpose) {
            sendGroupMessage(Constant.AVIMCMD_ExitLive, "", new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(int i, String s) {
                    notifyQuitReady();
                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    notifyQuitReady();
                }
            });
        } else {
            notifyQuitReady();
        }
    }

    public void pause() {
        isBakCameraOpen = isOpenCamera;
        isBakMicOpen = isMicOpen;
        if (isBakCameraOpen || isBakMicOpen) {    // 若摄像头或Mic打开
            sendGroupMessage(Constant.AVIMCMD_Host_Leave, "", new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(int i, String s) {
                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                }
            });
        }
    }

    public void resume() {
        if (isBakCameraOpen || isBakMicOpen) {
            sendGroupMessage(Constant.AVIMCMD_Host_Back, "", new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(int i, String s) {
                }

                @Override
                public void onSuccess(TIMMessage timMessage) {

                }
            });
        }
    }

//    /**
//     * 处理定制消息 赞 关注 取消关注
//     *
//     * @param elem
//     */
   /* private void handleCustomMsg(TIMElem elem, String identifier, String nickname) {
        try {
            String customText = new String(((TIMCustomElem) elem).getData(), "UTF-8");
            MyLogUtils.i(TAG + "cumstom msg  " + customText);

            JSONTokener jsonParser = new JSONTokener(customText);
            // 此时还未读取任何json文本，直接读取就是一个JSONObject对象。
            // 如果此时的读取位置在"name" : 了，那么nextValue就是"yuanzhifei89"（String）
            JSONObject json = (JSONObject) jsonParser.nextValue();
            int action = json.getInt(Constant.CMD_KEY);
            switch (action) {
                case Constant.AVIMCMD_MUlTI_HOST_INVITE:
                    MyLogUtils.d(TAG + LogConstants.ACTION_VIEWER_SHOW + LogConstants.DIV + id + LogConstants.DIV + "receive invite message" +
                            LogConstants.DIV + "id " + identifier);
                    if (mLiveView != null)
                        mLiveView.showInviteDialog();
                    break;
                case Constant.AVIMCMD_MUlTI_JOIN:
                    MyLogUtils.i(TAG + " handleCustomMsg " + identifier);
                    if (null != mLiveView)
                        mLiveView.cancelInviteView(identifier);
                    break;
                case Constant.AVIMCMD_MUlTI_REFUSE:
                    if (null != mLiveView)
                        mLiveView.cancelInviteView(identifier);
                    if (null != mContext) {
                        Toast.makeText(mContext, identifier + " refuse !", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constant.AVIMCMD_Praise:
                    if (null != mLiveView)
                        mLiveView.refreshThumbUp();
                    break;
                case Constant.AVIMCMD_EnterLive:
                    //mLiveView.refreshText("Step in live", sendId);
                    if (mLiveView != null)
                        mLiveView.memberJoin(identifier, nickname);
                    break;
                case Constant.AVIMCMD_ExitLive:
                    //mLiveView.refreshText("quite live", sendId);
                    if (mLiveView != null)
                        mLiveView.memberQuit(identifier, nickname);
                    break;
                case Constant.AVIMCMD_MULTI_CANCEL_INTERACT://主播关闭摄像头命令
                    //如果是自己关闭Camera和Mic
                    String closeId = json.getString(Constant.CMD_PARAM);
                    if (closeId.equals(id)) {//是自己
                        //TODO 被动下麦 下麦 下麦
//                        changeAuthandRole(false, Constant.NORMAL_MEMBER_AUTH, Constant.NORMAL_MEMBER_ROLE);

                    }
                    //其他人关闭小窗口
//                    QavsdkControl.getInstance().closeMemberView(closeId);
                    if (mLiveView != null) {
                        mLiveView.hideInviteDialog();
                        mLiveView.refreshUI(closeId);
                    }
                    break;
                case Constant.AVIMCMD_MULTI_HOST_CANCELINVITE:
                    if (null != mLiveView)
                        mLiveView.hideInviteDialog();
                    break;
                case Constant.AVIMCMD_MULTI_HOST_CONTROLL_CAMERA:
//                    toggleCamera();
                    break;
                case Constant.AVIMCMD_MULTI_HOST_CONTROLL_MIC:
//                    toggleMic();
                    break;
                case Constant.AVIMCMD_Host_Leave:
                    if (mLiveView != null)
                        mLiveView.hostLeave(identifier, nickname);
                    break;
                case Constant.AVIMCMD_Host_Back:
                    if (mLiveView != null)
                        mLiveView.hostBack(identifier, nickname);
                default:
                    break;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException ex) {
            // 异常处理代码
        }
    }*/


    public boolean isFrontCamera() {
        return mIsFrontCamera;
    }

    private boolean mIsFrontCamera = true;

    public void sendC2CMessage(final int cmd, String Param, final String sendId) {
        JSONObject inviteCmd = new JSONObject();
        try {
            inviteCmd.put(Constant.CMD_KEY, cmd);
            inviteCmd.put(Constant.CMD_PARAM, Param);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String cmds = inviteCmd.toString();
        MyLogUtils.i(TAG + " send cmd : " + cmd + "|" + cmds);
        TIMMessage msg = new TIMMessage();
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(cmds.getBytes());
        elem.setDesc("");
        msg.addElement(elem);
        mC2CConversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, sendId);
        mC2CConversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                MyLogUtils.e(TAG + " enter error" + i + ": " + s);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                MyLogUtils.i(TAG + " send praise succ !");
            }
        });
    }


    private TIMAvManager.RoomInfo roomInfo;
    private long streamChannelID;

//    public void stopPushAction() {
//        MyLogUtils.d(TAG + " Push stop Id " + streamChannelID);
//        List<Long> myList = new ArrayList<Long>();
//        myList.add(streamChannelID);
//        TIMAvManager.getInstance().requestMultiVideoStreamerStop(roomInfo, myList, new TIMCallBack() {
//            @Override
//            public void onError(int i, String s) {
//                MyLogUtils.e(TAG + " stop  push error " + i + " : " + s);
//                if (null != mContext) {
//                    Toast.makeText(mContext, " stop stream error,try again " + i + " : " + s, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onSuccess() {
//                MyLogUtils.i(TAG + " stop push success ");
//                if (null != mLiveView) {
//                    mLiveView.stopStreamSucc();
//                }
//            }
//        });
//    }

    private boolean isInRecord = false;

//    public void startRecord(TIMAvManager.RecordParam mRecordParam) {
//        TIMAvManager.RoomInfo roomInfo = TIMAvManager.getInstance().new RoomInfo();
//        roomInfo.setRelationId(CurLiveInfo.getRoomNum());
//        roomInfo.setRoomId(CurLiveInfo.getRoomNum());
//        TIMAvManager.getInstance().requestMultiVideoRecorderStart(roomInfo, mRecordParam, new TIMCallBack() {
//            @Override
//            public void onError(int i, String s) {
//                MyLogUtils.e(TAG + " start record error " + i + "  " + s);
//                mLiveView.startRecordCallback(false);
//            }
//
//            @Override
//            public void onSuccess() {
//                isInRecord = true;
//                MyLogUtils.i(TAG + " start record success ");
//                mLiveView.startRecordCallback(true);
//            }
//        });
//
//    }


//    public void stopRecord() {
//        if (isInRecord) {
//            TIMAvManager.RoomInfo roomInfo = TIMAvManager.getInstance().new RoomInfo();
//            roomInfo.setRelationId(CurLiveInfo.getRoomNum());
//            roomInfo.setRoomId(CurLiveInfo.getRoomNum());
//            TIMAvManager.getInstance().requestMultiVideoRecorderStop(roomInfo, new TIMValueCallBack<List<String>>() {
//                @Override
//                public void onError(int i, String s) {
//                    MyLogUtils.e(TAG + " stop record error " + i + " : " + s);
//                    if (mLiveView != null)
//                        mLiveView.stopRecordCallback(false, null);
//                }
//
//                @Override
//                public void onSuccess(List<String> files) {
//                    isInRecord = false;
//                    MyLogUtils.e(TAG + " stop record success ");
//                    if (mLiveView != null)
//                        mLiveView.stopRecordCallback(true, files);
//
//
//                }
//            });
//        }
//    }


    @Override
    public void onDestory() {
        mLiveView = null;
        mContext = null;
    }
}
