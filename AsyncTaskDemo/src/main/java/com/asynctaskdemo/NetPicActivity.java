package com.asynctaskdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 异步任务加载网络图片
 */
public class NetPicActivity extends AppCompatActivity {

    private ImageView netPicImage;
    private ProgressBar netPicProgressBar;
    private static String picUrl = "http://www.iamxiarui.com/wp-content/uploads/2016/05/壁纸.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_pic);
        netPicImage = (ImageView) findViewById(R.id.iv_netpic);
        netPicProgressBar = (ProgressBar) findViewById(R.id.pb_netpic);
        //通过调用execute方法开始处理异步任务.相当于线程中的start方法.
        new NetAsyncTask().execute(picUrl);
    }

    /**
     * 自定义网络请求异步任务
     */
    class NetAsyncTask extends AsyncTask<String, Void, Bitmap> {

        /**
         * onPreExecute用于异步处理前的操作
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //此处将progressBar设置为可见.
            netPicProgressBar.setVisibility(View.VISIBLE);
        }

        /**
         * 在doInBackground方法中进行异步任务的处理
         *
         * @param params 参数为URL
         * @return Bitmap对象
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            //获取传进来的参数
            String url = params[0];
            Bitmap bitmap = null;
            URLConnection connection;
            InputStream is;
            try {
                connection = new URL(url).openConnection();
                is = connection.getInputStream();
                //为了更清楚的看到加载图片的等待操作,将线程休眠3秒钟
                Thread.sleep(3000);
                BufferedInputStream bis = new BufferedInputStream(is);
                //通过decodeStream方法解析输入流
                bitmap = BitmapFactory.decodeStream(bis);
                is.close();
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        /**
         * onPostExecute用于UI的更新.此方法的参数为doInBackground方法返回的值
         *
         * @param bitmap 网络图片
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //隐藏progressBar
            netPicProgressBar.setVisibility(View.GONE);
            //更新imageView
            netPicImage.setImageBitmap(bitmap);
        }
    }

}
