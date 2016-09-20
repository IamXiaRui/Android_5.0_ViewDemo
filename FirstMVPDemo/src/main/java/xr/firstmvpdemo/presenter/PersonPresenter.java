package xr.firstmvpdemo.presenter;

import xr.firstmvpdemo.model.IPersonModel;
import xr.firstmvpdemo.model.PersonModel;
import xr.firstmvpdemo.view.IPersonView;

/**
 * @author xiarui 16/09/20
 * @description Person的Presenter类
 * @remark 必须要传M和V 因为P需要控制M和V
 */
public class PersonPresenter {

    private IPersonModel mPersonModel;
    private IPersonView mPersonView;

    public PersonPresenter(IPersonView mPersonView) {
        mPersonModel = new PersonModel();
        this.mPersonView = mPersonView;
    }

    /**
     * 注册用户
     * @param name 用户名
     * @param pwd 密码
     */
    public void registerPerson(String name, String pwd) {
       boolean isRegister = mPersonModel.onRegister(name, pwd);
        if(isRegister){
            mPersonView.onRegisterSucceed();
        }else{
            mPersonView.onRegisterFaild();
        }
    }

    /**
     * 用户登陆
     * @param name 用户名
     * @param pwd 密码
     */
    public void loginPerson(String name, String pwd) {
        boolean isLogin = mPersonModel.onLogin(name, pwd);
        if (isLogin) {
            mPersonView.onLoginSucceed();
        }else{
            mPersonView.onLoginFaild();
        }
    }
}
