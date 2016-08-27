package com.databinding.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.databinding.R;
import com.databinding.databinding.ActivityViewIdBinding;

/**
 * Created by xiarui on 2016/8/27.
 * 功能：带有ID的View的处理
 */
public class ViewIdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化数据
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        ActivityViewIdBinding aviBinding = DataBindingUtil.setContentView(ViewIdActivity.this, R.layout.activity_view_id);

        aviBinding.tvName.setText("张三");
        aviBinding.tvAge.setText("123");

    }
}
