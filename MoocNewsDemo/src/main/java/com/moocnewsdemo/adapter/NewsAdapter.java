package com.moocnewsdemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moocnewsdemo.R;
import com.moocnewsdemo.bean.NewsBean;

import java.util.List;

/**
 * 新闻列表适配器
 */
public class NewsAdapter extends BaseAdapter {

    private Context context;
    private List<NewsBean> list;

    public NewsAdapter(Context context, List<NewsBean> list) {
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_news, null);
        }
        // 得到一个ViewHolder
        viewHolder = ViewHolder.getViewHolder(convertView);
        viewHolder.iconImage.setImageResource(R.mipmap.ic_launcher);
        viewHolder.titleText.setText(list.get(position).newsTitle);
        viewHolder.contentText.setText(list.get(position).newsContent);

        return convertView;
    }


    static class ViewHolder {
        ImageView iconImage;
        TextView titleText;
        TextView contentText;

        // 构造函数中就初始化View
        public ViewHolder(View convertView) {
            iconImage = (ImageView) convertView.findViewById(R.id.iv_icon);
            titleText = (TextView) convertView.findViewById(R.id.tv_title);
            contentText = (TextView) convertView.findViewById(R.id.tv_content);
        }

        // 得到一个ViewHolder
        public static ViewHolder getViewHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }
    }
}
