package xr.loadermvpdemo.ui;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * @author homer 16/6/11
 * @revision xiarui 16/9/24
 * @description 笑脸开关自定义控件
 */
public class FunSwitchView extends View implements ValueAnimator.AnimatorUpdateListener,ValueAnimator.AnimatorListener{
    private final static float DEFAULT_WIDTH_HEIGHT_PERCENT = 0.65f;
    private final static float FACE_ANIM_MAX_FRACTION = 1.4f;
    private final static float NORMAL_ANIM_MAX_FRACTION = 1.0f;
    private float mWidthAndHeightPercent ;
    private float mWidth;
    private float mHeight;
    private float mTransitionLength;
    private Path mBackgroundPath;
    private Path mFacePath;

    //paint
    private int mOffBackgroundColor = 0xffcccccc;
    private int mOnBackgroundColor = 0xff33ccff;
    private int mCurrentColor = mOffBackgroundColor;
    // animation
    private ValueAnimator mValueAnimator;
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private float mAnimationFraction = 0.0f;


    private int mFaceColor = 0xffffffff;
    private Paint mPaint;
    private float mFaceRadius;
    private float mCenterX;
    private float mCenterY;

    private boolean mIsOpen = false;
    private boolean mIsDuringAnimation = false;

    private long mOnAnimationDuration = 850L;
    private long mOffAnimationDuration = (long)(mOnAnimationDuration * NORMAL_ANIM_MAX_FRACTION / FACE_ANIM_MAX_FRACTION);

    public FunSwitchView(Context context) {
        super(context);
        init(context);
    }

    public FunSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FunSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        mWidthAndHeightPercent = DEFAULT_WIDTH_HEIGHT_PERCENT;
        mPaint = new Paint();
        setState(false);
        // TODO: View ID 和 setSavedEnable都很重要的。
        setSaveEnabled(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width * DEFAULT_WIDTH_HEIGHT_PERCENT);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //TODO：还有padding的问题偶！！！
        mWidth = w;
        mHeight = h;
        float top = 0;
        float left = 0;
        float bottom = h*0.8f; //下边预留0.2空间来画阴影
        float right = w;

        RectF backgroundRecf = new RectF(left,top,bottom,bottom);
        mBackgroundPath = new Path();
        //TODO:???????????
        mBackgroundPath.arcTo(backgroundRecf,90,180);

        backgroundRecf.left = right - bottom;
        backgroundRecf.right = right;
        mBackgroundPath.arcTo(backgroundRecf,270,180);
        mBackgroundPath.close();

        float radius = (bottom / 2) * 0.98f;
        mCenterX =(top+bottom)/2;
        mCenterY = (left+bottom)/2;
        mFaceRadius = radius;
        mTransitionLength = right - bottom;

