package com.blurdemo.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blurdemo.R;
import com.blurdemo.view.BlurredView;

public class CustomBlurViewActivity extends AppCompatActivity {

    /*========== 控件相关 ==========*/
    private BlurredView customBView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_blur_view);

        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        customBView = (BlurredView) findViewById(R.id.bv_custom_blur);
        //设置模糊度
        customBView.setBlurredLevel(100);
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
