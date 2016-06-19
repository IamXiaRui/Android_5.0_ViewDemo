package com.firstrxjavademo.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.firstrxjavademo.R;
import com.firstrxjavademo.utils.GetBitmapForURL;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 利用RxJava的方式 异步加载网络图片
 */
public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.iv_main)
    ImageView mainImageView;
    @InjectView(R.id.pb_main)
    ProgressBar mainProgressBar;
    @InjectView(R.id.bt_main)
    Button mainButton;

    private final String url = "http://www.iamxiarui.com/wp-content/uploads/2016/06/套路.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化View
        ButterKnife.inject(this);

        //按钮监听
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainProgressBar.setVisibility(View.VISIBLE);
                setBitmap();
            }
        });
    }

    /**
     * 异步设置图片
     */
    private void setBitmap() {

        //创建被观察者
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            /**
             * 复写回调方法
             *
             * @param subscriber 观察者
             */
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                //通过URL得到图片的Bitmap对象
                Bitmap bitmap = GetBitmapForURL.getBitmap(url);
                //调用观察者方法
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io()) // 指定subscribe()发生在IO线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定Subscriber的回调发生在UI线程
                .subscribe(new Observer<Bitmap>() {   //订阅观察者（其实是观察者订阅被观察者）

                    @Override
                    public void onNext(Bitmap bitmap) {
                        mainImageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onCompleted() {
                        mainProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError --->", e.toString());
                    }
                });
    }
}
