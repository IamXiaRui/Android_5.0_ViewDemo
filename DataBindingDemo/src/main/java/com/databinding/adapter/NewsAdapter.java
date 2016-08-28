package com.databinding.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.databinding.R;
import com.databinding.bean.NewsBean;
import com.databinding.databinding.ItemNewsBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @Description: 新闻列表适配器
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsBean> list;
    private Context context;
    public NewsAdapter(Context context, List<NewsBean> list) {
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
        holder.bind(list.get(position));
        Picasso.with(context).load(list.get(position).getPicUrl()).into(holder.itemPicImage);
        holder.itemPicImage.setTag(list.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        private ItemNewsBinding inBinding;
        private ImageView itemPicImage;

        public NewsViewHolder(View itemView) {
            super(itemView);
            inBinding = DataBindingUtil.bind(itemView);
            itemPicImage = (ImageView) itemView.findViewById(R.id.iv_item_pic);
        }

        public void bind(@NonNull NewsBean news) {
            inBinding.setNews(news);
        }
    }
}