        RectF faceRecf = new RectF(mCenterX-mFaceRadius,mCenterY-mFaceRadius,mCenterX+mFaceRadius,mCenterY+mFaceRadius);
        mFacePath = new Path();
        mFacePath.arcTo(faceRecf,90,180);
        mFacePath.arcTo(faceRecf,270,180);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawForeground(canvas);
    }

    /**
     * 是否打开状态
     * @return true：打开 false：关闭
     */
    public boolean ismIsOpen() {
        return mIsOpen;
    }

    private void drawBackground(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setColor(mCurrentColor);
        mPaint.setStyle(Paint.Style.FILL);

        canvas.drawPath(mBackgroundPath,mPaint);
        mPaint.reset();
    }
    private void drawForeground(Canvas canvas) {
        //移动画布
        canvas.save();
        canvas.translate(getForegroundTransitionValue(),0);
        drawFace(canvas, mAnimationFraction);
        canvas.restore();
    }

    public void drawFace(Canvas canvas,float fraction) {
        mPaint.setAntiAlias(true);
        //面部背景
        mPaint.setColor(mFaceColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mFacePath,mPaint);

        translateAndClipFace(canvas,fraction);
        drawEye(canvas,fraction);
        drawMouth(canvas,fraction);
    }
    private void translateAndClipFace(Canvas canvas,float fraction) {
        //截掉超出face的部分。
        canvas.clipPath(mFacePath);

        float faceTransition ;
        //TODO：合理的转动区间，眼睛出现和消失的时间比为1：1,所以当fraction=0.25时，应该只显示侧脸
        if (fraction >=0.0f && fraction <0.5f) {
            faceTransition = fraction * mFaceRadius *4;
        } else if (fraction <=NORMAL_ANIM_MAX_FRACTION){
            faceTransition = - (NORMAL_ANIM_MAX_FRACTION - fraction) * mFaceRadius * 4;
        } else if (fraction <=(NORMAL_ANIM_MAX_FRACTION+FACE_ANIM_MAX_FRACTION)/2) {
            faceTransition =  (fraction - NORMAL_ANIM_MAX_FRACTION) * mFaceRadius * 2;
        } else {
            faceTransition = (FACE_ANIM_MAX_FRACTION - fraction) * mFaceRadius * 2;
        }

        canvas.translate(faceTransition,0);
    }

    private void drawEye(Canvas canvas,float fraction) {

        float scale;
        float startValue = 1.2f;
        float middleValue = (startValue + FACE_ANIM_MAX_FRACTION) /2; //1.3
        if (fraction >= startValue && fraction <= middleValue) {
            scale = (middleValue - fraction) * 10; //0.4f是最小缩放比
        } else if (fraction > middleValue && fraction <= FACE_ANIM_MAX_FRACTION) {
            scale = (fraction - middleValue) * 10 ;
        } else {
            scale = 1.0f;
        }

        float eyeRectWidth = mFaceRadius * 0.25f ;
        float eyeRectHeight = mFaceRadius * 0.35f;
        float eyeXOffSet = mFaceRadius * 0.14f;
        float eyeYOffSet = mFaceRadius * 0.12f;
        float leftEyeCenterX = mCenterX - eyeXOffSet - eyeRectWidth/2;
        float leftEyeCenterY = mCenterY - eyeYOffSet - eyeRectHeight/2;
        float rightEyeCenterX = mCenterX + eyeXOffSet + eyeRectWidth/2;

        eyeRectHeight *= scale; //眨眼缩放
        float eyeLeft = leftEyeCenterX - eyeRectWidth/2 ;
        float eyeTop = leftEyeCenterY - eyeRectHeight/2;
        float eyeRight = leftEyeCenterX + eyeRectWidth/2;
        float eyeBottom = leftEyeCenterY + eyeRectHeight/2;

        RectF leftEye = new RectF(eyeLeft,eyeTop,eyeRight,eyeBottom);

        eyeLeft = rightEyeCenterX - eyeRectWidth/2;
        eyeRight = rightEyeCenterX + eyeRectWidth/2;

        RectF rightEye = new RectF(eyeLeft,eyeTop,eyeRight,eyeBottom);

        mPaint.setColor(mCurrentColor);
        mPaint.setStyle(Paint.Style.FILL);
        //眨眼动画

        canvas.drawOval(leftEye,mPaint);
        canvas.drawOval(rightEye,mPaint);
    }
    private void drawMouth(Canvas canvas,float fraction) {
        //TODO:使用贝塞尔曲线来画嘴
        float eyeRectWidth = mFaceRadius * 0.2f;
        float eyeXOffSet = mFaceRadius * 0.14f;
        float eyeYOffSet = mFaceRadius * 0.21f;
        float mouthWidth = (eyeRectWidth + eyeXOffSet) * 2; //嘴的长度正好和双眼之间的距离一样
        float mouthHeight = (mFaceRadius * 0.05f);
        float mouthLeft = mCenterX - mouthWidth / 2;
        float mouthTop = mCenterY + eyeYOffSet; // mCenterY是face的原点

        //嘴巴
        if (fraction <=0.75) { //
            canvas.drawRect(mouthLeft, mouthTop, mouthLeft + mouthWidth, mouthTop + mouthHeight, mPaint);
        } else {
            Path path = new Path();
            path.moveTo(mouthLeft,mouthTop);
            float controlX = mouthLeft + mouthWidth/2;
            float controlY = mouthTop + mouthHeight + mouthHeight * 15 * (fraction - 0.75f);
            path.quadTo(controlX,controlY,mouthLeft+mouthWidth,mouthTop);
            path.close();
            canvas.drawPath(path,mPaint);
        }
    }

    private float getForegroundTransitionValue() {
        float result;
        if (mIsOpen) {
            if (mIsDuringAnimation) {
                result = mAnimationFraction > NORMAL_ANIM_MAX_FRACTION ?
                        mTransitionLength : mTransitionLength * mAnimationFraction;
            } else {
                result = mTransitionLength;
            }
        } else {
            if (mIsDuringAnimation) {
                result = mTransitionLength * mAnimationFraction;
            } else {
                result = 0;
            }
        }
        return result;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                if (mIsDuringAnimation) {
                    return true;
                }
                if (mIsOpen) {
                    startCloseAnimation();
                    mIsOpen = false;
                } else {
                    startOpenAnimation();
                    mIsOpen = true;
                }
                return true;
        }
        return super.onTouchEvent(event);
    }
    private void startOpenAnimation() {
        mValueAnimator = ValueAnimator.ofFloat(0.0f, FACE_ANIM_MAX_FRACTION);
        mValueAnimator.setDuration(mOnAnimationDuration);
        mValueAnimator.addUpdateListener(this);
        mValueAnimator.addListener(this);
        mValueAnimator.setInterpolator(mInterpolator);
        mValueAnimator.start();
        startColorAnimation();

    }
    private void startCloseAnimation() {
        mValueAnimator = ValueAnimator.ofFloat(NORMAL_ANIM_MAX_FRACTION,0);
        mValueAnimator.setDuration(mOffAnimationDuration);
        mValueAnimator.addUpdateListener(this);
        mValueAnimator.addListener(this);
        mValueAnimator.setInterpolator(mInterpolator);
        mValueAnimator.start();
        startColorAnimation();
    }
    private void startColorAnimation() {
        int colorFrom = mIsOpen?mOnBackgroundColor:mOffBackgroundColor; //mIsOpen为true则表示要启动关闭的动画
        int colorTo = mIsOpen? mOffBackgroundColor:mOnBackgroundColor;
        long duration = mIsOpen? mOffAnimationDuration:mOnAnimationDuration;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setInterpolator(mInterpolator);
        colorAnimation.setDuration(duration); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mCurrentColor = (int)animator.getAnimatedValue();
            }

        });
        colorAnimation.start();

    }
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mAnimationFraction = (float)animation.getAnimatedValue();
        invalidate();

    }

    @Override
    public void onAnimationStart(Animator animation) {
        mIsDuringAnimation = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mIsDuringAnimation = false;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        mIsDuringAnimation = false;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        mIsDuringAnimation = true;
    }

    public void setState(boolean open) {
        mIsOpen = open;
        refreshState();
    }
    public void refreshState() {
        mCurrentColor = mIsOpen ? mOnBackgroundColor : mOffBackgroundColor;
        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState =  super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.isOpen = mIsOpen ? 1:0;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(state);
        boolean result = ss.isOpen == 1;
        setState(result);
    }

    static class SavedState extends BaseSavedState {
        int isOpen;

        public SavedState(Parcel source) {
            super(source);
            isOpen = source.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(isOpen);
        }
        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[0];
            }
        };
    }
}
