#Android：跟着实战项目学缓存策略之LruCache详谈

##写在前面

前几天更新了一篇异步任务AsyncTask的文章，用了两个小小的例子，总体来说比较简单。今天我就通过一个比较完整的新闻小项目来继续说下AsyncTask在项目中的使用方法。因为不再是一个简单的例子所以考虑的情况要比之前多得多，也复杂许多。

同时由于项目中用到了最常用的ListView，所以ListView的优化也在本文的重点范围中。而优化的两个主要方面就是使用异步任务和控制异步任务执行的频率，也就是监听ListView滚动状态，只有当其静止的时候才异步加载网络图片数据。这样大大提高了ListView的性能及使用体验。

此外在项目中用到了通过URL加载网络图片，因为加载图片需要联网，如果每次都加载的话不仅耗费大量流量，而且用户体验极差，所以我们必须知道Android中的基本缓存策略。比如常见的LruCache与DiskLruCache。

所以本文总体来说就是通过一个小小的项目来学习三方面的知识：

1. **AsyncTask如何在项目中运用自如；**
2. **如何高效优化ListView**
3. **LruCache的概念与基本用法**

尽管具体的实现比较复杂，但是清楚原理和基本流程后，大体还是比较清晰简单的。下面是本文的目录：

* 项目介绍
* LruCache用法详解
* 进一步优化ListView
* 结语
* 项目源码


##项目介绍

项目本身很简单，就是一个通过解析JSON得到相关数据显示在ListView上，在这里我们先不采用缓存策略，直接加载图片，所以前期效果图如下：

![线程加载](http://www.iamxiarui.com/wp-content/uploads/2016/06/线程加载.gif)

可以看到，图中第一次打开是需要加载网络图片数据的，每次滑动的时候也需要加载网络数据，而且是边滑动边加载。很明显的看出，滑动过程是很卡顿的，所以这样的体验是不友好的，所以我们必须优化它。

那么在优化之前，我先简单说一下这个项目。布局就不提了，每个人都会。这是项目结构：


![项目结构](http://www.iamxiarui.com/wp-content/uploads/2016/06/项目结构.png)

* MainActivity：主页面
* NewsAdapter：新闻列表适配器
* NewsBean：封装的新闻模型
* GetJsonUtil：得到Json数据工具类
* JsonToStringUtil：Json转字符串工具类
* ThreadUtil：普通子线程加载URL图片工具类
* LruCacheUtil：内存缓存加载URL图片工具类

这里面NewsBean、GetJsonUtil、JsonToStringUtil三个文件我不详细说，因为对大家来说很简单。项目也源码附在文末，可以自行查看。

我们主要来看一下异步任务处理和缓存处理这一块。

首先来看主Activity，这里面自定义了一个 **GetJsonTask** 异步任务，并在异步任务的 **doInBackGround** 方法中进行Json数据的解析。最后将解析结果在 **onPostExecute** 方法中展示在ListView中。当然不要忘记开启异步任务。

	/**
	 * 新闻案例：异步任务与异步加载图片的使用
	 */
	public class MainActivity extends AppCompatActivity {
	
	    private ListView mainListView;
	    private Context mainContext = MainActivity.this;
	    private String url = "http://www.imooc.com/api/teacher?type=4&num=30";
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	
	        initView();
	
	        //开启异步任务
	        GetJsonTask getJsonTask = new GetJsonTask();
	        getJsonTask.execute(url);
	    }
	
	    /**
	     * 初始化View
	     */
	    private void initView() {
	        mainListView = (ListView) findViewById(R.id.lv_main);
	    }
	
	    /**
	     * 自定义异步任务解析JSON数据
	     */
	    class GetJsonTask extends AsyncTask<String, Void, List<NewsBean>> {
	
	        @Override
	        protected List<NewsBean> doInBackground(String... params) {
	            return GetJsonUtil.getJson(params[0]);
	        }
	
	        @Override
	        protected void onPostExecute(List<NewsBean> newsBeen) {
	            super.onPostExecute(newsBeen);
	            NewsAdapter newsAdapter = new NewsAdapter(mainContext, newsBeen,mainListView);
	            mainListView.setAdapter(newsAdapter);
	        }
	    }
	}

由于后面才讲缓存策略，所以我把通过子线程且没有缓存策略的加载URL图片的方法剥离成ThreadUtil，先来看一看效果。类中无非就是开启子线程通过URL获取图片的Bitmap对象，然后通过Handler设置给ListView。比较简单，直接上代码，不多解释。

	/**
	 * 普通线程加载URL图片类
	 */
	public class ThreadUtil {
	
	    private ImageView mImageView;
	    private String mIconUrl;
	
	    private Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            super.handleMessage(msg);
	            //只有当前的ImageView所对应的URL的图片是一致的,才会设置图片
	            if (mImageView.getTag().equals(mIconUrl)) {
	                mImageView.setImageBitmap((Bitmap) msg.obj);
	            }
	        }
	    };
	
	    /**
	     * 通过子线程的方式展示图片
	     *
	     * @param iv  图片的控件
	     * @param url 图片的URL
	     */
	    public void showImageByThread(ImageView iv, final String url) {
	        mImageView = iv;
	        mIconUrl = url;
	        //异步解析图片
	        new Thread(new Runnable() {
	            @Override
	            public void run() {
	                Bitmap bitmap = getBitmapFromURL(url);
	                //发送到主线程
	                Message msg = Message.obtain();
	                msg.obj = bitmap;
	                mHandler.sendMessage(msg);
	            }
	        }).start();
	    }
	
	    /**
	     * 将一个URL转换成bitmap对象
	     *
	     * @param urlStr 图片的URL
	     * @return
	     */
	    public Bitmap getBitmapFromURL(String urlStr) {
	        Bitmap bitmap;
	        InputStream is = null;
	
	        try {
	            URL url = new URL(urlStr);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            is = new BufferedInputStream(connection.getInputStream());
	            bitmap = BitmapFactory.decodeStream(is);
	            connection.disconnect();
	            return bitmap;
	        } catch (java.io.IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return null;
	    }
	}

