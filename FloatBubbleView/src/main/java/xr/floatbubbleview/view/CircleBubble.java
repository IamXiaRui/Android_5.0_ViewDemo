package xr.floatbubbleview.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * @author Mixiaoxiao
 * @revision xiarui 16/09/27
 * @description 圆形浮动气泡
 */

class CircleBubble {
    private final float cx, cy;                 //圆心坐标
    private final float dx, dy;                 //圆心偏移距离
    private final float radius;                 //半径
    private final int color;                    //画笔颜色
    private final float variationOfFrame;       //设置每帧变化量
    private boolean isGrowing = true;           //根据此标志位判断左右移动
    private float curVariationOfFrame = 0f;     //当前帧变化量

    CircleBubble(float cx, float cy, float dx, float dy, float radius, float variationOfFrame, int color) {
        this.cx = cx;
        this.cy = cy;
        this.dx = dx;
        this.dy = dy;
        this.radius = radius;
        this.variationOfFrame = variationOfFrame;
        this.color = color;
    }

    /**
     * 更新位置并重新绘制
     *
     * @param canvas 画布
     * @param paint  画笔
     * @param alpha  透明值
     */
    void updateAndDraw(Canvas canvas, Paint paint, float alpha) {
        /**
         * 每次绘制时都根据标志位(isGrowing)和每帧变化量(variationOfFrame)进行更新
         * 说白了其实就是每帧都会变化一段距离  连在一起 这就产生了动画效果
         */
        if (isGrowing) {
            curVariationOfFrame += variationOfFrame;
            if (curVariationOfFrame > 1f) {
                curVariationOfFrame = 1f;
                isGrowing = false;
            }
        } else {
            curVariationOfFrame -= variationOfFrame;
            if (curVariationOfFrame < 0f) {
                curVariationOfFrame = 0f;
                isGrowing = true;
            }
        }
        //根据当前帧变化量计算圆心偏移后的位置
        float curCX = cx + dx * curVariationOfFrame;
        float curCY = cy + dy * curVariationOfFrame;
        //设置画笔颜色
        int curColor = convertAlphaColor(alpha * (Color.alpha(color) / 255f), color);
        paint.setColor(curColor);
        //这里才真正的开始画圆形气泡
        canvas.drawCircle(curCX, curCY, radius, paint);
    }

    /**
     * 转成透明颜色
     *
     * @param percent       百分比
     * @param originalColor 初始颜色
     * @return 带有透明效果的颜色
     */
    private static int convertAlphaColor(float percent, final int originalColor) {
        int newAlpha = (int) (percent * 255) & 0xFF;
        return (newAlpha << 24) | (originalColor & 0xFFFFFF);
    }
}