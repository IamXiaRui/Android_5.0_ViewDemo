package com.moocnewsdemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.moocnewsdemo.R;
import com.moocnewsdemo.bean.NewsBean;
import com.moocnewsdemo.utils.DiskCacheUtil;
import com.moocnewsdemo.utils.ImageLoaderUtil;

import java.io.IOException;
import java.util.List;

/**
 * 新闻列表适配器
 */
public class NewsAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private Context context;
    private List<NewsBean> list;
    private ImageLoaderUtil imageLoader;
    private DiskCacheUtil mDiskCacheUtil;
    private int mStart, mEnd;//滑动的歧视位置
    public static String[] urls; //用来保存当前获取到的所有图片的Url地址

    private boolean mFirstIn;

    public NewsAdapter(Context context, List<NewsBean> list, ListView lv) {
        this.context = context;
        this.list = list;
        imageLoader = new ImageLoaderUtil(lv);

        mDiskCacheUtil = new DiskCacheUtil(context, lv);
        //存入url地址
        urls = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            urls[i] = list.get(i).newsIconUrl;
        }
        mFirstIn = true;
        //注册监听事件
        lv.setOnScrollListener(this);
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
        //先加载默认图片 防止有的没有图
        viewHolder.iconImage.setImageResource(R.mipmap.ic_launcher);

        String iconUrl = list.get(position).newsIconUrl;
        //当前位置的ImageView与URL中的图片绑定
        viewHolder.iconImage.setTag(iconUrl);
        //再加载联网图

        //第一种方式 通过子线程设置
        //new ImageLoaderUtil().showImageByThread(viewHolder.iconImage, iconUrl);

        //第二种方式 通过异步任务方式设置 且利用LruCache存储到内存缓存中
        //imageLoader.showImageByAsyncTask(viewHolder.iconImage, iconUrl);

        //第三种方式 通过异步任务方式设置 且利用DiskLruCache存储到磁盘缓存中
        try {
            mDiskCacheUtil.showImageByAsyncTask(viewHolder.iconImage, iconUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        viewHolder.titleText.setText(list.get(position).newsTitle);
        viewHolder.contentText.setText(list.get(position).newsContent);

        return convertView;
    }

    /**
     * 滑动状态改变的时候才会去调用此方法
     *
     * @param view        滚动的View
     * @param scrollState 滚动的状态
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            //加载可见项
            try {
                mDiskCacheUtil.loadImages(mStart, mEnd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //停止加载任务
            mDiskCacheUtil.cancelAllTask();
        }
    }

    /**
     * 滑动过程中 一直会调用此方法
     *
     * @param view             滚动的View
     * @param firstVisibleItem 第一个可见的item
     * @param visibleItemCount 可见的item的长度
     * @param totalItemCount   总共item的个数
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
        //如果是第一次进入 且可见item大于0 预加载
        if (mFirstIn && visibleItemCount > 0) {
            try {
                mDiskCacheUtil.loadImages(mStart, mEnd);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mFirstIn = false;
        }
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