如果你看了上面的代码，你可能对下面这段判断代码有疑问：

	//只有当前的ImageView所对应的URL的图片是一致的,才会设置图片
	if (mImageView.getTag().equals(mIconUrl)) {
		mImageView.setImageBitmap((Bitmap) msg.obj);
	}

没关系，因为我还没说Adapter，现在来说。先看代码，就是简单的继承BaseAdapter，还有个ViewHolder，其他都是常规的东西。

	/**
	 * 新闻列表适配器
	 */
	public class NewsAdapter extends BaseAdapter {
	
	    private Context context;
	    private List<NewsBean> list;
	
	    public NewsAdapter(Context context, List<NewsBean> list, ListView lv) {
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
	        ViewHolder viewHolder;
	        if (convertView == null) {
	            convertView = View.inflate(context, R.layout.item_news, null);
	        }
	        // 得到一个ViewHolder
	        viewHolder = ViewHolder.getViewHolder(convertView);
	        //先加载默认图片 防止有的没有图
	        viewHolder.iconImage.setImageResource(R.mipmap.ic_launcher);
	
	        String iconUrl = list.get(position).newsIconUrl;
	        //当前位置的ImageView与图片的URL绑定
	        viewHolder.iconImage.setTag(iconUrl);
	        //再加载联网图
	
	        //第一种方式 通过子线程设置
	        new ThreadUtil().showImageByThread(viewHolder.iconImage, iconUrl);
	
	        viewHolder.titleText.setText(list.get(position).newsTitle);
	        viewHolder.contentText.setText(list.get(position).newsContent);
	
	        return convertView;
	    }
	
	    static class ViewHolder {
	        ImageView iconImage;
	        TextView titleText;
	        TextView contentText;
	
	        // 构造函数中就初始化View
	        public ViewHolder(View convertView) {
	            iconImage = (ImageView) convertView.findViewById(R.id.iv_icon);
	            titleText = (TextView) convertView.findViewById(R.id.tv_title);
	            contentText = (TextView) convertView.findViewById(R.id.tv_content);
	        }
	
	        // 得到一个ViewHolder
	        public static ViewHolder getViewHolder(View convertView) {
	            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
	            if (viewHolder == null) {
	                viewHolder = new ViewHolder(convertView);
	                convertView.setTag(viewHolder);
	            }
	            return viewHolder;
	        }
	    }
	}

值得注意的是，在得到图片的Url的时候，给当前item的ImageView与图片的URL进行绑定。

	String iconUrl = list.get(position).newsIconUrl;
	//当前位置的ImageView与图片的URL绑定
	viewHolder.iconImage.setTag(iconUrl);

为什么呢？因为我们知道，ListView中的Item是属于复用机制的，所以在滑动过程中，滑动很快的话，有可能出现一个item中ImageView刚刚加载好一张图后突然加载另一张图，因为前面那张图还是上一个item所对应的图，由于滑动太快，刚加载就被复用了。也就是可能出现图片闪烁的现象，体验十分差，所以就将当然位置与URL绑定，在Hanlder中设置的时候判断标记，防止出现这种现象。这也算ListView的一种优化吧。

