package xr.recyclerview.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import xr.recyclerview.R;
import xr.recyclerview.adapter.RecyclerAdapter;
import xr.recyclerview.bean.ListBean;

/**
 * ListView替代者 RecyclerView的实现过程
 */
public class RecyclerActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<ListBean> mListData = new ArrayList<ListBean>();
    private List<ListBean> mStaggerData = new ArrayList<ListBean>();
    private int[] mitemIcons = new int[]{R.mipmap.g1, R.mipmap.g2, R.mipmap.g3, R.mipmap.g4,
            R.mipmap.g5, R.mipmap.g6, R.mipmap.g7, R.mipmap.g8, R.mipmap.g9, R.mipmap.g10, R
            .mipmap.g11, R.mipmap.g12, R.mipmap.g13, R.mipmap.g14, R.mipmap.g15, R.mipmap
            .g16, R.mipmap.g17, R.mipmap.g18, R.mipmap.g19, R.mipmap.g20, R.mipmap.g21, R
            .mipmap.g22, R.mipmap.g23, R.mipmap.g24, R.mipmap.g25, R.mipmap.g26, R.mipmap
            .g27, R.mipmap.g28, R.mipmap.g29};

    private int[] mStaggerIcons = new int[]{R.mipmap.p1, R.mipmap.p2, R.mipmap.p3, R
            .mipmap.p4, R.mipmap.p5, R.mipmap.p6, R.mipmap.p7, R.mipmap.p8, R.mipmap.p9, R
            .mipmap.p10, R.mipmap.p11, R.mipmap.p12, R.mipmap.p13, R.mipmap.p14, R.mipmap
            .p15, R.mipmap.p16, R.mipmap.p17, R.mipmap.p18, R.mipmap.p19, R.mipmap.p20, R
            .mipmap.p21, R.mipmap.p22, R.mipmap.p23, R.mipmap.p24, R.mipmap.p25, R.mipmap
            .p26, R.mipmap.p27, R.mipmap.p28, R.mipmap.p29, R.mipmap.p30, R.mipmap.p31, R
            .mipmap.p32, R.mipmap.p33, R.mipmap.p34, R.mipmap.p35, R.mipmap.p36, R.mipmap
            .p37, R.mipmap.p38, R.mipmap.p39, R.mipmap.p40, R.mipmap.p41, R.mipmap.p42, R
            .mipmap.p43, R.mipmap.p44};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        //初始化View
        initView();

        //设置数据
        initData();

        //设置适配器
        initListAdapterV();

        initRefresh();


    }

    /**
     * 初始化View
     */
    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_my);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.spl_main);
    }

    /**
     * 模拟刷新过程
     */
    private void initRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //停止刷新操作
                                mSwipeRefreshLayout.setRefreshing(false);
                                //得到adapter.然后刷新
                                mRecyclerView.getAdapter().notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
            }
        });
    }


    /**
     * 模拟列表数据
     */
    private void initData() {
        for (int i = 0; i < mitemIcons.length; i++) {
            ListBean listBean = new ListBean();
            listBean.iconId = mitemIcons[i];
            listBean.name = "item - " + i;
            mListData.add(listBean);
        }

        for (int i = 0; i < mStaggerIcons.length; i++) {
            ListBean listBean = new ListBean();
            listBean.iconId = mStaggerIcons[i];
            listBean.name = "item - " + i;
            mStaggerData.add(listBean);
        }
    }

    /**
     * 设置纵向列表
     */
    private void initListAdapterV() {
        //设置layoutManager
        LinearLayoutManager layoutManger = new LinearLayoutManager(RecyclerActivity.this);
        mRecyclerView.setLayoutManager(layoutManger);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(RecyclerActivity.this, mListData);
        mRecyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * 设置横向列表
     */
    private void initListAdapterH() {
        //设置layoutManager
        LinearLayoutManager layoutManger = new LinearLayoutManager(RecyclerActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManger);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(RecyclerActivity.this, mListData);
        mRecyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * 设置纵向网格
     */
    private void initGridAdapterV() {
        //设置layoutManager
        GridLayoutManager layoutManger = new GridLayoutManager(RecyclerActivity.this, 3);
        mRecyclerView.setLayoutManager(layoutManger);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(RecyclerActivity.this, mListData);
        mRecyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * 设置横向网格
     */
    private void initGridAdapterH() {
        //设置layoutManager
        GridLayoutManager layoutManger = new GridLayoutManager(RecyclerActivity.this, 3, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManger);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(RecyclerActivity.this, mListData);
        mRecyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * 设置纵向瀑布流
     */
    private void initStaggerAdapterV() {
        //设置layoutManager
        StaggeredGridLayoutManager layoutManger = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManger);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(RecyclerActivity.this, mStaggerData);
        mRecyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * 设置横向瀑布流
     */
    private void initStaggerAdapterH() {
        //设置layoutManager
        StaggeredGridLayoutManager layoutManger = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManger);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(RecyclerActivity.this, mStaggerData);
        mRecyclerView.setAdapter(recyclerAdapter);
    }


    /**
     * 设置菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recycler, menu);
        return true;
    }


    /**
     * 菜单的选择事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_list_v:
                initListAdapterV();
                break;
            case R.id.action_list_h:
                initListAdapterH();
                break;
            case R.id.action_grid_v:
                initGridAdapterV();
                break;
            case R.id.action_grid_h:
                initGridAdapterH();
                break;
            case R.id.action_stragger_v:
                initStaggerAdapterV();
                break;
            case R.id.action_stragger_h:
                initStaggerAdapterH();
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}
