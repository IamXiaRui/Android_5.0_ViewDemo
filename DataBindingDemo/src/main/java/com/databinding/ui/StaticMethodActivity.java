package com.databinding.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.databinding.R;
import com.databinding.bean.User;
import com.databinding.databinding.ActivityStaticMethodBinding;

/**
 * Created by xiarui on 2016/8/22.
 * 功能：静态方法与按钮的点击事件
 */
public class StaticMethodActivity extends AppCompatActivity {

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
        ActivityStaticMethodBinding asmBinding = DataBindingUtil.setContentView(StaticMethodActivity.this, R.layout.activity_static_method);

        //注意姓名为小写  UI上显示为大写
        User user = new User("zhangsan", "20");
        asmBinding.setUser(user);

        //点击事件
        asmBinding.setBtclick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StaticMethodActivity.this, "点击有效", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
