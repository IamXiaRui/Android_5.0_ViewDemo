package xr.floatbubbleview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import xr.floatbubbleview.R;
import xr.floatbubbleview.view.BubbleDrawer;
import xr.floatbubbleview.view.FloatBubbleView;

public class MainActivity extends AppCompatActivity {

    private FloatBubbleView mFBView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        mFBView = (FloatBubbleView) findViewById(R.id.fbv_main);
        BubbleDrawer bubbleDrawer = new BubbleDrawer(this);
        bubbleDrawer.setBackgroundGradient(new int[]{0xffffffff,0xffffffff});
        mFBView.setDrawer(bubbleDrawer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFBView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFBView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFBView.onDestroy();
    }
}
