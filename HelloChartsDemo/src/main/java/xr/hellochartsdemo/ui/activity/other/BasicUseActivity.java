package xr.hellochartsdemo.ui.activity.other;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import xr.hellochartsdemo.R;
import xr.hellochartsdemo.ui.activity.base.BaseActivity;
import xr.hellochartsdemo.ui.activity.chart.BubbleChartActivity;
import xr.hellochartsdemo.ui.activity.chart.ColumnChartActivity;
import xr.hellochartsdemo.ui.activity.chart.LineChartActivity;
import xr.hellochartsdemo.ui.activity.chart.PieChartActivity;

/**
 * @author xiarui 2016.09.08
 * @description 基础控件使用页面
 */
public class BasicUseActivity extends BaseActivity {

    private ImageView titleImage;

    private CardView lineCardView;
    private ImageView lineImage;

    private CardView columnCardView;
    private ImageView columnImage;

    private CardView pieCardView;
    private ImageView pieImage;

    private CardView bubbleCardView;
    private ImageView bubbleImage;


    @Override
    public int getLayoutId() {
        return R.layout.activity_basic_use;
    }

    @Override
    public void initView() {
        //透明状态栏 导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        titleImage = (ImageView) findViewById(R.id.iv_basic_title);

        lineCardView = (CardView) findViewById(R.id.cv_line_chart);
        lineImage = (ImageView) findViewById(R.id.iv_line_chart);

        columnCardView = (CardView) findViewById(R.id.cv_column_chart);
        columnImage = (ImageView) findViewById(R.id.iv_column_chart);

        pieCardView = (CardView) findViewById(R.id.cv_pie_chart);
        pieImage = (ImageView) findViewById(R.id.iv_pie_chart);

        bubbleCardView = (CardView) findViewById(R.id.cv_bubble_chart);
        bubbleImage = (ImageView) findViewById(R.id.iv_bubble_chart);
    }

    @Override
    public void initData() {
        Glide.with(this).load(R.mipmap.heng_3).into(titleImage);
    }

    @Override
    public void initListener() {
        lineCardView.setOnClickListener(this);
        columnCardView.setOnClickListener(this);
        pieCardView.setOnClickListener(this);
        bubbleCardView.setOnClickListener(this);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void processClick(View v) {
        switch (v.getId()) {
            case R.id.cv_line_chart:
                Intent i1 = new Intent(this, LineChartActivity.class);
                startActivity(i1, ActivityOptions.makeSceneTransitionAnimation(this, lineImage, "line").toBundle());
                break;
            case R.id.cv_column_chart:
                Intent i2 = new Intent(this, ColumnChartActivity.class);
                startActivity(i2, ActivityOptions.makeSceneTransitionAnimation(this, columnImage, "column").toBundle());
                break;
            case R.id.cv_pie_chart:
                Intent i3 = new Intent(this, PieChartActivity.class);
                startActivity(i3, ActivityOptions.makeSceneTransitionAnimation(this, pieImage, "pie").toBundle());
                break;
            case R.id.cv_bubble_chart:
                Intent i4 = new Intent(this, BubbleChartActivity.class);
                startActivity(i4, ActivityOptions.makeSceneTransitionAnimation(this, bubbleImage, "bubble").toBundle());
                break;
        }
    }
}
