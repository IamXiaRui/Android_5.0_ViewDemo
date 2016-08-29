package com.databinding.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.databinding.R;
import com.databinding.bean.ResultBean;
import com.databinding.databinding.ItemNewsBinding;

import java.util.List;

/**
 * @Description: 新闻列表适配器
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<ResultBean.NewsBean> list;
    private Context context;

    public NewsAdapter(Context context, List<ResultBean.NewsBean> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        //给ViewHolder绑定
        holder.bind(list.get(position));
        //加载图片
        Glide.with(context).load(list.get(position).getPicUrl()).into(holder.itemPicImage);
    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        //绑定类
        private ItemNewsBinding inBinding;
        private ImageView itemPicImage;

        public NewsViewHolder(View itemView) {
            super(itemView);
            //为每一个item设置
            inBinding = DataBindingUtil.bind(itemView);
            itemPicImage = (ImageView) itemView.findViewById(R.id.iv_item_pic);
        }

        /**
         * 绑定方法
         *
         * @param news Bean对象
         */
        public void bind(@NonNull ResultBean.NewsBean news) {
            inBinding.setNews(news);
        }
    }
}
