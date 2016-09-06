package xr.hellochartsdemo.ui.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import xr.hellochartsdemo.R;


/**
 * @author xiarui 16.09.06
 * @description 图表控件HelloCharts的使用
 */
public class MainActivity extends BaseActivity {

    private Button lineButton;
    private Button columnButton;
    private Button pieButton;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        lineButton = (Button) findViewById(R.id.bt_main_line);
        columnButton = (Button) findViewById(R.id.bt_main_column);
        pieButton = (Button) findViewById(R.id.bt_main_pie);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        lineButton.setOnClickListener(this);
        columnButton.setOnClickListener(this);
        pieButton.setOnClickListener(this);
    }

    @Override
    public void processClick(View v) {
        switch (v.getId()) {
            case R.id.bt_main_line:
                startActivity(new Intent(this, LineChartActivity.class));
                break;
            case R.id.bt_main_column:

                break;
            case R.id.bt_main_pie:

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
