package xr.hellochartsdemo.ui.activity.other;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import xr.hellochartsdemo.R;

/**
 * @author xiarui 2016.09.07
 * @description 关于页面
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
