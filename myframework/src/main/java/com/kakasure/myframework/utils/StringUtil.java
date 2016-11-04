package com.kakasure.myframework.utils;

import android.widget.EditText;
import android.widget.TextView;

import com.kakasure.myframework.MyFramework;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;

/**
 * Created by danke on 15/5/6.
 */
public class StringUtil {

    private StringUtil() {
    }

    /**
     * 获取输入框的内容
     *
     * @param textView
     * @return
     */
    public static String getInput(TextView textView) {
        String s = null;
        s = textView.getText().toString();
        if (!s.equals(s.trim())) {
            textView.setText(s.trim());
        }
        return s;
    }

    /**
     * 获取输入框的内容
     *
     * @param editText
     * @return
     */
    public static String getInput(EditText editText) {
        String s = null;
        s = editText.getText().toString();
        if (!s.equals(s.trim())) {
            editText.setText(s.trim());
        }
        return s;
    }


    public static String encodeStr(String str) {
        try {
            str = URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * 检查输入框内容是否为空
     *
     * @param editText
     * @return
     */
    public static boolean isNull(EditText editText) {
        return isNull(editText, 1);
    }

    /**
     * 检查输入框内容是否为空
     *
     * @param editText
     * @param minLength 允许输入的最短长度
     * @return
     */
    public static boolean isNull(EditText editText, int minLength) {
        if (editText == null) return false;
        return editText.getText().toString().trim().length() < (minLength < 1 ? 1 : minLength);
    }


    /**
     * 检查输入框内容是否为空
     *
     * @param textView
     * @return
     */
    public static boolean isNull(TextView textView) {
        return isNull(textView, 1);
    }

    /**
     * 检查输入框内容是否为空
     *
     * @param textView
     * @param minLength 允许输入的最短长度
     * @return
     */
    public static boolean isNull(TextView textView, int minLength) {
        if (textView == null) return false;
        return textView.getText().toString().trim().length() < (minLength < 1 ? 1 : minLength);
    }

    /**
     * 检查字符是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        return isNull(str, 1);
    }

    /**
     * 检查字符是否为空
     *
     * @param str
     * @param minLength 允许输入的最短长度
     * @return
     */
    public static boolean isNull(String str, int minLength) {
        if (str == null) return true;
        return str.trim().length() < (minLength < 1 ? 1 : minLength);
    }

    public static String getString(int resId) {
        return MyFramework.getAppContext().getResources().getString(resId);
    }

    public static String getFormatTwoNums(int i) {
        return i / 10 == 0 ? "0" + i : i + "";
    }

    public static String getFormatMillis(int millis) {
        return millis / 30 == 0 ? "00" : "30";
    }


    public static double round(float w, int scale) {
        return round(Double.parseDouble(String.valueOf(w)), scale);
    }

    /**
     * 提供精确的小数位四舍五入处理
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留的位数
     * @return 四舍五入后的结果
     */
    public static double round(Double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = null == v ? new BigDecimal("0.0") : new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 格式化电话串
     *
     * @param tel
     * @return
     */
    public static String formatTel(String tel) {
        String str = "";
        str = tel.substring(0, 3) + "-" + tel.substring(3, 7) + "-" + tel.substring(7, tel.length());
        return str;
    }

    /**
     * 将请求的字符数组转换为能够请求的字符串
     *
     * @param str
     * @return
     */
    public static String parseArray(String[] str) {
        if (str != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < str.length - 1; i++) {
                builder.append(str[i] + ",");
            }
            if (str.length != 0) {
                return builder.append(str[str.length - 1]).toString();
            }
        }
        return "";
    }

    /**
     * 检查手机号码是否正确
     *
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        // phone.matches("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(177))\\d{8}$")
        if (phone != null && phone.matches("^(1)\\d{10}$")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查手机号码是否正确
     *
     * @param editText
     * @return
     */
    public static boolean checkPhone(EditText editText) {
        String phone = getInput(editText);
        if (phone != null && phone.matches("^(1)\\d{10}$")) {
            return true;
        } else {
            return false;
        }
    }

}
