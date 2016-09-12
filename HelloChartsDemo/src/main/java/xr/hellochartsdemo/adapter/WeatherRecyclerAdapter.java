package xr.hellochartsdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import xr.hellochartsdemo.R;

/**
 * Recycler adapter
 *
 * @author Qiushui
 */
public class WeatherRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    //列表项数量
    private static final int ITEM_COUNT = 2;

    /*========== 列表项样式 ==========*/
    private static final int TYPE_BASIC = 0;
    private static final int TYPE_TEMP = 1;


    private LineChartData mLineData;            //图标数据
    public final static String[] dayStrs = new String[]{"9.12", "9.13", "9.14", "9.15", "9.16", "9.17", "9.18"};

    public WeatherRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BASIC) {
            return new BasicViewHolder(LayoutInflater.from(context).inflate(R.layout.item_info_basic, parent, false));
        } else {
            return new TempViewHolder(LayoutInflater.from(context).inflate(R.layout.item_info_temp, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 1) {
            setInitialLineDatas((TempViewHolder) holder);
        }
    }

    /**
     * 设置初始化线性图数据
     *
     * @param holder
     */
    private void setInitialLineDatas(TempViewHolder holder) {
        int numValues = 7;                      //7个值 注意与定义的X轴数量相同

        List<AxisValue> axisValues = new ArrayList<>();
        List<PointValue> values = new ArrayList<>();

        //设置默认值 都为0
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, 0));
            axisValues.add(new AxisValue(i).setLabel(dayStrs[i]));
        }

        //设置线
        Line line = new Line(values);
        line.setColor(Color.WHITE).setHasPoints(false).setCubic(true);
        List<Line> lines = new ArrayList<>();
        lines.add(line);

        //对数据进行一些设置 类似Line Chart
        mLineData = new LineChartData(lines);
        mLineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        mLineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));
        holder.dailyChart.setLineChartData(mLineData);
        holder.dailyChart.setViewportCalculationEnabled(false);

        //设置到窗口上
        Viewport v = new Viewport(0, 110, 6, -5);   //防止曲线超过范围 边界保护
        holder.dailyChart.setMaximumViewport(v);
        holder.dailyChart.setCurrentViewport(v);
        holder.dailyChart.setZoomType(ZoomType.HORIZONTAL);
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BASIC;
        } else {
            return TYPE_TEMP;
        }
    }

    public class BasicViewHolder extends RecyclerView.ViewHolder {
        BasicViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class TempViewHolder extends RecyclerView.ViewHolder {
        LineChartView dailyChart;           //每日预报
        ColumnChartView hourlyChart;        //每小时预报

        TempViewHolder(View itemView) {
            super(itemView);
            dailyChart = (LineChartView) itemView.findViewById(R.id.lcv_weather_daily);
            hourlyChart = (ColumnChartView) itemView.findViewById(R.id.ccv_weather_hourly);
        }
    }
}
