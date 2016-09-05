package com.blurdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.blurdemo.R;

/**
 * @author Xiarui 16.09.05
 * @description 主页面
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    /*===== 控件相关 =====*/
    private Button basicButton;
    private Button customButton;
    private Button dynamicButton;
    private Button recylcerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    /**
     * 初始化View
     */
    private void initView() {
        basicButton = (Button) findViewById(R.id.bt_basic_blur);
        customButton = (Button) findViewById(R.id.bt_custom_blur);
        dynamicButton = (Button) findViewById(R.id.bt_dynamic_blur);
        recylcerButton = (Button) findViewById(R.id.bt_recycler_blur);
    }

    /**
     * 绑定监听器
     */
    private void initListener() {
        basicButton.setOnClickListener(this);
        customButton.setOnClickListener(this);
        dynamicButton.setOnClickListener(this);
        recylcerButton.setOnClickListener(this);
    }

    /**
     * 点击事件
     *
     * @param v 点击的View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_basic_blur:
                startActivity(new Intent(this,BasicBlurActivity.class));
                break;
            case R.id.bt_custom_blur:
                startActivity(new Intent(this,CustomBlurViewActivity.class));
                break;
            case R.id.bt_dynamic_blur:
                startActivity(new Intent(this,DynamicBlurActivity.class));
                break;
            case R.id.bt_recycler_blur:
                startActivity(new Intent(this,RecyclerBlurActivity.class));
                break;
        }
    }
}
