package com.databinding.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.databinding.R;
import com.databinding.bean.User;
import com.databinding.databinding.ActivityConverterBinding;

/**
 * Created by xiarui on 2016/8/27.
 * 功能：转换器的使用
 */
public class ConverterActivity extends AppCompatActivity {

    private ActivityConverterBinding acBinding;

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
        acBinding = DataBindingUtil.setContentView(ConverterActivity.this, R.layout.activity_converter);

        acBinding.setUser(new User("张三", "20"));

        acBinding.setIsTrue(true);

        acBinding.setChangeColor(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acBinding.setIsTrue(!acBinding.getIsTrue());
            }
        });
    }
}
