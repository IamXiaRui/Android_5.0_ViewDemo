package xr.loadermvpdemo.loader;

import android.content.Context;
import android.content.Loader;
import android.util.Log;

import xr.loadermvpdemo.presenter.BasePresenter;
import xr.loadermvpdemo.presenter.PersonPresenter;
import xr.loadermvpdemo.presenter.PresenterFactory;

/**
 * @author xiarui
 * @date 2016/9/24 17:38
 * @description
 * @remark
 */

public class PresenterLoader<T extends BasePresenter> extends Loader<T> {

    private PersonPresenter presenter;
    private PresenterFactory factory;

    public PresenterLoader(Context context, PresenterFactory factory) {
        super(context);
        this.factory = factory;
    }

    @Override
    protected void onStartLoading() {

        // 如果已经有Presenter实例那就直接返回
        if (presenter != null) {
            deliverResult((T) presenter);
            return;
        }
        Log.e("====", "onStartLoading is call");
        // 如果没有
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        // 实例化Presenter
        presenter = factory.create();
        // 返回Presenter
        deliverResult((T) presenter);
        Log.e("====", "onForceLoad is call");
    }

    @Override
    protected void onReset() {
        presenter.onDestroyed();
        presenter = null;
    }
}
