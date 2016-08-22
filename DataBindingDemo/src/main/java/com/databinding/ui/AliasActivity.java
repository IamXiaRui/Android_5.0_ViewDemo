package com.databinding.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.databinding.R;
import com.databinding.databinding.ActivityAliasBinding;

/**
 * Created by xiarui on 2016/8/22.
 * DataBinding 案例演示
 * 功能：别名的使用
 */
public class AliasActivity extends AppCompatActivity {

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
        //绑定类
        ActivityAliasBinding aaBinding = DataBindingUtil.setContentView(AliasActivity.this, R.layout.activity_alias);

        //为了区别 加上包名
        com.databinding.bean.User user1 = new com.databinding.bean.User("张三", "24");
        aaBinding.setUser1(user1);

        com.databinding.pojo.User user2 = new com.databinding.pojo.User("man", "123456");
        aaBinding.setUser2(user2);
    }
}
