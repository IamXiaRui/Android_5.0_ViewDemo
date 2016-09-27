package xr.floatbubbleview.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * @author Mixiaoxiao
 * @revision xiarui 16/09/27
 * @description 自定义浮动圆形气泡 支持自定义气泡位置、颜色、大小、移动速度
 */

class CircleBubble {
    private final float cx, cy;             //圆心坐标
    private final float dx, dy;             //圆心偏移距离
    private final float radius;             //半径
    private final int color;                //画笔颜色
    private boolean isGrowing = true;       //是否正在运动
    private float curPercentSpeed = 0f;     //当前速度百分比
    private final float percentSpeed;       //设置初始速度百分比

    CircleBubble(float cx, float cy, float dx, float dy, float radius, float percentSpeed, int color) {
        this.cx = cx;
        this.cy = cy;
        this.dx = dx;
        this.dy = dy;
        this.radius = radius;
        this.percentSpeed = percentSpeed;
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
        //每次绘制时都要气泡的速度 这里是匀速 也可以是不匀速
        if (isGrowing) {
            curPercentSpeed += percentSpeed;
            if (curPercentSpeed > 1f) {
                curPercentSpeed = 1f;
                isGrowing = false;          //停止运动
            }
        } else {
            curPercentSpeed -= percentSpeed;
            if (curPercentSpeed < 0f) {
                curPercentSpeed = 0f;
                isGrowing = true;           //继续运动
            }
        }
        //根据运动速度计算圆心偏移后的位置
        float curCX = cx + dx * curPercentSpeed;
        float curCY = cy + dy * curPercentSpeed;
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