好了到此，就是一个很小的新闻项目，没有任何难度。下面我们就对这个项目不断进行优化。主要就是通过缓存策略，所以先来看缓存策略的介绍。

##LruCache用法详解

###初识Cache

对于缓存我想大家都应该了解，通俗的说就是把一些经常使用但需要联网获取文件，通过一种策略持久的保存在内存或者存储设备中，当下一次需要用到这些文件的时候，不需要联网，直接从内存或存储设备中获取就可以了。这种策略就是缓存策略。

缓存策略一般来说包含缓存的**添加、获取、删除**。至于删除，其实是指缓存的大小已经超过定义的缓存的大小后移除已有的一部分缓存。比如LRU算法，最近最少使用算法，会移除最近最少使用的那一部分缓存，以此来添加新的缓存。

关于缓存的好处，开篇已经说过了，无非就是两点：

* 节省流量
* 提高用户体验


![为什么流量又没了](http://www.iamxiarui.com/wp-content/uploads/2016/06/为什么我的流量又没了.png)


而接下来要说的LruCache和DiskLruCache就是基于LRU算法的缓存策略。LruCache是用于实现内存缓存的，而DiskLruCache实现存储设备缓存，也就是直接缓存到本地。其中LruCache在Android中已经封装成了类，直接用就可以了。而DiskLruCache需要下载对应的文件才能用，本项目中也有集成好的。如果需要可以直接拷贝来用。


###LruCache介绍

关于什么是LruCache，在开发艺术探索上任老师说的不能再清楚了：

> **LruCache是一个泛型类，它内部采用一个LinkedHashMap以强引用的方式存储外界的缓存对象，其提供了get与set方法来完成缓存的添加与获取操作。当缓存满时，LruCache会移除较早使用的缓存对象，然后再添加新的缓存对象。**

这里面提到了一个概念——强引用。也就是Java中的四种引用，不久前电话面试中面试官也问到了这个，可惜当时答的太烂。由于不作为重点，这里仅仅给出定义，点到为止：

* 强引用：直接的对象引用，gc绝不会回收它
* 软引用：当对象只具有软引用时，系统内存不足时才会被gc回收
* 弱引用：当对象只具有弱引用时，对象随时会被gc
* 虚引用：当对象只具有虚引用时，对象随时会被gc，但是必须与引用队列一起使用

那么如何使用LruCache呢？首先需要直接定义一个LruCache，注意内部实现是Map，所以要设置key和value的类型：

	//LRU缓存
    private LruCache<String, Bitmap> mCache;

然后就是初始化LruCache，来看下面这段代码：

	//返回Java虚拟机将尝试使用的最大内存
    int maxMemory = (int) Runtime.getRuntime().maxMemory();
    //指定缓存大小
    int cacheSize = maxMemory / 4;
    mCache = new LruCache<String, Bitmap>(cacheSize) {
    	@Override
        protected int sizeOf(String key, Bitmap value) {
        	//Bitmap的实际大小 注意单位与maxMemory一致
            return value.getByteCount();

			//也可以这样返回 结果是一样的
			//return value.getRowBytes()*value.getHeight();
		}
	};

可以看到上面这段代码规定了LruCache的缓存大小，它是通过返回Java虚拟机将尝试使用的最大内存来确定的。这就初始化了一个LruCache，现在就是简单的添加获取了。因为是Map机制，所以与Map的添加获取是一样的道理。

	//添加到缓存
	mCache.get(key);
	//从缓存中获取
	mCache.put(key,value);

这也就是LruCache的基本使用了，当然还有其他方法，这里暂且不考虑。而且上面的添加与获取在项目中可以封装成相关的方法。接下来，我们就对上一个项目进行优化，来看看如何将联网获取的图片缓存到内存。

###LruCache实战运用

为了与之前的ThreadUtil对比，这里讲LruCache方法剥离成LruCacheUtil。先看效果图，第一次打开需要加载图片，全部加载完成后，再滑动的时候，已经不需要加载图片了。

![LruCache加载](http://www.iamxiarui.com/wp-content/uploads/2016/06/LruCache加载.gif)

来看LruCacheUtil，为了方便讲解，我将各个部分分开来说明。完整代码在下一个部分 **进一步优化ListView** 中。

首先我们定义出相关需要的变量，然后在构造函数中，初始化LruCache：

 	//LRU缓存
    private LruCache<String, Bitmap> mCache;

    private ListView mListView;

	public LruCacheUtil(ListView listView) {
        this.mListView = listView;
        //返回Java虚拟机将尝试使用的最大内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //指定缓存大小
        int cacheSize = maxMemory / 4;
        mCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //Bitmap的实际大小 注意单位与maxMemory一致
                return value.getByteCount();
            }
        };
    }

