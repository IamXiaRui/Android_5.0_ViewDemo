package xr.firstmvpdemo.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiarui 16/09/20
 * @description Model类 实现IPersonModel接口
 * @remark 接口其实不必实现 只是为了讲解例子强行实现的
 */
public class PersonModel implements IPersonModel {

    //简单的存一下注册的账号
    private Map<String, String> personMap = new HashMap<>();

    /**
     * 注册账号 存入集合
     *
     * @param name 用户名
     * @param pwd  密码
     * @return true：注册成功，false：注册失败
     */
    @Override
    public boolean onRegister(String name, String pwd) {
        if (!personMap.containsKey(name)) {
            personMap.put(name, pwd);
            return true;
        }
        return false;
    }

    /**
     * 登录账号
     *
     * @param name 用户名
     * @param pwd  密码
     * @return true：登录成功，false：登录失败
     */
    @Override
    public boolean onLogin(String name, String pwd) {
        return pwd.equals(personMap.get(name));
    }
}
