package com.kakasure.myframework.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kakasure.myframework.R;
import com.kakasure.myframework.utils.StringUtil;

/**
 * 功能：选择对话框 -- 带输入框
 * Created by danke on 2016/1/18.
 */
public class ProgressDialogAlertEdit extends Dialog {
    private Context context;
    private String title;
    private String message;
    private String hint;
    private String content;
    private String buttonLeftText;
    private String buttonRightText;
    private ClickListenerInterface clickListenerInterface;
    private TextView tvMessage;
    private TextView tvTitle;
    private EditText etContent;

    public ProgressDialogAlertEdit(Context context, String title, String message, String hint, String content,
                                   String buttonLeftText, String buttonRightText) {
        super(context, R.style.ProgressDialogAlert);

        this.context = context;
        this.title = title;
        this.message = message;
        this.hint = hint;
        this.content = content;
        this.buttonLeftText = buttonLeftText;
        this.buttonRightText = buttonRightText;
    }

    public ProgressDialogAlertEdit(Context context, String title, String message, String hint, String content,
                                   String buttonText) {
        this(context, title, message, hint, content, null, buttonText);
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
        View view = inflater.inflate(R.layout.progress_alert_edit_view, null);
        setContentView(view);

        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        etContent = (EditText) view.findViewById(R.id.etContent);
        etContent.requestFocus();
        TextView tvLeft = (TextView) view.findViewById(R.id.tvBtnLeft);
        TextView tvRight = (TextView) view.findViewById(R.id.tvBtnRight);

        setTitle(title);
        setMessage(message);
        setContentHint(hint);
        setContent(content);

        // 如果没有左边的按钮，就隐藏起来
        if (buttonLeftText == null) {
            tvLeft.setVisibility(View.GONE);
            view.findViewById(R.id.line).setVisibility(View.GONE);
        } else {
            tvLeft.setText(buttonLeftText);
            tvLeft.setOnClickListener(new clickListener());
        }
        tvRight.setText(buttonRightText);

        tvRight.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();

        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);

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

    public void setMessage(String message) {
        this.message = message;
        if (StringUtil.isNull(message)) {
            tvMessage.setVisibility(View.GONE);
        } else {
            tvMessage.setText(message);
            tvMessage.setVisibility(View.VISIBLE);
        }
    }

    private void setContentHint(String hint) {
        this.hint = hint;
        if (!StringUtil.isNull(hint)) {
            etContent.setHint(hint);
        }
    }

    private void setContent(String content) {
        this.content = content;
        if (!StringUtil.isNull(content)) {
            etContent.setText(content);
            etContent.setSelection(content.length());
        }
    }

    public String getContent() {
        return etContent.getText().toString();
    }
}
