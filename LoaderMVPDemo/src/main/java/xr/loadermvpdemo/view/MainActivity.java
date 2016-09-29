package xr.loadermvpdemo.view;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import xr.loadermvpdemo.R;
import xr.loadermvpdemo.bean.PersonBean;
import xr.loadermvpdemo.loader.PresenterLoader;
import xr.loadermvpdemo.presenter.PersonPresenter;
import xr.loadermvpdemo.presenter.PresenterFactory;
import xr.loadermvpdemo.ui.FunSwitchView;

/**
 * @author xiarui 16/09/24
 * @description 用 Loader 绑定 Presenter 的生命周期
 * @remark Activity 为 View 层的实现类 需要实现接口 并持有 Presenter 的实例
 */
public class MainActivity extends AppCompatActivity implements IPersonView, LoaderManager.LoaderCallbacks<PersonPresenter>, View.OnClickListener {

    /*===== 控件相关 =====*/
    private TextView tvMainName;
    private TextView tvMainAge;
    private TextView tvMainPhone;
    private TextView tvMainEmail;
    private TextView tvMainAddress;
    private TextView tvMainWork;
    private TextView tvMainPay;
    private TextView tvMainMotto;
    private FunSwitchView fsvMainFinish;
    private Button btMainLoad;
    private Button btMainGoto;

    /*===== 控制相关 =====*/
    private int i = 0;
    private Toast mToast;
    private PersonPresenter mPersonPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("====", "onCreate is call");
        initView();
        initListener();
        initData();
    }

    private void initView() {
        tvMainName = (TextView) findViewById(R.id.tv_main_name);
        tvMainAge = (TextView) findViewById(R.id.tv_main_age);
        tvMainPhone = (TextView) findViewById(R.id.tv_main_phone);
        tvMainEmail = (TextView) findViewById(R.id.tv_main_email);
        tvMainAddress = (TextView) findViewById(R.id.tv_main_address);
        tvMainWork = (TextView) findViewById(R.id.tv_main_work);
        tvMainPay = (TextView) findViewById(R.id.tv_main_pay);
        tvMainMotto = (TextView) findViewById(R.id.tv_main_motto);

        fsvMainFinish = (FunSwitchView) findViewById(R.id.fsv_main_finish);
        btMainLoad = (Button) findViewById(R.id.bt_main_load);
        btMainGoto = (Button) findViewById(R.id.bt_main_goto);
    }

    private void initListener() {
        btMainLoad.setOnClickListener(this);
        btMainGoto.setOnClickListener(this);
    }

    private void initData() {
        //得到一个Loader管理者，并创建一个Loader
        getLoaderManager().initLoader(0, null, this);
        Log.e("====", "initData is call");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_main_load:
                mPersonPresenter.updateUIByLocal();
                break;
            case R.id.bt_main_goto:
                gotoOther(fsvMainFinish.ismIsOpen());
                break;
        }
    }

    private void gotoOther(boolean isFinish) {
        startActivity(new Intent(this, OtherActivity.class));
        if (isFinish) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPersonPresenter.onViewAttached(this);
        Log.e("====", "onResume is call");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPersonPresenter.onViewDetached();
        Log.e("====", "onStop is call");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPersonPresenter.onDestroyed();
        Log.e("====", "onDestroy is call");
    }

    /**
     * View 接口方法 更新UI
     *
     * @param personBean 用户集合
     */
    @Override
    public void updateUI(PersonBean personBean) {
        tvMainName.setText("姓名：" + personBean.getName());
        tvMainAge.setText("年龄：" + personBean.getAge());
        tvMainPhone.setText("手机：" + personBean.getPhone());
        tvMainEmail.setText("邮箱：" + personBean.getEmail());
        tvMainAddress.setText("地址：" + personBean.getAddress());
        tvMainWork.setText("工作：" + personBean.getWork());
        tvMainPay.setText("月薪：" + personBean.getPay());
        tvMainMotto.setText("格言：" + personBean.getMotto());
        showToast("第 " + i + " 次加载");
        i++;
    }

    /*========== Loader 的回调方法 ==========*/
    @Override
    public Loader<PersonPresenter> onCreateLoader(int id, Bundle args) {
        Log.e("====", "onCreateLoader is call");
        //创建
        return new PresenterLoader<>(this, new PresenterFactory(this));
    }

    @Override
    public void onLoadFinished(Loader<PersonPresenter> loader, PersonPresenter presenter) {
        //完成加载
        this.mPersonPresenter = presenter;
        Log.e("====", "onLoadFinished is call");
    }

    @Override
    public void onLoaderReset(Loader<PersonPresenter> loader) {
        //销毁
        this.mPersonPresenter = null;
    }

    /**
     * 显示Toast
     *
     * @param str Toast内容
     */
    private void showToast(String str) {
        if (mToast == null) {
            mToast = Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(str);
        }
        mToast.show();
    }
}
