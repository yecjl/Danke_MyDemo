package com.kakasure.myframework.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kakasure.myframework.R;
import com.kakasure.myframework.utils.StringUtil;
import com.kakasure.myframework.utils.UIUtil;

/**
 * 功能：选择对话框
 * Created by danke on 2016/1/18.
 */
public class ProgressDialogAlert extends Dialog {
    private Context context;
    private String title;
    private String message;
    private String buttonLeftText;
    private String buttonRightText;
    private ClickListenerInterface clickListenerInterface;
    private TextView tvMessage;
    private TextView tvTitle;
    private TextView tvLeft;
    private TextView tvRight;
    private View line;
    private int minHeight;

    public ProgressDialogAlert(Context context, String title, String message,
                               String buttonText) {
        this(context, title, message, null, buttonText, 0);
    }

    public ProgressDialogAlert(Context context, String title, String message,
                               String buttonLeftText, String buttonRightText) {
        this(context, title, message, buttonLeftText, buttonRightText, 0);
    }

    public ProgressDialogAlert(Context context, String title, String message,
                               String buttonLeftText, String buttonRightText, int minHeight) {
        super(context, R.style.ProgressDialogAlert);

        this.context = context;
        this.title = title;
        this.message = message;
        this.buttonLeftText = buttonLeftText;
        this.buttonRightText = buttonRightText;
        this.minHeight = minHeight;
    }

    public interface ClickListenerInterface {
        void doLeft();

        void doRight();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.progress_alert_view, null);
        setContentView(view);

        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvLeft = (TextView) view.findViewById(R.id.tvBtnLeft);
        tvRight = (TextView) view.findViewById(R.id.tvBtnRight);
        line = findViewById(R.id.line);

        setTitle(title);
        setMessage(message);

        // 如果没有左边的按钮，就隐藏起来
        hideLeft(buttonLeftText == null);

        tvRight.setText(buttonRightText);
        tvRight.setOnClickListener(new clickListener());

        // 设置宽度
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);

        // 设置内容高度
        if (minHeight != 0) {
            tvMessage.setMinimumHeight(minHeight);
        }
    }

    public void setClickListener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tvBtnLeft) {
                clickListenerInterface.doLeft();
            } else if (id == R.id.tvBtnRight) {
                clickListenerInterface.doRight();
            }
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setTitle(String title) {
        this.title = title;
        if (StringUtil.isNull(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 注意：需要放在show后面，否则的话tvMessage为空
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
        if (StringUtil.isNull(message)) {
            tvMessage.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvTitle.getLayoutParams();
            params.setMargins(UIUtil.Dp2Px(12), 0, UIUtil.Dp2Px(12), 0);
            tvTitle.setLayoutParams(params);
        } else {
            tvMessage.setText(message);
            tvMessage.setVisibility(View.VISIBLE);
        }
    }

    public void hideLeft(boolean isHide) {
        if (isHide) {
            tvLeft.setVisibility(View.GONE);
            tvLeft.setOnClickListener(null);
            line.setVisibility(View.GONE);
        } else {
            tvLeft.setText(buttonLeftText);
            tvLeft.setOnClickListener(new clickListener());
        }
    }
}
