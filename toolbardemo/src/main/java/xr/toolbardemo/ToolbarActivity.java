package xr.toolbardemo;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 侧滑菜单的实现案例
 */
public class ToolbarActivity extends ActionBarActivity {

    private Toolbar mainToolbar;
    private DrawerLayout mainDrawerLayout;
    private ActionBarDrawerToggle mainToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        //初始化View
        initView();

        //初始化侧滑菜单
        initDrawLayout();

    }

    /*初始化View*/
    private void initView() {
        mainToolbar = (Toolbar) findViewById(R.id.tb_main);

        //将一个ToolBar充当一个ActionBar
        setSupportActionBar(mainToolbar);
        mainDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main);
    }

    /*初始化侧滑菜单*/
    private void initDrawLayout() {
        mainToggle = new ActionBarDrawerToggle(this, mainDrawerLayout, mainToolbar, R
                .string.open, R.string.close);

        //同步状态
        mainToggle.syncState();

        //设置监听
        mainDrawerLayout.setDrawerListener(mainToggle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
