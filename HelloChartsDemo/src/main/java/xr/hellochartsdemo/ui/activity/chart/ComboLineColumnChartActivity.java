package xr.hellochartsdemo.ui.activity.chart;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ComboLineColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;
import xr.hellochartsdemo.R;
import xr.hellochartsdemo.ui.activity.base.BaseActivity;

/**
 * @author lecho
 * @revision xiarui 2016.09.08
 * @description 线性和柱状图结合的图表 ComboLineColumn Chart 的使用
 */
public class ComboLineColumnChartActivity extends BaseActivity {

    /*========== 控件相关 ==========*/
    private ComboLineColumnChartView mComboView;        //线性与柱状控件

    /*========== 状态相关 ==========*/
    private boolean isHasAxes = true;                   //是否有坐标轴
    private boolean isHasAxesName = true;               //坐标轴是否有名称
    private boolean isHasPoints = true;                 //是否有节点
    private boolean isHasLines = true;                  //是否有线
    private boolean isCubic = false;                    //是否是曲线
    private boolean isHasLabels = false;                //是否有标签

    /*========== 数据相关 ==========*/
    private ComboLineColumnChartData mComboData;        //联合图表数据
    private int numberOfLines = 1;                      //初始线条数
    private int maxNumberOfLines = 3;                   //最大线数
    private int numberOfPoints = 10;                    //最大点数
    //线与点的数据数组
    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];


    @Override
    public int getLayoutId() {
        return R.layout.activity_combo_line_column_chart;
    }

    @Override
    public void initView() {
        mComboView = (ComboLineColumnChartView) findViewById(R.id.clccv_main);
    }

    @Override
    public void initData() {
        setPointsDatas();           //设置点的数据
        setComboDatas();            //设置所有的数据
    }

    @Override
    public void initListener() {
        mComboView.setOnValueTouchListener(new ValueTouchListener());
    }

    @Override
    public void processClick(View v) {
        //TODO:其他点击事件
    }

    /**
     * 随机生成每条线上对应的点的数据
     */
    private void setPointsDatas() {
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 50f + 5;
            }
        }
    }

    /**
     * 设置结合起来的数据
     */
    private void setComboDatas() {
        //需要同时设置柱状数据和线性数据
        mComboData = new ComboLineColumnChartData(setColumnData(), setLineData());
        //坐标轴的相关设置 类似 Line Chart
        if (isHasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (isHasAxesName) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            mComboData.setAxisXBottom(axisX);
            mComboData.setAxisYLeft(axisY);
        } else {
            mComboData.setAxisXBottom(null);
            mComboData.setAxisYLeft(null);
        }
        //设置数据
        mComboView.setComboLineColumnChartData(mComboData);
    }

    /**
     * 设置线性图数据 类似Line Chart 省略注释
     *
     * @return 线性图数据
     */
    private LineChartData setLineData() {
        List<Line> lines = new ArrayList<>();

        for (int i = 0; i < numberOfLines; ++i) {
            List<PointValue> values = new ArrayList<>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setCubic(isCubic);
            line.setHasLabels(isHasLabels);
            line.setHasLines(isHasLines);
            line.setHasPoints(isHasPoints);
            lines.add(line);
        }
        return new LineChartData(lines);
    }

    /**
     * 设置柱状图数据 类似 Column Chart 省略注释
     *
     * @return 柱状图数据
     */
    private ColumnChartData setColumnData() {
        int numSubcolumns = 1;
        int numColumns = 10;

        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;

        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50 + 5, ChartUtils.COLOR_GREEN));
            }
            columns.add(new Column(values));
        }

        return new ColumnChartData(columns);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_combo_line_column_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_combo_reset:
                resetCombos();                      //重置数据
                return true;
            case R.id.menu_combo_add_line:
                addLineToData();                    //增加线条
                return true;
            case R.id.menu_combo_show_hide_line:
                showOrHideLines();                  //显示/隐藏线条
                return true;
            case R.id.menu_combo_show_hide_point:
                showOrHidePoints();                 //显示/隐藏节点
                return true;
            case R.id.menu_combo_cubic:
                changeCubicLines();                 //改变曲线/直线
                return true;
            case R.id.menu_combo_show_hide_labels:
                showOrHideLabels();                 //显示/隐藏标签
                return true;
            case R.id.menu_combo_show_hide_axes:
                showOrHideAxes();                   //显示/隐藏坐标轴
                return true;
            case R.id.menu_combo_show_hide_axes_name:
                showOrHideAxesName();               //显示/坐标轴名称
                return true;
            case R.id.menu_combo_animate:
                changeDatasAnimate();               //改变数据时动画
                return true;
            case R.id.menu_combo_touch_zoom:
                mComboView.setZoomEnabled(!mComboView.isZoomEnabled());     //是否可以缩放
                return true;
            case R.id.menu_combo_touch_zoom_xy:
                mComboView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);   //水平垂直缩放
                return true;
            case R.id.menu_combo_touch_zoom_x:
                mComboView.setZoomType(ZoomType.HORIZONTAL);                //只能水平缩放
                return true;
            case R.id.menu_combo_touch_zoom_y:
                mComboView.setZoomType(ZoomType.VERTICAL);                  //只能垂直缩放
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 重置图表
     */
    private void resetCombos() {
        numberOfLines = 1;
        isHasAxes = true;
        isHasAxesName = true;
        isHasLines = true;
        isHasPoints = true;
        isHasLabels = false;
        isCubic = false;
    }

    /**
     * 添加一条线
     */
    private void addLineToData() {
        if (mComboData.getLineChartData().getLines().size() >= maxNumberOfLines) {
            Toast.makeText(ComboLineColumnChartActivity.this, "最多只能有三条线", Toast.LENGTH_SHORT).show();
            return;
        } else {
            ++numberOfLines;
        }
        setComboDatas();
    }

    /**
     * 显示或隐藏线
     */
    private void showOrHideLines() {
        isHasLines = !isHasLines;
        setComboDatas();
    }

    /**
     * 显示或隐藏点
     */
    private void showOrHidePoints() {
        isHasPoints = !isHasPoints;
        setComboDatas();
    }

    /**
     * 切换曲线/直线
     */
    private void changeCubicLines() {
        isCubic = !isCubic;
        setComboDatas();
    }

    /**
     * 显示/隐藏标签
     */
    private void showOrHideLabels() {
        isHasLabels = !isHasLabels;
        setComboDatas();
    }

    /**
     * 显示/隐藏坐标轴
     */
    private void showOrHideAxes() {
        isHasAxes = !isHasAxes;
        setComboDatas();
    }

    /**
     * 显示/隐藏坐标轴名称
     */
    private void showOrHideAxesName() {
        isHasAxesName = !isHasAxesName;
        setComboDatas();
    }

    /**
     * 数据改变动画
     */
    private void changeDatasAnimate() {

        // 线条动画
        for (Line line : mComboData.getLineChartData().getLines()) {
            for (PointValue value : line.getValues()) {
                value.setTarget(value.getX(), (float) Math.random() * 50 + 5);
            }
        }

        // 柱状图动画
        for (Column column : mComboData.getColumnChartData().getColumns()) {
            for (SubcolumnValue value : column.getValues()) {
                value.setTarget((float) Math.random() * 50 + 5);
            }
        }

        mComboView.startDataAnimation();
    }


    /**
     * 图表点击事件
     */
    private class ValueTouchListener implements ComboLineColumnChartOnValueSelectListener {

        @Override
        public void onColumnValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            Toast.makeText(ComboLineColumnChartActivity.this, "当前选中值约为：" + (int) value.getValue(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPointValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(ComboLineColumnChartActivity.this, "选中第 " + ((int) value.getX() + 1) + " 个节点", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {

        }
    }

}
