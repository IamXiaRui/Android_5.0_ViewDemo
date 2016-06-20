package com.firstrxjavademo.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.firstrxjavademo.R;
import com.firstrxjavademo.utils.GetBitmapForURL;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 利用RxJava的方式 异步加载网络图片
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.iv_main)
    ImageView mainImageView;
    @InjectView(R.id.pb_main)
    ProgressBar mainProgressBar;
    @InjectView(R.id.bt_one)
    Button btOne;
    @InjectView(R.id.bt_two)
    Button btTwo;

    private final String url = "http://www.iamxiarui.com/wp-content/uploads/2016/06/套路.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化View
        ButterKnife.inject(this);
    }

    @OnClick({R.id.bt_one, R.id.bt_two})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_one:
                //mainProgressBar.setVisibility(View.VISIBLE);
                //setBitmap1();
                //setBitmap2();
                setBitmap3();
                break;
            case R.id.bt_two:
                Intent intent = new Intent(MainActivity.this, OtherActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * 异步设置图片
     */
    private void setBitmap1() {
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

    /**
     * 事件参数类型的变换
     * 将String类型的URL转换成Bitmap
     */
    private void setBitmap2() {
        Observable.just(url)
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String s) {
                        return GetBitmapForURL.getBitmap(s);
                    }
                })
                .subscribeOn(Schedulers.io()) // 指定subscribe()发生在IO线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定Subscriber的回调发生在UI线程
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        mainImageView.setImageBitmap(bitmap);
                        mainProgressBar.setVisibility(View.GONE);
                    }
                });
    }


    /**
     * 事件参数类型的变换
     * 将String类型的URL转换成Bitmap
     * doOnSubscribe的使用
     */
    private void setBitmap3() {
        Observable.just(url)    //IO线程
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String s) {
                        Log.i(" map ---> ", Thread.currentThread().getName());
                        return GetBitmapForURL.getBitmap(s);
                    }
                })
                .subscribeOn(Schedulers.newThread()) // 指定subscribe()发生在IO线程
                .doOnSubscribe(new Action0() { //需要在主线程中执行 测试来看 默认运行在main线程 ？
                    @Override
                    public void call() {
                        mainProgressBar.setVisibility(View.VISIBLE);
                        Log.i(" doOnSubscribe ---> ", Thread.currentThread().getName());
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread()) // 指定subscribe()发生在主线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定Subscriber的回调发生在主线程
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        mainImageView.setImageBitmap(bitmap);
                        mainProgressBar.setVisibility(View.GONE);
                        Log.i(" subscribe ---> ", Thread.currentThread().getName());
                    }
                });
    }

}
