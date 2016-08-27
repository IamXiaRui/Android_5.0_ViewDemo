package com.databinding.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.databinding.R;
import com.databinding.bean.User;
import com.databinding.databinding.ActivityAttrSetBinding;

/**
 * Created by xiarui on 2016/8/27.
 * 功能：自定义属性的使用
 */
public class AttrSetActivity extends AppCompatActivity {

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
        ActivityAttrSetBinding aasBinding = DataBindingUtil.setContentView(AttrSetActivity.this, R.layout.activity_attr_set);

        aasBinding.setUser(new User("张三", "20"));


    }
}
