package com.firstrxjavademo.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.firstrxjavademo.R;

import java.util.List;

/**
 * 网格视图适配器
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Bitmap> list;

    public GridViewAdapter(Context context, List<Bitmap> list) {
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
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = View.inflate(context, R.layout.item_gv, null);
        }
        ImageView iv = (ImageView) view.findViewById(R.id.iv_item);
        iv.setImageBitmap(list.get(position));
        return view;
    }
}
