package xr.hellochartsdemo.ui.activity.scene;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import xr.hellochartsdemo.R;
import xr.hellochartsdemo.adapter.WeatherRecyclerAdapter;
import xr.hellochartsdemo.ui.activity.base.BaseActivity;
import xr.hellochartsdemo.ui.view.BlurredView;

/**
 * @author xiarui 2016.09.09
 * @description 实战场景 天气预报
 */
public class WeatherActivity extends BaseActivity {

    /*========== 控件相关 ===========*/
    private BlurredView weatherBView;           //背景模糊图
    private RecyclerView weatherRView;          //滑动列表
    private TextView tempText;                  //温度
    private TextView weatherText;               //天气
    private TextView windText;                  //风向
    private TextView windPowerText;             //风力
    private TextView humPowerText;              //湿度
    private TextView flPowerText;               //体感温度

    /*========== 数据相关 ===========*/


    /*========== 其他 ===========*/
    private int mScrollerY;                     //滚动距离
    private int mAlpha;                         //透明值
    private static final String weather_url = "https://api.heweather.com/x3/weather?cityid=CN101190101&key=573a3ba3c95a43ad94e70c34610720f9";

    @Override
    public int getLayoutId() {
        return R.layout.activity_weather;
    }

    @Override
    public void initView() {
        //透明状态栏 导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        tempText = (TextView) findViewById(R.id.tv_basic_temp);
        weatherText = (TextView) findViewById(R.id.tv_basic_weather);
        windText = (TextView) findViewById(R.id.tv_basic_wind);
        windPowerText = (TextView) findViewById(R.id.tv_basic_wind_power);
        humPowerText = (TextView) findViewById(R.id.tv_basic_hum_power);
        flPowerText = (TextView) findViewById(R.id.tv_basic_fl_power);

        weatherBView = (BlurredView) findViewById(R.id.bv_weather);
        weatherRView = (RecyclerView) findViewById(R.id.rv_weather);
        weatherRView.setLayoutManager(new LinearLayoutManager(this));
        weatherRView.setAdapter(new WeatherRecyclerAdapter(this));
    }

    @Override
    public void initData() {
        //TODO:请求网络数据
    }

    @Override
    @SuppressWarnings("deprecation")
    public void initListener() {
        weatherRView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollerY += dy;                       //滚动距离
                if (Math.abs(mScrollerY) > 1000) {      //根据滚动距离控制模糊程度 滚动距离是模糊程度的十倍
                    mAlpha = 100;
                } else {
                    mAlpha = Math.abs(mScrollerY) / 10;
                }
                weatherBView.setBlurredLevel(mAlpha);    //设置透明度等级
            }
        });
    }

    @Override
    public void processClick(View v) {

    }
}

