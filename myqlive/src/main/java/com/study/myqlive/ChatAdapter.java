package com.study.myqlive;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakasure.myframework.utils.UIUtil;
import com.kakasure.myframework.view.VerticalImageSpan;
import com.kakasure.myframework.widget.SimpleAdapter;
import com.kakasure.myframework.widget.ViewHolder;
import com.study.myqlive.constant.Constant;
import com.study.myqlive.utils.UIUtiles;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 功能：
 * Created by danke on 2016/6/24.
 */
public class ChatAdapter extends SimpleAdapter<AbsChatMessage> {
    private static final int TYPE_CHAT = 0;
    private static final int TYPE_JOIN = 1;
    private String username;
    private long mUserId;
    private final String[] titles;
    private final int[] images;
    private final LinkedHashMap<String, Integer> browMap;

    public ChatAdapter(Context context, LinkedHashMap<String, Integer> browMap) {
        super(context);
        titles = Constant.GIFT_TITLES;
        images = Constant.GIFT_IMAGES;
        this.browMap = browMap;
    }

    public void init(long mUserID, String username) {
        this.mUserId = mUserID;
        this.username = username;
        mList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            AbsChatMessage absChatMessage = new AbsChatMessage();
            absChatMessage.setText("danke" + i);
            mList.add(absChatMessage);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder<AbsChatMessage> getViewHolder() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
//        if (mList != null && mList.size() > 0) {
//            return mList.get(position).isChat() ? TYPE_CHAT : TYPE_JOIN;
//        }

        return TYPE_CHAT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ChatViewHolder chatViewHolder = null;
        GiftViewHolder giftViewHolder = null;
        if (convertView == null) {
            switch (type) {
                case TYPE_CHAT:
                    convertView = UIUtiles.inflate(R.layout.item_chat);
                    chatViewHolder = new ChatViewHolder();
                    chatViewHolder.onFindView(convertView);
                    convertView.setTag(chatViewHolder);
                    break;
                case TYPE_JOIN:
                    convertView = UIUtiles.inflate(R.layout.item_chat_git);
                    giftViewHolder = new GiftViewHolder();
                    giftViewHolder.onFindView(convertView);
                    convertView.setTag(giftViewHolder);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_CHAT:
                    chatViewHolder = (ChatViewHolder) convertView.getTag();
                    break;
                case TYPE_JOIN:
                    giftViewHolder = (GiftViewHolder) convertView.getTag();
                    break;
            }
        }

        if (mList != null && mList.size() > 0 && chatViewHolder != null) {
            chatViewHolder.onBindData(mList.get(position));
        }
        if (mList != null && mList.size() > 0 && giftViewHolder != null) {
            giftViewHolder.onBindData(mList.get(position));
        }
        return convertView;
    }

    /**
     * 聊天
     */
    public class ChatViewHolder extends ViewHolder<AbsChatMessage> {
        @Bind(R.id.tv_storeName)
        TextView tvUsername;
        @Bind(R.id.tv_chatContext)
        TextView tvChatContext;

        @Override
        public void onFindView(View root) {
            ButterKnife.bind(this, root);
        }

        @Override
        public void onBindData(AbsChatMessage data) {
            tvUsername.setText(data.getSendUserName());
            // 设置聊天内容，带图片
            String text = data.getText();
            SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            String regex = "\\[([^\\[\\]]+)\\]";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(text);
            boolean find = m.find();
            if (find) {
                int start = m.start();
                int end = m.end();
                String group = m.group();
                Integer resId = browMap.get(group);
                if (resId != null) {
                    // 根据资源ID获得资源图像的Bitmap对象
                    Bitmap bitmap = BitmapFactory.decodeResource(UIUtiles.getResources(), resId);

                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    // 设置想要的大小
                    int newWidth = UIUtil.Dp2Px(22);
                    int newHeight = (int) (newWidth * 1.0 / width * height);
                    // 计算缩放比例
                    float scaleWidth = ((float) newWidth) / width;
                    float scaleHeight = ((float) newHeight) / height;
                    // 取得想要缩放的matrix参数
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
                    // 得到新的图片
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
                            true);
                    // 根据Bitmap对象创建ImageSpan对象
                    ImageSpan iconSpan = new VerticalImageSpan(context, bitmap);
                    // 增加两个空格
                    ssb = ssb.insert(start, " ");
                    //用ImageSpan替换文本
                    ssb.setSpan(iconSpan, start + 1, end + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    tvChatContext.setTextColor(UIUtiles.getColor(R.color.gift));
                } else {
                    tvChatContext.setTextColor(UIUtiles.getColor(R.color.white));
                }
            } else {
                tvChatContext.setTextColor(UIUtiles.getColor(R.color.white));
            }
            tvChatContext.setText(ssb);
        }
    }

    /**
     * 赠送礼物
     */
    public class GiftViewHolder extends ViewHolder<AbsChatMessage> {
        @Bind(R.id.tv_storeName)
        TextView tvUsername;
        @Bind(R.id.tv_giftNum)
        TextView tvGiftNum;
        @Bind(R.id.iv_gift)
        ImageView ivGift;

        @Override
        public void onFindView(View root) {
            ButterKnife.bind(this, root);
        }

        @Override
        public void onBindData(AbsChatMessage data) {
            tvUsername.setText(username);
            tvGiftNum.setText("送了" + data.getGifNum() + "个");
            ivGift.setImageResource(data.getGifId());
        }
    }
}
