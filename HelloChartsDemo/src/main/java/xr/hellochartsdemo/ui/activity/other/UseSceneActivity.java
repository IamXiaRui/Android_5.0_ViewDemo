package xr.hellochartsdemo.ui.activity.other;

import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import xr.hellochartsdemo.R;
import xr.hellochartsdemo.ui.activity.base.BaseActivity;

/**
 * @author xiarui 2016.09.08
 * @description 应用场景页面
 */
public class UseSceneActivity extends BaseActivity {

    private ImageView titleImage;

    @Override
    public int getLayoutId() {
        return R.layout.activity_use_scene;
    }

    @Override
    public void initView() {
        //透明状态栏 导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        titleImage = (ImageView) findViewById(R.id.iv_use_title);

    }

    @Override
    public void initData() {
        Glide.with(this).load(R.mipmap.heng_4).into(titleImage);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void processClick(View v) {

    }
}
