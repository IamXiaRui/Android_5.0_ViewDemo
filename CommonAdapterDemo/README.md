#Android：打造“万能”Adapter与ViewHolder

##写在前面

最近一直忙着各种结课大作业，重新看起Android还有种亲切感。前段时间写项目的时候，学习了一个万能Adapter与ViewHolder的写法。说是“万能”其实就是在各种情况下都能通用。

我们知道，在写项目的时候，项目中肯定有很多的ListView或者RecyclerView，这个时候我们就要写大量的Adapter与ViewHolder。尽管重复写的难度并不大，但是这会让项目看起来十分冗余，因为存在大量的重复代码。

所以能不能有一个通用的ViewHolder与Adapter，让项目中只存在一个ViewHolder与Adapter呢？

当然可以，现在就通过一个小Demo将我学习的知识分享给大家。下面是本文的目录：

* 项目介绍
* 传统写法分析
* 简单认识SparseArray
* 万能ViewHolder
* 万能Adapter
* 结语
* 项目源码

##项目介绍

先来看这个Demo，很简单，我就不多说了。

![项目图](http://www.iamxiarui.com/wp-content/uploads/2016/06/项目图.png)

这是项目结构，为了方便后期对比，我将三种Adapter分离开了：

![项目结构](http://www.iamxiarui.com/wp-content/uploads/2016/06/项目结构-1.png)

* MainActivity：模拟新闻页面
* NewsBean：封装了新闻的Bean
* CommonViewHolder：通用ViewHolder
* CommonAdapter：通用Adapter
* TraditionAdapterWithTraditionHolder：基于传统Holder的传统Adapter
* TraditionAdapterWithCommonHolder：基于通用ViewHolder的传统Adapter
* CommonAdapterWithCommoeHolder：基于通用ViewHolder的通用Adapter

##传统写法分析

至于页面布局、模拟加载数据在这里我就不提了，十分简单。现在主要看一下传统的Adapter的写法。

	/**
	 * 基于传统Holder的传统Adapter
	 */
	public class TraditionAdapterWithTraditionHolder extends BaseAdapter {
	    private Context context;
	    private List<NewsBean> list;
	
	    public TraditionAdapterWithTraditionHolder(Context context, List<NewsBean> list) {
	        this.context = context;
	        this.list = list;
	    }
	
	    @Override
	    public int getCount() {
	        return list.size();
	    }
	
	    @Override
	    public Object getItem(int position) {
	        return list.get(position);
	    }
	
	    @Override
	    public long getItemId(int position) {
	        return position;
	    }
	
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder viewHolder ;
	        if (convertView == null) {
	            convertView = View.inflate(context, R.layout.item_list, null);
	            viewHolder = new ViewHolder();
	
	            viewHolder.titleText = (TextView) convertView.findViewById(R.id.tv_title);
	            viewHolder.descText = (TextView) convertView.findViewById(R.id.tv_desc);
	            viewHolder.timeText = (TextView) convertView.findViewById(R.id.tv_time);
	            viewHolder.phoneText = (TextView) convertView.findViewById(R.id.tv_phone);
	
	            convertView.setTag(viewHolder);
	            
	        }else{
	            viewHolder = (ViewHolder) convertView.getTag();
	        }
	
	        NewsBean bean = list.get(position);
	
	        viewHolder.titleText.setText(bean.getTitle());
	        viewHolder.descText.setText(bean.getDesc());
	        viewHolder.timeText.setText(bean.getTime());
	        viewHolder.phoneText.setText(bean.getPhone());
	
	        return convertView;
	    }
	
	    private class ViewHolder {
	        TextView titleText;
	        TextView descText;
	        TextView timeText;
	        TextView phoneText;
	    }
	}

由于代码也比较简单，基本都是 **套路** 代码，大家都会写，都能看懂，所以我就不加以详细注释了。

![全都是套路](http://www.iamxiarui.com/wp-content/uploads/2016/06/套路.png)


我们知道，如果需要一个通用的Adapter，肯定要对之前的代码进行封装。所以现在主要来分析一下这个传统写法，看到底哪个地方可以进行封装。

依次来看，首先是构造函数。只要稍微有点经验的开发者都知道，一般来说，这里面传递的参数几乎都是一个 **Context** 与 **List** ，而List中通常都装了一个具体内容的 **Bean** 。

	public TraditionAdapterWithTraditionHolder(Context context, List<NewsBean> list) {
			this.context = context;
			this.list = list;
	}

所以设想，既然这个Bean每次都需要，那我们是否能否将这个Bean直接给自定义的Adapter呢？比如这样：

	public MyAdapter<T>(Context context, List<T> list) {
			this.context = context;
			this.list = list;
	}

先把这个问题抛向天空，来看固定的三个方法，这也没啥好说的，依旧是套路，所以可以封装成固定方法，内部实现它，不需暴露出来再复写：

	@Override
	public int getCount() {
		return list.size();
	}
	
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

然后在重头戏 **getView** 方法中需要一个ViewHolder，来复用已有的View。然后 **new** 出List中的 **Bean** ，赋值后显示在View上。这就是基本的套路。


    private class ViewHolder {
        TextView titleText;
        TextView descText;
        TextView timeText;
        TextView phoneText;
    }

	Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_list, null);
            viewHolder = new ViewHolder();

            viewHolder.titleText = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.descText = (TextView) convertView.findViewById(R.id.tv_desc);
            viewHolder.timeText = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.phoneText = (TextView) convertView.findViewById(R.id.tv_phone);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NewsBean bean = list.get(position);

        viewHolder.titleText.setText(bean.getTitle());
        viewHolder.descText.setText(bean.getDesc());
        viewHolder.timeText.setText(bean.getTime());
        viewHolder.phoneText.setText(bean.getPhone());

        return convertView;
    }

