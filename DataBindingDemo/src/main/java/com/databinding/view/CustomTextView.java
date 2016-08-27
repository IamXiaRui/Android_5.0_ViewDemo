package com.databinding.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.databinding.R;

/**
 * @Description: 自定义TextView 添加两个属性
 */
public class CustomTextView extends LinearLayout {

    private TextView mName;
    private TextView mAge;

    public CustomTextView(Context context) {
        super(context);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.custom_text, this);
        mName = (TextView) findViewById(R.id.ctv_name);
        mAge = (TextView) findViewById(R.id.ctv_age);
    }

    public void setName(@NonNull final String name) {
        mName.setText(name);
    }

    public void setAge(@NonNull final String age) {
        mAge.setText(age);
    }
}
