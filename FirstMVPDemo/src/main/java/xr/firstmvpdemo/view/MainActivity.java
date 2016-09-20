package xr.firstmvpdemo.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import xr.firstmvpdemo.R;
import xr.firstmvpdemo.presenter.PersonPresenter;

/**
 * @author xiarui 16/09/20
 * @description MVP的简单例子
 * @remark View 必须持有 Presenter 的实例才能与 Model 交互
 */
public class MainActivity extends AppCompatActivity implements IPersonView, View.OnClickListener {

    /*===== 控件相关 =====*/
    private MaterialEditText nameEText;
    private MaterialEditText pwdEText;
    private Button registerButton;
    private Button loginButton;

    /*===== 数据相关 =====*/
    private Toast mToast;
    private String inputName;
    private String inputPwd;
    private PersonPresenter personPersenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();     //初始化View
        initData();     //初始化Data
    }

    /**
     * 初始化View
     */
    private void initView() {
        nameEText = (MaterialEditText) findViewById(R.id.met_main_name);
        pwdEText = (MaterialEditText) findViewById(R.id.met_main_pwd);
        registerButton = (Button) findViewById(R.id.bt_main_register);
        loginButton = (Button) findViewById(R.id.bt_main_login);

        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    /**
     * 初始化Data
     */
    private void initData() {
        personPersenter = new PersonPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_main_register:
                if (checkInputInfo()) {
                    personPersenter.registerPerson(inputName, inputPwd);
                }
                break;
            case R.id.bt_main_login:
                if (checkInputInfo()) {
                    personPersenter.loginPerson(inputName, inputPwd);
                }
                break;
        }
    }

    /*========== IPersonView接口方法 START ==========*/

    /**
     * 检查输入信息的合法性
     *
     * @return true：输入合法，false：输入不合法
     */
    @Override
    public boolean checkInputInfo() {
        inputName = nameEText.getText().toString().trim();
        inputPwd = pwdEText.getText().toString().trim();

        if (inputName.equals("")) {
            nameEText.setError("用户名不能为空");
            return false;
        }
        if (inputPwd.equals("")) {
            pwdEText.setError("密码不能为空");
            return false;
        }
        return true;
    }

    @Override
    public void onRegisterSucceed() {
        showToast("注册成功");
        loginButton.setText("注册成功");
    }

    @Override
    public void onRegisterFaild() {
        showToast("用户已存在");
    }

    @Override
    public void onLoginSucceed() {
        showToast("登录成功");
    }

    @Override
    public void onLoginFaild() {
        showToast("用户不存在或密码错误");
    }

    /*========== IPersonView接口方法 END ==========*/

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
