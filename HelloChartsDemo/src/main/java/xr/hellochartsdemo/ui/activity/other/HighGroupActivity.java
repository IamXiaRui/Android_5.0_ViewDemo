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
import xr.hellochartsdemo.ui.activity.chart.ComboLineColumnChartActivity;
import xr.hellochartsdemo.ui.activity.chart.LineDependColumnActivity;
import xr.hellochartsdemo.ui.activity.chart.PreviewColumnChartActivity;
import xr.hellochartsdemo.ui.activity.chart.PreviewLineChartActivity;

/**
 * @author xiarui 2016.09.08
 * @description 高级组合使用页面
 */
public class HighGroupActivity extends BaseActivity {

    private ImageView titleImage;

    private CardView preLineCardView;
    private ImageView preLineImage;

    private CardView preColumnCardView;
    private ImageView preColumnImage;

    private CardView comboLineColumnCardView;
    private ImageView comboLineColumnImage;

    private CardView lineDependColumnCardView;
    private ImageView lineDependColumnImage;

    @Override
    public int getLayoutId() {
        return R.layout.activity_high_group;
    }

    @Override
    public void initView() {
        //透明状态栏 导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        titleImage = (ImageView) findViewById(R.id.iv_high_title);

        preLineCardView = (CardView) findViewById(R.id.cv_pre_line_chart);
        preLineImage = (ImageView) findViewById(R.id.iv_pre_line_chart);

        preColumnCardView = (CardView) findViewById(R.id.cv_pre_column_chart);
        preColumnImage = (ImageView) findViewById(R.id.iv_pre_column_chart);

        comboLineColumnCardView = (CardView) findViewById(R.id.cv_combo_line_column_chart);
        comboLineColumnImage = (ImageView) findViewById(R.id.iv_combo_line_column_chart);

        lineDependColumnCardView = (CardView) findViewById(R.id.cv_depend_chart);
        lineDependColumnImage = (ImageView) findViewById(R.id.iv_depend_chart);
    }

    @Override
    public void initData() {
        Glide.with(this).load(R.mipmap.heng_1).into(titleImage);
    }

    @Override
    public void initListener() {
        preLineCardView.setOnClickListener(this);
        preColumnCardView.setOnClickListener(this);
        comboLineColumnCardView.setOnClickListener(this);
        lineDependColumnCardView.setOnClickListener(this);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void processClick(View v) {
        switch (v.getId()) {
            case R.id.cv_pre_line_chart:
                Intent i1 = new Intent(this, PreviewLineChartActivity.class);
                startActivity(i1, ActivityOptions.makeSceneTransitionAnimation(this, preLineImage, "pre_line").toBundle());
                break;
            case R.id.cv_pre_column_chart:
                Intent i2 = new Intent(this, PreviewColumnChartActivity.class);
                startActivity(i2, ActivityOptions.makeSceneTransitionAnimation(this, preColumnImage, "pre_column").toBundle());
                break;
            case R.id.cv_combo_line_column_chart:
                Intent i3 = new Intent(this, ComboLineColumnChartActivity.class);
                startActivity(i3, ActivityOptions.makeSceneTransitionAnimation(this, comboLineColumnImage, "combo_line_column").toBundle());
                break;
            case R.id.cv_depend_chart:
                Intent i4 = new Intent(this, LineDependColumnActivity.class);
                startActivity(i4, ActivityOptions.makeSceneTransitionAnimation(this, lineDependColumnImage, "line_depend_column").toBundle());
                break;
        }
    }
}
