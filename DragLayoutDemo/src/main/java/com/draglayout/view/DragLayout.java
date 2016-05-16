package com.draglayout.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @Description:自定义滑动布局
 */
public class DragLayout extends FrameLayout {

    private ViewDragHelper viewDragHelper;
    private View redView, blueView;

    public DragLayout(Context context) {
        super(context);
        initView();
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        //创建一个View滑动帮助类
        viewDragHelper = ViewDragHelper.create(this, callback);
    }

    /**
     * 当自定义控件的XML布局结束标签被读取完成后会执行此方法
     * 目的是知道自身有几个子View
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        redView = getChildAt(0);
        blueView = getChildAt(1);
    }

    /**
     * 测量View的大小,其实继承FrameLayout后不需要测量了
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //对于子View没有特殊的测量需求，可以这样测量
        measureChild(redView, widthMeasureSpec, heightMeasureSpec);
        measureChild(blueView, widthMeasureSpec, heightMeasureSpec);
    }*/

    /**
     * 摆放子View的位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft() + getMeasuredWidth() / 2 - redView.getMeasuredWidth() / 2;
        int top = getPaddingTop();
        redView.layout(left, top, left + redView.getMeasuredWidth(), top + redView.getMeasuredHeight());
        blueView.layout(left, redView.getBottom() + 20, left + blueView.getMeasuredWidth(), redView.getBottom() + 20 + blueView.getMeasuredHeight());
    }

    /**
     * 触摸事件
     *
     * @param event
     * @return true, 表示事件在这里消费
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 将触摸事件交给ViewDragHelper来解析处理
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    /**
     * 事件拦截
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 让ViewDragHelper帮我们判断是否应该拦截
        boolean result = viewDragHelper.shouldInterceptTouchEvent(ev);
        return result;
    }

    /**
     * 定义一个回调对象 实现相关方法 指定滑动过程中的相关参数
     */
    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        /**
         * 判断是否捕获当前child的触摸事件
         * @param child 当前触摸的子View
         * @param pointerId
         * @return true:捕获并解析 false:不处理
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == blueView || child == redView;
        }

        /**
         * 当View开始捕获和解析的时候回调，一般处理Log事件
         * @param capturedChild 当前被捕获的View
         * @param activePointerId
         */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        /**
         * 获取水平方向的拖拽范围，但不能限定范围
         * @param child
         * @return 返回的值默认为0，一般用于手指抬起的时候View缓慢移动的动画时间的计算，最好不为0
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        /**
         * 获取垂直方向的拖拽范围，但不能限定范围
         * @param child
         * @return 最好不为0
         */
        @Override
        public int getViewVerticalDragRange(View child) {
            return getMeasuredHeight() - child.getMeasuredHeight();
        }

        /**
         * 控制child在水平方向上的移动
         * @param child 当前解析的子View
         * @param left 表示想让当前子View的left改变的值，left = child.getLeft()+dx
         * @param dx 本次child水平方向移动的距离
         * @return 真正想让child的left变成的值
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (left < 0) {
                // 限制左边界
                left = 0;
            } else if (left > (getMeasuredWidth() - child.getMeasuredWidth())) {
                // 限制右边界
                left = getMeasuredWidth() - child.getMeasuredWidth();
            }
            return left;
        }

        /**
         * 控制child在水平方向上的移动
         * @param child 当前解析的子View
         * @param top 表示想让当前子View的top改变的值，top = child.getTop()+dy
         * @param dy 本次child垂直方向移动的距离
         * @return 真正想让child的top变成的值
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (top < 0) {
                top = 0;
            } else if (top > getMeasuredHeight() - child.getMeasuredHeight()) {
                top = getMeasuredHeight() - child.getMeasuredHeight();
            }
            return top;
        }

        /**
         * 当child的位置改变的时候执行此方法，一般用来处理其他子View的伴随动画
         * @param changedView 当前位置改变的子View
         * @param left 当前子View最新的left
         * @param top 当前子View最新的top
         * @param dx 本次水平移动的距离
         * @param dy 本次水平垂直的距离
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == redView) {
                blueView.layout(blueView.getLeft() + dx, blueView.getTop() + dy, blueView.getRight() + dx, blueView.getBottom() + dy);
            } else if (changedView == blueView) {
                redView.layout(redView.getLeft() + dx, redView.getTop() + dy, redView.getRight() + dx, redView.getBottom() + dy);
            }
        }

        /**
         * 手指抬起后的移动动画
         * @param releasedChild 当前抬起的子View
         * @param xvel x方向的移动的速度 正：向右移动,负：向左移动
         * @param yvel y方向的移动的速度 正：向下移动,负：向上移动
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //ViewGroup中间位置
            int centerPosition = getMeasuredWidth() / 2 - releasedChild.getMeasuredWidth() / 2;
            //小于中间位平滑移向左
            if (releasedChild.getLeft() < centerPosition) {
                //平滑移动
                viewDragHelper.smoothSlideViewTo(releasedChild, 0, releasedChild.getTop());
                //刷新整个ViewGroup
                ViewCompat.postInvalidateOnAnimation(DragLayout.this);
            }
            //大于中间为值平滑移向右
            else {
                viewDragHelper.smoothSlideViewTo(releasedChild, getMeasuredWidth() - releasedChild.getMeasuredWidth(), releasedChild.getTop());
                ViewCompat.postInvalidateOnAnimation(DragLayout.this);

            }
        }
    };

    /**
     * 平滑移动动画
     */
    @Override
    public void computeScroll() {
        //当动画没有结束时，刷新整个ViewGroup
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(DragLayout.this);
        }
    }
}
