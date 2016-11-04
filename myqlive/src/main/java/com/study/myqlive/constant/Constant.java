package com.study.myqlive.constant;

import com.study.myqlive.R;
import com.study.myqlive.utils.UIUtiles;
import com.tencent.av.sdk.AVRoomMulti;

import static java.lang.annotation.ElementType.PACKAGE;

/**
 * Created by xgy on 2015/12/21.
 * 常量类
 */
public class Constant {

    /**
     * 服务器选择
     */
    public enum Server {
        OFFICIAL, // 正式
        TEST, // 测试
        DEVELOP // 开发
    }

    public static int WELCOME_DELAY = 1000;
    public static int MSG_WHAT0 = 0;
    public static int MSG_WHAT1 = 1;

    /**
     * 订单类型
     */
    public enum OrderType {
        ORDER("order_new"), // 订单
        RAFFLE("raffle"), // 抽奖
        QIMANET("qimanet"), // 奇码网
        PARK("park");

        private String value;

        OrderType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 数据请求成功
     */
    public static final int SUCCESS_CODE = 200000;
    public static final int TOKEN_ERROR_CODE = 200401;
    /**
     * 数据请求错误码
     */
    public static final int ERROR_CODE = 200444;

    /**
     * 设置request的重新请求策略 的参数
     */
    public static final int R_SOCKET_TIMEOUT = 5000;
    public static final int MAX_RETRIES = 0;

    /**
     * 用来区分客户端（ios:2, android:1）、微信(普通网页):3
     */
    public enum Client {
        ANDROID(1, "android"), IOS(2, "ios"), WECHAT(3, "wechat");
        private String value;
        private int id;

