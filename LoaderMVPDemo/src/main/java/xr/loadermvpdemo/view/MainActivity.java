package xr.loadermvpdemo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import xr.loadermvpdemo.R;
import xr.loadermvpdemo.bean.PersonBean;
import xr.loadermvpdemo.presenter.PersonPresenter;
import xr.loadermvpdemo.ui.FunSwitchView;

/**
 * @author xiarui 16/09/24
 * @description 用 Loader 绑定 Presenter 的生命周期
 * @remark Activity 为 View 层的实现类 需要实现接口 并持有 Presenter 的实例
 */
public class MainActivity extends AppCompatActivity implements IPersonView, View.OnClickListener {

    private PersonPresenter mPersonPresenter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        mPersonPresenter = new PersonPresenter(this);
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
    public void updateUI(ArrayList<PersonBean> personList) {
        PersonBean personBean = personList.get(0);
        tvMainName.setText("姓名：" + personBean.getName());
        tvMainAge.setText("年龄：" + personBean.getAge());
        tvMainPhone.setText("手机：" + personBean.getPhone());
        tvMainEmail.setText("邮箱：" + personBean.getEmail());
        tvMainAddress.setText("地址：" + personBean.getAddress());
        tvMainWork.setText("工作：" + personBean.getWork());
        tvMainPay.setText("月薪：" + personBean.getPay());
        tvMainMotto.setText("格言：" + personBean.getMotto());
        Toast.makeText(this, "加载成功", Toast.LENGTH_SHORT).show();
    }
}
