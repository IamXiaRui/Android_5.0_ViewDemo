package xr.loadermvpdemo.presenter;

/**
 * @author xiarui
 * @date 2016/9/24 17:56
 * @description
 * @remark
 */

public interface BasePresenter<V> {
    void onViewAttached(V view);
    void onViewDetached();
    void onDestroyed();
}