        Client(int id, String value) {
            this.id = id;
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 获取Android客户端
     *
     * @return
     */
    public static int getAndroidId() {
        return Client.ANDROID.getId();
    }

    //---------------------------------------------------------------------------
    /**
     * 直播需要的参数
     */
    public static final String LIVE_DOMAIN = "kakasure.gensee.com";

    /**
     * app类型：1买家，2卖家，3码商
     */
    public static final int typeApp = 1;
//---------------------------------------------------------------------------
    /**
     * 获得积分：类型：3登录；6分享客户端；
     */
    public static final int TYPE_LOGIN = 3;
    public static final int TYPE_SHARE = 6;
//---------------------------------------------------------------------------
    /**
     * 不是扫码操作，不是强制更新
     */
    public static final String N = "n";
    /**
     * 是扫码操作, 强制更新
     */
    public static final String Y = "y";
//---------------------------------------------------------------------------
    /**
     * 订单状态（全部：'all'）
     */
    public static final String ORDER_ALL = "all";
    /**
     * 待付款：'WAIT_BUYER_PAY'
     */
    public static final String ORDER_CREATED = "WAIT_BUYER_PAY";
    /**
     * 待发货：'WAIT_SELLER_SEND_GOODS'
     */
    public static final String ORDER_PAID = "WAIT_SELLER_SEND_GOODS";
    /**
     * 待收货：'WAIT_BUYER_CONFIRM_GOODS'
     */
    public static final String ORDER_DELIVERED = "WAIT_BUYER_CONFIRM_GOODS";
    /**
     * 待评价/已完成：'TRADE_FINISHED'
     */
    public static final String ORDER_FINISHED = "TRADE_FINISHED";
    /**
     * 售后
     */
    public static final String ORDER_AFTER_SALES = "AFTER_SALES";
//---------------------------------------------------------------------------
    /**
     * status  = 0  // 待付款
     */
    public static final int STATUS_WAIT_BUYER_PAY = 0;
    /**
     * status  = 2  // 待确认
     */
    public static final int STATUS_WAIT_COMFIRM = 2;
    /**
     * status  = 3  // 已收货 ---> 买家手动收货
     */
    public static final int STATUS_DELIVERED = 3;
    /**
     * status  = 12 // 已发货
     */
    public static final int STATUS_WAIT_SELLER_SEND_GOODS = 12;
    /**
     * status  = 13  // 已收货 ---> 自动延期收货
     */
    public static final int STATUS_DELIVERED_SYSTEM = 13;
    /**
     * status  = 15 // 服务类，付款后，待消费
     */
    public static final int STATUS_SERVICE_PAYED = 15;
//---------------------------------------------------------------------------
    /**
     * 001 代表登陆
     */
    public static final String POINT_CODE_LOGIN = "001";
    /**
     * 002 代表确认收货,确认消费，自动收货
     */
    public static final String POINT_CODE_ORDER = "002";
    /**
     * 003 代表评论
     */
    public static final String POINT_CODE_COMMENT = "003";
    /**
     * 004代表分享
     */
    public static final String POINT_CODE_SHARE = "004";

    /**
     * 微信登录
     */
    public static final String WECHAT_APP_ID = "wxdb7df1ea20e1c92e";

    /**
     * 赠送礼物
     */
    public static final int[] GIFT_IMAGES = new int[]{R.mipmap.gift01_666, R.mipmap.gift03_lollipop, R.mipmap.gift05_mmda, R.mipmap.gift07_reward,
            R.mipmap.gift02_flower, R.mipmap.gift04_luckystar, R.mipmap.gift06_headline, R.mipmap.gift08_goldmicrophone,
            R.mipmap.gift09_cake, R.mipmap.gift11_hug, R.mipmap.gift13_thanks, R.mipmap.gift15_redrose,
            R.mipmap.gift10_duang, R.mipmap.gift12_balloon, R.mipmap.gift14_kiss, R.mipmap.gift16_bluerose,
            R.mipmap.gift17_hongbao, R.mipmap.gift19_fireworks, R.mipmap.gift18_starshower, R.mipmap.gift20_champagne};
    public static final String[] GIFT_TITLES = UIUtiles.getStringArray(R.array.point_boba_item_title);
    public static final int[] GIFT_POINTS = UIUtiles.getIntegerArray(R.array.point_boba_item_num);

    /**
     * 从哪一个页面发起的登录请求
     */
    public static final int FORM_MADIANFRAG = 1;
    public static final int FORM_CARTFRAG = 3;
    public static final int FORM_MSG = 5;
    public static final int FORM_RECODE = 6;
    public static final int FORM_BALANCE = 7;
    public static final int FORM_ORDER = 8;
    public static final int FORM_FUKUAN = 9;
    public static final int FORM_FAHUO = 10;
    public static final int FORM_PINGJIA = 11;
    public static final int FORM_SHOUHUO = 12;
    public static final int FORM_JIFEN = 13;
    public static final int FORM_HONGBAO = 14;
    public static final int FORM_FEEDBACK = 15;
    public static final int FORM_GOCARTLIST = 16;
    public static final int FORM_BUY = 17;
    public static final int FORM_ADDCART = 18;
    public static final int FORM_PERSONAL = 19;
    public static final int FORM_QIMADETAIL = 20;
    public static final int FORM_MEFRAG = 21;
    public static final int FORM_SCANRECORD = 22;
    public static final int FORM_MYJIFEN = 23;
    public static final int FORM_REBATE = 24;
    public static final int FORM_MYORDER = 25;
    public static final int FORM_BOBAFRAG = 26;
    public static final int FORM_BOBADETAIL = 27;
    public static final int FORM_CONFIRMORDER = 28;
    public static final int FROM_MYJIFENRECHARGE = 29;
    public static final int FROM_AFTERSAFEDETAIL = 30;
    public static final int FROM_ORDERITEMLIST = 31;
    public static final int FROM_MORECOMMENT = 32;
    public static final int FROM_QIMANEWDETAIL = 33;
    public static final int FROM_MADIANDETAILFRAG = 34;

    /**
     * IM腾讯云 begin
     */
    public static final int WRITE_PERMISSION_REQ_CODE = 2;
    public static final int VIDEO_VIEW_MAX = 4;

    public static final int ACCOUNT_TYPE = 8177; // accountType
    public static final int SDK_APPID = 1400017320; // SdkAppId

    public static final String APPLY_CHATROOM = "申请加入";

    public static final int IS_ALREADY_MEMBER = 10013;

    public static final int IS_ALREADY_IN_ROOM = 10025;

    public static final int AVIMCMD_MULTI = 0x800;             // 多人互动消息类型
    public static final int AVIMCMD_MUlTI_HOST_INVITE = AVIMCMD_MULTI + 1;         // 多人主播发送邀请消息, C2C消息
    public static final int AVIMCMD_MULTI_CANCEL_INTERACT = AVIMCMD_MUlTI_HOST_INVITE + 1;       // 断开互动，Group消息，带断开者的imUsreid参数
    public static final int AVIMCMD_MUlTI_JOIN = AVIMCMD_MULTI_CANCEL_INTERACT + 1;       // 多人互动方收到AVIMCMD_Multi_Host_Invite多人邀请后，同意，C2C消息
    public static final int AVIMCMD_MUlTI_REFUSE = AVIMCMD_MUlTI_JOIN + 1;      // 多人互动方收到AVIMCMD_Multi_Invite多人邀请后，拒绝，C2C消息

    public static final int AVIMCMD_Multi_Host_EnableInteractMic = AVIMCMD_MUlTI_REFUSE + 1;  // 主播打开互动者Mic，C2C消息
    public static final int AVIMCMD_Multi_Host_DisableInteractMic = AVIMCMD_Multi_Host_EnableInteractMic + 1;// 主播关闭互动者Mic，C2C消息
    public static final int AVIMCMD_Multi_Host_EnableInteractCamera = AVIMCMD_Multi_Host_DisableInteractMic + 1; // 主播打开互动者Camera，C2C消息
    public static final int AVIMCMD_Multi_Host_DisableInteractCamera = AVIMCMD_Multi_Host_EnableInteractCamera + 1; // 主播打开互动者Camera，C2C消息
    public static final int AVIMCMD_MULTI_HOST_CANCELINVITE = AVIMCMD_Multi_Host_DisableInteractCamera + 1;
    public static final int AVIMCMD_MULTI_HOST_CONTROLL_CAMERA = AVIMCMD_MULTI_HOST_CANCELINVITE + 1;
    public static final int AVIMCMD_MULTI_HOST_CONTROLL_MIC = AVIMCMD_MULTI_HOST_CONTROLL_CAMERA + 1;

    public static final String ACTION_CLOSE_CONTEXT_COMPLETE = PACKAGE
            + ".ACTION_CLOSE_CONTEXT_COMPLETE";
    public static final String ACTION_SURFACE_CREATED = PACKAGE
            + ".ACTION_SURFACE_CREATED";
    public static final String ACTION_HOST_ENTER = PACKAGE
            + ".ACTION_HOST_ENTER";
    public static final String ACTION_SWITCH_VIDEO = PACKAGE
            + ".ACTION_SWITCH_VIDEO";
    public static final String ACTION_HOST_LEAVE = PACKAGE
            + ".ACTION_HOST_LEAVE";
    public static final String ACTION_CAMERA_OPEN_IN_LIVE = PACKAGE
            + ".ACTION_CAMERA_OPEN_IN_LIVE";
    public static final String ACTION_SCREEN_SHARE_IN_LIVE = PACKAGE
            + ".ACTION_SCREEN_SHARE_IN_LIVE";
    public static final String ACTION_CAMERA_CLOSE_IN_LIVE = PACKAGE
            + ".ACTION_CAMERA_CLOSE_IN_LIVE";

    public static final int DEMO_ERROR_BASE = -99999999;
    public static final int DEMO_ERROR_NULL_POINTER = DEMO_ERROR_BASE + 1; // 空指针
    public static final String BD_EXIT_APP = "bd_sxb_exit";
    public static final int AUDIO_VOICE_CHAT_MODE = 0;

    public static final int HOST = 1;
    public static final int MEMBER = 0;

    public static final long HOST_AUTH = AVRoomMulti.AUTH_BITS_DEFAULT;//权限位；TODO：默认值是拥有所有权限。
    public static final long VIDEO_MEMBER_AUTH = AVRoomMulti.AUTH_BITS_DEFAULT;//权限位；TODO：默认值是拥有所有权限。
    public static final long NORMAL_MEMBER_AUTH = AVRoomMulti.AUTH_BITS_JOIN_ROOM | AVRoomMulti.AUTH_BITS_RECV_AUDIO | AVRoomMulti.AUTH_BITS_RECV_CAMERA_VIDEO | AVRoomMulti.AUTH_BITS_RECV_SCREEN_VIDEO;

    public static final String HOST_ROLE = "Host";
    public static final String VIDEO_MEMBER_ROLE = "VideoMember";
    public static final String NORMAL_MEMBER_ROLE = "NormalMember";

    public static final String CMD_KEY = "userAction";
    public static final String CMD_PARAM = "actionParam";

    public static final int AVIMCMD_Text = -1;         // 普通的聊天消息
    public static final int AVIMCMD_None = AVIMCMD_Text + 1;               // 无事件
    // 以下事件为TCAdapter内部处理的通用事件
    public static final int AVIMCMD_EnterLive = AVIMCMD_None + 1;          // 用户加入直播, Group消息  1
    public static final int AVIMCMD_ExitLive = AVIMCMD_EnterLive + 1;         // 用户退出直播, Group消息  2
    public static final int AVIMCMD_Praise = AVIMCMD_ExitLive + 1;           // 点赞消息, Demo中使用Group消息  3
    public static final int AVIMCMD_Host_Leave = AVIMCMD_Praise + 1;         // 主播离开, Group消息 ： 4
    public static final int AVIMCMD_Host_Back = AVIMCMD_Host_Leave + 1;         // 主播回来, Demo中使用Group消息 ： 5
    /**
     * IM腾讯云 end
     */
}
