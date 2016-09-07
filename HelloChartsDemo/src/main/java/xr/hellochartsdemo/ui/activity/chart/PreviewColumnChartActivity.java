package xr.hellochartsdemo.ui.activity.chart;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PreviewColumnChartView;
import xr.hellochartsdemo.R;
import xr.hellochartsdemo.ui.activity.base.BaseActivity;

/**
 * @author lecho
 * @revision xiarui 2016.09.07
 * @description 支持预览的柱状图 PreviewColumn Chart 的使用
 */
public class PreviewColumnChartActivity extends BaseActivity {

    /*========== 控件相关 ==========*/
    private ColumnChartView mColumnChartView;
    private PreviewColumnChartView mPreColumnChartView;

    /*========== 数据相关 ==========*/
    private ColumnChartData mChartData;
    private ColumnChartData mPreChartData;

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview_column_chart;
    }

    @Override
    public void initView() {
        mColumnChartView = (ColumnChartView) findViewById(R.id.ccv_pre_main);
        mPreColumnChartView = (PreviewColumnChartView) findViewById(R.id.pccv_pre_main);
    }

    @Override
    public void initData() {
        setAllDatas();                                              //设置所有的数据

        mColumnChartView.setColumnChartData(mChartData);            //设置选中区内容
        mPreColumnChartView.setColumnChartData(mPreChartData);      //设置预览区内容

        mColumnChartView.setZoomEnabled(false);                     //禁用缩放
        mColumnChartView.setScrollEnabled(false);                   //禁用滚动

        previewX();                                                 //初识只能X方向滑动
    }

    @Override
    public void initListener() {
        mPreColumnChartView.setViewportChangeListener(new ViewportListener());
    }

    @Override
    public void processClick(View v) {
        //TODO:其他点击事件
    }

    /**
     * 设置选区和预览区的所有数据
     */
    private void setAllDatas() {
        int numSubcolumns = 1;        //单子列
        int numColumns = 42;          //总列数

        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;

        //循环给每个列设置不同的随机值
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
            }
            columns.add(new Column(values));
        }

        //设置一些其他属性
        mChartData = new ColumnChartData(columns);
        mChartData.setAxisXBottom(new Axis());
        mChartData.setAxisYLeft(new Axis().setHasLines(true));

        //预览区数据相同
        mPreChartData = new ColumnChartData(mChartData);

        //所有的预览区列都变成灰色 好看一点
        for (Column column : mPreChartData.getColumns()) {
            for (SubcolumnValue value : column.getValues()) {
                value.setColor(ChartUtils.DEFAULT_DARKEN_COLOR);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview_column_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pre_column_reset:
                setAllDatas();                          //重新设置
                previewX();                             //只在X方向预览
                return true;
            case R.id.menu_pre_column_both:
                previewXY();                            //X/Y方向都可预览
                return true;
            case R.id.menu_pre_column_x:
                previewX();                             //只在X方向预览
                return true;
            case R.id.menu_pre_column_y:
                previewY();                             //只在Y方向预览
                return true;
            case R.id.menu_pre_column_change_color:
                changePreviewBoxColor();                //改变预览区选框颜色
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * X/Y方向都能预览
     */
    private void previewXY() {
        /*========== 类似于 PreviewLine Chart ==========*/
        Viewport tempViewport = new Viewport(mColumnChartView.getMaximumViewport());
        float dx = tempViewport.width() / 4;
        float dy = tempViewport.height() / 4;
        tempViewport.inset(dx, dy);
        mPreColumnChartView.setCurrentViewportWithAnimation(tempViewport);
        mPreColumnChartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
    }

    /**
     * 只在X方向预览
     */
    private void previewX() {
        /*========== 类似于 PreviewLine Chart ==========*/
        Viewport tempViewport = new Viewport(mColumnChartView.getMaximumViewport());
        float dx = tempViewport.width() / 4;
        tempViewport.inset(dx, 0);
        mPreColumnChartView.setCurrentViewportWithAnimation(tempViewport);
        mPreColumnChartView.setZoomType(ZoomType.HORIZONTAL);
    }

    /**
     * 只在Y方向预览
     */
    private void previewY() {
        /*========== 类似于 PreviewLine Chart ==========*/
        Viewport tempViewport = new Viewport(mColumnChartView.getMaximumViewport());
        float dy = tempViewport.height() / 4;
        tempViewport.inset(0, dy);
        mPreColumnChartView.setCurrentViewportWithAnimation(tempViewport);
        mPreColumnChartView.setZoomType(ZoomType.VERTICAL);
    }

    /**
     * 改变预览区选框颜色
     */
    private void changePreviewBoxColor() {
        int color = ChartUtils.pickColor();
        //必须与当前显示颜色不同
        while (color == mPreColumnChartView.getPreviewColor()) {
            color = ChartUtils.pickColor();
        }
        mPreColumnChartView.setPreviewColor(color);           //重新设置颜色
    }

    /**
     * 预览区滑动监听
     */
    private class ViewportListener implements ViewportChangeListener {
        @Override
        public void onViewportChanged(Viewport newViewport) {
            // 这里切记不要使用动画，因为预览图是不需要动画的
            mColumnChartView.setCurrentViewport(newViewport);         //直接设置当前窗口图表
        }
    }
}
