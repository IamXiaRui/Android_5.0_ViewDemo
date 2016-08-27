package com.databinding.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.databinding.BR;

/**
 * @Description: Observable DataBinding
 */
public class ObservableUser extends BaseObservable {

    private String name;

    private String age;

    /**
     * 注意需要添加注解
     *
     * @return 姓名
     */
    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        //动态更改时通知
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }
}
