package com.firstrxjavademo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 通过图片的URL地址下载图片
 */
public class GetBitmapForURL {

    public static Bitmap getBitmap(String url) {
        //获取传进来的参数
        Bitmap bitmap = null;
        URLConnection connection;
        InputStream is;
        BufferedInputStream bis;
        try {
            connection = new URL(url).openConnection();
            is = connection.getInputStream();
            //为了更清楚的看到加载图片的等待操作,将线程休眠2秒钟
            Thread.sleep(2000);
            bis = new BufferedInputStream(is);
            //通过decodeStream方法解析输入流
            bitmap = BitmapFactory.decodeStream(bis);

            is.close();
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
