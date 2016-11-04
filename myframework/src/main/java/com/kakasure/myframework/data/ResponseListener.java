package com.kakasure.myframework.data;

import com.android.volley.Response;

/**
 * 相应监听
 * Created by danke on 15/8/10.
 * 网络请求响应监听
 */
public interface ResponseListener<T> extends Response.ErrorListener, Response.Listener<T> {


}