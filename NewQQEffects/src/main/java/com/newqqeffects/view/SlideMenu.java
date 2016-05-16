package com.newqqeffects.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @Description:自定义QQ侧滑菜单
 */
public class SlideMenu extends FrameLayout {
    private ViewDragHelper viewDragHelper;
    private View menuView;//菜单的view
    private View mainView;//主界面的view
    private int width;
    private float dragRange;//拖拽范围

    public SlideMenu(Context context) {
        super(context);
        initView();
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        viewDragHelper = ViewDragHelper.create(this, callback);
    }

    /**
     * 得出本身有几个子View
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //简单的异常处理
        if (getChildCount() != 2) {
            throw new IllegalArgumentException("SlideMenu only have 2 children");
        }
        menuView = getChildAt(0);
        mainView = getChildAt(1);
    }

    /**
     * 在onMeasure方法之后执行，可以初始化自己与子View的宽高
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        dragRange = width * 0.6f;
    }

    /**
     * 触摸点击事件
     *
     * @param event 触摸事件
     * @return true, 消费触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    /**
     * 触摸事件拦截
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    //回调参数
    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        /**
         * 用于判断是否捕获当前View的触摸事件
         * @param child
         * @param pointerId
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == menuView || child == mainView;
        }

        /**
         * 获取View水平方向拖拽范围
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return (int) dragRange;
        }

        /**
         * 控制子View在水平方向的移动
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mainView) {
                if (left < 0) {
                    left = 0;
                } else if (left > dragRange) {
                    left = (int) dragRange;
                }
            }
            return left;
        }

        /**
         * 当child的位置改变的时候执行此方法，一般用来处理其他子View的伴随动画
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == menuView) {
                //固定菜单位置
                menuView.layout(0, 0, menuView.getMeasuredWidth(), menuView.getMeasuredHeight());
                //得到mainView移动位置并进行范围限制
                int newLeft = mainView.getLeft() + dx;
                if (newLeft < 0) {
                    newLeft = 0;
                } else if (newLeft > dragRange) {
                    newLeft = (int) dragRange;
                }
                mainView.layout(newLeft, mainView.getTop() + dy, newLeft + mainView.getMeasuredWidth(), mainView.getMeasuredHeight() + dy);
            }
        }

        /**
         * 处理平滑动画
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (mainView.getLeft() < dragRange / 2) {
                //在左半边
                closeMainView();
            } else {
                //在右半边
                openMainView();
            }
        }


    };

    /**
     * 打开主界面
     */
    private void openMainView() {
        viewDragHelper.smoothSlideViewTo(mainView,(int) dragRange,mainView.getTop());
        ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
    }

    /**
     * 关闭主界面
     */
    private void closeMainView() {
        viewDragHelper.smoothSlideViewTo(mainView,0,mainView.getTop());
        ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
    }

    /**
     * 处理平滑动画
     */
    public void computeScroll() {
        if(viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
        }
    };
}
