package com.databinding.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.databinding.R;
import com.databinding.adapter.UserAdapter;
import com.databinding.bean.User;
import com.databinding.databinding.ActivityDynamicBinding;

import java.util.ArrayList;
import java.util.List;

public class DynamicActivity extends AppCompatActivity {

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
        ActivityDynamicBinding adBinding = DataBindingUtil.setContentView(DynamicActivity.this, R.layout.activity_dynamic);
        adBinding.rlvMain.setLayoutManager(new LinearLayoutManager(this));
        adBinding.rlvMain.setAdapter(new UserAdapter(localData()));
    }

    /**
     * 加载一下本地模拟数据
     *
     * @return 集合
     */
    private List<User> localData() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(new User("张三", "20"));
            users.add(new User("李四", "30"));
            users.add(new User("王五", "40"));
        }
        return users;
    }
}
