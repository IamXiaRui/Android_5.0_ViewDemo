#Android：简单靠谱的动态高斯模糊效果

##写在前面

最近一直在做毕设项目的准备工作，考虑到可能要用到一个模糊的效果，所以就学习了一些高斯模糊效果的实现。比较有名的就是 **FastBlur** 以及它衍生的一些优化方案，还有就是今天要说的 **RenderScript** 。

因为这东西是现在需要才去学习的，所以关于一些图像处理和渲染问题就不提了。不过在使用的过程中确实能感受到，虽然不同的方案都能实现相同的模糊效果，但是效率差别真的很大。

本篇文章实现的高斯模糊是根据下面这篇文章学习的，先推荐一下。本文内容与其内容差不多，只是稍微讲的详细一点，并修改了代码中部分实现逻辑和细节上的处理。不过主体内容不变，所以选择哪篇文章去学都是一样的。

> [湫水长天 - 教你一分钟实现动态模糊效果](http://wl9739.github.io/2016/07/14/%E6%95%99%E4%BD%A0%E4%B8%80%E5%88%86%E9%92%9F%E5%AE%9E%E7%8E%B0%E6%A8%A1%E7%B3%8A%E6%95%88%E6%9E%9C/)

##简单聊聊 Renderscript

因为效果的实现是基于 **Renderscript** 的，所以有必要先来了解一下。

从它的[官方文档](https://developer.android.com/guide/topics/renderscript/compute.html)来看，说的很是玄乎。我们只需要知道一点就好了：

> **RenderScript is a framework for running computationally intensive tasks at high performance on Android.**
> 
> **Renderscript 是 Android 平台上进行高性能计算的框架。**

既然是高性能计算，那么说明 **RenderScript** 对图像的处理非常强大，所以用它来实现高斯模糊还是比较好的选择。

那么如何使用它呢？从官方文档中可以看到，如果需要在 **Java** 代码中使用 **Renderscript** 的话，就必须依赖 **android.renderscript** 或者 **android.support.v8.renderscript** 中的 **API** 。既然有 **API** 那就好办多了。

下面简单说一下使用的步骤，这也是官方文档中的说明：

* 首先需要通过 **Context** 创建一个 **Renderscript** ；
* 其次通过创建的 **Renderscript** 来创建一个自己需要的脚本( **ScriptIntrinsic** )，比如这里需要模糊，那就是 **ScriptIntrinsicBlur** ；
* 然后至少创建一个 **Allocation** 类来创建、分配内存空间；
* 接着就是对图像进行一些处理，比如说模糊处理；
* 处理完成后，需要刚才的 **Allocation** 类来填充分配好的内存空间；
* 最后可以选择性的对一些资源进行回收。

文档中的解释永远很规矩，比较难懂，我们结合原博主 [湫水长天](http://wl9739.github.io/) 的代码来看一看步骤：

	/**
	 * @author Qiushui
	 * @description 模糊图片工具类
	 * @revision Xiarui 16.09.05
	 */
	public class BlurBitmapUtil {
	    //图片缩放比例
	    private static final float BITMAP_SCALE = 0.4f;
	
	    /**
	     * 模糊图片的具体方法
	     *
	     * @param context 上下文对象
	     * @param image   需要模糊的图片
	     * @return 模糊处理后的图片
	     */
	    public static Bitmap blurBitmap(Context context, Bitmap image,float blurRadius) {
	        // 计算图片缩小后的长宽
	        int width = Math.round(image.getWidth() * BITMAP_SCALE);
	        int height = Math.round(image.getHeight() * BITMAP_SCALE);
	
	        // 将缩小后的图片做为预渲染的图片
	        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
	        // 创建一张渲染后的输出图片
	        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
	
	        // 创建RenderScript内核对象
	        RenderScript rs = RenderScript.create(context);
	        // 创建一个模糊效果的RenderScript的工具对象
	        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
	
	        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
	        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
	        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
	        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
	
	        // 设置渲染的模糊程度, 25f是最大模糊度
	        blurScript.setRadius(blurRadius);
	        // 设置blurScript对象的输入内存
	        blurScript.setInput(tmpIn);
	        // 将输出数据保存到输出内存中
	        blurScript.forEach(tmpOut);
	
	        // 将数据填充到Allocation中
	        tmpOut.copyTo(outputBitmap);
	
	        return outputBitmap;
	    }
	}

上面就是处理高斯模糊的代码，其中注释写的十分详细，而且已经将图片缩放处理了一下。结合刚才说的步骤，大家应该能有一个大概的印象，实在不懂也没关系，这是一个工具类，直接 Copy 过来即可。

当然，原博主将代码封装成轮子了，也可以直接在项目中引用 **Gradle** 也是可以的，但是我觉得源码还是要看一看的。

##简单的模糊

好了，有了一个大概的印象后，来看一下如何实现高斯模糊效果吧！

首先你可以在项目中直接引用原博主封装的轮子：

	compile 'com.qiushui:blurredview:0.8.1'

如果不想引用的话，就必须在当前 **Module** 的 **build.gradle** 中添加如下代码：

	defaultConfig {
	    renderscriptTargetApi 19
	    renderscriptSupportModeEnabled true
	}

等构建好就可以使用了。如果构建失败的话，只需要把 **minSdkVersion** 设置成 **19** 就好了，暂时不知是何原因。不过从 **StackOverflow** 中了解到这是个 **Bug** ，那就不必深究。

现在来看代码实现，首先布局文件中就一个 ImageView ，没啥好说的，从上面的模糊图片工具类可以看出，要想获得一个高斯模糊效果的图片，需要三样东西：

* Context：上下文对象
* Bitmap：需要模糊的图片
* BlurRadius：模糊程度

>**这里需要注意一下：**
>
>目前这种方案只适用于 PNG 格式的图片，而且图片大小最好小一点，虽然代码中已经缩放了图片，但仍然可能会出现卡顿的情况。

现在只要设置一下图片和模糊程度就好了：

	/**
     * 初始化View
     */
    @SuppressWarnings("deprecation")
    private void initView() {
        basicImage = (ImageView) findViewById(R.id.iv_basic_pic);
		//拿到初始图
        Bitmap initBitmap = BitmapUtil.drawableToBitmap(getResources().getDrawable(R.raw.pic));
		//处理得到模糊效果的图
        Bitmap blurBitmap = BlurBitmapUtil.blurBitmap(this, initBitmap, 20f);
        basicImage.setImageBitmap(blurBitmap);
    }

来看一下运行图：

![模糊](http://www.iamxiarui.com/wp-content/uploads/2016/09/模糊.gif)

可以看到，图片已经实现了模糊效果，而且速度还蛮快的，总的来说通过 **BlurBitmapUtil.blurBitmap()** 就能得到一张模糊效果的图 。

##自定义模糊控件

原博主的轮子里给我们封装了一个自定义的 **BlurredView** ，刚开始我觉得没必要自定义。后来发现自定义的原因是需要实现动态模糊效果。

那为什么不能手动去设置模糊程度呢？他给出的解释是：

> “如果使用上面的代码进行实时渲染的话，会造成界面严重的卡顿。”

我也亲自试了一试，确实有点卡。他实现动态模糊处理的方案是这样的：

> “先将图片进行最大程度的模糊处理，再将原图放置在模糊后的图片上面，通过不断改变原图的透明度(Alpha值）来实现动态模糊效果。”

这个方案确实很巧妙的实现动态效果，但是注意如果要使用这种方式，就必须有两张一模一样的图片。如果在代码中直接写，就需要两个控件，如果图片多的话，显然是不可取的。所以轮子里有一个自定义的 **BlurredView** 。

不过这个 **BlurredView** 封装的不是太好，我删减了一部分内容，原因稍后再说。先来看一下核心代码。

首先是自定义的 **BlurredView** 继承于 **RelativeLayout** ，在布局文件中可以看到，里面有两个 **ImageView** ，且是叠在一起的。


	<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
	              android:layout_width="match_parent"
	              android:layout_height="match_parent">
	
	    <ImageView
	        android:id="@+id/blurredview_blurred_img"
	        .../>
	
	    <ImageView
	        android:id="@+id/blurredview_origin_img"
	        .../>
	
	</FrameLayout>

同时也定义了一些属性：

	<resources>
	    <declare-styleable name="BlurredView">
	        <attr name="src" format="reference"/>
	        <attr name="disableBlurred" format="boolean"/>
	    </declare-styleable>
	</resources>

一个是设置图片，一个是设置是否禁用模糊。最后就是 **BlurredView** 类，代码如下，有大量删减，只贴出核心代码：

	/**
	 * @author Qiushui
	 * @description 自定义模糊View类
	 * @revision Xiarui 16.09.05
	 */
	public class BlurredView extends RelativeLayout {
	
	    /*========== 全局相关 ==========*/
	    private Context mContext;//上下文对象
	    private static final int ALPHA_MAX_VALUE = 255;//透明最大值
	    private static final float BLUR_RADIUS = 25f;//最大模糊度(在0.0到25.0之间)
	
	    /*========== 图片相关 ==========*/
	    private ImageView mOriginImg;//原图ImageView
	    private ImageView mBlurredImg;//模糊后的ImageView
	    private Bitmap mBlurredBitmap;//模糊后的Bitmap
	    private Bitmap mOriginBitmap;//原图Bitmap
	    
	    /*========== 属性相关 ==========*/
	    private boolean isDisableBlurred;//是否禁用模糊效果

		...
	
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
	    ...
	
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
	
	    ...
	}

从代码中可以看到，最核心的就是下面三个方法：

* setBlurredImg(Bitmap blurredBitmap)：设置图片，并复制两份；
* setImageView()：给两个ImageView设置相应的图片，内部调用；
* setBlurredLevel(int level)：设置透明程度；

> 思路就是先选定一张图片，一张作为原图，一张作为模糊处理过的图。再分别将这两张图设置给自定义 **BlurredView** 中的两个 **ImageView** ，最后处理模糊过后的那张图的透明度。

好了，现在来写一个自定义的模糊效果图，首先是布局，很简单：

	 <com.blurdemo.view.BlurredView
	        android:id="@+id/bv_custom_blur"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent"
	        app:src="@raw/pic"
	        app:disableBlurred="false" />

可以看到，设置了图片，设置了开启模糊，那么我们在Activity中只需设置透明程度即可：

	private void initView() {
	        customBView = (BlurredView) findViewById(R.id.bv_custom_blur);
	        //设置模糊度
	        customBView.setBlurredLevel(100);
	    }

效果图与上图一样，这里就不重复贴了。可以看到，代码简单了很多，不过仅仅因为方便简单可不是自定义 **View** 的作用，作用在于接下来要说的 **动态模糊效果** 的实现。

##动态模糊

我们先来看一下啥叫动态模糊效果：

![动态模糊](http://www.iamxiarui.com/wp-content/uploads/2016/09/动态.gif)

从图中可以看到，随着我们触摸屏幕的时候，背景的模糊程度会跟着变化。如果要直接设置其模糊度会及其的卡顿，所以正如原博主所说，可以用两张图片来实现。

大体思路就是，上面的图片模糊处理，下面的图片不处理，然后通过手势改变上面模糊图片的透明度即可。

所以跟前面的代码几乎一样，只需要重写 **onTouchEvent** 方法即可：

	/**
     * 初始化View
     */
    private void initView() {
        customBView = (BlurredView) findViewById(R.id.bv_dynamic_blur);
        //设置初始模糊度
        initLevel = 100;
        customBView.setBlurredLevel(initLevel);
    }

	/**
     * 触摸事件
     */
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
                //手指滑动距离
                float offsetY = moveY - downY;
                //屏幕高度 十倍是为了看出展示效果
                int screenY = getWindowManager().getDefaultDisplay().getHeight() * 10;
                //手指滑动距离占屏幕的百分比
                movePercent = offsetY / screenY;
                currentLevel = initLevel + (int) (movePercent * 100);
                if (currentLevel < 0) {
                    currentLevel = 0;
                }
                if (currentLevel > 100) {
                    currentLevel = 100;
                }
                //设置模糊度
                customBView.setBlurredLevel(currentLevel);
                //更改初始模糊等级
                initLevel = currentLevel;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(ev);
    }

从代码中可以看到，这里是通过手指滑动距离占屏幕的百分比来计算改变后的透明等级的，代码应该不难，很容易理解。当然原博主博客中是通过进度条来改变的，也是可以的，就不在赘述了。

##与 RecylcerView 相结合

先来看一张效果图，这个图也是仿照原博主去实现的，但是还是有略微的不同。

![RecylcerView](http://www.iamxiarui.com/wp-content/uploads/2016/09/rec.gif)

本来的自定义 **BlurredView** 中还有几段代码是改变背景图的位置的，因为希望上拉下拉的时候背景图也是可以移动的，但是从体验来看效果不是太好，上拉的过程中会出现留白的问题。

虽然原博主给出了解决方案：手动给背景图增加一个高度，但这并不是最好的解决方式，所以我就此功能给删去了，等找到更好的实现方式再来补充。

现在来看如何实现？首先布局就是底下一层自定义的 **BlurredView** ，上面一个 **RecylcerView** ，**RecylcerView** 有两个 **Type** ，一个是头布局，一个是底下的列表，很简单，就不详细说了。

重点仍然是动态模糊的实现，在上面的动态模糊中，我们采取了重写 **onTouchEvent** 方法，但是这里刚好是 **RecylcerView** ，我们可以根据它的滚动监听，也就是 **onScrollListener** 来完成动态改变透明度，核心方法如下：

		//RecyclerView 滚动监听
        mainRView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //滚动距离
                mScrollerY += dy;
                //根据滚动距离控制模糊程度 滚动距离是模糊程度的十倍
                if (Math.abs(mScrollerY) > 1000) {
                    mAlpha = 100;
                } else {
                    mAlpha = Math.abs(mScrollerY) / 10;
                }
                //设置透明度等级
                recyclerBView.setBlurredLevel(mAlpha);
            }
        });

代码很简单，就是在 **onScrolled** 方法中计算并动态改变透明度，只要掌握了原理，实现起来还是很容易的。

##总结

从前面所有的动态图可以看到，运行起来还是比较快的，但是我从 **Android Monitor** 中看到，在每一次刚开始渲染模糊的时候，**GPU** 渲染的时间都很长，所以说可能在性能方面还是有所欠佳。

![GPU情况](http://www.iamxiarui.com/wp-content/uploads/2016/09/GPU.jpg)

当然也可能跟模拟器有关系，真机上测试是很快的。而且貌似比 **FastBlur** 还快一点，等有空测试几个高斯模糊实现方法的性能，来对比一下。

到此，这种实现高斯模糊的方法已经全部讲完了，感谢原博主这么优秀的文章，再次附上链接：

> [湫水长天 - 教你一分钟实现动态模糊效果](http://wl9739.github.io/2016/07/14/%E6%95%99%E4%BD%A0%E4%B8%80%E5%88%86%E9%92%9F%E5%AE%9E%E7%8E%B0%E6%A8%A1%E7%B3%8A%E6%95%88%E6%9E%9C/)

###其他参考资料

[RenderScript - Android Developers](https://developer.android.com/guide/topics/renderscript/compute.html)

[Android RenderScript入门（1）](http://imgtec.eetrend.com/article/2003)

[高斯模糊效果实现方案及性能对比 - lcyFox](http://blog.csdn.net/huli870715/article/details/39378349)
