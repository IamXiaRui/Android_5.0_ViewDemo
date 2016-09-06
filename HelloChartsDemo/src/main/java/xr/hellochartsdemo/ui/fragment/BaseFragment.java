package xr.hellochartsdemo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import xr.hellochartsdemo.interfaces.UIInterface;

/**
 * 基类Fragment
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener, UIInterface {

    private View baseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseView = View.inflate(getActivity(), getLayoutId(), null);
        //初始化View
        initView();

        //绑定监听器与适配器
        initListener();

        //初始化界面数据
        initData();

        return baseView;
    }

    /**
     * 返回View引用
     *
     * @param viewId View的Id
     * @return 返回View引用
     */
    protected View findViewById(int viewId) {
        return baseView.findViewById(viewId);
    }



    /**
     * 对统一的按钮进行统一处理
     *
     * @param v 点击的View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                processClick(v);
                break;
        }
    }

    /**
     * 显示一个Toast
     *
     * @param msg 吐司内容
     */
    protected void baseToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示一个Toast
     *
     * @param msgId 吐司内容
     */
    protected void baseToast(int msgId) {
        Toast.makeText(getActivity(), msgId, Toast.LENGTH_SHORT).show();
    }

}
