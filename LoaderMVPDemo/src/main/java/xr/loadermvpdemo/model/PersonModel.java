package xr.loadermvpdemo.model;

import java.util.ArrayList;

import xr.loadermvpdemo.bean.PersonBean;

/**
 * @author xiarui
 * @description PersonModel类 实现Model接口
 * @remark 这里其实不用实现接口 纯属演示用
 */

public class PersonModel implements IPersonModel {

    //存一下Person的信息
    private ArrayList<PersonBean> personList = new ArrayList<>();

    /**
     * 加载Person信息
     *
     * @return 返回信息集合
     */
    @Override
    public ArrayList<PersonBean> loadPersonInfo() {
        personList.add(initPerson());
        return personList;
    }

    private PersonBean initPerson() {
        PersonBean personBean = new PersonBean();
        personBean.setName("张三");
        personBean.setAge("24");
        personBean.setPhone("13512345678");
        personBean.setEmail("123@foxmail.com");
        personBean.setAddress("北京");
        personBean.setWork("Android Dev");
        personBean.setPay("20K");
        personBean.setMotto("先赚一个亿");
        return personBean;
    }
}
