package xr.recyclerview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
    private List<ListBean> mListData = new ArrayList<ListBean>();
    private int[] mitemIcons = new int[]{R.mipmap.g1, R.mipmap.g2, R.mipmap.g3, R.mipmap.g4,
            R.mipmap.g5, R.mipmap.g6, R.mipmap.g7, R.mipmap.g8, R.mipmap.g9, R.mipmap.g10, R
            .mipmap.g11, R.mipmap.g12, R.mipmap.g13, R.mipmap.g14, R.mipmap.g15, R.mipmap
            .g16, R.mipmap.g17, R.mipmap.g18, R.mipmap.g19, R.mipmap.g20, R.mipmap.g21, R
            .mipmap.g22, R.mipmap.g23, R.mipmap.g24, R.mipmap.g25, R.mipmap.g26, R.mipmap
            .g27, R.mipmap.g28, R.mipmap.g29};

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


    }

    /**
     * 初始化View
     */
    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_my);
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
    }

    /**
     * 设置列表适配器
     */
    private void initListAdapterV() {
        //设置layoutManager
        LinearLayoutManager layoutManger = new LinearLayoutManager(RecyclerActivity.this);
        mRecyclerView.setLayoutManager(layoutManger);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(RecyclerActivity.this, mListData);
        mRecyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * 设置列表适配器
     */
    private void initListAdapterH() {
        //设置layoutManager
        LinearLayoutManager layoutManger = new LinearLayoutManager(RecyclerActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManger);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(RecyclerActivity.this, mListData);
        mRecyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * 设置列表适配器
     */
    private void initGridAdapterV() {
        //设置layoutManager
        GridLayoutManager layoutManger = new GridLayoutManager(RecyclerActivity.this, 3);
        mRecyclerView.setLayoutManager(layoutManger);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(RecyclerActivity.this, mListData);
        mRecyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * 设置列表适配器
     */
    private void initGridAdapterH() {
        //设置layoutManager
        GridLayoutManager layoutManger = new GridLayoutManager(RecyclerActivity.this, 3, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManger);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(RecyclerActivity.this, mListData);
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
                Toast.makeText(this, "No Action", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_stragger_h:
                Toast.makeText(this, "No Action", Toast.LENGTH_SHORT).show();
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}
