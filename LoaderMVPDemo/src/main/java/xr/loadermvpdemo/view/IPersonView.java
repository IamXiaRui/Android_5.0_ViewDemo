package xr.loadermvpdemo.view;

import java.util.ArrayList;

import xr.loadermvpdemo.bean.PersonBean;

/**
 * @author xiarui 16/09/24
 * @description View层的接口
 */

public interface IPersonView {

    void updateUI(ArrayList<PersonBean> personList);

}
