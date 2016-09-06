package xr.hellochartsdemo.ui.activity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import xr.hellochartsdemo.R;

/**
 * @author xiarui 16.19.06
 * @description 柱状图 Column Chart的使用
 */
public class ColumnChartActivity extends BaseActivity {

    /*========== 控件相关 ==========*/
    private ColumnChartView mColumnCharView;                //柱形图控件


    /*========== 状态相关 ==========*/
    private boolean isHasAxes = true;                       //是否显示坐标轴
    private boolean isHasAxesNames = true;                  //是否显示坐标轴
    private boolean isHasColumnLabels = false;              //是否显示列标签
    private boolean isColumnsHasSelected = false;           //设置列点击后效果(消失/显示标签)

    private static final int DEFAULT_DATA = 0;              //默认数据状态
    private static final int SUBCOLUMNS_DATA = 1;           //子列数据状态
    private static final int NEGATIVE_SUBCOLUMNS_DATA = 2;  //反向子列状态
    private static final int STACKED_DATA = 3;              //堆放数据状态
    private static final int NEGATIVE_STACKED_DATA = 4;     //反向堆放数据状态

    /*========== 数据相关 ==========*/
    private ColumnChartData mColumnChartData;               //柱状图数据
    private int dataType = DEFAULT_DATA;                    //默认数据状态

    @Override
    public int getLayoutId() {
        return R.layout.activity_column_chart;
    }

    @Override
    public void initView() {
        mColumnCharView = (ColumnChartView) findViewById(R.id.ccv_main);
    }

    @Override
    public void initData() {
        setColumnDatas();        //根据状态设置相关数据
    }

    @Override
    public void initListener() {
        //节点触摸监听
        mColumnCharView.setOnValueTouchListener(new ValueTouchListener());
    }

    @Override
    public void processClick(View v) {
        //TODO：其他点击事件
    }

    private void setColumnDatas() {
        switch (dataType) {
            case DEFAULT_DATA:
                setDefaultColumnDatas();                 //设置默认数据
                break;
            case SUBCOLUMNS_DATA:
                //generateSubcolumnsData();
                break;
            case NEGATIVE_SUBCOLUMNS_DATA:
                //generateNegativeSubcolumnsData();
                break;
            case STACKED_DATA:
                //generateStackedData();
                break;
            case NEGATIVE_STACKED_DATA:
                //generateNegativeStackedData();
                break;
            default:
                //generateDefaultData();
                break;
        }
    }

    /**
     * 设置默认数据
     */
    private void setDefaultColumnDatas() {
        int numSubcolumns = 1;      //子列数
        int numColumns = 8;         //总列数

        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        //双重for循环给每个子列设置随机的值和随机的颜色
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
            }

            /*===== 柱状图相关设置 =====*/
            Column column = new Column(values);
            column.setHasLabels(isHasColumnLabels);                         //没有标签
            column.setHasLabelsOnlyForSelected(isColumnsHasSelected);  //点击只放大
            columns.add(column);
        }
        mColumnChartData = new ColumnChartData(columns);              //设置数据

        /*===== 坐标轴相关设置 类似与Line Charts =====*/
        if (isHasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (isHasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            mColumnChartData.setAxisXBottom(axisX);
            mColumnChartData.setAxisYLeft(axisY);
        } else {
            mColumnChartData.setAxisXBottom(null);
            mColumnChartData.setAxisYLeft(null);
        }
        mColumnCharView.setColumnChartData(mColumnChartData);
    }

    /**
     * 菜单
     *
     * @param menu 菜单
     * @return true 显示
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_column_chart, menu);
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
            case R.id.menu_column_reset:
                return true;
            case R.id.menu_column_subcolumns:
                return true;
            case R.id.menu_column_negative_subcolumns:
                return true;
            case R.id.menu_column_stacked:
                return true;
            case R.id.menu_column_negative_stacked:
                return true;
            case R.id.menu_column_show_hide_lables:
                showOrHideColumnsLables();                                          //显示或隐藏列标签
                return true;
            case R.id.menu_column_show_hide_axes:
                showOrHideAxes();                                                   //显示或者隐藏坐标轴
                return true;
            case R.id.menu_column_show_hide_axes_name:
                showOrHideAxesName();                                               //显示或者隐藏坐标轴名称
                return true;
            case R.id.menu_column_animate:
                return true;
            case R.id.menu_column_point_select_mode:
                return true;
            case R.id.menu_column_touch_zoom:
                mColumnCharView.setZoomEnabled(!mColumnCharView.isZoomEnabled());   //全局缩放
                return true;
            case R.id.menu_column_touch_zoom_xy:
                mColumnCharView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);      //水平垂直缩放
                return true;
            case R.id.menu_column_touch_zoom_x:
                mColumnCharView.setZoomType(ZoomType.HORIZONTAL);                   //水平缩放
                return true;
            case R.id.menu_column_touch_zoom_y:
                mColumnCharView.setZoomType(ZoomType.VERTICAL);                     //垂直缩放
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示或者隐藏节点标签
     */
    private void showOrHideColumnsLables() {
        isHasColumnLabels = !isHasColumnLabels;   //取反即可
        setColumnDatas();                          //重新设置
    }

    /**
     * 显示或者隐藏坐标轴
     */
    private void showOrHideAxes() {
        isHasAxes = !isHasAxes;                   //取反即可
        setColumnDatas();                       //重新设置
    }

    /**
     * 显示或者隐藏坐标轴名称
     */
    private void showOrHideAxesName() {
        isHasAxesNames = !isHasAxesNames;         //取反即可
        setColumnDatas();                          //重新设置
    }


    /**
     * 节点触摸监听
     */
    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            Toast.makeText(ColumnChartActivity.this, "当前条目值约为 " + (int) value.getValue(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {

        }
    }
}
