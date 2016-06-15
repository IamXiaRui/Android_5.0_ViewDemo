package com.commonadapter.adapter;

import android.content.Context;

import com.commonadapter.R;
import com.commonadapter.bean.NewsBean;
import com.commonadapter.utils.CommonAdapter;
import com.commonadapter.utils.CommonViewHolder;

import java.util.List;

/**
 * 继承通用Adapter且使用通用Holder的适配器
 */
public class CommonAdapterWithCommonHolder extends CommonAdapter<NewsBean> {


    public CommonAdapterWithCommonHolder(Context context, List<NewsBean> list) {
        super(context, list,R.layout.item_list);
    }

    /**
     * 复写抽象方法
     * @param viewHolder 一个ViewHolder
     * @param bean Bean对象
     */
    @Override
    public void setViewContent(CommonViewHolder viewHolder, NewsBean bean) {

        //直接设置内容 链式调用
        viewHolder.setText(R.id.tv_title, bean.getTitle())
                .setText(R.id.tv_desc, bean.getDesc())
                .setText(R.id.tv_time, bean.getTime())
                .setText(R.id.tv_phone, bean.getPhone());
    }
}
