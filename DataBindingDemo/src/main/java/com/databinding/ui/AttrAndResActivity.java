package com.databinding.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.databinding.R;
import com.databinding.bean.Person;
import com.databinding.databinding.ActivityAttrResBinding;

/**
 * Created by xiarui on 2016/8/22.
 * DataBinding 案例演示
 * 功能：资源文件与属性的使用 表达式的使用
 */
public class AttrAndResActivity extends AppCompatActivity {

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
        ActivityAttrResBinding aarBinding = DataBindingUtil.setContentView(AttrAndResActivity.this, R.layout.activity_attr_res);

        //注意第一个参数为 true
        Person person = new Person(true, "小明", "20");
        aarBinding.setPerson(person);

        //为true
        aarBinding.setLarge(true);
    }
}
