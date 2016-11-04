package com.kakasure.myframework.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 功能：自定义WebView
 * Created by danke on 2016/1/30.
 */
public class MyWebView extends WebView {

    private PageStateListener listener;

    public PageStateListener getListener() {
        return listener;
    }

    public void setListener(PageStateListener listener) {
        this.listener = listener;
    }

    public MyWebView(Context context) {
        this(context, null);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initWebView();
    }

    private void initWebView() {
        /*
         * webSettings 保存着WebView中的状态信息。
         * 当WebView第一次被创建时，webSetting中存储的都为默认值。
         * WebSetting和WebView一一绑定的。
         * 如果webView被销毁了，那么我们再次调用webSetting中的方法时，会抛出异常。
         */

        WebSettings webSettings = getSettings();
        //允许在webview中执行JavaScript代码
        webSettings.setJavaScriptEnabled(true);
        //设置webview是否支持缩放
        webSettings.setSupportZoom(false);
        //是否支持app cache
        webSettings.setAppCacheEnabled(true);
        //WebViewClient就是帮助WebView处理各种通知、请求事件的
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (listener != null) {
                    listener.onShouldOverrideUrlLoading(view, url);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (listener != null) {
                    listener.onPageStarted(view, url, favicon);
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (listener != null) {
                    listener.onPageFinished(view, url);
                }
                super.onPageFinished(view, url);
            }
        });
        //WebChromeClient是辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等
        setWebChromeClient(new WebChromeClient());

//        if (jsObjectListener != null) {
//            webView.addJavascriptInterface(new JsObject(), "kks_android");//传递Java对象，JS通过别名可以直接调用obj的方法。
//        }

    }

    /**
     * 设置JavaScript 的调用对象
     *
     * @param jsObject
     * @param <T>
     */
    public <T> void setJsObject(T jsObject) {
        if (jsObject != null) {
            addJavascriptInterface(jsObject, "kks_android");//传递Java对象，JS通过别名可以直接调用obj的方法。
        }
    }

    /**
     * 设置WebView的加载url
     *
     * @param url
     */
    public void setLoadUrl(String url) {
        //加载本地html代码，此代码位于assets目录下，通过file:///android_asset/xxx.html访问。
        loadUrl(url);
    }

    public interface PageStateListener {

        /**
         * 开始加载
         *
         * @param view
         * @param url
         * @param favicon
         */
        void onPageStarted(WebView view, String url, Bitmap favicon);


        /**
         * 加载结束
         *
         * @param view
         * @param url
         */
        void onPageFinished(WebView view, String url);

        /**
         * 每次加载的时候
         * @param view
         * @param url
         */
        void onShouldOverrideUrlLoading(WebView view, String url);
    }
}
