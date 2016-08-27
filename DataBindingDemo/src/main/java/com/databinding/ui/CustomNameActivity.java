package com.databinding.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.databinding.CustomBinding;
import com.databinding.R;
import com.databinding.bean.User;

/**
 * Created by xiarui on 2016/8/22.
 * 功能：自定义绑定类名
 */
public class CustomNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //绑定层
        initData();
    }

    /**
     * 绑定层：初始化数据
     */
    private void initData() {
        //自定义绑定类名 类名与data标签中的class中定义的名称一致
        CustomBinding cBinding = DataBindingUtil.setContentView(CustomNameActivity.this, R.layout.activity_custom_name);
        User user = new User("tom", "20");
        cBinding.setUser(user);
    }
}
