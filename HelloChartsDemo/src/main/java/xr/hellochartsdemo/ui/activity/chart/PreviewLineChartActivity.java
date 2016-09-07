package xr.hellochartsdemo.ui.activity.chart;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PreviewLineChartView;
import xr.hellochartsdemo.R;
import xr.hellochartsdemo.ui.activity.base.BaseActivity;

/**
 * @author lecho
 * @revision xiarui 2016.09.07
 * @description 支持预览的线性图 PreviewLine Chart 的使用
 */
public class PreviewLineChartActivity extends BaseActivity {

    /*========== 控件相关 ==========*/
    private LineChartView mLineChartView;               //展示区域的控件
    private PreviewLineChartView mPreLineChartView;     //预览区域的控件

    /*========== 数据相关 ==========*/
    private LineChartData mChartData;                   //展示区域的数据
    private LineChartData mPreChartData;                //预览区域的数据

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview_line_chart;
    }

    @Override
    public void initView() {
        mLineChartView = (LineChartView) findViewById(R.id.lcv_pre_main);
        mPreLineChartView = (PreviewLineChartView) findViewById(R.id.plcv_pre_main);
    }

    @Override
    public void initData() {
        setAllDatas();                                       //设置数据

        mLineChartView.setLineChartData(mChartData);         //设置选中区数据
        mPreLineChartView.setLineChartData(mPreChartData);   //设置预览区数据

        /*===== 必须禁用部分图的缩放与滑动效果 因为部分图取决于预览图选区 =====*/
        mLineChartView.setZoomEnabled(false);                //禁用缩放
        mLineChartView.setScrollEnabled(false);              //禁用滚动

        previewX();                                          //初识默认显示1/4选区框
    }

    @Override
    public void initListener() {
        //预览区滑动监听
        mPreLineChartView.setViewportChangeListener(new ViewportListener());
    }

    @Override
    public void processClick(View v) {
        //TODO:其他View监听事件
    }

    /**
     * 设置选区和预览区的所有数据
     */
    private void setAllDatas() {
        int numValues = 50;         //总共有50个点

        //给每个点设置随机的值
        List<PointValue> values = new ArrayList<>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, (float) Math.random() * 100f));
        }

        //设置线的相关属性
        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN);
        line.setHasPoints(false);               //点太多不画

        List<Line> lines = new ArrayList<>();
        lines.add(line);

        //设置数据其他属性 比如坐标轴
        mChartData = new LineChartData(lines);
        mChartData.setAxisXBottom(new Axis());
        mChartData.setAxisYLeft(new Axis().setHasLines(true));

        //将相同的数据也设置到预览区 并更改颜色
        mPreChartData = new LineChartData(mChartData);
        mPreChartData.getLines().get(0).setColor(ChartUtils.DEFAULT_DARKEN_COLOR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview_line_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pre_line_reset:
                setAllDatas();                       //重新设置数据
                previewX();                          //并有动画的显示选区
                return true;
            case R.id.menu_pre_line_both:
                previewXY();                         //设置X/Y方向都能预览
                return true;
            case R.id.menu_pre_line_x:
                previewX();                          //设置只能X方向预览
                return true;
            case R.id.menu_pre_line_y:
                previewY();                          //设置只能Y方向预览
                return true;
            case R.id.menu_pre_line_change_color:
                changePreviewBoxColor();             //改变预览区选框颜色
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 根据X/Y方向预览
     */
    private void previewXY() {
        Viewport tempViewport = new Viewport(mLineChartView.getMaximumViewport());
        float dx = tempViewport.width() / 4;                //宽高都是1/4
        float dy = tempViewport.height() / 4;
        tempViewport.inset(dx, dy);                         //设置临时窗口大小
        mPreLineChartView.setCurrentViewportWithAnimation(tempViewport);
        //水平垂直都可以缩放
        mPreLineChartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
    }

    /**
     * 根据X方向预览
     */
    private void previewX() {
        Viewport tempViewport = new Viewport(mLineChartView.getMaximumViewport());
        float dx = tempViewport.width() / 4;            //原区域1/4
        tempViewport.inset(dx, 0);                      //设置临时窗口大小
        //根据是否有动画 设置数据
        mPreLineChartView.setCurrentViewportWithAnimation(tempViewport);
        //只能水平缩放
        mPreLineChartView.setZoomType(ZoomType.HORIZONTAL);
    }

    /**
     * 根据Y方向预览
     */
    private void previewY() {
        Viewport tempViewport = new Viewport(mLineChartView.getMaximumViewport());
        float dy = tempViewport.height() / 4;       //高度为原图1/4
        tempViewport.inset(0, dy);                  //设置临时窗口大小
        mPreLineChartView.setCurrentViewportWithAnimation(tempViewport);
        //只能垂直方向预览
        mPreLineChartView.setZoomType(ZoomType.VERTICAL);
    }

    /**
     * 改变预览区选框颜色
     */
    private void changePreviewBoxColor() {
        int color = ChartUtils.pickColor();
        //必须与当前显示颜色不同
        while (color == mPreLineChartView.getPreviewColor()) {
            color = ChartUtils.pickColor();
        }
        mPreLineChartView.setPreviewColor(color);           //重新设置颜色
    }


    /**
     * 预览区滑动监听
     */
    private class ViewportListener implements ViewportChangeListener {
        @Override
        public void onViewportChanged(Viewport newViewport) {
            // 这里切记不要使用动画，因为预览图是不需要动画的
            mLineChartView.setCurrentViewport(newViewport);         //直接设置当前窗口图表
        }
    }
}
