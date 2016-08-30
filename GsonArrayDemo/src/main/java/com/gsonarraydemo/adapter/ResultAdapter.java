package com.gsonarraydemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gsonarraydemo.R;
import com.gsonarraydemo.bean.ResultBean;

import java.util.List;

/**
 * Created by xiarui on 2016/8/30.
 * 用户列表Adapter
 */
public class ResultAdapter extends BaseAdapter {

    private Context context;
    private List<ResultBean.UserBean> list;

    public ResultAdapter(Context context, List<ResultBean.UserBean> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
            convertView = View.inflate(context, R.layout.item_list, null);
        }
        // 得到一个ViewHolder
        viewHolder = ViewHolder.getViewHolder(convertView);
        viewHolder.nameText.setText(list.get(position).getName());
        viewHolder.ageText.setText(list.get(position).getAge());
        viewHolder.phoneText.setText(list.get(position).getPhone());
        viewHolder.emailText.setText(list.get(position).getEmail());

        return convertView;
    }

    private static class ViewHolder {

        TextView nameText, ageText, phoneText, emailText;

        private ViewHolder(View convertView) {
            nameText = (TextView) convertView.findViewById(R.id.tv_name);
            ageText = (TextView) convertView.findViewById(R.id.tv_age);
            phoneText = (TextView) convertView.findViewById(R.id.tv_phone);
            emailText = (TextView) convertView.findViewById(R.id.tv_email);
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
