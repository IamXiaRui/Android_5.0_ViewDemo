package xr.transitionsanimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;

/**
 * @Description:滑动动画
 */
public class SlideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(new Slide().setDuration(1000));
        getWindow().setExitTransition(new Slide().setDuration(1000));
        setContentView(R.layout.activity_other);
    }
}