而这个ViewHolder套路就更深了，先定义一个ViewHolder类，类中是布局中所需的控件，然后在getView方法中new一个ViewHolder出来，通过这个ViewHolder找到对应的控件，找到后需要设置个Tag，方便之后复用。最后就是通过ViewHolder设置控件的内容了。

既然熟悉了过程，那封装起来就简单了许多。首先肯定需要封装ViewHolder类，不然怎么算的上通用，但是每一个ListView中item布局可能不一样，肯定不能将控件写死，那么如何定义控件呢？当控件定义好后，又如何找到这些控件呢？控件找到后又如何设置控件内容呢？

仍然将这些问题抛向天空，接下来再考虑convertView的复用问题，固定写法，当然也可以封装。

所以目前来看，如果想要一个Adapter与ViewHolder可以通用，那么 **至少** 必须做如下工作：

* 将List的泛型参数转移到Adapter中
* 封装 getCount、getItem、getItemId方法
* 封装ViewHolder，并解决不同布局控件不统一问题
* 通用ViewHolder需要找到相应的控件
* 通用ViewHolder需要提供方法来设置相应控件的内容


##简单认识SparseArray

在写万能ViewHolder之前，先来了解一个新的API。我们知道，在Java中一般会用HashMap以键值对的形式来存储一些数据。但是Android给我们提供了一种工具类 **SparseArray** ，它是Android框架独有的类，在标准的JDK中不存在这个类。

为什么需要用SparseArray代替HashMap呢？

> **SparseArray要比 HashMap 节省内存，某些情况下比HashMap性能更好**

那为什么SparseArray性能更好呢？按照官方的解释，原因有以下几点：

* SparseArray不需要对key和value进行自动装箱
* 结构比HashMap简单
* SparseArray内部主要使用两个一维数组来保存数据，一个用来存key，一个用来存value
* 不需要额外的数据结构（主要是针对HashMap中的HashMapEntry 而言的）

从源码的构造函数来看，与List一样，可以通过new的形式来创建一个SparseArray，与Map一样，可以通过 **put(int key, E value)** 的形式来添加键值对。也可以通过 **get(int key)** 的方式来获取值。

好了，就介绍这么多，关于具体的用法，文末附有参考资料链接，如有需要可以自行查看。

##万能ViewHolder

现在就来打造万能ViewHolder，打造之前再次明确我们需要做的事情：

* 提供方法返回ViewHolder
* 提供方法获取控件
* 提供方法对控件进行设置
* 提供方法返回复用的View，也就是convertView

先来看如何解决不同布局有不同控件的问题。由于每个控件都有自己固定的ID和控件类型，那么我们可以通过键值对的形式来存储这些控件。在之前可以看到SparseArray能够提高性能，所以就用SparseArray来存储控件。

这样可以先写出构造函数，在构造函数中，初始化SparseArray，并设置一些内容。

	/**
	 * 通用ViewHolder
	 */
	public class CommonViewHolder {
	
	    //所有控件的集合
	    private SparseArray<View> mViews;
		//记录位置 可能会用到
	    private int mPosition;
		//复用的View
	    private View mConvertView;
	
	
	    /**
	     * 构造函数
	     *
	     * @param context  上下文对象
	     * @param parent   父类容器
	     * @param layoutId 布局的ID
	     * @param position item的位置
	     */
	    public CommonViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
	        this.mPosition = position;
	        this.mViews = new SparseArray<>();
			//构造方法中就指定布局
	        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
			//设置Tag
	        mConvertView.setTag(this);
	    }
	}

