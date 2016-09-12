package xr.hellochartsdemo.ui.activity.chart;

import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;
import xr.hellochartsdemo.R;
import xr.hellochartsdemo.ui.activity.base.BaseActivity;

/**
 * @author lecho
 * @revision xiarui 2016.09.07
 * @description 饼状图 Pie Chart 的使用
 */
public class PieChartActivity extends BaseActivity {

    /*========= 控件相关 =========*/
    private PieChartView mPieChartView;                 //饼状图控件

    /*========= 状态相关 =========*/
    private boolean isExploded = false;                 //每块之间是否分离
    private boolean isHasLabelsInside = false;          //标签在内部
    private boolean isHasLabelsOutside = false;         //标签在外部
    private boolean isHasCenterCircle = false;          //空心圆环
    private boolean isPiesHasSelected = false;          //块选中标签样式
    private boolean isHasCenterSingleText = false;      //圆环中心单行文字
    private boolean isHasCenterDoubleText = false;      //圆环中心双行文字

    /*========= 数据相关 =========*/
    private PieChartData mPieChartData;                 //饼状图数据

    @Override
    public int getLayoutId() {
        return R.layout.activity_pie_chart;
    }

    @Override
    public void initView() {
        mPieChartView = (PieChartView) findViewById(R.id.pcv_main);
    }

    @Override
    public void initData() {
        setPieDatas();
    }

    @Override
    public void initListener() {
        mPieChartView.setOnValueTouchListener(new ValueTouchListener());
    }

    @Override
    public void processClick(View v) {
        //TODO:其他View点击事件
    }

