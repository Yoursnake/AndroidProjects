package com.example.shengliyi.photogallery.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by shengliyi on 2017/5/12.
 */

public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private boolean mHasQuit = false;
    private Handler mRequestHandler;
    // 线程安全的 HashMap
    private ConcurrentMap<T, String> mRequestMap = new ConcurrentHashMap<>();
    // 这个监听器把处理已下载图片的任务代理给 PhotoGalleryFragment
    private ThumbnailDownloadListener<T> mThumbnailDownloadListener;
    private Handler mResponseHandler;
    private LruCache<String, Bitmap> mCache;

    // 创建一个借口，在 PhotoGalleryFragment 使用回调来实现 onThumbnailDownloader 方法
    public interface ThumbnailDownloadListener<T> {
        void onThumbnailDownloaded(T target, Bitmap thumbnail);
    }

    public void setThumbnailDownloadListener(ThumbnailDownloadListener<T> listener) {
        mThumbnailDownloadListener = listener;
    }

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    T target = (T) msg.obj;
                    Log.i(TAG, "Got a request for URL:" + mRequestMap.get(target));
                    handleRequest(target);
                }
            }
        };

        // 建立 Cache
        int maxCacheSize = 4 * 1024 * 1024;
        mCache = new LruCache<>(maxCacheSize);
    }

    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }

    public void queueThumbnail(T target, String url) {
        Log.i(TAG, "Get an URL:" + url);
        if (url == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target)
                    .sendToTarget();
        }
    }

    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

    public void handleRequest(final T target) {
        final String url = mRequestMap.get(target);
        if (url == null) {
            return;
        } else {
            try {
                final Bitmap bitmap;
                if (mCache.get(url) == null) {
                    byte[] bitmapByte = new FlickrFetchr().getUrlBytes(url);
                    bitmap = BitmapFactory
                            .decodeByteArray(bitmapByte, 0, bitmapByte.length);
                    Log.i(TAG, "BitMap created");

                    mCache.put(url, bitmap);
                } else {
                    bitmap = mCache.get(url);
                    Log.i(TAG, "BitMap from Cache");
                }

                mResponseHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mRequestMap.get(target) != url && mHasQuit) {
                            return;
                        }

                        mRequestMap.remove(target);
                        mThumbnailDownloadListener.onThumbnailDownloaded(target, bitmap);
                    }
                });
            } catch (IOException e) {
                Log.e(TAG, "Error downloading img", e);
            }
        }

    }
}
