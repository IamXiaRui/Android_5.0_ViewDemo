package com.moocnewsdemo.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.moocnewsdemo.R;
import com.moocnewsdemo.adapter.NewsAdapter;
import com.moocnewsdemo.bean.NewsBean;
import com.moocnewsdemo.utils.GetJsonUtil;

import java.util.List;

/**
 * 新闻案例：异步任务与异步加载图片的使用
 */
public class MainActivity extends AppCompatActivity {

    private ListView mainListView;
    private Context mainContext = MainActivity.this;
    private String url = "http://www.imooc.com/api/teacher?type=4&num=30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        //开启异步任务
        GetJsonTask getJsonTask = new GetJsonTask();
        getJsonTask.execute(url);
    }

    /**
     * 初始化View
     */
    private void initView() {
        mainListView = (ListView) findViewById(R.id.lv_main);
    }

    /**
     * 自定义异步任务解析JSON数据
     */
    class GetJsonTask extends AsyncTask<String, Void, List<NewsBean>> {

        @Override
        protected List<NewsBean> doInBackground(String... params) {
            return GetJsonUtil.getJson(params[0]);
        }

        @Override
        protected void onPostExecute(List<NewsBean> newsBeen) {
            super.onPostExecute(newsBeen);
            NewsAdapter newsAdapter = new NewsAdapter(mainContext, newsBeen,mainListView);
            mainListView.setAdapter(newsAdapter);
        }
    }
}
