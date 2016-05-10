package xr.palettedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Palette调色板功能
 */
public class PaletteActivity extends AppCompatActivity implements View.OnClickListener {
    private Button nextButton, getColorButton;
    private View vibrantView, vibrantDarkView, vibrantLightView, mutedView, mutedDarkView, mutedLightView;
    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

        //初始化View
        initView();
    }

    /*初始化View*/
    private void initView() {

        nextButton = (Button) findViewById(R.id.bt_next);
        getColorButton = (Button) findViewById(R.id.bt_getcolor);

        vibrantView = (View) findViewById(R.id.view_vibrant);
        vibrantDarkView = (View) findViewById(R.id.view_vibrant_dark);
        vibrantLightView = (View) findViewById(R.id.view_vibrant_light);

        mutedView = (View) findViewById(R.id.view_muted);
        mutedDarkView = (View) findViewById(R.id.view_muted_dark);
        mutedLightView = (View) findViewById(R.id.view_muted_light);

        backgroundImage = (ImageView) findViewById(R.id.iv_background);
        backgroundImage.setImageDrawable(getDrawable(R.drawable.pic_rabbit));
        backgroundImage.setTag("rabbit");

        nextButton.setOnClickListener(this);
        getColorButton.setOnClickListener(this);

    }


    /*按钮点击事件*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next:
                //下一张
                if (backgroundImage.getTag() == "rabbit") {
                    backgroundImage.setImageDrawable(getDrawable(R.drawable.pic_girl));
                    backgroundImage.setTag("girl");
                } else if (backgroundImage.getTag() == "girl") {
                    backgroundImage.setImageDrawable(getDrawable(R.drawable.pic_rabbit));
                    backgroundImage.setTag("rabbit");
                }
                break;
            case R.id.bt_getcolor:
                //取色
                if (backgroundImage.getTag() == "rabbit") {
                    getColorUtil(R.drawable.pic_rabbit);
                } else if (backgroundImage.getTag() == "girl") {
                    getColorUtil(R.drawable.pic_girl);
                }
                break;
        }

    }

    /*取色器工具类*/
    private void getColorUtil(int picId) {
        //得到一个Bitmap对象
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), picId);
        // 如果操作本来就属于后台线程，可以使用：
        // Palette p = Palette.generate(bitmap);
        //如果在主线程中，我们可以使用异步的方式：
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                //设置颜色，查看取出的颜色
                vibrantView.setBackgroundColor(palette.getVibrantColor(Color.WHITE));
                vibrantDarkView.setBackgroundColor(palette.getDarkVibrantColor(Color.WHITE));
                vibrantLightView.setBackgroundColor(palette.getLightVibrantColor(Color.WHITE));
                mutedView.setBackgroundColor(palette.getMutedColor(Color.WHITE));
                mutedDarkView.setBackgroundColor(palette.getDarkMutedColor(Color.WHITE));
                mutedLightView.setBackgroundColor(palette.getLightMutedColor(Color.WHITE));
            }
        });
    }
}