注意这个地方，LurCache的key是字符串类型的图片Url地址，value当然就是图片的Bitmap对象，所以我们很轻易的封装出缓存的添加删除方法：

	/**
     * 将Bitmap存入缓存
     *
     * @param url    Bitmap对象的key
     * @param bitmap 对象的key
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
        //如果缓存中没有
        if (getBitmapFromCache(url) == null) {
            //保存到缓存中
            mCache.put(url, bitmap);
        }
    }

    /**
     * 从缓存中获取Bitmap对象
     *
     * @param url Bitmap对象的key
     * @return 缓存中Bitmap对象
     */
    public Bitmap getBitmapFromCache(String url) {
        return mCache.get(url);
    }


其次呢，由于前几天说了异步任务AsyncTask，所以我们这里就改成异步任务。所以我们先定义一个AsyncTask类，比较简单，不多解释。

	/**
     * 异步任务类
     */
    private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private String url;

        public NewsAsyncTask(String url) {
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = getBitmapFromURL(params[0]);
            //保存到缓存中
            if (bitmap != null) {
                addBitmapToCache(params[0], bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //只有当前的ImageView所对应的UR的图片是一致的,才会设置图片
            ImageView imageView = (ImageView) mListView.findViewWithTag(url);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

剩下的当然就是展示了，先从缓存中找，没找到才联网获取，联网获取的方法getBitmapFromURL在之前已经介绍了，不再多说了。代码如下：

	/**
     * 通过异步任务的方式加载数据
     *
     * @param iv  图片的控件
     * @param url 图片的URL
     */
    public void showImageByAsyncTask(ImageView iv, final String url) {
        //从缓存中取出图片
        Bitmap bitmap = getBitmapFromCache(url);
        //如果缓存中没有，先设为默认图片
        if (bitmap == null) {
            iv.setImageResource(R.mipmap.ic_launcher);
        } else {
            //如果缓存中有 直接设置
            iv.setImageBitmap(bitmap);
        }
    }

到此，这个类就算完成了，当然不要忘了，注意我们在自定义的AsyncTask中传递了一个类型为ListView的参数，所以要改一下自定义Adapter的参数，并在改一下图片的加载方式：

	private LruCacheUtil lruCacheUtil;

	public NewsAdapter(Context context, List<NewsBean> list, ListView lv) {
		...
		//初始化
        lruCacheUtil = new LruCacheUtil(lv);
		...
	}

	...


	@Override
    public View getView(int position, View convertView, ViewGroup parent) {

		...
		//第二种方式 通过异步任务方式设置 且利用LruCache存储到内存缓存中
    	lruCacheUtil.showImageByAsyncTask(viewHolder.iconImage, iconUrl);

	...
	}

好了，到这我们就算实现了LruCache，再总结一下思路就是在加载图片的时候，先从缓存中找图，如果没有才从网络中获取。而且在获取后存入到缓存中，以方便下一次加载。

##进一步优化ListView

不知道大家注意到没有，上面一个动态图中有一个细节。就是我在滑动ListView的时候，并没有加载图片，等到列表停止滑动的时候才加载图片。对，这也是一个优化ListView的方式之一。

那它是怎么实现的呢？其实就是实现一个列表滚动监听，也就是 **OnScrollListener** ，它的核心方法是

* public void onScrollStateChanged(AbsListView view, int scrollState)
* public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)

其中参数的意思等会代码中有详细解释，先来说一下思路。

首先在滑动过程中需要记录滑动的起止位置，根据起止位置判断中间有多少个item，然后再加载图片。那么如何加载呢？有了起止位置，要得到起止位置之间的元素，这跟数组不是很相似嘛，所以干脆我们用数据把图片的URL记录起来，然后直接从数组中取就好了。

思路大体就是这样，但是实现起来还是比较复杂的，主要有很多细节问题。

先来改造一下自定义的Adapter：
	
	/**
	 * 新闻列表适配器
	 */
	public class NewsAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
	
	    private Context context;
	    private List<NewsBean> list;

	    private LruCacheUtil lruCacheUtil;

	    private int mStart, mEnd;//滑动的起始位置
	    public static String[] urls; //用来保存当前获取到的所有图片的Url地址
	
	    //是否是第一次进入
	    private boolean mFirstIn;
	
	    public NewsAdapter(Context context, List<NewsBean> list, ListView lv) {
	        this.context = context;
	        this.list = list;

	        lruCacheUtil = new LruCacheUtil(lv);

	        //存入url地址
	        urls = new String[list.size()];
	        for (int i = 0; i < list.size(); i++) {
	            urls[i] = list.get(i).newsIconUrl;
	        }

	        mFirstIn = true;

	        //注册监听事件
	        lv.setOnScrollListener(this);
	    }
	
	
	    ...
	
	    /**
	     * 滑动状态改变的时候才会去调用此方法
	     *
	     * @param view        滚动的View
	     * @param scrollState 滚动的状态
	     */
	    @Override
	    public void onScrollStateChanged(AbsListView view, int scrollState) {
	        if (scrollState == SCROLL_STATE_IDLE) {
	            //加载可见项
	            lruCacheUtil.loadImages(mStart, mEnd);
	        } else {
	            //停止加载任务
	            lruCacheUtil.cancelAllTask();
	        }
	    }
	
	    /**
	     * 滑动过程中 一直会调用此方法
	     *
	     * @param view             滚动的View
	     * @param firstVisibleItem 第一个可见的item
	     * @param visibleItemCount 可见的item的长度
	     * @param totalItemCount   总共item的个数
	     */
	    @Override
	    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	        mStart = firstVisibleItem;
	        mEnd = firstVisibleItem + visibleItemCount;
	        //如果是第一次进入 且可见item大于0 预加载
	        if (mFirstIn && visibleItemCount > 0) {
	            try {
	                lruCacheUtil.loadImages(mStart, mEnd);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            mFirstIn = false;
	        }
	    }
	}

也许你有个疑问？因为LruCacheUtil中自定义的AsyncTask类是用来加载图片的。但是你在滑动过程中，停止了这些Task，那么在滑动停止的时候如何得到这些没执行的Task呢？没错，解决办法就是把这些滑动过程中产生的Task放在集合中。当滑动的时候，停止这些Task的执行；滑动停止的时候，再执行这些Task。也就是如下这样定义:

` private Set<NewsAsyncTask> mTaskSet `

好了，现在回头看滑动停止、静止加载的主要两个核心方法：

	/**
     * 加载从start到end的所有的Image
     *
     * @param start
     * @param end
     */
    public void loadImages(int start, int end) {
        for (int i = start; i < end; i++) {
            String url = NewsAdapter.urls[i];
            //从缓存中取出图片
            Bitmap bitmap = getBitmapFromCache(url);
            //如果缓存中没有，则需要从网络中下载
            if (bitmap == null) {
                NewsAsyncTask task = new NewsAsyncTask(url);
                task.execute(url);
                mTaskSet.add(task);
            } else {
                //如果缓存中有 直接设置
                ImageView imageView = (ImageView) mListView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            }
        }
    }


    /**
     * 停止所有当前正在运行的任务
     */
    public void cancelAllTask() {
        if (mTaskSet != null) {
            for (NewsAsyncTask task : mTaskSet) {
                task.cancel(false);
            }
        }
    }

说到这里，现在来看整个LruCacheUtil类，是不是清晰很多呢？

	/**
	 * 异步加载图片的工具类
	 */
	public class LruCacheUtil {
	
	    //LRU缓存
	    private LruCache<String, Bitmap> mCache;
	
	    private ListView mListView;
	    private Set<NewsAsyncTask> mTaskSet;
	
	
	    public LruCacheUtil(ListView listView) {
	        this.mListView = listView;
	        mTaskSet = new HashSet<>();
	        //返回Java虚拟机将尝试使用的最大内存
	        int maxMemory = (int) Runtime.getRuntime().maxMemory();
	        //指定缓存大小
	        int cacheSize = maxMemory / 4;
	        mCache = new LruCache<String, Bitmap>(cacheSize) {
	            @Override
	            protected int sizeOf(String key, Bitmap value) {
	                //Bitmap的实际大小 注意单位与maxMemory一致
	                return value.getByteCount();
	
	                //也可以这样返回 结果是一样的
	                //return value.getRowBytes()*value.getHeight();
	            }
	        };
	    }
	
	    /**
	     * 通过异步任务的方式加载数据
	     *
	     * @param iv  图片的控件
	     * @param url 图片的URL
	     */
	    public void showImageByAsyncTask(ImageView iv, final String url) {
	        //从缓存中取出图片
	        Bitmap bitmap = getBitmapFromCache(url);
	        //如果缓存中没有，则需要从网络中下载
	        if (bitmap == null) {
	            bitmap = getBitmapFromURL(url);
	            iv.setImageBitmap(bitmap);
	        } else {
	            //如果缓存中有 直接设置
	            iv.setImageBitmap(bitmap);
	        }
	    }
	
	    /**
	     * 将一个URL转换成bitmap对象
	     *
	     * @param urlStr 图片的URL
	     * @return
	     */
	    public Bitmap getBitmapFromURL(String urlStr) {
	        Bitmap bitmap;
	        InputStream is = null;
	
	        try {
	            URL url = new URL(urlStr);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            is = new BufferedInputStream(connection.getInputStream());
	            bitmap = BitmapFactory.decodeStream(is);
	            connection.disconnect();
	            return bitmap;
	        } catch (java.io.IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return null;
	    }
	
	    /**
	     * 加载从start到end的所有的Image
	     *
	     * @param start
	     * @param end
	     */
	    public void loadImages(int start, int end) {
	        for (int i = start; i < end; i++) {
	            String url = NewsAdapter.urls[i];
	            //从缓存中取出图片
	            Bitmap bitmap = getBitmapFromCache(url);
	            //如果缓存中没有，则需要从网络中下载
	            if (bitmap == null) {
	                NewsAsyncTask task = new NewsAsyncTask(url);
	                task.execute(url);
	                mTaskSet.add(task);
	            } else {
	                //如果缓存中有 直接设置
	                ImageView imageView = (ImageView) mListView.findViewWithTag(url);
	                imageView.setImageBitmap(bitmap);
	            }
	        }
	    }
	
	
	    /**
	     * 停止所有当前正在运行的任务
	     */
	    public void cancelAllTask() {
	        if (mTaskSet != null) {
	            for (NewsAsyncTask task : mTaskSet) {
	                task.cancel(false);
	            }
	        }
	    }
	
	    /*--------------------------------LruCaChe的实现-----------------------------------------*/
	
	    /**
	     * 将Bitmap存入缓存
	     *
	     * @param url    Bitmap对象的key
	     * @param bitmap 对象的key
	     */
	    public void addBitmapToCache(String url, Bitmap bitmap) {
	        //如果缓存中没有
	        if (getBitmapFromCache(url) == null) {
	            //保存到缓存中
	            mCache.put(url, bitmap);
	        }
	    }
	
	    /**
	     * 从缓存中获取Bitmap对象
	     *
	     * @param url Bitmap对象的key
	     * @return 缓存中Bitmap对象
	     */
	    public Bitmap getBitmapFromCache(String url) {
	        return mCache.get(url);
	    }
	
	    /*--------------------------------LruCaChe的实现-----------------------------------------*/
	
	    /**
	     * 异步任务类
	     */
	    private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap> {
	        private String url;
	
	        public NewsAsyncTask(String url) {
	            this.url = url;
	        }
	
	        @Override
	        protected Bitmap doInBackground(String... params) {
	            Bitmap bitmap = getBitmapFromURL(params[0]);
	            //保存到缓存中
	            if (bitmap != null) {
	                addBitmapToCache(params[0], bitmap);
	            }
	            return bitmap;
	        }
	
	        @Override
	        protected void onPostExecute(Bitmap bitmap) {
	            super.onPostExecute(bitmap);
	            //只有当前的ImageView所对应的UR的图片是一致的,才会设置图片
	            ImageView imageView = (ImageView) mListView.findViewWithTag(url);
	            if (imageView != null && bitmap != null) {
	                imageView.setImageBitmap(bitmap);
	            }
	            //移除所有Task
	            mTaskSet.remove(this);
	        }
	    }
	}

至此，有了缓存与ListView滑动监听，我们可以说项目已经有了良好的性能优化和体验了。


##结语

其实一开始准备将DiskLruCache也加到本文中，后来发现本文的篇幅已经过长了，大家可能看的比较枯燥。加上DiskLruCache相较于LruCache来说复杂一点，所以就将缓存策略分为两篇了。

由于篇幅过长，项目也比较复杂，有些地方并没有将所有细节说清楚。如果大家有不明白的地方，或者文中有错误的，可以与我交流。由于我还在学习阶段，水平有限，希望大家多多支持。
