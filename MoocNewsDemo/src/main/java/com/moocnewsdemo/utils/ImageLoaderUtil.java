package com.moocnewsdemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 异步加载图片的工具类
 */
public class ImageLoaderUtil {

    private ImageView mImageView;
    private String mIconUrl;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //只有当前的ImageView所对应的UR的图片是一致的,才会设置图片
            if (mImageView.getTag().equals(mIconUrl)) {
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };

    /**
     * 通过子线程的方式展示图片
     *
     * @param iv  图片的控件
     * @param url 图片的URL
     */
    public void showImageByThread(ImageView iv, final String url) {
        mImageView = iv;
        mIconUrl = url;
        //异步解析图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getBitmapFromURL(url);
                //发送到主线程
                Message msg = Message.obtain();
                msg.obj = bitmap;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 将一个URL转换成bitmap对象
     *
     * @param urlStr 图片的URL
     * @return
     */
    public Bitmap getBitmapFromURL(String urlStr) {
        Bitmap bitmap;
        InputStream is = null;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
            return bitmap;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 通过异步任务的方式加载数据
     *
     * @param iv  图片的控件
     * @param url 图片的URL
     */
    public void showImageByAsyncTask(ImageView iv, final String url) {
        new NewsAsyncTask(iv, url).execute(url);
    }

    /**
     * 异步任务类
     */
    private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView iv;
        private String url;

        public NewsAsyncTask(ImageView iv, String url) {
            this.iv = iv;
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return getBitmapFromURL(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //只有当前的ImageView所对应的UR的图片是一致的,才会设置图片
            if (iv.getTag().equals(url)) {
                iv.setImageBitmap(bitmap);
            }
        }
    }
}
