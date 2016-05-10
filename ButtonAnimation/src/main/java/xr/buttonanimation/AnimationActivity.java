package xr.buttonanimation;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;

/**
 * 按钮的水纹动画
 */
public class AnimationActivity extends AppCompatActivity implements View.OnClickListener {
    private Button myAnimationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        //初始化View
        initView();
    }

    /*初始化View*/
    private void initView() {
        myAnimationButton = (Button) findViewById(R.id.bt_myanimation);
        myAnimationButton.setOnClickListener(this);

    }

    /*按钮点击事件*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_myanimation:
                //参数说明：需要添加动画的View,起始位置中心点x值，y值 ，动画开始半径，动画结束半径
                Animator myAnimator = ViewAnimationUtils.createCircularReveal(v, 0, 0, 0, v.getWidth());
                //设置动画时间
                myAnimator.setDuration(500);
                //打开动画
                myAnimator.start();
                break;
        }
    }
}
