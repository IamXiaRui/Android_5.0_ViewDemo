package com.blurdemo.ui.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.blurdemo.R;
import com.blurdemo.utils.BitmapUtil;
import com.blurdemo.utils.BlurBitmapUtil;

/**
 * @author Xiarui 16.09.05
 * @description 简单的模糊图片页面
 */
public class BasicBlurActivity extends AppCompatActivity {

    private ImageView basicImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_blur);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {

        basicImage = (ImageView) findViewById(R.id.iv_basic_pic);
        Bitmap bitmap = getBlurBitmap();
        basicImage.setImageBitmap(bitmap);
    }

    /**
     * 模糊图片
     *
     * @return 模糊后Bitmap
     */
    @SuppressWarnings({"ResourceType", "deprecation"})
    private Bitmap getBlurBitmap() {
        Bitmap bitmap = BitmapUtil.drawableToBitmap(getResources().getDrawable(R.raw.pic));
        return BlurBitmapUtil.blurBitmap(this, bitmap, 20f);
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