    /**
     * 设置相关数据
     */
    private void setPieDatas() {
        int numValues = 6;                //把一张饼切成6块

        /*===== 随机设置每块的颜色和数据 =====*/
        List<SliceValue> values = new ArrayList<>();
        for (int i = 0; i < numValues; ++i) {
            SliceValue sliceValue = new SliceValue((float) Math.random() * 30 + 15, ChartUtils.pickColor());
            values.add(sliceValue);
        }

        /*===== 设置相关属性 类似Line Chart =====*/
        mPieChartData = new PieChartData(values);
        mPieChartData.setHasLabels(isHasLabelsInside);
        mPieChartData.setHasLabelsOnlyForSelected(isPiesHasSelected);
        mPieChartData.setHasLabelsOutside(isHasLabelsOutside);
        mPieChartData.setHasCenterCircle(isHasCenterCircle);

        //是否分离
        if (isExploded) {
            mPieChartData.setSlicesSpacing(18);                 //分离间距为18
        }

        //是否显示单行文本
        if (isHasCenterSingleText) {
            mPieChartData.setCenterText1("Hello");             //文本内容
        }

        //是否显示双行文本
        if (isHasCenterDoubleText) {
            mPieChartData.setCenterText2("World");             //文本内容

            /*===== 设置内置字体 不建议设置 除非有特殊需求 =====*/
            Typeface tf = Typeface.createFromAsset(this.getAssets(), "Roboto-Italic.ttf");
            mPieChartData.setCenterText2Typeface(tf);
            mPieChartData.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_double_text_size)));
        }
        mPieChartView.setPieChartData(mPieChartData);         //设置控件
    }

    /**
     * 菜单
     *
     * @param menu 菜单
     * @return true 显示
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pie_chart, menu);
        return true;
    }

    /**
     * 菜单选项
     *
     * @param item 菜单项
     * @return true 执行
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pie_reset:
                resetPieDatas();                            //重置数据
                return true;
            case R.id.menu_pie_explode_implode:
                explodeChart();                             //分离每部分
                return true;
            case R.id.menu_pie_center_circle:
                setCenterCircle();                          //设置空心圆环
                return true;
            case R.id.menu_pie_center_text_single:
                setSingleTextInCenter();                    //设置环心单行文本
                return true;
            case R.id.menu_pie_center_text_double:
                setDoubleTextInCenter();                    //设置环心双行文本
                return true;
            case R.id.menu_pie_show_hide_labels_inside:
                showOrHideLabelsInside();                   //在内部设置标签
                return true;
            case R.id.menu_pie_show_hide_labels_outside:
                showOrHideLabelsOutside();                  //在外部设置标签
                return true;
            case R.id.menu_pie_animate:
                changePiesAnimate();                        //改变数据时动画
                return true;
            case R.id.menu_pie_pies_select_mode:
                showOrHideLablesByPiesSelected();           //点击显示/不显示标签
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 重置饼状图
     */
    private void resetPieDatas() {
        /*========== 恢复相关属性 ==========*/
        mPieChartView.setCircleFillRatio(1.0f);     //充满整张饼
        isHasLabelsInside = false;
        isHasLabelsOutside = false;
        isHasCenterCircle = false;
        isHasCenterSingleText = false;
        isHasCenterDoubleText = false;
        isExploded = false;
        isPiesHasSelected = false;

        setPieDatas();                              //重新设置
    }

    /**
     * 块之间有空隙
     */
    private void explodeChart() {
        isExploded = !isExploded;           //取反即可
        setPieDatas();                      //重新设置
    }

    /**
     * 设置空心圆环
     */
    private void setCenterCircle() {
        isHasCenterCircle = !isHasCenterCircle;         //取反即可
        if (!isHasCenterCircle) {
            isHasCenterSingleText = false;              //无圆环则不显示字
            isHasCenterDoubleText = false;
        }
        setPieDatas();                                  //重新设置
    }

    /**
     * 圆环中心设置单行文字
     */
    private void setSingleTextInCenter() {
        isHasCenterSingleText = !isHasCenterSingleText;     //取反即可
        if (isHasCenterSingleText) {
            isHasCenterCircle = true;                       //必须是空心圆环
        }
        isHasCenterDoubleText = false;                      //没有两行
        setPieDatas();                                      //重新设置
    }

    /**
     * 圆环中心设置双行文字
     */
    private void setDoubleTextInCenter() {
        isHasCenterDoubleText = !isHasCenterDoubleText;     //取反即可
        if (isHasCenterDoubleText) {
            isHasCenterSingleText = true;                   //如果设置双行 则单行必须显示
            isHasCenterCircle = true;                       //必须是空心圆环
        }
        setPieDatas();                                      //重新设置
    }

    /**
     * 在内部显示标签
     */
    private void showOrHideLabelsInside() {
        isHasLabelsInside = !isHasLabelsInside;         //取反即可
        if (isHasLabelsInside) {
            isPiesHasSelected = false;                  //点击不显示标签
            //设置点击不显示标签
            mPieChartView.setValueSelectionEnabled(isPiesHasSelected);
            //已经在外部的话 适当变化形状
            if (isHasLabelsOutside) {
                mPieChartView.setCircleFillRatio(0.7f);
            } else {
                mPieChartView.setCircleFillRatio(1.0f);
            }
        }
        setPieDatas();                                  //重新设置
    }

    /**
     * 在外部显示标签
     */
    private void showOrHideLabelsOutside() {
        isHasLabelsOutside = !isHasLabelsOutside;       //取反即可
        if (isHasLabelsOutside) {
            isHasLabelsInside = true;                   //如果外面不显示 就在内部显示
            isPiesHasSelected = false;                  //点击不显示标签
            //设置点击不显示标签
            mPieChartView.setValueSelectionEnabled(isPiesHasSelected);
        }
        //已经在外部的话 适当变化形状
        if (isHasLabelsOutside) {
            mPieChartView.setCircleFillRatio(0.7f);
        } else {
            mPieChartView.setCircleFillRatio(1.0f);
        }
        setPieDatas();                                  //重新设置
    }

    /**
     * 改变数据时的动画
     */
    private void changePiesAnimate() {
        //随机设置值
        for (SliceValue value : mPieChartData.getValues()) {
            value.setTarget((float) Math.random() * 30 + 15);
        }
        mPieChartView.startDataAnimation();         //设置动画
    }

    /**
     * 点击每部分是否显示标签信息
     */
    private void showOrHideLablesByPiesSelected() {
        isPiesHasSelected = !isPiesHasSelected;             //取反即可
        //点击是否显示标签
        mPieChartView.setValueSelectionEnabled(isPiesHasSelected);
        if (isPiesHasSelected) {
            isHasLabelsInside = false;                      //内外都不显示标签
            isHasLabelsOutside = false;
            //如果已经在外部 适当变形
            if (isHasLabelsOutside) {
                mPieChartView.setCircleFillRatio(0.7f);
            } else {
                mPieChartView.setCircleFillRatio(1.0f);
            }
        }
        setPieDatas();                                      //重新设置
    }

    /**
     * 每部分点击监听
     */
    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            Toast.makeText(PieChartActivity.this, "当前选中块约占: " + (int) value.getValue() + " %", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
        }
    }
}
