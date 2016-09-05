package com.blurdemo.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blurdemo.R;
import com.blurdemo.adapter.RecyclerViewAdapter;
import com.blurdemo.view.BlurredView;

/**
 * @author Qiushui
 * @description 与Recycler相结合的Blur
 * @revision Xiarui 16.09.05
 */
public class RecyclerBlurActivity extends AppCompatActivity {

    /*========== 控件相关 ===========*/
    private BlurredView recyclerBView;
    private RecyclerView mainRView;

    /*========== 数据相关 ===========*/
    private int mScrollerY; //滚动距离
    private int mAlpha; //透明值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_blur);

        initView();
        initListener();
    }

    /**
     * 初始化View
     */
    private void initView() {

        recyclerBView = (BlurredView) findViewById(R.id.bv_recycler_blur);
        mainRView = (RecyclerView) findViewById(R.id.rv_main);

        mainRView.setLayoutManager(new LinearLayoutManager(this));
        mainRView.setAdapter(new RecyclerViewAdapter(this));
    }

    /**
     * 绑定监听器
     */
    @SuppressWarnings("deprecation")
    private void initListener() {
        //RecyclerView 滑动监听
        mainRView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //滚动距离
                mScrollerY += dy;
                //根据滚动距离控制模糊程度 滚动距离是模糊程度的十倍
                if (Math.abs(mScrollerY) > 1000) {
                    mAlpha = 100;
                } else {
                    mAlpha = Math.abs(mScrollerY) / 10;
                }
                //设置透明度等级
                recyclerBView.setBlurredLevel(mAlpha);
            }
        });
    }

    /**
     * 实现沉浸式通知栏与导航栏
     *
     * @param hasFocus 是否有焦点
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
