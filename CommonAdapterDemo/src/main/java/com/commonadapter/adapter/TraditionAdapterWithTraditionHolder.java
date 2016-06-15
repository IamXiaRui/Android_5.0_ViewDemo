package com.commonadapter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.commonadapter.R;
import com.commonadapter.bean.NewsBean;

import java.util.List;

/**
 * 基于传统Holder的传统Adapter
 */
public class TraditionAdapterWithTraditionHolder extends BaseAdapter {
    private Context context;
    private List<NewsBean> list;

    public TraditionAdapterWithTraditionHolder(Context context, List<NewsBean> list) {
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
        ViewHolder viewHolder ;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_list, null);
            viewHolder = new ViewHolder();

            viewHolder.titleText = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.descText = (TextView) convertView.findViewById(R.id.tv_desc);
            viewHolder.timeText = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.phoneText = (TextView) convertView.findViewById(R.id.tv_phone);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NewsBean bean = list.get(position);

        viewHolder.titleText.setText(bean.getTitle());
        viewHolder.descText.setText(bean.getDesc());
        viewHolder.timeText.setText(bean.getTime());
        viewHolder.phoneText.setText(bean.getPhone());

        return convertView;
    }

    private class ViewHolder {
        TextView titleText;
        TextView descText;
        TextView timeText;
        TextView phoneText;
    }
}
