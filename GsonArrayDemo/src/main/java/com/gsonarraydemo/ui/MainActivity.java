package com.gsonarraydemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.gsonarraydemo.R;

/**
 * Created by xiarui on 2016/8/30.
 * 主界面
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button parseArrayButton;
    private Button parseByReaderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化View
        initView();
        //绑定监听器
        initListener();
    }

    /**
     * 初始化View
     */
    private void initView() {
        parseArrayButton = (Button) findViewById(R.id.bt_parse_array);
        parseByReaderButton = (Button) findViewById(R.id.bt_parse_reader);
    }

    /**
     * 绑定监听器
     */
    private void initListener() {
        parseArrayButton.setOnClickListener(this);
        parseByReaderButton.setOnClickListener(this);
    }

    /**
     * 按钮点击事件
     *
     * @param v 点击的View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_parse_array:
                startActivity(new Intent(this, ParseJsonArrayActivity.class));
                break;
            case R.id.bt_parse_reader:
                startActivity(new Intent(this, ParseByReaderActivity.class));
                break;
        }
    }
}
