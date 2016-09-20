package xr.firstmvpdemo.view;

/**
 * @author xiarui 16/09/20
 * @description IPersonView接口
 */
public interface IPersonView {

    //检查输入的合法性
    boolean checkInputInfo();

    //注册成功
    void onRegisterSucceed();

    //注册失败
    void onRegisterFaild();

    //登录成功
    void onLoginSucceed();

    //登录失败
    void onLoginFaild();
}
