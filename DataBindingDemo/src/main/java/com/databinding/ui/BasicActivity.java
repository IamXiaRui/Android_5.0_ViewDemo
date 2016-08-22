package com.databinding.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.databinding.R;
import com.databinding.bean.User;
import com.databinding.databinding.ActivityBasicBinding;

/**
 * Created by xiarui on 2016/8/22.
 * DataBinding 案例演示
 * 功能：基本绑定 与 import 的使用
 */
public class BasicActivity extends AppCompatActivity {

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
        //绑定类 类名与XML文件名一致 自动生成
        ActivityBasicBinding abBinding = DataBindingUtil.setContentView(BasicActivity.this, R.layout.activity_basic);
        User user = new User("tom", "20");
        abBinding.setUser(user);
    }

}
