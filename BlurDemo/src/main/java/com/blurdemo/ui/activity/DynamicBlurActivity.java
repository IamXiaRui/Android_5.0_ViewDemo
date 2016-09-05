package com.blurdemo.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.blurdemo.R;
import com.blurdemo.view.BlurredView;

/**
 * @author Qiushui
 * @description 自定义模糊View类
 * @revision Xiarui 16.09.05
 */
public class DynamicBlurActivity extends AppCompatActivity {

    /*========== 控件相关 ==========*/
    private BlurredView customBView;

    /*========== 数据相关 ==========*/
    private float downY;
    private float movePercent;
    private int initLevel;
    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_blur);

        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        customBView = (BlurredView) findViewById(R.id.bv_dynamic_blur);
        //设置模糊度
        initLevel = 100;
        customBView.setBlurredLevel(initLevel);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
                //手指滑动距离
                float offsetY = moveY - downY;
                //屏幕高度 十倍是为了看出展示效果
                int screenY = getWindowManager().getDefaultDisplay().getHeight() * 10;
                //手指滑动距离占屏幕的百分比
                movePercent = offsetY / screenY;
                currentLevel = initLevel + (int) (movePercent * 100);
                if (currentLevel < 0) {
                    currentLevel = 0;
                }
                if (currentLevel > 100) {
                    currentLevel = 100;
                }
                //设置模糊度
                customBView.setBlurredLevel(currentLevel);
                //更改初始模糊等级
                initLevel = currentLevel;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(ev);
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
