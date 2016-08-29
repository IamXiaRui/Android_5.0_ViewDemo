package com.databinding.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.databinding.R;
import com.databinding.adapter.NewsAdapter;
import com.databinding.bean.ResultBean;
import com.databinding.databinding.ActivityNewsBinding;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by xiarui on 2016/8/27.
 * 功能：新闻列表
 */
public class NewsActivity extends AppCompatActivity {

    private final static String NEWS_URL = "http://api.tianapi.com/keji/?key=3cd7cc6ab7736c7b29b2280b0f282c39&num=30&rand";

    private List<ResultBean> newsLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //从URL获取数据
        getDataFromHttp();
    }


    /**
     * Okhttp请求获取数据
     */
    private void getDataFromHttp() {
        OkHttpUtils
                .get()
                .url(NEWS_URL)
                .build()
                .execute(new NewsCallback());
    }

    class NewsCallback extends Callback<List<ResultBean.NewsBean>> {

        @Override
        public List<ResultBean.NewsBean> parseNetworkResponse(Response response, int id) throws Exception {
            ResultBean resultBean = new Gson().fromJson(response.body().string(), ResultBean.class);
            return resultBean.getNewslist();
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            Toast.makeText(NewsActivity.this, "onError", Toast.LENGTH_SHORT).show();
            Log.e("===== onError =====", e.toString());
        }

        @Override
        public void onResponse(List<ResultBean.NewsBean> response, int id) {
            ActivityNewsBinding anBinding = DataBindingUtil.setContentView(NewsActivity.this, R.layout.activity_news);
            anBinding.rlvNews.setLayoutManager(new LinearLayoutManager(NewsActivity.this));
            NewsAdapter newsAdapter = new NewsAdapter(NewsActivity.this, response);
            anBinding.rlvNews.setAdapter(newsAdapter);
        }
    }
}



