#Android：DataBinding的一二事

##写在前面

最近冷静了一个星期，学了大名鼎鼎的DataBinding。导师说这跟H5一样，是未来Android发展的趋势。看了一下，确实是Google官方跟着MVVM一起推出来的，所以就学了相关的知识，个别也运用到项目中。

不过讲心里话，不知道是不是平时的findViewById用习惯了，还是ButterKnife太好用了，一直觉得用这样的方式去绑定数据有点浑身不得劲，先学着吧。

所以今天来分享一下学习DataBinding过程。不过不同的是，在这里我不介绍它的使用规范或者方法，因为目前网上关于DataBinding的优秀文章很多很多，文末也会有相关链接。

只不过由于大部分文章都是针对Gradle1.5版本去写的，有些方法和注意事项已经不适用了，而且文章几乎只分开介绍了基本使用，并没有介绍开发中的一些实际问题，我在学的过程中也踩了不少坑，所以本篇文章主要先介绍一下新版本的不同（坑），然后用一个小的实战项目来具体展示如何使用DataBinding。


##站在前人的肩膀上，帮后人踩坑

###初始配置

第一次使用的时候需要注意，Gradle版本需要大于1.5。如果大于1.5，**只需要在当前 Module 下的 build.gradle 文件中添加如下代码即可**：

	android {
	    ...
	    dataBinding {
	        enabled true
	    }
		...
	}

注意，只需要在 **android{ }** 里面加省略号之间的三行就行了，其他啥都不用配置。在这里，我项目用的版本是 **Gradle 2.1.3** 。

###使用注意事项

**1、 android:text 中只能写" @{...}"的形式**

在其他优秀DataBinding使用教程，我们学会了像下面这样的使用规范：

	<TextView
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:text="@{user.age}" />

但是千万不能这样写：

	<TextView
		...
		android:text="年龄@{user.age}" />

	<TextView
		...
		android:text="年龄+@{user.age}" />

对比一下吧，没有对比就没有伤害：

