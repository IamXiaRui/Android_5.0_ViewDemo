package xr.floatbubbleview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;

import java.util.ArrayList;

/**
 * @author Mixiaoxiao
 * @revision xiarui 16/09/27
 * @description 绘制圆形浮动气泡及设定渐变背景
 */
public class BubbleDrawer {

    /*===== 图形相关 =====*/
    private GradientDrawable mGradientBg;       //渐变背景
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //抗锯齿画笔
    private final float mDensity;              //屏幕密度

    /*===== 数据相关 =====*/
    private Context mContext;                   //上下文对象
    private int mWidth, mHeight;                //上下文对象
    //存放气泡的集合
    private final ArrayList<CircleBubble> bubbles = new ArrayList<>();
    private int[] gradientColors;

    /**
     * 构造函数
     *
     * @param context 上下文对象
     * @remark 注意这里需要根据上下文获取当前屏幕的密度
     */
    public BubbleDrawer(Context context) {
        this.mContext = context;
        //需要获取当前屏幕的密度
        this.mDensity = context.getResources().getDisplayMetrics().density;
    }

    public void setSize(int width, int height) {
        if (this.mWidth != width && this.mHeight != height) {
            this.mWidth = width;
            this.mHeight = height;
            if (this.mGradientBg != null) {
                mGradientBg.setBounds(0, 0, width, height);
            }
        }
        if (bubbles.size() == 0) {
            bubbles.add(new CircleBubble(0.20f * width, -0.30f * width, 0.06f * width, 0.022f * width, 0.56f * width,
                    0.0175f, 0x56ffc7c7));
            bubbles.add(new CircleBubble(0.58f * width, -0.15f * width, -0.15f * width, 0.032f * width, 0.6f * width,
                    0.00625f, 0x45fffc9e));
            bubbles.add(new CircleBubble(0.9f * width, -0.19f * width, 0.08f * width, -0.015f * width, 0.44f * width,
                    0.00325f, 0x5096ff8f));
            bubbles.add(new CircleBubble(1.1f * width, 0.25f * width, -0.08f * width, -0.015f * width, 0.42f * width,
                    0.00225f, 0x48c7dcff));
            bubbles.add(new CircleBubble(0.20f * width, 0.50f * width, -0.06f * width, 0.022f * width, 0.42f * width,
                    0.0185f, 0x52efc2ff));
        }
    }

    /**
     * 设置渐变背景色
     *
     * @param gradientColors 渐变色数组 必须 >= 2 不然没法渐变
     */
    public void setBackgroundGradient(int[] gradientColors) {
        this.gradientColors = gradientColors;
    }

    /**
     * 获取渐变色数组
     *
     * @return 渐变色数组
     */
    private int[] getBackgroundGradient() {
        return gradientColors;
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

    /**
     * 用画笔在画布上画气泡
     *
     * @param canvas 画布
     * @param alpha  透明值
     * @return 全部画完标志
     */
    private boolean drawCircleBubble(Canvas canvas, float alpha) {
        //循环遍历所有设置的圆形气泡
        for (CircleBubble bubble : this.bubbles) {
            bubble.updateAndDraw(canvas, mPaint, alpha);
        }
        return true;
    }

    /**
     * 画所有的图
     *
     * @param canvas 画布
     * @param alpha  透明值
     * @return 是否需要画下一帧
     */
    boolean draw(Canvas canvas, float alpha) {
        drawGradientBackground(canvas, alpha);
        return drawCircleBubble(canvas, alpha);
    }
}
