package com.commonadapter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.commonadapter.R;
import com.commonadapter.bean.NewsBean;
import com.commonadapter.utils.ViewHolder;

import java.util.List;

/**
 * 通用Holder的适配器
 */
public class ComonHolderAdapter extends BaseAdapter {
    private Context context;
    private List<NewsBean> list;

    public ComonHolderAdapter(Context context, List<NewsBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = ViewHolder.get(context, convertView, parent, R.layout.item_list, position);

        NewsBean bean = list.get(position);

        TextView titleText = viewHolder.getView(R.id.tv_title);
        TextView descText = viewHolder.getView(R.id.tv_desc);
        TextView timeText = viewHolder.getView(R.id.tv_time);
        TextView phoneText = viewHolder.getView(R.id.tv_phone);

        titleText.setText(bean.getTitle());
        descText.setText(bean.getDesc());
        timeText.setText(bean.getTime());
        phoneText.setText(bean.getPhone());

        return viewHolder.getConvertView();
    }
}
