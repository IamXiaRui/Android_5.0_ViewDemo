package xr.changetheme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 切换主题样式案例
 */
public class ChangeThemeActivity extends Activity implements View.OnClickListener {

    private Button redButton, greenButton;
    private int themeId;
    private static final int DEFLUAT_THEME = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeId = getIntent().getIntExtra("theme_id", DEFLUAT_THEME);
        if (themeId != DEFLUAT_THEME) {
            setTheme(themeId);
        }
        setContentView(R.layout.activity_changetheme);

        //初始化View
        initView();
    }

    /*初始化View*/
    private void initView() {
        redButton = (Button) findViewById(R.id.bt_red);
        greenButton = (Button) findViewById(R.id.bt_green);

        redButton.setOnClickListener(this);
        greenButton.setOnClickListener(this);
    }

    /*按钮点击事件*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_red:
                ChangeThemeUtil(R.style.AppTheme_Red);
                break;
            case R.id.bt_green:
                ChangeThemeUtil(R.style.AppTheme_Green);
                break;
        }
    }

    /*更换主题工具类*/
    private void ChangeThemeUtil(int themeId) {
        //先结束当前Activity
        finish();
        //紧靠finish()执行
        overridePendingTransition(0, 0);
        Intent intent = new Intent(ChangeThemeActivity.this, ChangeThemeActivity.class);
        //切换主题
        intent.putExtra("theme_id", themeId);
        startActivity(intent);
        //紧靠 startActivity()执行
        overridePendingTransition(0, 0);
    }

}
