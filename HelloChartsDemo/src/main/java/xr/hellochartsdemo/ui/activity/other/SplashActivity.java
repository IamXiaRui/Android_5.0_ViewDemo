package xr.hellochartsdemo.ui.activity.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * @author mingjunli
 * @revision xiarui 2016.09.12
 * @description 启动页
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** 注意, 这里并没有setContentView, 单纯只是用来跳转到相应的Activity
         *  目的是减少首屏渲染
         * */
        this.startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
