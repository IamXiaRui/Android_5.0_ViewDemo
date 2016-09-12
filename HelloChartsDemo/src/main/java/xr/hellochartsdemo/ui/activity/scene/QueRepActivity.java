package xr.hellochartsdemo.ui.activity.scene;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import xr.hellochartsdemo.R;
import xr.hellochartsdemo.adapter.WeatherRecyclerAdapter;
import xr.hellochartsdemo.ui.activity.base.BaseActivity;
import xr.hellochartsdemo.ui.view.BlurredView;

/**
 * @author xiarui 2016.09.08
 * @description 问卷调查使用场景
 */
public class QueRepActivity extends BaseActivity {

    /*========== 控件相关 ===========*/
    private BlurredView queRepBView;
    private RecyclerView queRepRView;

    /*========== 数据相关 ===========*/
    private int mScrollerY; //滚动距离
    private int mAlpha; //透明值


    @Override
    public int getLayoutId() {
        return R.layout.activity_que_rep;
    }

    @Override
    public void initView() {
        //透明状态栏 导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        queRepRView = (RecyclerView) findViewById(R.id.rv_que_rep);
        queRepBView = (BlurredView) findViewById(R.id.bv_que_rep);
    }

    @Override
    public void initData() {
        queRepRView.setLayoutManager(new LinearLayoutManager(this));
        queRepRView.setAdapter(new WeatherRecyclerAdapter(this));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void initListener() {
        queRepRView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollerY += dy;                       //滚动距离
                if (Math.abs(mScrollerY) > 1000) {      //根据滚动距离控制模糊程度 滚动距离是模糊程度的十倍
                    mAlpha = 100;
                } else {
                    mAlpha = Math.abs(mScrollerY) / 10;
                }
                queRepBView.setBlurredLevel(mAlpha);    //设置透明度等级
            }
        });
    }

    @Override
    public void processClick(View v) {

    }
}
