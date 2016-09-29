package xr.floatbubbleview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;

import java.util.ArrayList;

/**
 * @author Mixiaoxiao
 * @revision xiarui 16/09/27
 * @description 绘制圆形浮动气泡及设定渐变背景的绘制对象
 */
public class BubbleDrawer {

    /*===== 图形相关 =====*/
    private GradientDrawable mGradientBg;       //渐变背景
    private Paint mPaint; //抗锯齿画笔

    private int mWidth, mHeight;                //上下文对象
    private ArrayList<CircleBubble> mBubbles; //存放气泡的集合
    private int[] mGradientColors;              //渐变颜色数组

    /**
     * 构造函数
     *
     * @param context 上下文对象 可能会用到
     */
    public BubbleDrawer(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBubbles = new ArrayList<>();
    }

    /**
     * 设置显示悬浮气泡的范围
     * @param width 宽度
     * @param height 高度
     */
    void setViewSize(int width, int height) {
        if (this.mWidth != width && this.mHeight != height) {
            this.mWidth = width;
            this.mHeight = height;
            if (this.mGradientBg != null) {
                mGradientBg.setBounds(0, 0, width, height);
            }
        }
        //设置一些默认的气泡
        initDefaultBubble(width);
    }

    /**
     * 初始化默认的气泡
     *
     * @param width 宽度
     */
    private void initDefaultBubble(int width) {
        if (mBubbles.size() == 0) {
            mBubbles.add(new CircleBubble(0.20f * width, -0.30f * width, 0.06f * width, 0.022f * width, 0.56f * width,
                    0.0150f, 0x80ffc7c7));
            mBubbles.add(new CircleBubble(0.58f * width, -0.15f * width, -0.15f * width, 0.032f * width, 0.6f * width,
                    0.00600f, 0x85fffc9e));
            mBubbles.add(new CircleBubble(0.9f * width, -0.19f * width, 0.08f * width, -0.015f * width, 0.44f * width,
                    0.00300f, 0x7596ff8f));
            mBubbles.add(new CircleBubble(1.1f * width, 0.25f * width, -0.08f * width, -0.015f * width, 0.42f * width,
                    0.00200f, 0x80c7dcff));
            mBubbles.add(new CircleBubble(0.20f * width, 0.50f * width, -0.06f * width, 0.022f * width, 0.42f * width,
                    0.0150f, 0x70efc2ff));
            mBubbles.add(new CircleBubble(0.70f * width, 0.60f * width, 0.10f * width, 0.050f * width, 0.30f * width,
                    0.0100f, 0x75E99161));
        }
    }

    /**
     * 用画笔在画布上画气泡
     *
     * @param canvas 画布
     * @param alpha  透明值
     */
    private void drawCircleBubble(Canvas canvas, float alpha) {
        //循环遍历所有设置的圆形气泡
        for (CircleBubble bubble : this.mBubbles) {
            bubble.updateAndDraw(canvas, mPaint, alpha);
        }
    }

    /**
     * 画背景 画所有的气泡
     *
     * @param canvas 画布
     * @param alpha  透明值
     */
    void drawBgAndBubble(Canvas canvas, float alpha) {
        drawGradientBackground(canvas, alpha);
        drawCircleBubble(canvas, alpha);
    }

    /**
     * 设置渐变背景色
     *
     * @param gradientColors 渐变色数组 必须 >= 2 不然没法渐变
     */
    public void setBackgroundGradient(int[] gradientColors) {
        this.mGradientColors = gradientColors;
    }

    /**
     * 获取渐变色数组
     *
     * @return 渐变色数组
     */
    private int[] getBackgroundGradient() {
        return mGradientColors;
    }

    /**
     * 绘制渐变背景色
     *
     * @param canvas 画布
     * @param alpha  透明值
     */
    private void drawGradientBackground(Canvas canvas, float alpha) {
        if (mGradientBg == null) {
            //设置渐变模式和颜色
            mGradientBg = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, getBackgroundGradient());
            //规定背景宽高 一般都为整屏
            mGradientBg.setBounds(0, 0, mWidth, mHeight);
        }
        //然后开始画
        mGradientBg.setAlpha(Math.round(alpha * 255f));
        mGradientBg.draw(canvas);
    }

}
