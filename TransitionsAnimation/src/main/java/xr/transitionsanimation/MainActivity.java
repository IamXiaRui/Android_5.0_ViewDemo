package xr.transitionsanimation;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button explodeButton, fadeButton, shareButton, slideButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化View
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {

        explodeButton = (Button) findViewById(R.id.bt_explode);
        fadeButton = (Button) findViewById(R.id.bt_fade);
        shareButton = (Button) findViewById(R.id.bt_share);
        slideButton = (Button) findViewById(R.id.bt_slide);

        explodeButton.setOnClickListener(this);
        fadeButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        slideButton.setOnClickListener(this);
    }

    /**
     * 按钮点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_explode:
                commonAnimation(ExplodeActivity.class);
                break;
            case R.id.bt_fade:
                commonAnimation(FadeActivity.class);
                break;
            case R.id.bt_slide:
                commonAnimation(SlideActivity.class);
                break;
            case R.id.bt_share:
                shareElementAnimation(ShareActivity.class);
                break;
        }

    }

    /**
     * 三个普通转场动画跳转到另一个Activity
     */
    private void commonAnimation(Class otherActivity) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, otherActivity);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, bundle);
    }

    /**
     * 共享元素转场动画
     *
     * @param otherActivity
     */
    private void shareElementAnimation(Class otherActivity) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, otherActivity);
        //多个共享元素的话:
        // ActivityOptions.makeSceneTransitionAnimation(this, Pair.create(((View) iv1),"share1"), create(((View) textView),"share2")
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this, shareButton, "shareView").toBundle();
        startActivity(intent, bundle);
    }


}
