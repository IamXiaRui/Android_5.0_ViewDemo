package com.databinding.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayMap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.databinding.R;
import com.databinding.bean.ObservableUser;
import com.databinding.databinding.ActivityObservableBinding;
import com.databinding.pojo.PlainUser;

/**
 * Created by xiarui on 2016/8/27.
 * 功能：Observer Binding的演示
 */
public class ObservableActivity extends AppCompatActivity implements View.OnClickListener {

    private ObservableUser observableUser = new ObservableUser();
    private PlainUser plainUser = new PlainUser();

    //集合
    private ObservableArrayMap<String, Object> mapUser = new ObservableArrayMap<>();

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
        ActivityObservableBinding aoBinding = DataBindingUtil.setContentView(ObservableActivity.this, R.layout.activity_observable);

        observableUser.setName("张三");
        observableUser.setAge("20");

        plainUser.name.set("zhangsan");
        plainUser.age.set(40);

        mapUser.put("name", "李四");
        mapUser.put("age", 111);

        aoBinding.setUser(observableUser);
        aoBinding.setPuser(plainUser);
        aoBinding.setMuser(mapUser);

        //绑定监听
        aoBinding.setChangeInfo(this);
    }

    /**
     * 只要更改数据即可
     */
    @Override
    public void onClick(View view) {
        observableUser.setName("zhangsan");
        observableUser.setAge("40");

        plainUser.name.set("张三");
        plainUser.age.set(20);

        mapUser.put("name", "lisi");
        mapUser.put("age", 222);

        Toast.makeText(this, "更改成功", Toast.LENGTH_SHORT).show();
    }
}
