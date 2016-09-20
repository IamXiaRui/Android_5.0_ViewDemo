package xr.firstmvpdemo.model;

/**
 * @author xiarui 16/09/20
 * @description IPersonModel接口
 * @remark 接口其实不必实现 只是为了讲解例子强行抽取的方法
 */
public interface IPersonModel {

    //注册账号
    boolean onRegister(String name, String pwd);

    //登录账号
    boolean onLogin(String name, String pwd);
}
