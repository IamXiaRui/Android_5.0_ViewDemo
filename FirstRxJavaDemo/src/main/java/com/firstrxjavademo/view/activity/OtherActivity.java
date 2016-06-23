package com.firstrxjavademo.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firstrxjavademo.R;
import com.firstrxjavademo.utils.GetBitmapForURL;
import com.firstrxjavademo.view.adapter.GridViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 操作符的使用
 */
public class OtherActivity extends AppCompatActivity {

    @InjectView(R.id.pb_other)
    ProgressBar pbOther;
    @InjectView(R.id.bt_other)
    Button btOther;
    @InjectView(R.id.gv_other)
    GridView gvOther;

    private final String url1 = "http://www.iamxiarui.com/wp-content/uploads/2016/06/套路.png";
    private final String url2 = "http://www.iamxiarui.com/wp-content/uploads/2016/06/为什么我的流量又没了.png";
    private final String url3 = "http://www.iamxiarui.com/wp-content/uploads/2016/05/cropped-iamxiarui.com_2016-05-05_14-42-31.jpg";
    private final String url4 = "http://www.iamxiarui.com/wp-content/uploads/2016/05/微信.png";

    //一组Url数据
    private final String[] urls = new String[]{url1, url2, url3, url4};

    private List<Bitmap> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.bt_other)
    public void onClick() {
        pbOther.setVisibility(View.VISIBLE);
        setBitmap();
    }

    /**
     * from 与 flatMap的使用
     * take 与 doOnNext的使用
     */
    private void setBitmap() {
        Observable.from(urls)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return Observable.just(s);
                    }
                })
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String s) {
                        return GetBitmapForURL.getBitmap(s);
                    }
                })
                .subscribeOn(Schedulers.io()) // 指定subscribe()发生在IO线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定后面所发生的回调发生在主线程
                .doOnNext(new Action1<Bitmap>() {    //每运行一次所要执行的操作
                    @Override
                    public void call(Bitmap bitmap) {
                        Toast.makeText(OtherActivity.this, "图片增加", Toast.LENGTH_SHORT).show();
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        //将获取到的Bitmap对象添加到集合中
                        list.add(bitmap);
                        //设置图片
                        gvOther.setAdapter(new GridViewAdapter(OtherActivity.this, list));
                        pbOther.setVisibility(View.GONE);
                    }
                });
    }
}
