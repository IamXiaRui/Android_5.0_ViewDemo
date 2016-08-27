package com.databinding.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.databinding.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by xiarui on 2016/8/27.
 * 功能：DataBinding功能演示
 */
public class MainActivity extends AppCompatActivity {

    /**
     * TODO:下面的定义变量只是为了ButterKnife功能演示，并没有用到，可以删除
     */
    @InjectView(R.id.bt_basic)
    Button btBasic;
    @InjectView(R.id.bt_alias)
    Button btAlias;
    @InjectView(R.id.bt_static_method)
    Button btStaticMethod;
    @InjectView(R.id.bt_resource)
    Button btResource;
    @InjectView(R.id.bt_custom)
    Button btCustom;
    @InjectView(R.id.bt_observer)
    Button btObserver;
    @InjectView(R.id.bt_view_id)
    Button btViewId;
    @InjectView(R.id.bt_view_stubs)
    Button btViewStubs;
    @InjectView(R.id.bt_dynamic_variables)
    Button btDynamicVariables;
    @InjectView(R.id.bt_converters)
    Button btConverters;
    @InjectView(R.id.bt_include)
    Button btInclude;
    @InjectView(R.id.bt_attr_set)
    Button btAttrSet;
    @InjectView(R.id.bt_demo)
    Button btDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //控制器注入
        ButterKnife.inject(this);
    }

    /**
     * 点击事件
     * 用注解的方式 为每个Button绑定点击事件
     *
     * @param view 点击View
     */
    @OnClick({R.id.bt_basic, R.id.bt_alias, R.id.bt_static_method, R.id.bt_resource, R.id.bt_custom, R.id.bt_observer, R.id.bt_view_id, R.id.bt_view_stubs, R.id.bt_dynamic_variables, R.id.bt_converters, R.id.bt_include, R.id.bt_attr_set, R.id.bt_demo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_basic:
                startActivity(new Intent(this, BasicActivity.class));
                break;
            case R.id.bt_alias:
                startActivity(new Intent(this, AliasActivity.class));
                break;
            case R.id.bt_static_method:
                startActivity(new Intent(this, StaticMethodActivity.class));
                break;
            case R.id.bt_resource:
                startActivity(new Intent(this, ResourceActivity.class));
                break;
            case R.id.bt_custom:
                startActivity(new Intent(this, CustomNameActivity.class));
                break;
            case R.id.bt_observer:
                startActivity(new Intent(this, ObservableActivity.class));
                break;
            case R.id.bt_view_id:
                startActivity(new Intent(this, ViewIdActivity.class));
                break;
            case R.id.bt_view_stubs:
                startActivity(new Intent(this, ViewStubActivity.class));
                break;
            case R.id.bt_converters:
                startActivity(new Intent(this,ConverterActivity.class));
                break;
            case R.id.bt_include:
                startActivity(new Intent(this,IncludeActivity.class));
                break;
            case R.id.bt_attr_set:
                startActivity(new Intent(this,AttrSetActivity.class));
                break;
            case R.id.bt_dynamic_variables:
                startActivity(new Intent(this,DynamicActivity.class));
                break;
            case R.id.bt_demo:
                startActivity(new Intent(this,RealDemoActivity.class));
                break;
        }
    }
}