接下来我们就需要得到一个ViewHolder，这个比较简单，大家都能看懂，就是对Adapter中的getView方法进行一定的封装：

	/**
     * 得到一个ViewHolder
     *
     * @param context     上下文对象
     * @param convertView 复用的View
     * @param parent      父类容器
     * @param layoutId    布局的ID
     * @param position    item的位置
     * @return
     */
    public static CommonViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        //如果为空  直接新建一个ViewHolder
        if (convertView == null) {
            return new CommonViewHolder(context, parent, layoutId, position);
        } else {
            //否则返回一个已经存在的ViewHolder
            CommonViewHolder viewHolder = (CommonViewHolder) convertView.getTag();
            //记得更新条目位置
            viewHolder.mPosition = position;
            return viewHolder;
        }
    }


再接下来就是一个重难点，如何得到布局中的控件？因为我们肯定知道控件的ID，那么可以通过控件的ID来从SparseArray得到具体的控件类型。而Android中所有的控件都是继承自 **View** ,所以可以如下这样写：

	/**
     * 通过ViewId获取控件
     *
     * @param viewId View的Id
     * @param <T>    View的子类
     * @return 返回View
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

通过上述方法，就能得到对应的控件类型。既然得到了，那么设置控件内容就比较简单了，在本例中都是TextView，所以我封装了下面的方法：

	/**
     * 为文本设置text
     *
     * @param viewId view的Id
     * @param text   文本
     * @return 返回ViewHolder
     */
    public CommonViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

最后提供一个方法返回复用的convertView，这也比较简单。

	 /**
     * @return 返回复用的View
     */
    public View getConvertView() {
        return mConvertView;
    }

好了，再来看全部的代码，是不是清晰了很多：

	/**
	 * 通用ViewHolder
	 */
	public class CommonViewHolder {
	
	    //所有控件的集合
	    private SparseArray<View> mViews;
		//记录位置 可能会用到
	    private int mPosition;
		//复用的View
	    private View mConvertView;
	
	
	    /**
	     * 构造函数
	     *
	     * @param context  上下文对象
	     * @param parent   父类容器
	     * @param layoutId 布局的ID
	     * @param position item的位置
	     */
	    public CommonViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
	        this.mPosition = position;
	        this.mViews = new SparseArray<>();
	        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
	        mConvertView.setTag(this);
	    }
	
	    /**
	     * 得到一个ViewHolder
	     *
	     * @param context     上下文对象
	     * @param convertView 复用的View
	     * @param parent      父类容器
	     * @param layoutId    布局的ID
	     * @param position    item的位置
	     * @return
	     */
	    public static CommonViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
	        //如果为空  直接新建一个ViewHolder
	        if (convertView == null) {
	            return new CommonViewHolder(context, parent, layoutId, position);
	        } else {
	            //否则返回一个已经存在的ViewHolder
	            CommonViewHolder viewHolder = (CommonViewHolder) convertView.getTag();
	            //记得更新条目位置
	            viewHolder.mPosition = position;
	            return viewHolder;
	        }
	    }
	
	    /**
	     * @return 返回复用的View
	     */
	    public View getConvertView() {
	        return mConvertView;
	    }
	
	    /**
	     * 通过ViewId获取控件
	     *
	     * @param viewId View的Id
	     * @param <T>    View的子类
	     * @return 返回View
	     */
	    public <T extends View> T getView(int viewId) {
	        View view = mViews.get(viewId);
	        if (view == null) {
	            view = mConvertView.findViewById(viewId);
	            mViews.put(viewId, view);
	        }
	        return (T) view;
	    }
	
	    /**
	     * 为文本设置text
	     *
	     * @param viewId view的Id
	     * @param text   文本
	     * @return 返回ViewHolder
	     */
	    public CommonViewHolder setText(int viewId, String text) {
	        TextView tv = getView(viewId);
	        tv.setText(text);
	        return this;
	    }
	}

接下来我们就重写一个基于万能ViewHolder的Adapter，其他方法都不变，主要是getView方法。

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {

		//得到一个ViewHolder
        CommonViewHolder viewHolder = CommonViewHolder.get(context, convertView, parent, R.layout.item_list, position);

        NewsBean bean = list.get(position);

		//直接设置控件内容，链式调用
        viewHolder.setText(R.id.tv_title, bean.getTitle())
                .setText(R.id.tv_desc, bean.getDesc())
                .setText(R.id.tv_time, bean.getTime())
                .setText(R.id.tv_phone, bean.getPhone());

		//返回复用的View
        return viewHolder.getConvertView();
    }

