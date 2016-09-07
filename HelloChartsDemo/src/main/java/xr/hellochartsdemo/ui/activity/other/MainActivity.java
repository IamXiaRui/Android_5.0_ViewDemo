package xr.hellochartsdemo.ui.activity.other;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import xr.hellochartsdemo.R;
import xr.hellochartsdemo.ui.activity.base.BaseActivity;
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

    private Button lineButton;
    private Button columnButton;
    private Button pieButton;
    private Button preLineButton;
    private Button preColumnButton;
    private Button comboButton;
    private Button lineRelyOnColumnButton;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        lineButton = (Button) findViewById(R.id.bt_main_line);
        columnButton = (Button) findViewById(R.id.bt_main_column);
        pieButton = (Button) findViewById(R.id.bt_main_pie);
        preLineButton = (Button) findViewById(R.id.bt_main_preview_line);
        preColumnButton = (Button) findViewById(R.id.bt_main_preview_column);
        comboButton = (Button) findViewById(R.id.bt_main_combo_line_column);
        lineRelyOnColumnButton = (Button) findViewById(R.id.bt_main_line_depend_on_column);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        lineButton.setOnClickListener(this);
        columnButton.setOnClickListener(this);
        pieButton.setOnClickListener(this);
        preLineButton.setOnClickListener(this);
        preColumnButton.setOnClickListener(this);
        comboButton.setOnClickListener(this);
        lineRelyOnColumnButton.setOnClickListener(this);
    }

    @Override
    public void processClick(View v) {
        switch (v.getId()) {
            case R.id.bt_main_line:
                startActivity(new Intent(this, LineChartActivity.class));
                break;
            case R.id.bt_main_column:
                startActivity(new Intent(this, ColumnChartActivity.class));
                break;
            case R.id.bt_main_pie:
                startActivity(new Intent(this, PieChartActivity.class));
                break;
            case R.id.bt_main_preview_line:
                startActivity(new Intent(this, PreviewLineChartActivity.class));
                break;
            case R.id.bt_main_preview_column:
                startActivity(new Intent(this, PreviewColumnChartActivity.class));
                break;
            case R.id.bt_main_combo_line_column:
                //startActivity(new Intent(this, ComboLineColumnChartActivity.class));
                break;
            case R.id.bt_main_line_depend_on_column:
                //startActivity(new Intent(this, LineDependOnColumnActivity.class));
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
