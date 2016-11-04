package com.kakasure.myframework.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.kakasure.myframework.utils.SystemUtil;

/**
 * 设置请求管理
 * Created by danke on 2015/12/30.
 */
public class RequestManager {
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    private RequestManager() {
    }

    public static void init(Context context, ImageLoader.ImageCache imageCache) {
        mRequestQueue = Volley.newRequestQueue(context);
        if (imageCache == null) {
            /**
             * 图片在内存缓存为系统内存的1/8，磁盘缓存1周
             */
            imageCache = new BitmapLruCache(SystemUtil.getMemorySize(context) / 8);
        }

        mImageLoader = new ImageLoader(mRequestQueue, imageCache);
    }

    /**
     * 获取请求队列
     *
     * @return
     */
    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    /**
     * 在请求队列中添加请求
     *
     * @param request
     */
    public static void addRequest(Request<?> request) {
        mRequestQueue.add(request);
    }

    /**
     * 取消所有的请求
     *
     * @param request
     */
    public static void cancelAll(Request<?> request) {
        if (request.getTag() != null && mRequestQueue != null)
            mRequestQueue.cancelAll(request.getTag());
    }

    /**
     * 取消所有的tag
     *
     * @param tag
     */
    public static void cancelAll(Object tag) {
        if (tag != null && mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * 获取imageLoader
     * Returns instance of ImageLoader initialized with {@see FakeImageCache}
     * which effectively means that no memory caching is used. This is useful
     * for images that you know that will be showLoadingView only once.
     *
     * @return
     */
    public static ImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }
}
