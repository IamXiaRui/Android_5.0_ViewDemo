package com.databinding.pojo;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

/**
 * @Description: 更简单的Observable Binding
 */
public class PlainUser {

    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableInt age = new ObservableInt();
}
