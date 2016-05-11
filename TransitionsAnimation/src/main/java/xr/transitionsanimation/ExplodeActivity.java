package xr.transitionsanimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;

/**
 * @Description:分解动画
 */
public class ExplodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(new Explode().setDuration(1000));
        getWindow().setExitTransition(new Explode().setDuration(1000));
        setContentView(R.layout.activity_other);
    }
}
