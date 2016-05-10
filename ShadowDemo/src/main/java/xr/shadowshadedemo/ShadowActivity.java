package xr.shadowshadedemo;

import android.graphics.Outline;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * 阴影与裁剪效果
 */
public class ShadowActivity extends AppCompatActivity {
    private View circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow);

        //初始化View
        initView();

        //阴影裁剪
        cutCircle();
    }

    /*初始化View*/
    private void initView() {
        circleView = (View) findViewById(R.id.view_circle);

    }

    /*阴影裁剪*/
    private void cutCircle() {
        circleView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                //指定阴影小于View的大小
                outline.setOval(0, 0, view.getWidth() - 30, view.getHeight() - 30);
            }
        });
        //设置裁剪
        circleView.setClipToOutline(true);
    }


}
