package com.commonadapter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.commonadapter.R;
import com.commonadapter.adapter.CommonAdapterWithComonHolder;
import com.commonadapter.adapter.ComonHolderAdapter;
import com.commonadapter.adapter.TraditionAdapter;
import com.commonadapter.bean.NewsBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mainListView;
    private List<NewsBean> listDatas;
    private TraditionAdapter tAdapter;
    private ComonHolderAdapter chAdapter;
    private CommonAdapterWithComonHolder cawchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDatas();

        initView();

    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        listDatas = new ArrayList<>();

        NewsBean bean1 = new NewsBean("第一条数据", "第一条数据的描述", "2016-6-1", "11111");
        listDatas.add(bean1);

        NewsBean bean2 = new NewsBean("第二条数据", "第二条数据的描述", "2016-6-2", "2222");
        listDatas.add(bean2);

        NewsBean bean3 = new NewsBean("第三条数据", "第三条数据的描述", "2016-6-3", "33333");
        listDatas.add(bean3);

        NewsBean bean4 = new NewsBean("第四条数据", "第四条数据的描述", "2016-6-4", "44444");
        listDatas.add(bean4);

        NewsBean bean5 = new NewsBean("第五条数据", "第五条数据的描述", "2016-6-5", "55555");
        listDatas.add(bean5);
    }

    /**
     * 初始化View
     */
    private void initView() {
        mainListView = (ListView) findViewById(R.id.lv_main);

        //传统方式的Adapter
        //tAdapter = new TraditionAdapter(this, listDatas);
        //mainListView.setAdapter(tAdapter);

        //通用ViewHolder方式的Adapter
        //chAdapter = new ComonHolderAdapter(this, listDatas);
        //mainListView.setAdapter(chAdapter);

        //通用Adapter
        cawchAdapter = new CommonAdapterWithComonHolder(this, listDatas);
        mainListView.setAdapter(cawchAdapter);
    }


}
