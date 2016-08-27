package com.databinding.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.databinding.R;
import com.databinding.bean.User;
import com.databinding.databinding.ActivityIncludeBinding;

/**
 * Created by xiarui on 2016/8/27.
 * 功能：include标签的使用
 */
public class IncludeActivity extends AppCompatActivity {

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
        ActivityIncludeBinding aiBinding = DataBindingUtil.setContentView(IncludeActivity.this, R.layout.activity_include);

        aiBinding.setUser(new User("张三", "20"));

        aiBinding.setBtClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(IncludeActivity.this, "点击有效", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
