package xr.hellochartsdemo.ui.activity.chart;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.BubbleChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.BubbleChartData;
import lecho.lib.hellocharts.model.BubbleValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.BubbleChartView;
import xr.hellochartsdemo.R;
import xr.hellochartsdemo.ui.activity.base.BaseActivity;

/**
 * @author lecho
 * @revision xiarui 2016.09.08
 * @description 气泡图表 Bubble Chart 的使用
 */
public class BubbleChartActivity extends BaseActivity {

    /*========== 控件相关 ==========*/
    private BubbleChartView mBubbleView;                    //气泡图表控件

    /*========== 状态相关 ==========*/
    private boolean isHasAxes = true;                       //是否有坐标轴
    private boolean isHasAxesName = true;                   //坐标轴是否有名称
    private boolean isHasLabels = false;                    //是否显示气泡标签
    private boolean isBubblesHasSelected = false;           //气泡选中是否显示标签
    private ValueShape bubbleShape = ValueShape.CIRCLE;     //默认气泡形状为圆形(还有方形)

    /*========== 数据相关 ==========*/
    private static final int BUBBLES_NUM = 8;               //总气泡数
    private BubbleChartData mBubbleData;                    //气泡图表数据

    @Override
    public int getLayoutId() {
        return R.layout.activity_bubble_chart;
    }

    @Override
    public void initView() {
        mBubbleView = (BubbleChartView) findViewById(R.id.bcv_main);
    }

    @Override
    public void initData() {
        setBubbleDatas();
    }

    @Override
    public void initListener() {
        mBubbleView.setOnValueTouchListener(new ValueTouchListener());
    }

    @Override
    public void processClick(View v) {
        //TODO:其他的点击事件
    }

    /**
     * 设置气泡图表的数据
     */
    private void setBubbleDatas() {
        List<BubbleValue> values = new ArrayList<>();
        //设置每个气泡的数据、颜色、形状
        for (int i = 0; i < BUBBLES_NUM; ++i) {
            BubbleValue value = new BubbleValue(i, (float) Math.random() * 100, (float) Math.random() * 1000);
            value.setColor(ChartUtils.pickColor());
            value.setShape(bubbleShape);
            values.add(value);
        }

        //设置其他属性
        mBubbleData = new BubbleChartData(values);
        mBubbleData.setHasLabels(isHasLabels);
        mBubbleData.setHasLabelsOnlyForSelected(isBubblesHasSelected);

        //设置坐标轴相关属性
        if (isHasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (isHasAxesName) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            mBubbleData.setAxisXBottom(axisX);
            mBubbleData.setAxisYLeft(axisY);
        } else {
            mBubbleData.setAxisXBottom(null);
            mBubbleData.setAxisYLeft(null);
        }

        mBubbleView.setBubbleChartData(mBubbleData);
    }

    /**
     * 菜单
     *
     * @param menu 菜单
     * @return true 显示
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bubble_chart, menu);
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
            case R.id.menu_bubble_reset:
                resetBubbles();                         //重置数据
                return true;
            case R.id.menu_bubble_shape_circles:
                setCirclesBubble();                     //设置圆形气泡
                return true;
            case R.id.menu_bubble_shape_square:
                setSquaresBubble();                     //设置方形气泡
                return true;
            case R.id.menu_bubble_show_hide_labels:
                showOrHideBubbleLabels();               //显示/隐藏气泡标签
                return true;
            case R.id.menu_bubble_show_hide_axes:
                showOrHideAxes();                       //显示/隐藏坐标轴
                return true;
            case R.id.menu_bubble_show_hide_axes_name:
                showOrHideAxesNames();                  //显示/隐藏坐标轴名称
                return true;
            case R.id.menu_bubble_animate:
                changeDatasAnimate();                   //改变数据的动画
                return true;
            case R.id.menu_bubble_point_select_mode:
                showOrHideLablesByBubblesSelected();    //气泡选中显示/隐藏气泡标签
                return true;
            case R.id.menu_bubble_touch_zoom:
                mBubbleView.setZoomEnabled(!mBubbleView.isZoomEnabled());       //是否能缩放
                return true;
            case R.id.menu_bubble_touch_zoom_xy:
                mBubbleView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);      //水平垂直都能缩放
                return true;
            case R.id.menu_bubble_touch_zoom_x:
                mBubbleView.setZoomType(ZoomType.HORIZONTAL);                   //只能水平方向缩放
                return true;
            case R.id.menu_bubble_touch_zoom_y:
                mBubbleView.setZoomType(ZoomType.VERTICAL);                     //只能垂直方向缩放
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 重置成初始状态
     */
    private void resetBubbles() {
        //重置成初识属性
        isHasAxes = true;
        isHasAxesName = true;
        bubbleShape = ValueShape.CIRCLE;
        isHasLabels = false;
        isBubblesHasSelected = false;
        mBubbleView.setValueSelectionEnabled(isBubblesHasSelected);
    }

    /**
     * 设置圆形气泡
     */
    private void setCirclesBubble() {
        bubbleShape = ValueShape.CIRCLE;
        setBubbleDatas();
    }

    /**
     * 设置方形气泡
     */
    private void setSquaresBubble() {
        bubbleShape = ValueShape.SQUARE;
        setBubbleDatas();
    }

    /**
     * 显示/隐藏气泡标签
     */
    private void showOrHideBubbleLabels() {
        isHasLabels = !isHasLabels;             //取反即可
        if (isHasLabels) {
            isBubblesHasSelected = false;        //有标签就点击不显示标签
            mBubbleView.setValueSelectionEnabled(isBubblesHasSelected);
        }
        setBubbleDatas();                       //重新设置
    }

    /**
     * 显示/隐藏坐标轴
     */
    private void showOrHideAxes() {
        isHasAxes = !isHasAxes;         //取反即可
        setBubbleDatas();               //重新设置
    }

    /**
     * 显示/隐藏坐标轴名称
     */
    private void showOrHideAxesNames() {
        isHasAxesName = !isHasAxesName;     //取反即可
        setBubbleDatas();                   //重新设置
    }

    /**
     * 点击气泡显示/隐藏气泡标签
     */
    private void showOrHideLablesByBubblesSelected() {
        isBubblesHasSelected = !isBubblesHasSelected;       //取反即可
        //设置是否能点击
        mBubbleView.setValueSelectionEnabled(isBubblesHasSelected);
        if (isBubblesHasSelected) {
            isHasLabels = false;                            //如果点击显示标签则本身不显示标签
        }
        setBubbleDatas();                                   //重新设置
    }

    /**
     * 改变数据时的动画
     */
    private void changeDatasAnimate() {
        //重新设置数据
        for (BubbleValue value : mBubbleData.getValues()) {
            value.setTarget(value.getX() + (float) Math.random() * 4 * getSign(), (float) Math.random() * 100,
                    (float) Math.random() * 1000);
        }
        mBubbleView.startDataAnimation();                   //开始动画
    }

    /**
     * 可能有部分区域在X轴下方
     *
     * @return -1 或者 1
     */
    private int getSign() {
        int[] sign = new int[]{-1, 1};
        return sign[Math.round((float) Math.random())];
    }

    /**
     * 气泡点击事件
     */
    private class ValueTouchListener implements BubbleChartOnValueSelectListener {
        @Override
        public void onValueSelected(int bubbleIndex, BubbleValue value) {
            Toast.makeText(BubbleChartActivity.this, "选中的气泡值约: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
        }
    }

}
