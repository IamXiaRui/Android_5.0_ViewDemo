package com.databinding.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;
import android.widget.Toast;

import com.databinding.R;
import com.databinding.bean.User;
import com.databinding.databinding.ActivityViewStubBinding;
import com.databinding.databinding.LayoutViewStubBinding;


/**
 * Created by xiarui on 2016/8/27.
 * 功能：ViewStub处理
 */
public class ViewStubActivity extends AppCompatActivity {

    private ActivityViewStubBinding avsBinding;

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
        avsBinding = DataBindingUtil.setContentView(ViewStubActivity.this, R.layout.activity_view_stub);

        avsBinding.vsMain.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub viewStub, View view) {
                LayoutViewStubBinding lvsBinding = DataBindingUtil.bind(view);
                lvsBinding.setUser(new User("张三", "123"));
            }
        });

        //TODO：别管报错 项目能运行 原因暂未知
        avsBinding.setShowViewStub(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (avsBinding.vsMain.isInflated()) {
                    Toast.makeText(ViewStubActivity.this, "请勿反复加载", Toast.LENGTH_SHORT).show();
                    return;
                }
                avsBinding.vsMain.getViewStub().inflate();
            }
        });
    }
}
