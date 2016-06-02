package com.updatecolordemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * 动态更改标题栏颜色
 */
public class MainActivity extends Activity {

    private View backView;
    private ListView colorList;
    private String[] colors = new String[]{"#87CEFA", "#B3EE3A", "#DEB887", "#8A2BE2", "#FF7F00", "#87CEFA", "#FF4040", "#000080", "#BFEFFF", "#9A32CD",
            "#87CEFA", "#B3EE3A", "#DEB887", "#8A2BE2", "#FF7F00", "#87CEFA", "#FF4040", "#000080", "#BFEFFF", "#9A32CD"};
    private ColorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        backView = findViewById(R.id.view_back);
        colorList = (ListView) findViewById(R.id.lv_color);

        adapter = new ColorAdapter(this, colors);
        colorList.setAdapter(adapter);

        //滚动监听
        colorList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                backView.setBackgroundColor(Color.parseColor(adapter.getItem(firstVisibleItem)));
            }
        });
    }
}

class ColorAdapter extends BaseAdapter {
    private Context context;
    private String[] list;

    public ColorAdapter(Context context, String[] list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public String getItem(int position) {
        return list[position];
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
        //先加载默认背景
        viewHolder.iv.setBackgroundColor(Color.parseColor(list[position]));

        return convertView;
    }
}

class ViewHolder {
    ImageView iv;

    // 构造函数中就初始化View
    public ViewHolder(View convertView) {
        iv = (ImageView) convertView.findViewById(R.id.iv_list);
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
