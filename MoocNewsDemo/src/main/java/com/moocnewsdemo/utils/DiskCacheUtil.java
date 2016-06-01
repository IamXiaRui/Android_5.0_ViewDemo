package com.moocnewsdemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.libcore.io.DiskLruCache;
import com.moocnewsdemo.R;
import com.moocnewsdemo.adapter.NewsAdapter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

/**
 * 利用DiskLruCache来缓存图片
 */
public class DiskCacheUtil {
    private Context mContext;

    private ListView mListView;
    private Set<NewsAsyncTask> mTaskSet;

    private static final String TAG = "ImageLoader";

    //DiskLru缓存k
    private DiskLruCache mDiskCache;
    //指定磁盘缓存大小
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;//50MB
    //IO缓存流大小
    private static final int IO_BUFFER_SIZE = 8 * 1024;
    //缓存个数
    private static final int DISK_CACHE_INDEX = 0;
    //缓存文件是否创建
    private boolean mIsDiskLruCacheCreated = false;

    public DiskCacheUtil(Context context, ListView listView) {
        this.mListView = listView;
        mTaskSet = new HashSet<>();
        mContext = context.getApplicationContext();
        //得到缓存文件
        File diskCacheDir = getDiskCacheDir(mContext, "diskcache");
        //如果文件不存在 直接创建
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                mDiskCache = DiskLruCache.open(diskCacheDir, 1, 1,
                        DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过异步任务的方式加载数据
     *
     * @param iv  图片的控件
     * @param url 图片的URL
     */
    public void showImageByAsyncTask(ImageView iv, final String url) throws IOException {
        //从缓存中取出图片
        Bitmap bitmap = getBitmapFromDiskCache(url);
        //如果缓存中没有，则需要从网络中下载
        if (bitmap == null) {
            iv.setImageResource(R.mipmap.ic_launcher);
        } else {
            //如果缓存中有 直接设置
            iv.setImageBitmap(bitmap);
        }
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
            is = new BufferedInputStream(connection.getInputStream(), IO_BUFFER_SIZE);
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
     * 将图片的URL地址保存到输出流中
     *
     * @param urlString    图片的URL地址
     * @param outputStream 输出流
     * @return 输出流
     */
    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 加载从start到end的所有的Image
     *
     * @param start
     * @param end
     */
    public void loadImages(int start, int end) throws IOException {
        for (int i = start; i < end; i++) {
            String url = NewsAdapter.urls[i];
            //从缓存中取出图片
            Bitmap bitmap = getBitmapFromDiskCache(url);
            //如果缓存中没有，则需要从网络中下载
            if (bitmap == null) {
                NewsAsyncTask task = new NewsAsyncTask(url);
                task.execute(url);
                mTaskSet.add(task);
            } else {
                //如果缓存中有 直接设置
                ImageView imageView = (ImageView) mListView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * 停止所有当前正在运行的任务
     */
    public void cancelAllTask() {
        if (mTaskSet != null) {
            for (NewsAsyncTask task : mTaskSet) {
                task.cancel(false);
            }
        }
    }

    /*--------------------------------DiskLruCaChe的实现-----------------------------------------*/

    /**
     * 创建缓存文件路径
     *
     * @param context  上下文对象
     * @param filePath 文件路径
     * @return 返回一个文件
     */
    public File getDiskCacheDir(Context context, String filePath) {
        boolean externalStorageAvailable = Environment
                .getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + filePath);
    }

    /**
     * 得到当前可用的空间大小
     *
     * @param path 文件的路径
     * @return
     */
    private long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    /**
     * 将URL转换成MD5值
     *
     * @param url 图片的URL
     * @return
     */
    private String hashKeyFormUrl(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    /**
     * 将字节数组转换成Hex字符串
     *
     * @param bytes URL的字节数组
     * @return
     */
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 将Bitmap写入缓存
     *
     * @param url
     * @return
     * @throws IOException
     */
    private Bitmap addBitmapToDiskCache(String url) throws IOException {
        //如果当前线程是在主线程 则异常
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI Thread.");
        }
        if (mDiskCache == null) {
            return null;
        }

        //设置key，并根据URL保存输出流的返回值决定是否提交至缓存
        String key = hashKeyFormUrl(url);
        DiskLruCache.Editor editor = mDiskCache.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if (downloadUrlToStream(url, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskCache.flush();
        }
        return getBitmapFromDiskCache(url);
    }


    /**
     * 从缓存中取出Bitmap
     *
     * @param url 图片的URL
     * @return 返回Bitmap对象
     * @throws IOException
     */
    private Bitmap getBitmapFromDiskCache(String url) throws IOException {
        //如果当前线程是主线程 则异常
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.w(TAG, "load bitmap from UI Thread, it's not recommended!");
        }
        //如果缓存中为空  直接返回为空
        if (mDiskCache == null) {
            return null;
        }

        //通过key值在缓存中找到对应的Bitmap
        Bitmap bitmap = null;
        String key = hashKeyFormUrl(url);
        DiskLruCache.Snapshot snapShot = mDiskCache.get(key);
        if (snapShot != null) {
            FileInputStream fileInputStream = (FileInputStream) snapShot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        }
        return bitmap;
    }

     /*--------------------------------DiskLruCaChe的实现-----------------------------------------*/


    /*--------------------------------异步任务AsyncTask的实现--------------------------------------*/


    /**
     * 异步任务类
     */
    private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap> {
        //private ImageView iv;
        private String url;

        public NewsAsyncTask(String url) {
            // this.iv = iv;
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap bitmap = getBitmapFromURL(params[0]);
            //保存到缓存中
            if (bitmap != null) {
                try {
                    addBitmapToDiskCache(params[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) mListView.findViewWithTag(url);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            mTaskSet.remove(this);
        }
    }

    /*--------------------------------异步任务AsyncTask的实现--------------------------------------*/
}



