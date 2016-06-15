package com.commonadapter.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 通用Adapter抽象类
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T> list;
    private int layoutId;

    public CommonAdapter(Context context, List<T> list, int layoutId) {
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 封装getView方法
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //得到一个ViewHolder
        CommonViewHolder viewHolder = CommonViewHolder.get(context, convertView, parent, layoutId, position);

        //设置控件内容
        setViewContent(viewHolder, (T) getItem(position));

        //返回复用的View
        return viewHolder.getConvertView();
    }

    /**
     * 提供抽象方法，来设置控件内容
     *
     * @param viewHolder 一个ViewHolder
     * @param t          一个数据集
     */
    public abstract void setViewContent(CommonViewHolder viewHolder, T t);
}
