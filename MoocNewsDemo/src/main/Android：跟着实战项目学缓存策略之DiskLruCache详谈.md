# Android：跟着实战项目学缓存策略之DiskLruCache详谈


## 写在前面

之前花费大心思更新了一篇《Android：跟着实现项目学缓存策略之LruCache详谈》，本来是准备用项目实战的方式分享一下缓存策略的使用。但是由于篇幅过长，DiskLruCache也比较复杂，所以决定把DiskLruCache抽取出来单独讲。


[Android：跟着实现项目学缓存策略之DiskLruCache详谈](http://www.jianshu.com/p/745912f9dd41)

前一篇文章的链接在这里，如果大家有不了解Cache的，建议先看一下，然后再来看本文。本文仍然是在上一篇文章中新闻小项目基础上来说明DiskLurCache的用法，以及与LruCache的不同。文章的目录如下：

* 写在前面
* 遗留问题
* DiskLruCache详解
  * 基本介绍
  * 实战运用
* 缓存策略对比与总结
* 结语
* 项目源码


## 遗留问题

上一篇文章中已经将图片成功的缓存到内存中，当所有图片缓存完成后，再次滑动就已经不需要重新加载图片了。但是注意看下面这张图的现象：

![存回收，缓存随之回收](http://www.iamxiarui.com/wp-content/uploads/2016/06/内存回收，缓存随之回收.gif)


可以看到，成功缓存后确实在应用内再次滑动就不需要加载了，但是如果此时我们kill掉APP，重新打开的话，仍然是需要加载的。这是为什么呢？

答案很显然，因为LruCache是将文件类型缓存到内存中，随着APP中Activity的销毁，内存也会随之回收。也就将内存中的缓存回收掉，再次打开APP的时候，内存中找不到缓存，当然需要重新加载了。

所以如何才能缓存到存储设备中呢？下面就来详细说说。


## DiskLruCache详解

### 基本介绍

DiskLruCache与LruCache不同，它不是Android中已经封装好的类，所以 想要使用的话需要从网上下载。关于下载这个类，我也是费了不少功夫，大家如果想尝试的话，可以直接Copy我这个项目中的 **com.libcore.io** 包下的所有文件即可，这个就不多说了。下面这是它的一个基本定义，也是开发艺术探索中任老师说的：

> **DiskLruCache用于实现存储设备缓存，即磁盘缓存，它通过将缓存对象写入文件系统从而实现缓存的效果。**

注意，重点是将缓存对象写入**文件系统**，大家可能不太理解，不过不用担心，后面会说到。先来它的创建、添加、获取方法。

#### 1、创建

与LruCache不同的是，它不能通过构造方法的方式来创建，它的创建方法是通过DiskLruCache类的一个静态方法 **open** 来创建。具体如下：

> **public static DiskLruCache open(File directory,int appVersion,int valueCount,long maxSize)**

其中有四个参数，很好理解：

* File directory：这是缓存文件在磁盘中的存储路径，这是必须要指定的，一般来说是选择SD卡上的缓存目录，APP卸载后自动删除缓存。
* int appVersion：这个是版本号，用处不大，正常设置为1即可。
* int valueCount：这个是单个节点所对应的数据个数，其实就是一个key对应多少个value，正常设置为1即可，这样key和value一一对应，方便查找。
* long maxSize：这个就是缓存的总大小，很好理解。

这样看来，创建一个DiskLruCache就至少要指定文件的目录与缓存大小。所以创建方式如下：

	//DiskLruCache
	private DiskLruCache mDiskCache;
	//指定磁盘缓存大小
	private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;//50MB
	//得到缓存文件
	File diskCacheDir = getDiskCacheDir(mContext, "diskcache");
	//如果文件不存在 直接创建
	if (!diskCacheDir.exists()) {
		diskCacheDir.mkdirs();
	}
	mDiskCache = DiskLruCache.open(diskCacheDir, 1, 1,DISK_CACHE_SIZE);


	/**
	 * 创建缓存文件
	 *
	 * @param context  上下文对象
	 * @param filePath 文件路径
	 * @return 返回一个文件
	 */
	public File getDiskCacheDir(Context context, String filePath) {
	    boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	    final String cachePath;
	    if (externalStorageAvailable) {
	        cachePath = context.getExternalCacheDir().getPath();
	    } else {
	        cachePath = context.getCacheDir().getPath();
	    }
	    return new File(cachePath + File.separator + filePath);
	}

注意，下面的方法是一个工具方法，用来返回一个文件，难度不大。这样就创建了一个DiskLruCache。

#### 2、设置key

一般来说，需要用到缓存的地方都是需要联网下载的，所以这个key最好的就是需要下载的文件的Url。但是Url中可能有一些特殊字符，所以最好的方式就是将其转换成MD5值。

> MD5是计算机安全领域广泛使用的一种散列函数，用以提供消息的完整性保护。

说简单点，就是一种加密算法，将一串信息转成定长的一串字符。这里只是防止Url中的特殊字符影响正常使用。下面给出如何转成MD5，这是《Android开发艺术探索》中的源码，可以当成工具方法，直接用即可。

	/**
	 * 将URL转换成key
	 *
	 * @param url 图片的URL
	 * @return
	 */
	private String hashKeyFormUrl(String url) {
	    String cacheKey;
	    try {
	        final MessageDigest mDigest = MessageDigest.getInstance("MD5");
	        mDigest.update(url.getBytes());
	        cacheKey = bytesToHexString(mDigest.digest());
	    } catch (NoSuchAlgorithmException e) {
	        cacheKey = String.valueOf(url.hashCode());
	    }
	    return cacheKey;
	}
	
	/**
	 * 将Url的字节数组转换成哈希字符串
	 *
	 * @param bytes URL的字节数组
	 * @return
	 */
	private String bytesToHexString(byte[] bytes) {
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < bytes.length; i++) {
	        String hex = Integer.toHexString(0xFF & bytes[i]);
	        if (hex.length() == 1) {
	            sb.append('0');
	        }
	        sb.append(hex);
	    }
	    return sb.toString();
	}

#### 3、添加

与LruCache不同的是，LruCache内部实现是Map，添加直接用put即可；而DiskLruCache是将文件存储到文件中，所以需要通过文件输出流的形式将文件写入到文件系统中。但是仅仅写入是不够的，必须通过Editor对象来提交。它是缓存对象的编辑对象。它是根据文件的Url对应的key的 **edit()** 方法获取。

值得注意的是，如果返回的Editor对象正在被编辑，那么返回的结果不为null。反之如果返回null，表示编辑对象可用。所以我们在使用前必须判断一下返回的Editor对象是否为空。如果不为空的话，那就通过Editor对象的 **commi** 方法来提交写入操作，当然你也可以通过 **abort** 方法来撤销写入操作。

说了这么多，归纳来说DiskLruCache的添加操作分为三步：

* 通过文件的Url将文件写入文件系统
* 通过Url对应的key来得到一个不为空的Editor对象
* 通过这个Editor对象来对写入操作进行提交或者撤销操作

好了，现在来看具体的实现代码，代码逻辑应该很清晰：

	/**
	 * 将URL中的图片保存到输出流中
	 *
	 * @param urlString    图片的URL地址
	 * @param outputStream 输出流
	 * @return 输出流
	 */
	private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
	    HttpURLConnection urlConnection = null;
	    BufferedOutputStream out = null;
	    BufferedInputStream in = null;
	    try {
	        final URL url = new URL(urlString);
	        urlConnection = (HttpURLConnection) url.openConnection();
	        in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
	        out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);
	        int b;
	        while ((b = in.read()) != -1) {
	            out.write(b);
	        }
	        return true;
	    } catch (final IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (urlConnection != null) {
	            urlConnection.disconnect();
	        }
	        try {
	            if (out != null) {
	                out.close();
	            }
	            if (in != null) {
	                in.close();
	            }
	        } catch (final IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return false;
	}


	/**
	 * 将Bitmap写入缓存
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private Bitmap addBitmapToDiskCache(String url) throws IOException {
	    //如果当前线程是在主线程 则异常
	    if (Looper.myLooper() == Looper.getMainLooper()) {
	        throw new RuntimeException("can not visit network from UI Thread.");
	    }
	    if (mDiskCache == null) {
	        return null;
	    }
	
	    //设置key，并根据URL保存输出流的返回值决定是否提交至缓存
	    String key = hashKeyFormUrl(url);
	    DiskLruCache.Editor editor = mDiskCache.edit(key);
	    if (editor != null) {
	        OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
	        if (downloadUrlToStream(url, outputStream)) {
	            editor.commit();
	        } else {
	            editor.abort();
	        }
	        mDiskCache.flush();
	    }
	    return getBitmapFromDiskCache(url);
	}



#### 4、获取

相比较于添加操作，获取操作很简单。当然还是通过key来获取。有了key，可以通过DiskLruCache的get方法获取到一个 **Snapshot** 对象，再通过这个对象的 **getInputStream** 方法得到文件的输入流，得到了输出流当然可以获取流中的文件了。

所以概括起来，获取缓存中文件的步骤也有三个：

* 通过key来得到一个Snapshot对象
* 通过Snapshot得到一个文件输入流
* 通过文件输入流得到文件对象

具体的代码实现如下：

	/**
	 * 从缓存中取出Bitmap
	 *
	 * @param url 图片的URL
	 * @return 返回Bitmap对象
	 * @throws IOException
	 */
	private Bitmap getBitmapFromDiskCache(String url) throws IOException {
	    //如果当前线程是主线程 则异常
	    if (Looper.myLooper() == Looper.getMainLooper()) {
	        Log.w(TAG, "load bitmap from UI Thread, it's not recommended!");
	    }
	    //如果缓存中为空  直接返回为空
	    if (mDiskCache == null) {
	        return null;
	    }
	
	    //通过key值在缓存中找到对应的Bitmap
	    Bitmap bitmap = null;
	    String key = hashKeyFormUrl(url);
	    //通过key得到Snapshot对象
	    DiskLruCache.Snapshot snapShot = mDiskCache.get(key);
	    if (snapShot != null) {
	        //得到文件输入流
	        FileInputStream fileInputStream = (FileInputStream) snapShot.getInputStream(DISK_CACHE_INDEX);
	        FileDescriptor fileDescriptor = fileInputStream.getFD();
	        bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
	    }
	    return bitmap;
	}



#### 5、补充

如果大家仔细看了上面的代码会发现不管是缓存的添加还是获取方法中，都有下面这段代码：

	//如果当前线程是主线程 则异常
	    if (Looper.myLooper() == Looper.getMainLooper()) {
	        Log.w("DiskLruCache", "load bitmap from UI Thread, it's not recommended!");
	    }	

这是因为这两个方法都不能在主线程中调用，所以需要检查一下，如果不是主线程的话，直接抛出异常。这也算是一个细节吧。

### 实战运用

好了，通过上面的分块讲解，大家应该对DiskLruCache有了基本的认识了。现在我们就对上一个项目添加这样的缓存策略。同样的，为了方便大家对比查看，我仍然把这些方法封装到DiskCacheUtil类。

给出代码之前，我们也大致梳理一下思路：

* 首先要初始化DiskLruCache，这个毋庸置疑
* 其次就需要提供DiskLruCache的添加、获取方法。
* 而这个添加获取方法需要用到key值，所以要将Url转成MD5值。
* 剩下的就是通过AsyncTask来展示图片了，并在展示过程中添加到缓存中。
* 当然不要忘了，前一篇所说的ListView滑动停止加载，静止才能加载的优化。

下面直接给出代码，代码比较长，但是冷静下来，按照前面说的逻辑来看是不是很清晰呢？

	/**
	 * 利用DiskLruCache来缓存图片
	 */
	public class DiskCacheUtil {
	    private Context mContext;
	
	    private ListView mListView;
	    private Set<NewsAsyncTask> mTaskSet;
	
	    //定义DiskLruCache
	    private DiskLruCache mDiskCache;
	    //指定磁盘缓存大小
	    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;//50MB
	    //IO缓存流大小
	    private static final int IO_BUFFER_SIZE = 8 * 1024;
	    //缓存个数
	    private static final int DISK_CACHE_INDEX = 0;
	    //缓存文件是否创建
	    private boolean mIsDiskLruCacheCreated = false;
	
	    public DiskCacheUtil(Context context, ListView listView) {
	        this.mListView = listView;
	        mTaskSet = new HashSet<>();
	        mContext = context.getApplicationContext();
	        //得到缓存文件
	        File diskCacheDir = getDiskCacheDir(mContext, "diskcache");
	        //如果文件不存在 直接创建
	        if (!diskCacheDir.exists()) {
	            diskCacheDir.mkdirs();
	        }
	        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
	            try {
	                mDiskCache = DiskLruCache.open(diskCacheDir, 1, 1,
	                        DISK_CACHE_SIZE);
	                mIsDiskLruCacheCreated = true;
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	
	    /**
	     * 通过异步任务的方式加载数据
	     *
	     * @param iv  图片的控件
	     * @param url 图片的URL
	     */
	    public void showImageByAsyncTask(ImageView iv, final String url) throws IOException {
	        //从缓存中取出图片
	        Bitmap bitmap = getBitmapFromDiskCache(url);
	        //如果缓存中没有，则需要从网络中下载
	        if (bitmap == null) {
	            iv.setImageResource(R.mipmap.ic_launcher);
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
	            is = new BufferedInputStream(connection.getInputStream(), IO_BUFFER_SIZE);
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
	     * 将URL中的图片保存到输出流中
	     *
	     * @param urlString    图片的URL地址
	     * @param outputStream 输出流
	     * @return 输出流
	     */
	    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
	        HttpURLConnection urlConnection = null;
	        BufferedOutputStream out = null;
	        BufferedInputStream in = null;
	        try {
	            final URL url = new URL(urlString);
	            urlConnection = (HttpURLConnection) url.openConnection();
	            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
	            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);
	            int b;
	            while ((b = in.read()) != -1) {
	                out.write(b);
	            }
	            return true;
	        } catch (final IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (urlConnection != null) {
	                urlConnection.disconnect();
	            }
	            try {
	                if (out != null) {
	                    out.close();
	                }
	                if (in != null) {
	                    in.close();
	                }
	            } catch (final IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return false;
	    }
	
	    /**
	     * 加载从start到end的所有的Image
	     *
	     * @param start
	     * @param end
	     */
	    public void loadImages(int start, int end) throws IOException {
	        for (int i = start; i < end; i++) {
	            String url = NewsAdapter.urls[i];
	            //从缓存中取出图片
	            Bitmap bitmap = getBitmapFromDiskCache(url);
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
	
	    /*--------------------------------DiskLruCaChe的实现-----------------------------------------*/
	
	    /**
	     * 创建缓存文件
	     *
	     * @param context  上下文对象
	     * @param filePath 文件路径
	     * @return 返回一个文件
	     */
	    public File getDiskCacheDir(Context context, String filePath) {
	        boolean externalStorageAvailable = Environment
	                .getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	        final String cachePath;
	        if (externalStorageAvailable) {
	            cachePath = context.getExternalCacheDir().getPath();
	        } else {
	            cachePath = context.getCacheDir().getPath();
	        }
	
	        return new File(cachePath + File.separator + filePath);
	    }
	
	    /**
	     * 得到当前可用的空间大小
	     *
	     * @param path 文件的路径
	     * @return
	     */
	    private long getUsableSpace(File path) {
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
	            return path.getUsableSpace();
	        }
	        final StatFs stats = new StatFs(path.getPath());
	        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
	    }
	
	    /**
	     * 将URL转换成key
	     *
	     * @param url 图片的URL
	     * @return
	     */
	    private String hashKeyFormUrl(String url) {
	        String cacheKey;
	        try {
	            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
	            mDigest.update(url.getBytes());
	            cacheKey = bytesToHexString(mDigest.digest());
	        } catch (NoSuchAlgorithmException e) {
	            cacheKey = String.valueOf(url.hashCode());
	        }
	        return cacheKey;
	    }
	
	    /**
	     * 将Url的字节数组转换成哈希字符串
	     *
	     * @param bytes URL的字节数组
	     * @return
	     */
	    private String bytesToHexString(byte[] bytes) {
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < bytes.length; i++) {
	            String hex = Integer.toHexString(0xFF & bytes[i]);
	            if (hex.length() == 1) {
	                sb.append('0');
	            }
	            sb.append(hex);
	        }
	        return sb.toString();
	    }
	
	    /**
	     * 将Bitmap写入缓存
	     *
	     * @param url
	     * @return
	     * @throws IOException
	     */
	    private Bitmap addBitmapToDiskCache(String url) throws IOException {
	        //如果当前线程是在主线程 则异常
	        if (Looper.myLooper() == Looper.getMainLooper()) {
	            throw new RuntimeException("can not visit network from UI Thread.");
	        }
	        if (mDiskCache == null) {
	            return null;
	        }
	
	        //设置key，并根据URL保存输出流的返回值决定是否提交至缓存
	        String key = hashKeyFormUrl(url);
	        //得到Editor对象
	        DiskLruCache.Editor editor = mDiskCache.edit(key);
	        if (editor != null) {
	            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
	            if (downloadUrlToStream(url, outputStream)) {
	                //提交写入操作
	                editor.commit();
	            } else {
	                //撤销写入操作
	                editor.abort();
	            }
	            mDiskCache.flush();
	        }
	        return getBitmapFromDiskCache(url);
	    }


	    /**
	     * 从缓存中取出Bitmap
	     *
	     * @param url 图片的URL
	     * @return 返回Bitmap对象
	     * @throws IOException
	     */
	    private Bitmap getBitmapFromDiskCache(String url) throws IOException {
	        //如果当前线程是主线程 则异常
	        if (Looper.myLooper() == Looper.getMainLooper()) {
	            Log.w("DiskLruCache", "load bitmap from UI Thread, it's not recommended!");
	        }
	        //如果缓存中为空  直接返回为空
	        if (mDiskCache == null) {
	            return null;
	        }
	
	        //通过key值在缓存中找到对应的Bitmap
	        Bitmap bitmap = null;
	        String key = hashKeyFormUrl(url);
	        //通过key得到Snapshot对象
	        DiskLruCache.Snapshot snapShot = mDiskCache.get(key);
	        if (snapShot != null) {
	            //得到文件输入流
	            FileInputStream fileInputStream = (FileInputStream) snapShot.getInputStream(DISK_CACHE_INDEX);
	            //得到文件描述符
	            FileDescriptor fileDescriptor = fileInputStream.getFD();
	            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
	        }
	        return bitmap;
	    }
	
	     /*--------------------------------DiskLruCaChe的实现-----------------------------------------*/


	    /*--------------------------------异步任务AsyncTask的实现--------------------------------------*/

​	
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
	                try {
	                    //写入缓存
	                    addBitmapToDiskCache(params[0]);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            return bitmap;
	        }
	
	        @Override
	        protected void onPostExecute(Bitmap bitmap) {
	            super.onPostExecute(bitmap);
	            ImageView imageView = (ImageView) mListView.findViewWithTag(url);
	            if (imageView != null && bitmap != null) {
	                imageView.setImageBitmap(bitmap);
	            }
	            mTaskSet.remove(this);
	        }
	    }
	
	    /*--------------------------------异步任务AsyncTask的实现--------------------------------------*/
	}


最后不要忘了在自定义Adapter中调用DiskCache这个工具类，并把图片加载方法换成DiskLruCache方式：

	//第三种方式 通过异步任务方式设置 且利用DiskLruCache存储到磁盘缓存中
	    try {
	        mDiskCacheUtil.showImageByAsyncTask(viewHolder.iconImage, iconUrl);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

好了，现在来看效果图吧：

![DiskLruCaChe](http://www.iamxiarui.com/wp-content/uploads/2016/06/DiskLruCaChe.gif)

从图中可以看出尽管退出了APP，但是重新打开的时候，仍然不需要加载图片，大功告成！

## 缓存策略对比与总结

好了，DiskLruCache也讲完了。回顾之前的LruCache，同样是Android中的缓存策略。那它们之间有什么不同呢？

1. LruCache是Android中的已经封装好的类，可以直接用。但是DiskLruCache需要导入对应的包后，才能使用。
2. LruCache实现的是内存缓存，当APP被kill的时候，缓存也随之消失。而DiskLruCache实现的是磁盘缓存，当APP被kill的时候，缓存仍然不会消失。
3. LruCache的内部实现是LinkedHashMap，也就是集合。所以添加获取方式通过put与get就行了。而DiskLruCache是通过文件流的形式来缓存，所以添加获取是通过输入输出流来实现。

大体也就也上三种主要的区别。

最后我想说的是，本项目是为了大家看起来方便，有对比性，所以把普通线程加载、LruCache加载、DiskLruCache加载分别封装了不同的类。

但是在日常开发中，需要Bitmap的压缩类与这几种加载方式在一起封装成一个大的类。就是大家常提到的 **ImageLoader** 。它专门用来处理Bitmap的加载。

这样做的好处就是将三种加载方式结合，也就是大家常听说的 **三级缓存机制** ，网上也有很多优秀的ImageLoader，当然大家也可以尝试尝试，自己写出一个ImageLoader。



## 结语

通过两篇文章中的一个小小的实战项目，终于把缓存策略说完了。写文章的过程中自己也是回顾了整个项目，受益匪浅。有些时候把一个东西用自己的话分享出来并且让别人能听懂，比自己学一个东西要难很多。所以觉得经常写博客，还是对知识的消化有点帮助的。

最后由于我水平有限，项目和文章中难免会有错误，欢迎大家指正与交流。
