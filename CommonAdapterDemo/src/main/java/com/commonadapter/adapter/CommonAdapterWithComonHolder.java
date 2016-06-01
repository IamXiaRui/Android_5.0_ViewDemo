package com.commonadapter.adapter;

import android.content.Context;

import com.commonadapter.R;
import com.commonadapter.bean.NewsBean;
import com.commonadapter.utils.CommonAdapter;
import com.commonadapter.utils.ViewHolder;

import java.util.List;

/**
 * 继承通用Adapter且使用通用Holder的适配器
 */
public class CommonAdapterWithComonHolder extends CommonAdapter<NewsBean> {


    public CommonAdapterWithComonHolder(Context context, List<NewsBean> list) {
        super(context, list);
    }

    @Override
    public void convert(ViewHolder viewHolder, NewsBean bean) {
//        ((TextView) viewHolder.getView(R.id.tv_title)).setText(bean.getTitle());
//        ((TextView) viewHolder.getView(R.id.tv_desc)).setText(bean.getDesc());
//        ((TextView) viewHolder.getView(R.id.tv_time)).setText(bean.getTime());
//        ((TextView) viewHolder.getView(R.id.tv_phone)).setText(bean.getPhone());

        viewHolder.setText(R.id.tv_title, bean.getTitle())
                .setText(R.id.tv_desc, bean.getDesc())
                .setText(R.id.tv_time, bean.getTime())
                .setText(R.id.tv_phone, bean.getPhone());
    }
}
