package com.databinding.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.databinding.R;
import com.databinding.adapter.NewsAdapter;
import com.databinding.bean.NewsBean;
import com.databinding.databinding.ActivityNewsBinding;
import com.databinding.utils.GetNewsUtil;

import java.util.List;

/**
 * Created by xiarui on 2016/8/27.
 * 功能：新闻列表
 */
public class NewsActivity extends AppCompatActivity {

    private final static String NEWS_URL = "http://api.tianapi.com/keji/?key=3cd7cc6ab7736c7b29b2280b0f282c39&num=30&rand";

    private List<NewsBean> newsLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化数据
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //开启子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                newsLists = GetNewsUtil.getJson(NEWS_URL);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onUIThread();
                    }
                });
            }
        }).start();
    }

    /**
     * 在UI中执行的操作
     */
    private void onUIThread() {
        ActivityNewsBinding anBinding = DataBindingUtil.setContentView(NewsActivity.this, R.layout.activity_news);
        anBinding.rlvNews.setLayoutManager(new LinearLayoutManager(NewsActivity.this));
        NewsAdapter newsAdapter = new NewsAdapter(NewsActivity.this, newsLists);
        anBinding.rlvNews.setAdapter(newsAdapter);
    }
}
