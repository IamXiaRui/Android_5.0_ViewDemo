package xr.hellochartsdemo.ui.activity.other;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import xr.hellochartsdemo.R;
import xr.hellochartsdemo.ui.activity.base.BaseActivity;
import xr.hellochartsdemo.ui.activity.chart.BubbleChartActivity;
import xr.hellochartsdemo.ui.activity.chart.ColumnChartActivity;
import xr.hellochartsdemo.ui.activity.chart.LineChartActivity;
import xr.hellochartsdemo.ui.activity.chart.PieChartActivity;
import xr.hellochartsdemo.ui.activity.chart.PreviewColumnChartActivity;
import xr.hellochartsdemo.ui.activity.chart.PreviewLineChartActivity;


/**
 * @author lecho
 * @revision xiarui 2016.09.07
 * @description 图表控件HelloCharts的使用
 */
public class MainActivity extends BaseActivity {

    private CardView lineCardView;
    private ImageView lineImage;

    private CardView columnCardView;
    private ImageView columnImage;

    private CardView pieCardView;
    private ImageView pieImage;

    private CardView bubbleCardView;
    private ImageView bubbleImage;

    private CardView preLineCardView;
    private ImageView preLineImage;

    private CardView preColumnCardView;
    private ImageView preColumnImage;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        lineCardView = (CardView) findViewById(R.id.cv_line_chart);
        lineImage = (ImageView) findViewById(R.id.iv_line_chart);

        columnCardView = (CardView) findViewById(R.id.cv_column_chart);
        columnImage = (ImageView) findViewById(R.id.iv_column_chart);

        pieCardView = (CardView) findViewById(R.id.cv_pie_chart);
        pieImage = (ImageView) findViewById(R.id.iv_pie_chart);

        bubbleCardView = (CardView) findViewById(R.id.cv_bubble_chart);
        bubbleImage = (ImageView) findViewById(R.id.iv_bubble_chart);

        preLineCardView = (CardView) findViewById(R.id.cv_pre_line_chart);
        preLineImage = (ImageView) findViewById(R.id.iv_pre_line_chart);

        preColumnCardView = (CardView) findViewById(R.id.cv_pre_column_chart);
        preColumnImage = (ImageView) findViewById(R.id.iv_pre_column_chart);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        lineCardView.setOnClickListener(this);
        columnCardView.setOnClickListener(this);
        pieCardView.setOnClickListener(this);
        bubbleCardView.setOnClickListener(this);
        preLineCardView.setOnClickListener(this);
        preColumnCardView.setOnClickListener(this);
    }

    @Override
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
            case R.id.cv_pre_line_chart:
                Intent i5 = new Intent(this, PreviewLineChartActivity.class);
                startActivity(i5, ActivityOptions.makeSceneTransitionAnimation(this, preLineImage, "pre_line").toBundle());
                break;
            case R.id.cv_pre_column_chart:
                Intent i6 = new Intent(this, PreviewColumnChartActivity.class);
                startActivity(i6, ActivityOptions.makeSceneTransitionAnimation(this, preColumnImage, "pre_column").toBundle());
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