![text注意点](http://www.iamxiarui.com/wp-content/uploads/2016/08/text.jpg)

当然如果你这样写的话：

	<TextView
		...
		android:text="@{年龄user.age}" />

	<TextView
		...
		android:text="@{年龄+user.age}" />

压根编译都过不去，请珍惜生命，善待自己。

**2、注意 android:text 中是 String 类型**

假如上面的 **user.age** 是 **int** 类型的话，一定要记得转成 **String** 类型，怎么转？当然是 **String.valueOf()** 了：

	<TextView
		...
		android:text="@{String.valueOf(user.age)}" />

**3、引用资源Bug已经被解决**

在之前所有的文章中，都有提到[官方有个bug](http://blog.csdn.net/feelang/article/details/46342699)，说在引用资源时必须加上 **int** 才能通过编译。

但是经过我的测试，不加也是可以编译且正常运行的，可能这个bug已经被解决掉了：

	<!--不同间距-->
    <TextView
		...
		android:text="@{person.name}"
		android:paddingLeft="@{person.isTrue ? @dimen/largeDP : @dimen/smallDP}"/>

	<!--不同字号-->
	<TextView
		...
		android:text="@{person.phone}"
		android:textSize="@{large ? @dimen/largeSP : @dimen/smallSP}" />

从下图可以看到，第一个行间距不同，第二行字号不同：

![资源引用](http://www.iamxiarui.com/wp-content/uploads/2016/08/res.jpg)

**4、include不能添加在非根结点的ViewGroup中？**

同样的，在之前几乎所有的教程中，说 include 标签不能非根结点的 ViewGroup 中，说程序会Crash掉，但是经过我的测试，程序也能正常编译运行，估计也是一个Bug吧：

	<!--根结点-->
	<LinearLayout
        ...
        android:orientation="vertical">

        <include
            layout="@layout/layout_text"
            bind:user="@{user}" />

        <include
            layout="@layout/layout_button"
            bind:btClick="@{btClick}" />

        <!-- 非根节点 ViewGroup 仍然有效-->
        <LinearLayout
            ...
            android:orientation="vertical">

            <include
                layout="@layout/layout_text"
                bind:user="@{user}" />

            <include
                layout="@layout/layout_button"
                bind:btClick="@{btClick}" />

        </LinearLayout>
    </LinearLayout>

如下图，布局是一样的：

![include问题](http://www.iamxiarui.com/wp-content/uploads/2016/08/include.jpg)


>以上两个Bug问题是仅在**有限测试**后得出的结论，如有错误请指正，谢谢！


**5、使用资源或者属性时，记得引包**

什么意思呢，比如下面根据 **large** 的值引用不同资源/属性：

	<!--显示-->
	<TextView
		...
		 android:visibility="@{large ? View.INVISIBLE : View.VISIBLE}"/>

	<!--颜色-->
	<TextView
		...
		android:textColor="@{large ? Color.RED : Color.YELLOW}"/>

这个时候，我们用到了 **View** 包和 **Color** 包，所以必须在 **data** 标签中 **import** 这两个包，才能显示正常：

	<import type="android.view.View" />
	<import type="android.graphics.Color"/>

从下图可以看到第一行没有显示，第二行是红色的：

![](http://www.iamxiarui.com/wp-content/uploads/2016/08/resandattr.jpg)


**6、自定义绑定类名称注意包名**

一般来说，编译器会给我们自动生成一个绑定类，类名与布局文件的名称一致。有些时候，我们需要自己自定义：

	<import class="**.CustomBinding" />

需要注意的是，这里 **CustomBinding** 之前的 * 号部分必须是项目的包名，那包名需要一直写到哪呢？

这个没有具体规定，比如我项目的目录是这样的：

	|--com
		|--dataBinding
			|--adapter
			|--bean
			|--pojo
			|--ui
			|--utils

我可以写 **com.databinding.CustomBinding** 也可以写 **com.databinding.ui.CustomBinding** ，只要在项目包中即可。当然了，不能只写个 **com.CustomBinding** ，这是个细节问题，大家注意一下就好。

**7、XML文件中不能包含< >符号**

实际上这是个很有趣的问题，开始我还没有发现，先看下面这段代码：

	 <!--数据层-->
    <data>
        <import type="android.databinding.ObservableMap" />
        <variable
            name="muser"
            type="ObservableMap&lt;String, Object>" />
    </data>

这里的 **variable** 类型为一个 **ObservableMap** ，既然是 Map 当然就有 Key 和 Value ，这里是 String 类型的 Key ，Object 类型的 Value ，但是注意这里并不是你看错了，就是这么写的。因为这里不允许使用 **"< >"** 这样的格式，否则会直接报错。


**8、IDE的各种问题**

因为DataBinding推出不是很久，用的人不是很多，听说在早前的AS版本中，IDE是没有智能提示的。但是现在的IDE已经对DataBinding有了很好的支持了，但是仍然还是有很多小的问题。

比如在XML文件的UI层的根结点中，一些常用的属性没法提示，连 width 和 height 有时候都打不出来，但是如果真的手打出来的话，仍然是有效的，估计的IDE的问题。

同样的，有时候在Activity中想要的到绑定类的时候，总是提示没有这个类，但是绑定命名却跟XML文件命名一致的，这个时候我们只要把XML文件名称随便重命名一下就行了，估计也是IDE没有反应过来。

###常见问题

**1、xx missing it**

	Error:Execution failed for task ':DataBindingDemo:compileDebugJavaWithJavac'.
	> java.lang.RuntimeException: Found data binding errors.
	****/ data binding error ****msg:Identifiers must have user defined types from the XML file. Color is missing it
	file:D:\Android\AndroidProject\Android5.0Demo\DataBindingDemo\src\main\res\layout\activity_resource.xml
	loc:120:45 - 120:49
	****\ data binding error ****

这个问题很常见，不过跟我上面说的第五点是一致的，大部分都是因为没有导包的原因，所以找不到这个 Color 。

> **解决办法就是一句话，什么Missing导什么。**

**2、Could not find method XX**

这也是个细节性问题了，比如下面这段代码(有省略)：

	<variable
		name="btclick"
		type="android.view.View.OnClickListener" />

		...

	<!--点击事件-->
        <Button
            ...
            android:onClick="btclick" />

这里就是给按钮定义一个监听，一般我们写 **android:onClick** 的时候直接写方法名就可以了，但是这里如果我们直接写的话，能编译通过，但是一点击就会Crash，错误如下：

> java.lang.IllegalStateException: Could not find method btclick(View) in a parent or ancestor Context for android:onClick attribute defined on view class android.support.v7.widget.AppCompatButton

提示说没有找到方法，解决办法就是 **android:onClick="@{btclick}"** ，没什么好解释的，细心细心。

**3、NullPoint怎么办**

这个问题也很有趣，刚接触DataBinding的时候，我也有相同的疑问，但是实际上，DataBinding不会出现NollPoint，至少在已经绑定的对象上不会出现。

为什么呢？下面这是[Data Binding（数据绑定）用户指南](http://www.jianshu.com/p/b1df61a4df77)给出的解释：

> “Data Binding代码生成时自动检查是否为nulls来避免出现null pointer exceptions错误。例如，在表达式@{user.name}中，如果user是null，user.name会赋予它的默认值（null）。如果你引用user.age，age是int类型，那么它的默认值是0。”

可以看到，DataBinding已经给我们处理好了，所以不用担心这个问题。

**4、编译出错，控制台一大串错误信息怎么办**

在编译过程中，如果有错，控制台会打印一大串问题，这个时候只需要看错误信息的最后一条就行了。

而且一般来说，关键性错误信息都是下面这样的格式：

	****/ data binding error ****
	msg:...
	file:...
	****\ data binding error ****


##举个栗子

好了，大概了解了DataBinding的使用方式，现在来用一个小小的例子来说具体如何在项目中使用它。

关于下面的例子很简单，但是有几点说明：

* 只说DataBinding相关部分；
* 特地选用了比较复杂的RecyclerView；
* 特地在item中添加图片和文字，图片来源为Url；
* 重点在于RecyclerView的动态绑定。

先来看一下item中的布局吧，也是精简了一部分：

	<layout 
		xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto">
	
	    <data>
	        <variable
	            name="news"
	            type="com.databinding.bean.NewsBean" />
	    </data>
	
	    <android.support.v7.widget.CardView
	        ...>
	
			<ImageView
				.../>
	
			<TextView
				...
				android:text="@{news.title}"/>
	
			<TextView
				...
				android:text="@{news.desc}"/>
	
			<TextView
				...
				android:text="@{news.time}"/>

	    </android.support.v7.widget.CardView>
	</layout>

可以看到，这里只有TextView使用了DataBinding，图片并没有设置，让我们来看看在复杂的布局中如何使用动态绑定。

因为是一个列表数据，item是复用的，所以没办法直接绑定，那么就给ViewHolder绑定。

直接上源码吧，难度不大：

	public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
	
	    private List<NewsBean> list;
	    private Context context;
	
	    public NewsAdapter(Context context, List<NewsBean> list) {
	        this.list = list;
	        this.context = context;
	    }
	
	    @Override
	    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
	        View itemView = LayoutInflater.from(parent.getContext())
	                .inflate(R.layout.item_news, parent, false);
	        return new NewsViewHolder(itemView);
	    }
	
	    @Override
	    public void onBindViewHolder(NewsViewHolder holder, int position) {
	        //给ViewHolder绑定
	        holder.bind(list.get(position));
	        //加载图片
	        Picasso.with(context).load(list.get(position).getPicUrl()).into(holder.itemPicImage);
	    }
	
	    @Override
	    public int getItemCount() {
	        return list.size();
	    }
	
	    public class NewsViewHolder extends RecyclerView.ViewHolder {
	
	        //绑定类
	        private ItemNewsBinding inBinding;
	        private ImageView itemPicImage;
	
	        public NewsViewHolder(View itemView) {
	            super(itemView);
	            //为每一个item设置
	            inBinding = DataBindingUtil.bind(itemView);
	            itemPicImage = (ImageView) itemView.findViewById(R.id.iv_item_pic);
	        }
	
	        /**
	         * 绑定方法
	         *
	         * @param news Bean对象
	         */
	        public void bind(@NonNull NewsBean news) {
	            inBinding.setNews(news);
	        }
	    }

如果你仔细看了上面的代码，你会发现其实只是多了三件事情：

* ViewHolder中创建绑定类；
* 利用绑定类设置相关内容；
* 绑定ViewHolder的时候绑定上相关内容。

就这么多，来看看效果图吧：

![示例图](http://www.iamxiarui.com/wp-content/uploads/2016/08/news.png)

其实例子本身很简单，但是想说的不是例子本身。从代码中我们可以发现，图片的加载并没有使用DataBinding，而是用Picasso直接加载。当然也可以使用DataBinding的静态方法去直接展示，但是感觉有点怪怪的。而且如果这个item的布局十分复杂，考虑的情况很多的话，这种情况显然很麻烦很麻烦。何况这里还没有考虑点击事件。

所以总的来说就是，不熟悉就慎用慎用。

##总结

因为是刚接触，对这种数据绑定的方式各种不适应，而且很多方法可能暂时不知道，就像上面复杂的布局不知道有没有更简单的方法去处理。反正给我的感觉就是有点麻烦，也不准备继续深入学习DataBinding了。

###优点

1. 节省了给View设置Id的工作；
2. 节省了findViewById的工作；
3. 大部分情况都能很快捷的处理；
4. 没有空指针的问题；
5. 还有很多优点待发掘。

###缺点

1. ButterKnife好像更方便；
2. 复杂的布局处理起来很困难；
3. IDE不够完善。
4. 还有很多缺点待发现。

###资源分享

[Google官方文档 - 请科学上网](https://developer.android.com/tools/data-binding/guide.html)

[Data Binding（数据绑定）用户指南 - 田浩浩_DockOne](http://www.jianshu.com/p/b1df61a4df77)

[完全掌握Android DataBinding - 泡在网上的日子](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0603/2992.html)

[Android数据绑定框架DataBinding，堪称解决界面逻辑的黑科技 - 非著名程序员](http://www.jianshu.com/p/2d3227d9707d)
