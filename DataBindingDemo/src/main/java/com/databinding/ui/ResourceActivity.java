package com.databinding.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.databinding.R;
import com.databinding.bean.Person;
import com.databinding.databinding.ActivityResourceBinding;

/**
 * Created by xiarui on 2016/8/22.
 * DataBinding 案例演示
 * 功能：资源文件与属性的使用 表达式的使用
 */
public class ResourceActivity extends AppCompatActivity {

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
        ActivityResourceBinding arBinding = DataBindingUtil.setContentView(ResourceActivity.this, R.layout.activity_resource);

        //注意第一个参数为 true
        Person person = new Person(true, "小明", "20");
        arBinding.setPerson(person);

        //为true
        arBinding.setLarge(true);
    }
}
