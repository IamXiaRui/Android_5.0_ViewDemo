package xr.hellochartsdemo.ui.activity.other;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import xr.hellochartsdemo.R;
import xr.hellochartsdemo.ui.activity.base.BaseActivity;

/**
 * @author xiarui 2016.09.08
 * @description 项目首页
 */
public class MainActivity extends BaseActivity {

    private CardView basicCard;
    private CardView highCard;
    private CardView useCard;

    private ImageView basicImage;
    private ImageView highImage;
    private ImageView useImage;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        basicCard = (CardView) findViewById(R.id.cv_main_basic);
        highCard = (CardView) findViewById(R.id.cv_main_high);
        useCard = (CardView) findViewById(R.id.cv_main_use);

        basicImage = (ImageView) findViewById(R.id.iv_main_basic);
        highImage = (ImageView) findViewById(R.id.iv_main_high);
        useImage = (ImageView) findViewById(R.id.iv_main_use);
    }

    @Override
    public void initData() {
        Glide.with(this).load(R.mipmap.heng_3).into(basicImage);
        Glide.with(this).load(R.mipmap.heng_1).into(highImage);
        Glide.with(this).load(R.mipmap.heng_4).into(useImage);
    }

    @Override
    public void initListener() {
        basicCard.setOnClickListener(this);
        highCard.setOnClickListener(this);
        useCard.setOnClickListener(this);
    }


    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void processClick(View v) {
        switch (v.getId()) {
            case R.id.cv_main_basic:
                Intent i1 = new Intent(this, BasicUseActivity.class);
                startActivity(i1, ActivityOptions.makeSceneTransitionAnimation(this, basicCard, "basic").toBundle());
                break;
            case R.id.cv_main_high:
                Intent i2 = new Intent(this, HighGroupActivity.class);
                startActivity(i2, ActivityOptions.makeSceneTransitionAnimation(this, highCard, "high").toBundle());
                break;
            case R.id.cv_main_use:
                Intent i3 = new Intent(this, UseSceneActivity.class);
                startActivity(i3, ActivityOptions.makeSceneTransitionAnimation(this, useCard, "use").toBundle());
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
