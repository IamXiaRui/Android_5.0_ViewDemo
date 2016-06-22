package com.firstrxjavademo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firstrxjavademo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * 基础测试RxJava运用
 */
public class BasicActivity extends AppCompatActivity {

    @InjectView(R.id.bt_basic)
    Button btBasic;
    @InjectView(R.id.bt_chain)
    Button btChain;
    @InjectView(R.id.bt_just)
    Button btJust;
    @InjectView(R.id.bt_from)
    Button btFrom;
    @InjectView(R.id.bt_load)
    Button btLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        ButterKnife.inject(this);
    }


    @OnClick({R.id.bt_basic, R.id.bt_chain, R.id.bt_just, R.id.bt_from, R.id.bt_load})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_basic:
                testBasic();
                break;
            case R.id.bt_chain:
                testChain();
                break;
            case R.id.bt_just:
                testJust();
                break;
            case R.id.bt_from:
                testFrom();
                break;
            case R.id.bt_load:
                Intent intent = new Intent(BasicActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 如何实现RxJava
     */
    private void testBasic() {
        //创建Observer
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onNext(String s) {
                Log.i("onNext ---> ", "Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.i("onCompleted ---> ", "完成");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("onError ---> ", e.toString());
            }
        };

        //创建Observable
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("World");
                subscriber.onCompleted();
            }
        });

        //订阅
        observable.subscribe(observer);
    }


    /**
     * 链式调用
     */
    private void testChain() {
        //创建Observable
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("World");
                subscriber.onCompleted();
                Log.i("执行顺序 ---> ", " call ");
            }
        }).subscribe(new Observer<String>() {

            @Override
            public void onNext(String s) {
                Log.i("onNext ---> ", s);
                Log.i("执行顺序 ---> ", " subscribe onNext");
            }

            @Override
            public void onCompleted() {
                Log.i("onCompleted ---> ", "完成");
                Log.i("执行顺序 ---> ", " subscribe onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("onError ---> ", "出错 --->" + e.toString());
            }
        });
    }

    /**
     * 使用Just操作符
     */
    private void testJust() {
        //创建Observable
        Observable.just("Hello", "World", null)
                .subscribe(new Observer<String>() {

                    @Override
                    public void onNext(String s) {
                        if (s == null) {
                            Log.i("onNext ---> ", "null");
                        } else {
                            Log.i("onNext ---> ", s);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        Log.i("onCompleted ---> ", "完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("onError ---> ", "出错 --->" + e.toString());
                    }
                });
    }

    /**
     * 使用From操作符
     */
    private void testFrom() {
        String[] str = new String[]{"Hello", "World"};
        //创建Observable
        Observable.from(str)
                .subscribe(new Observer<String>() {

                    @Override
                    public void onNext(String s) {
                        Log.i("onNext ---> ", s);
                    }

                    @Override
                    public void onCompleted() {
                        Log.i("onCompleted ---> ", "完成");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
