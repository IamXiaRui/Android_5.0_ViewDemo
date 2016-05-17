package com.newqqeffects.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @Description: 自定义Layout
 */
public class MyLinearLayout extends LinearLayout {

    private SlideMenu slideMenu;

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSlideMenu(SlideMenu slideMenu){
        this.slideMenu = slideMenu;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(slideMenu!=null && slideMenu.getCurrentState()==SlideMenu.OPEN_STATE){
            if(event.getAction()==MotionEvent.ACTION_UP){
                //抬起则应该关闭slideMenu
                slideMenu.closeMainView();
            }
            //如果slideMenu打开则应该拦截并消费掉事件
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(slideMenu!=null && slideMenu.getCurrentState()==SlideMenu.OPEN_STATE){
            //如果slideMenu打开则应该拦截并消费掉事件
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
