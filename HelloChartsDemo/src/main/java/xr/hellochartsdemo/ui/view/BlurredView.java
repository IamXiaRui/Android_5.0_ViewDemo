package xr.hellochartsdemo.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import xr.hellochartsdemo.R;
import xr.hellochartsdemo.utils.BitmapUtil;
import xr.hellochartsdemo.utils.BlurBitmapUtil;

/**
 * @author Qiushui
 * @description 自定义模糊View类
 * @revision Xiarui 16.09.05
 */

public class BlurredView extends RelativeLayout {

    /*========== 全局相关 ==========*/

    //上下文对象
    private Context mContext;
    //透明最大值
    private static final int ALPHA_MAX_VALUE = 255;
    //最大模糊度(在0.0到25.0之间)
    private static final float BLUR_RADIUS = 25f;

    /*========== 图片相关 ==========*/

    //原图ImageView
    private ImageView mOriginImg;
    //模糊后的ImageView
    private ImageView mBlurredImg;
    //模糊后的Bitmap
    private Bitmap mBlurredBitmap;
    //原图Bitmap
    private Bitmap mOriginBitmap;


    /*========== 属性相关 ==========*/

    //是否禁用模糊效果
    private boolean isDisableBlurred;

    /*========== 四个构造函数 ==========*/

    public BlurredView(Context context) {
        super(context);
        initView(context);
    }

    public BlurredView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttr(context, attrs);
    }

    public BlurredView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttr(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BlurredView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
        initAttr(context, attrs);
    }

    /**
     * 初始化View
     *
     * @param context 上下文对象
     */
    @SuppressWarnings("deprecation")
    private void initView(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_blurred, this);
        mOriginImg = (ImageView) findViewById(R.id.iv_origin);
        mBlurredImg = (ImageView) findViewById(R.id.iv_blur);
    }

    /**
     * 初始化属性
     *
     * @param context 上下文对象
     * @param attrs   相关属性
     */
    private void initAttr(Context context, AttributeSet attrs) {
        //查找一些属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BlurredView);
        Drawable drawable = typedArray.getDrawable(R.styleable.BlurredView_src);
        //默认为false
        isDisableBlurred = typedArray.getBoolean(R.styleable.BlurredView_disableBlurred, false);
        //必须回收 方便重用
        typedArray.recycle();

        // 模糊图片
        if (null != drawable) {
            mOriginBitmap = BitmapUtil.drawableToBitmap(drawable);
            mBlurredBitmap = BlurBitmapUtil.blurBitmap(context, mOriginBitmap, BLUR_RADIUS);
        }

        // 设置是否可见
        if (!isDisableBlurred) {
            mBlurredImg.setVisibility(VISIBLE);
        }
    }

    /**
     * 以代码的方式添加待模糊的图片
     *
     * @param blurredBitmap 待模糊的图片
     */
    public void setBlurredImg(Bitmap blurredBitmap) {
        if (null != blurredBitmap) {
            mOriginBitmap = blurredBitmap;
            mBlurredBitmap = BlurBitmapUtil.blurBitmap(mContext, blurredBitmap, BLUR_RADIUS);
            setImageView();
        }
    }

    /**
     * 以代码的方式添加待模糊的图片
     *
     * @param blurDrawable 待模糊的图片
     */
    public void setBlurredImg(Drawable blurDrawable) {
        if (null != blurDrawable) {
            mOriginBitmap = BitmapUtil.drawableToBitmap(blurDrawable);
            mBlurredBitmap = BlurBitmapUtil.blurBitmap(mContext, mOriginBitmap, BLUR_RADIUS);
            setImageView();
        }
    }

    /**
     * 当所有子View出现后 设置相关内容
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setImageView();
    }

    /**
     * 填充ImageView
     */
    private void setImageView() {
        mBlurredImg.setImageBitmap(mBlurredBitmap);
        mOriginImg.setImageBitmap(mOriginBitmap);
    }

    /**
     * 设置模糊程度
     *
     * @param level 模糊程度, 数值在 0~100 之间.
     */
    @SuppressWarnings("deprecation")
    public void setBlurredLevel(int level) {
        //超过模糊级别范围 直接抛异常
        if (level < 0 || level > 100) {
            throw new IllegalStateException("No validate level, the value must be 0~100");
        }

        //禁用模糊直接返回
        if (isDisableBlurred) {
            return;
        }

        //设置透明度
        mOriginImg.setAlpha((int) (ALPHA_MAX_VALUE - level * 2.55));
    }

    /**
     * 显示模糊图片
     */
    public void showBlurredView() {
        mBlurredImg.setVisibility(VISIBLE);
    }

    /**
     * 选择是否启动/禁止模糊效果
     *
     * @param isDisableBlurred 是否禁用模糊效果
     */
    @SuppressWarnings("deprecation")
    public void setBlurredable(boolean isDisableBlurred) {
        if (isDisableBlurred) {
            mOriginImg.setAlpha(ALPHA_MAX_VALUE);
            mBlurredImg.setVisibility(INVISIBLE);
        } else {
            mBlurredImg.setVisibility(VISIBLE);
        }
    }

    /**
     * 禁用模糊效果
     */
    @SuppressWarnings("deprecation")
    public void disableBlurredView() {
        isDisableBlurred = true;
        mOriginImg.setAlpha(ALPHA_MAX_VALUE);
        mBlurredImg.setVisibility(INVISIBLE);
    }

    /**
     * 启用模糊效果
     */
    public void enableBlurredView() {
        isDisableBlurred = false;
        mBlurredImg.setVisibility(VISIBLE);
    }
}
