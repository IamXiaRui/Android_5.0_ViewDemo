package xr.loadermvpdemo.presenter;

import xr.loadermvpdemo.view.IPersonView;

/**
 * @author xiarui
 * @date 2016/9/24 18:32
 * @description
 * @remark
 */

public class PresenterFactory{

    private IPersonView mPersonView;

    public PresenterFactory(IPersonView mPersonView) {
        this.mPersonView = mPersonView;
    }

    public PersonPresenter create() {
        return new PersonPresenter(mPersonView);
    }
}