现在来与之前的方法对比，是不是简单了很多，只需三步：

* 得到一个ViewHolder
* 通过这个ViewHolder直接设置控件内容
* 返回复用的View

看到这里大家肯定有个疑问，在上面ViewHolder中只提供了TextView设置文本的方法，那如果控件不是TextView呢？没关系，继续在万能ViewHolder中封装就好了：

	/**
     * 设置ImageView
     *
     * @param viewId view的Id
     * @param resId  资源Id
     * @return
     */
    public CommonViewHolder setImageResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    /**
     * 还可以添加更多的方法
     */

至此，我们就搞定了一个通用的“万能”ViewHolder。

##万能Adapter

有了万能ViewHolder，我们就可以来打造万能Adapter了，在文章开头已经分析过，需要做的事情有一下几点：

* 将Bean对象直接设置成Adapter的泛型
* 封装三个固定方法
* 封装getView方法
* 提供方法设置控件内容

先直接上代码，其实比较简单，大家应该能看懂：

	/**
	 * 通用Adapter抽象类
	 */
	public abstract class CommonAdapter<T> extends BaseAdapter {
	
	    protected Context context;
	    protected List<T> list;
	    private int layoutId;
	
	    public CommonAdapter(Context context, List<T> list, int layoutId) {
	        this.context = context;
	        this.list = list;
	        this.layoutId = layoutId;
	    }
	
	    @Override
	    public int getCount() {
	        return list.size();
	    }
	
	    @Override
	    public T getItem(int position) {
	        return list.get(position);
	    }
	
	    @Override
	    public long getItemId(int position) {
	        return position;
	    }
	
	    /**
	     * 封装getView方法
	     */
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        //得到一个ViewHolder
	        CommonViewHolder viewHolder = CommonViewHolder.get(context, convertView, parent, layoutId, position);
	
	        //设置控件内容
	        setViewContent(viewHolder, (T) getItem(position));
	
	        //返回复用的View
	        return viewHolder.getConvertView();
	    }
	
	    /**
	     * 提供抽象方法，来设置控件内容
	     *
	     * @param viewHolder 一个ViewHolder
	     * @param t          一个数据集
	     */
	    public abstract void setViewContent(CommonViewHolder viewHolder, T t);
	}

这里可以看到我们先自定义一个Adapter继承BaseAdapter，并将Bean换成Adapter的泛型T了，然后封装了四个方法。又由于各个控件不一样，所以提供抽象方法来设置控件内容，我们只要复写就行了。

此时我们再来看基于万能ViewHolder的万能Adapter应该怎样写：

	/**
	 * 继承通用Adapter且使用通用Holder的适配器
	 */
	public class CommonAdapterWithCommonHolder extends CommonAdapter<NewsBean> {
	
	    public CommonAdapterWithCommonHolder(Context context, List<NewsBean> list) {
	        super(context, list,R.layout.item_list);
	    }
	
	    /**
	     * 复写抽象方法
	     * @param viewHolder 一个ViewHolder
	     * @param bean Bean对象
	     */
	    @Override
	    public void setViewContent(CommonViewHolder viewHolder, NewsBean bean) {
	
	        //直接设置内容 链式调用
	        viewHolder.setText(R.id.tv_title, bean.getTitle())
	                .setText(R.id.tv_desc, bean.getDesc())
	                .setText(R.id.tv_time, bean.getTime())
	                .setText(R.id.tv_phone, bean.getPhone());
	    }
	}

看到这里，是不是有点神奇，对比之前的Adapter，这里只要几行代码就OK了。


##结语

由于本文说明的不是一种固定的知识，而是一种设计的思想，所以理解起来比较晦涩难懂。我自己在学这个的时候，也是消化了很久，现在回头看看真的是很巧妙。

不过值得注意的是，这里说的“万能”其实就是一个俗称，代表一种通用的Adapter，能避免项目中的大量的重复代码，提高代码质量。而这种通用，不一定就是文中的这样的格式，这里只是提供一个设计思想与大致流程，大家可以自己写一个通用的、更加强大的Adapter。

最后由于我水平有限与篇幅限制等原因，在写文章的过程中，有很多地方写的不够详细或者有明显的疏漏与错误，欢迎大家交流与指正。

##参考资料

[Android应用性能优化之使用SparseArray替代HashMap](https://liuzhichao.com/p/832.html)

[SparseArray替代HashMap来提高性能](http://www.open-open.com/lib/view/open1402906434918.html)

[如何打造万能适配器](http://www.imooc.com/learn/372)
