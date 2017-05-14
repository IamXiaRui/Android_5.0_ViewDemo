# 大话RxJava：一、初识RxJava与基本运用


## 写在前面

关注RxJava已经有很久一段时间了，因为当你有一天打开技术论坛、打开Github、打开简书的时候满屏都是各种Rx的时候，心里是很慌的。所以乘结课大作业全部搞定后，静下心花了几天时间系统地学习了一下RxJava。

现在网上有各种优秀的博客或文档来讲解RxJava，最出名的莫过于扔物线老师（暂且称为老师吧...）的这篇教程了，我也是看这篇文章入门的，强推。

> [给 Android 开发者的 RxJava 详解——扔物线](http://gank.io/post/560e15be2dca930e00da1083)

当然也有很多很多优秀的教程，在文末我会加链接。但是从我这几天学习的过程和经验来看，教程不在多，而在于吃透。所以尽管网上已经有了很多优秀的教程了，但在这里我还是想把自己学习的过程记录下来。

因为这个RxJava内容不算少，而且应用场景非常广，所以这个大话RxJava应该会写很久。

今天就来先来个入门RxJava吧，下面是本文的目录：

* 写在前面
* 初识RxJava
	* 什么是Rx
	* 什么是RxJava
	* 扩展的观察者模式
* 如何实现RxJava
	* 创建Observer
	* 创建Observable
	* 订阅（Subscribe）
* 线程控制——Scheduler
* 第一个RxJava案例
* 总结
* 参考资料


## 初识RxJava

### 什么是Rx

很多教程在讲解RxJava的时候，上来就介绍了什么是RxJava。这里我先说一下什么是Rx，Rx就是ReactiveX，官方定义是：

> **Rx是一个函数库，让开发者可以利用可观察序列和LINQ风格查询操作符来编写异步和基于事件的程序**

![一脸懵B](http://www.iamxiarui.com/wp-content/uploads/2016/06/一脸懵B-2.png)

看到这个定义我只能呵呵，稍微通俗点说是这样的：

> **Rx是微软.NET的一个响应式扩展。Rx借助可观测的序列提供一种简单的方式来创建异步的，基于事件驱动的程序。**

这个有点清晰了，至少看到我们熟悉的**异步**与**事件驱动**，所以简单点且不准确地来说：

> **Rx就是一种响应式编程，来创建基于事件的异步程序**

注意，这个定义是不准确的，但是对于初学者来说，已经可以有个基本的认知了。

另外还有一点就是Rx其实是一种编程思想，用很多语言都可以实现，比如RxJava、RxJS、RxPHP等等。而现在我们要说的就是RxJava。

### RxJava是什么

二话不说，先上定义：

> **RxJava就是一种用Java语言实现的响应式编程，来创建基于事件的异步程序**

有人问你这不是废话么，好吧那我上官方定义：

> **一个在 Java VM 上使用可观测的序列来组成异步的、基于事件的程序的库**

反正我刚看这句话的时候也呵呵了，当然现在有所领悟了。

除此之外，扔物线老师总结的就更精辟了：**异步**，它就是一个实现异步操作的库。

### 扩展的观察者模式

对于普通的观察者模式，这里我就不细说了。简单概括就是，**观察者（Observer）**需要在**被观察者(Observable)**变化的一瞬间做出反应。

而两者通过**注册（Register）**或者**订阅(Subscribe)**的方式进行绑定。

就拿扔物线老师给的例子来说，我丰富了一下如图所示：

![观察者模式](http://www.iamxiarui.com/wp-content/uploads/2016/06/观察者模式.png)


其中这个Button就是被观察者（Observable），OnClickListener就是观察者（Observer），两者通过setOnClickListener达成订阅（Subscribe）关系，之后当Button产生OnClick事件的时候，会直接发送给OnClickListener，它做出相应的响应处理。

当然还有其他的例子，比如Android四大组件中的ContentProvider与ContentObserver之间也存在这样的关系。

而RxJava的观察者模式呢，跟这个差不多，但是也有几点差别：

* Observer与Observable是通过 **subscribe()** 来达成订阅关系。
* RxJava中事件回调有三种：**onNext()** 、 **onCompleted()** 、 **onError()** 。
* 如果一个Observerble没有任何的Observer，那么这个Observable是不会发出任何事件的。

其中关于第三点，这里想说明一下，在Rx中，其实Observable有两种形式：热启动Observable和冷启动Observable。

> **热启动Observable任何时候都会发送消息，即使没有任何观察者监听它。**
> 
> **冷启动Observable只有在至少有一个订阅者的时候才会发送消息**

这个地方虽然对于初学者来说区别不大，但是要注意一下，所以上面的第三点其实就针对于冷启动来说的。

另外，关于RxJava的回调事件，扔物线老师总结的很好，我就不班门弄斧了：

* onNext()：基本事件。
* onCompleted(): 事件队列完结。RxJava 不仅把每个事件单独处理，还会把它们看做一个队列。RxJava 规定，当不会再有新的 onNext() 发出时，需要触发 onCompleted() 方法作为标志。
* onError(): 事件队列异常。在事件处理过程中出异常时，onError() 会被触发，同时队列自动终止，不允许再有事件发出。

值得注意的是在一个正确运行的事件序列中, onCompleted() 和 onError() 有且只有一个，并且是事件序列中的最后一个。如果在队列中调用了其中一个，就不应该再调用另一个。

好了，那我们也附一张图对比一下吧：

![RxJava观察者模式](http://www.iamxiarui.com/wp-content/uploads/2016/06/RxJava观察者模式.png)

## 如何实现RxJava

关于实现RxJava的步骤，扔物线老师已经说的非常好了，这里我就大体总结概括一下。

### 创建Observer

在Java中，一想到要创建一个对象，我们马上就想要new一个。没错，这里我们也是要new一个Observer出来，其实就是实现Observer的接口，注意String是接收参数的类型：

	//创建Observer
	Observer<String> observer = new Observer<String>() {
		@Override
		public void onNext(String s) {
			Log.i("onNext ---> ", "Item: " + s);
		}
	
		@Override
		public void onCompleted() {
			Log.i("onCompleted ---> ", "完成");
		}
	
		@Override
		public void onError(Throwable e) {
			Log.i("onError ---> ", e.toString());
		}
	};

当然这里也要提另外一个接口：**Subscriber** ，它跟Observer接口几乎完全一样，只是多了两个方法，看看扔物线老师的总结：

* onStart(): 它会在 subscribe 刚开始，而事件还未发送之前被调用，可以用于做一些准备工作，例如数据的清零或重置。这是一个可选方法，默认情况下它的实现为空。需要注意的是，如果对准备工作的线程有要求（例如弹出一个显示进度的对话框，这必须在主线程执行）， onStart() 就不适用了，因为它总是在 subscribe 所发生的线程被调用，而不能指定线程。

* unsubscribe(): 用于取消订阅。在这个方法被调用后，Subscriber 将不再接收事件。一般在这个方法调用前，可以使用 isUnsubscribed() 先判断一下状态。 要在不再使用的时候尽快在合适的地方（例如 onPause() onStop() 等方法中）调用 unsubscribe() 来解除引用关系，以避免内存泄露的发生。

虽然多了两个方法，但是基本实现方式跟Observer是一样的，所以暂时可以不考虑两者的区别。不过值得注意的是：

> **实质上，在 RxJava 的 subscribe 过程中，Observer 也总是会先被转换成一个 Subscriber 再使用。**


### 创建Observable

与Observer不同的是，Observable是通过 **create()** 方法来创建的。注意String是发送参数的类型：

	//创建Observable
	Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
		@Override
	    public void call(Subscriber<? super String> subscriber) {
		    subscriber.onNext("Hello");
		    subscriber.onNext("World");
		    subscriber.onCompleted();
	    }
	});


关于这其中的流程，我们暂且不考虑，在下一篇的源码解析中，我们再详细说明。

### 订阅（Subscribe）

在之前，我们创建了 Observable 和 Observer ，现在就需要用 **subscribe()** 方法来将它们连接起来，形成一种订阅关系：

	//订阅
	observable.subscribe(observer);

这里其实确实有点奇怪，为什么是Observable（被观察者）订阅了Observer（观察者）呢？其实我们想一想之前Button的点击事件：

	Button.setOnClickListener(new View.OnClickListener())

Button是被观察者，OnClickListener是观察者，setOnClickListener是订阅。我们惊讶地发现，也是被观察者订阅了观察者，所以应该是一种流式API的设计吧，也没啥影响。

完整代码如下： 

		//创建Observer
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onNext(String s) {
                Log.i("onNext ---> ", "Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.i("onCompleted ---> ", "完成");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("onError ---> ", e.toString());
            }
        };

        //创建Observable
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("World");
                subscriber.onCompleted();
            }
        });

        //订阅
        observable.subscribe(observer);

运行的结果如下，可以看到Observable中发送的String已经被Observer接收并打印了出来：

![基本实现](http://www.iamxiarui.com/wp-content/uploads/2016/06/基本实现.png)


## 线程控制——Scheduler

好了，这里就是RxJava的精髓之一了。

在RxJava中，Scheduler相当于线程控制器，可以通过它来指定每一段代码运行的线程。

RxJava已经内置了几个Scheduler，扔物线老师也总结得完美：

* Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。

* Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。


* Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
	
* Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。


* AndroidSchedulers.mainThread()，Android专用线程，指定操作在主线程运行。

那我们如何切换线程呢？RxJava中提供了两个方法：**subscribeOn()** 和 **observeOn()** ，两者的不同点在于：

* subscribeOn(): 指定subscribe() 订阅所发生的线程，即 call() 执行的线程。或者叫做事件产生的线程。

* observeOn(): 指定Observer所运行在的线程，即onNext()执行的线程。或者叫做事件消费的线程。

具体实现如下：

	//改变运行的线程
	observable.subscribeOn(Schedulers.io());
	observable.observeOn(AndroidSchedulers.mainThread());

这里确实不好理解，没关系，下面我们在具体例子中观察现象。

而这其中的原理，会在之后的源码级分析的文章中详细解释，现在我们暂且搁下。

## 第一个RxJava案例

好了，当看完之前的所有基础东西，现在我们就完全可以写一个基于RxJava的Demo了。

这里我们用一个基于RxJava的异步加载网络图片来演示。

由于重点在于RxJava对于异步的处理，所以关于如何通过网络请求获取图片，这里就不详细说明了。

另外这里采用的是链式调用，并为重要位置打上Log日志，观察方法执行的所在线程。

首先需要添加依赖，这没什么好说的：

	dependencies {
	    compile fileTree(include: ['*.jar'], dir: 'libs')
	    testCompile 'junit:junit:4.12'
	    ...
	    compile 'io.reactivex:rxjava:1.1.6'
	    
	}

然后按照步骤来，首先通过create创建Observable，注意发送参数的类型是Bitmap：

	//创建被观察者
	Observable.create(new Observable.OnSubscribe<Bitmap>() {
		/**
		* 复写call方法
		*
		* @param subscriber 观察者对象
		*/
		@Override
		public void call(Subscriber<? super Bitmap> subscriber) {
			//通过URL得到图片的Bitmap对象
			Bitmap bitmap = GetBitmapForURL.getBitmap(url);
			//回调观察者方法
			subscriber.onNext(bitmap);
			subscriber.onCompleted();
			Log.i(" call ---> ", "运行在 " + Thread.currentThread().getName() + " 线程");
		}
	})

然后我们需要创建Observer，并进行订阅，这里是链式调用


	.subscribe(new Observer<Bitmap>() {   //订阅观察者（其实是观察者订阅被观察者）

    	@Override
        public void onNext(Bitmap bitmap) {
        	mainImageView.setImageBitmap(bitmap);
            Log.i(" onNext ---> ", "运行在 " + Thread.currentThread().getName() + " 线程");
        }

        @Override
        public void onCompleted() {
        	mainProgressBar.setVisibility(View.GONE);
            Log.i(" onCompleted ---> ", "完成");
        }

        @Override
        public void onError(Throwable e) {
        	Log.e(" onError --->", e.toString());
        }
     });

当然网络请求是耗时操作，我们需要在其他线程中执行，而更新UI需要在主线程中执行，所以需要设置线程：

	.subscribeOn(Schedulers.io()) // 指定subscribe()发生在IO线程
	.observeOn(AndroidSchedulers.mainThread()) // 指定Subscriber的回调发生在UI线程

这样我们就完成了一个RxJava的基本编写，现在整体看一下代码：

	//创建被观察者
	Observable.create(new Observable.OnSubscribe<Bitmap>() {
		/**
		* 复写call方法
		*
		* @param subscriber 观察者对象
		*/
		@Override
		public void call(Subscriber<? super Bitmap> subscriber) {
			//通过URL得到图片的Bitmap对象
			Bitmap bitmap = GetBitmapForURL.getBitmap(url);
			//回调观察者方法
			subscriber.onNext(bitmap);
			subscriber.onCompleted();
			Log.i(" call ---> ", "运行在 " + Thread.currentThread().getName() + " 线程");
		}
	})
	.subscribeOn(Schedulers.io()) // 指定subscribe()发生在IO线程
	.observeOn(AndroidSchedulers.mainThread()) // 指定Subscriber的回调发生在UI线程
	.subscribe(new Observer<Bitmap>() {   //订阅观察者（其实是观察者订阅被观察者）

    	@Override
        public void onNext(Bitmap bitmap) {
        	mainImageView.setImageBitmap(bitmap);
            Log.i(" onNext ---> ", "运行在 " + Thread.currentThread().getName() + " 线程");
        }

        @Override
        public void onCompleted() {
        	mainProgressBar.setVisibility(View.GONE);
            Log.i(" onCompleted ---> ", "完成");
        }

        @Override
        public void onError(Throwable e) {
        	Log.e(" onError --->", e.toString());
        }
     });

好了，下面是运行的动态图：

![RxJava异步加载网络图片](http://www.iamxiarui.com/wp-content/uploads/2016/06/RxJava异步加载网络图片.gif)


现在来看一下运行的Log日志：

![Log](http://www.iamxiarui.com/wp-content/uploads/2016/06/Log.png)

可以看到，call方法（事件产生）执行在IO线程，而onNext方法（事件消费）执行在main线程。说明之前分析的是对的。


## 总结

好了，由于本文是一个RxJava的基础，所以篇幅稍微过长了点。即使这样，很多细节性问题都没有交代清楚。但所幸的是，本文已经将RxJava必要的基础入门知识讲解完了。

在后期的文章中，主要是对RxJava的源码进行必要的分析，以及RxJava中各种操作符（比如常用的map、from、just、take、flatmap等等）的使用方式进行讲解。

跟现有的博客或者教程不一样的是，我不打算直接用理论知识来讲解，而是会用一个具体的项目来完成一系列的说明。

其实在写博客的时候，对自己所学也是一个复习的过程，能发现之前学习过程中所疏忽的问题。但由于技术水平有限，文中难免会有错误或者疏忽之处，欢迎大家指正与交流。

最后感谢扔物线老师的文章，写的真的是太好了。

## 参考资料

[给 Android 开发者的 RxJava 详解——扔物线](http://gank.io/post/560e15be2dca930e00da1083)

[深入浅出RxJava（一：基础篇）](http://blog.csdn.net/lzyzsd/article/details/41833541)

[ReactiveX 的理念和特点](http://www.open-open.com/lib/view/open1440166491833.html)

# 大话RxJava:二、轻松学源码之基础篇


## 写在前面

[前篇文章](http://www.jianshu.com/p/856297523728)简单介绍了一下RxJava的概念与基本使用，本来准备继续说一下RxJava的进阶使用，包括一些复杂操作符的使用与线程控制的相关内容。然后想了想，不能这样急躁地去学如何使用，应该先稍微明白它的一些过程，后面学习起来思路更加清晰，这也是我之前学习RxJava的一个误区所在。

所以今天这篇文章，打算先认识一下基本的常见的两种操作符及其使用，然后采用图文的模式详细介绍一下RxJava的基础源码，主要包括create()与subscribe()这两个方法到底做了什么事情。

有了这两个基础后，后期学大量复杂且重要的操作符及其原理就有了基本的认知，至少学到复杂的地方不慌。而采用图文模式讲解源码看起来也不烦躁，轻轻松松得去学习。

下面是本文的目录:

- 写在前面
- 初识操作符
	- Observable.just()
	- Observable.from()
- 基础源码
	- create()
	- subscribe()
- 结语
	- 参考资料


## 初识操作符

所谓操作符（Operators），简单来说就是一种指令，表示需要执行什么样的操作。Rx中的每种编程语言实现都实现了一组操作符的集合。RxJava也不例外。

RxJava中有大量的操作符，比如创建操作符、变换操作符、过滤操作符等等，这些操作符要全部讲解完几乎是不可能也没必要的事情。所以我们只介绍常见的、有用的、重要的操作符。其他的如果用到直接到文档查找就行了。

下面就针对前篇文章的创建（create）来说明一下另外两种常见的创建操作符。先来回顾一下create()操作符，比较简单，这里就不解释了：

	//创建Observable
	Observable.create(new Observable.OnSubscribe<String>() {
		@Override
		public void call(Subscriber<? super String> subscriber) {
			subscriber.onNext("Hello");
			subscriber.onNext("World");
			subscriber.onCompleted();
		}
		}).subscribe(new Observer<String>() {
		
			@Override
			public void onNext(String s) {
				Log.i("onNext ---> ", s);
			}
			
			 @Override
			public void onCompleted() {
				Log.i("onCompleted ---> ", "完成");
			}
			
			@Override
			public void onError(Throwable e) {
			
			}
	});

### Observable.just()

首先给出定义：

> **Just操作符是创建一个将参数依次发送出来的Observable**

具体一点来说就是， **just()** 中会接收1~9个参数，它会返回一个**按照传入参数的顺序依次发送**这些参数的Observable。

这样说可能还是不够清晰，所以画个图来看：

![just流程图](http://www.iamxiarui.com/wp-content/uploads/2016/06/just.png)

从图中可以看出，其实就是依次发送单个数据，它的具体写法是这样的，非常简单：

	Observable.just("Hello","world");
	//其实就相当于依次调用：
	//subscriber.onNext("Hello");
	//subscriber.onNext("World");

但是这里要注意一点，如果你传递null给just，它会返回一个**发送null值**的Observable，而不是返回一个空Observable（完全不发送任何数据的Observable）。后面会讲到，如果需要空Observable应该使用 **Empty**  操作符。

现在来看完整的代码，代码本身很简单，注意看Log日志：

	//创建Observable
	Observable.just("Hello", "World", null)
		.subscribe(new Observer<String>() {
	
			@Override
			public void onNext(String s) {
				if (s == null) {
					Log.i("onNext ---> ", "null");
				}else {
	            	Log.i("onNext ---> ", s);
				}
			}
	
			@Override
			public void onCompleted() {
				Log.i("onCompleted ---> ", "完成");
			}
	
			@Override
			public void onError(Throwable e) {
				Log.i("onError ---> ", "出错 --->" + e.toString());
			}
	});

![Log](http://www.iamxiarui.com/wp-content/uploads/2016/06/just-Log.png)

这里因为我们要打印字符串，所以不能为null，我就处理了一下，可以看到当发送 null 的时候，s确实等于null。

### Observable.from()

尽管与just一样是创建操作符，但是from操作符稍微强大点。因为from操作符的作用是：

>  **将传入的数组或 Iterable 拆分成具体对象后，依次发送出来。**

注意，这里不再是发送单个对象，而是直接发送一组对象。为了与just对比，也来画个图描述一下：

![from流程图](http://www.iamxiarui.com/wp-content/uploads/2016/06/from-1.png)

它的具体写法是这样的，也非常简单：

	String[] str = new String[]{"Hello", "World"};
	//创建Observable
	Observable.from(str);

这里由于篇幅关系，我就不贴完整代码了，跟just是类似的，十分简单。

## 基础源码

讲了两个简单但是常用的操作符后，我们回过头来看一下之前的实现RxJava的代码，这里我打上了Log日志，来看一下每个方法执行的顺序。

	//创建Observable
	Observable.create(new Observable.OnSubscribe<String>() {
		@Override
		public void call(Subscriber<? super String> subscriber) {
			subscriber.onNext("Hello");
			subscriber.onNext("World");
			subscriber.onCompleted();
			Log.i("执行顺序 ---> ", " call ");
		}
		}).subscribe(new Observer<String>() {
	
		@Override
		public void onNext(String s) {
			Log.i("onNext ---> ", s);
			Log.i("执行顺序 ---> ", " subscribe onNext");
		}
		
		@Override
		public void onCompleted() {
			Log.i("onCompleted ---> ", "完成");
			Log.i("执行顺序 ---> ", " subscribe onCompleted");
		}
		
		@Override
		public void onError(Throwable e) {
			Log.i("onError ---> ", "出错 --->" + e.toString());
		}
	});

好了，来看一下Log日志：

![执行顺序Log](http://www.iamxiarui.com/wp-content/uploads/2016/06/chain-Log.png)

从图中可以看到，subscribe方法先执行，等执行完成后再执行call方法。

好了，这就是结论。先在脑子里产生个印象，方便后面追溯。

### create()

先来看看Observable的create()方法做了些什么？Ctrl点进去看看：

	public static <T> Observable<T> create(OnSubscribe<T> f) {
        return new Observable<T>(hook.onCreate(f));
    }

啥事没干，就是返回一个 **Observable**。

再看看它的构造函数，构造一下对象：

	protected Observable(OnSubscribe<T> f) {
        this.onSubscribe = f;
    }

再来看这个 **hook.onCreate(f)** 。 **hook** 是啥呢？

> **hook是一个代理对象, 仅仅用作调试的时候可以插入一些测试代码。** 

    static final RxJavaObservableExecutionHook hook = RxJavaPlugins.getInstance().getObservableExecutionHook();

注意是仅仅，所以它几乎没啥用处，完全可以忽略。来看 **hook.onCreate(f)** ：

	public <T> OnSubscribe<T> onCreate(OnSubscribe<T> f) {
        return f;
    }

依然啥事没干，只是把 **OnSubscribe** 这个对象返回了一下。

OK，说到这里，虽然我一直在强调它“啥事没干”，其实仔细推敲，它还真做了三件事情：

* 返回了一个Observable（假设为ObservableA）
* 返回了一个OnSubscribe（假设为OnSubscribeA）
* 把返回的OnSubscribe在Observable构造函数中保存为Observable的 **.onSubscribe** 属性

说到这里，不知道大家看懂了没有。我第一次看这到这里的时候，还有略微有点模糊的，没关系，只要模糊那就画图理解：

![create流程图](http://www.iamxiarui.com/wp-content/uploads/2016/06/create.png)

这样看起来是不是轻松很多，create()就做了这样简单的事情，所以概括（但可能并不准确）地来说就是：

> **create()方法创建了一个Observable，且在这个Observable中有个OnSubscribe。**

所以就画个简图就如下图所示这样，这个图要注意，之后还会扩展：

![create简图](http://www.iamxiarui.com/wp-content/uploads/2016/06/create简图.png)

好了，create方法就分析到这里，虽然有点啰嗦，但是已经十分详细了。

### subscribe()

现在来看另外一个比较重要的操作 **subscribe()** ，在前篇文章中说过，这个是将观察者（Observer）与被观察者(Observable)联系到一起的操作，也就是产生一种订阅(Subcribe)关系。

跟前面的一样，先来看一下源码，点进去是这样的：

	public final Subscription subscribe(final Observer<? super T> observer) {
        if (observer instanceof Subscriber) {
            return subscribe((Subscriber<? super T>)observer);
        }
        return subscribe(new ObserverSubscriber<T>(observer));
    }

之前我们说过一句话：

> **实质上，在 RxJava 的 subscribe 过程中，Observer 也总是会先被转换成一个 Subscriber 再使用。**

在这里就能够看出，首先 **if** 中的语句意思是如果这个Observer已经是Subscriber类型，那就直接返回。如果不是的话 **new了一个ObserverSubscriber** ，再点进去看看：

	public final class ObserverSubscriber<T> extends Subscriber<T> {
	    final Observer<? super T> observer;
	
	    public ObserverSubscriber(Observer<? super T> observer) {
	        this.observer = observer;
	    }
	    
	    @Override
	    public void onNext(T t) {
	        observer.onNext(t);
	    }
	    
	    @Override
	    public void onError(Throwable e) {
	        observer.onError(e);
	    }
	    
	    @Override
	    public void onCompleted() {
	        observer.onCompleted();
	    }
	}

果然，它还是转成了Subscriber类型，刚好印证了之前的话。所以为了方便起见，之后文章中，**所有的观察者（Observer）我都用Subscriber来代替。** 这是一个小插曲，注意一下就好。

好了，继续看 **subscribe** 源码：

	public final Subscription subscribe(Subscriber<? super T> subscriber) {
    	return Observable.subscribe(subscriber, this);
	}

	private static <T> Subscription subscribe(Subscriber<? super T> subscriber, Observable<T> observable) {
	    ...	
	    hook.onSubscribeStart(observable, observable.onSubscribe).call(subscriber);
		...
	}

把一些暂时无关的代码省略掉来看，其实就是执行了一句 **hook.onSubscribeStart(observable, observable.onSubscribe).call(subscriber);** 。

而这个 **hook.onSubscribeStart** 方法再点进去看看：

	public <T> OnSubscribe<T> onSubscribeStart(Observable<? extends T> observableInstance, final OnSubscribe<T> onSubscribe) {
	        // pass through by default
	        return onSubscribe;
	    }

可以看到，竟然直接返回了一个 **onSubscribe** ，由于之前说过这个hook没什么作用，直接删掉，那就等于整个 **subscribe** 做了一件事就是 **onSubscribe.call(subscriber)** ，当然这个call里面的参数subscriber是我们代码中传递进去的。

而onSubscribe在create源码解析中我们已经知道是新建 **ObservableA** 的一个属性，所以总结来说，subscribe()方法做的事情就是这样：

> **ObservableA.onSubscribe.call(subscriber);**

而调用 **call** 方法，就是调用传入的参数subscriber的onNext/onCompleted/onError方法。

这就是全部的过程。

看到这里，估计大家又迷糊了。没关系，依然画个图来说，图中省略了create中的创建步骤：

![全过程](http://www.iamxiarui.com/wp-content/uploads/2016/06/subscribe-1.png)

结合图我们最后再顺一下思路：

* 首先创建过程也就是create()方法中创建了一个Observable，并有一个onSubscribe属性；
* 其次在订阅过程也就是subscribe()方法中，调用了create()方法中创建的Observable的onSubscribe属性的call方法；
* 最后这个call回调的就是代码中创建的Subscriber的onNext/onCompleted/onError方法。

不知道大家还记得那个Log日志么，从日志可以看到，将onNext与onCompleted方法执行完后，call方法才结束。这也印证了call方法回调Subscriber的方法这一说。

## 结语

好了，终于把源码中比较简单的部分讲解完了，等于是再复习了一边之前学的。

而且终于也把RxJava的入门知识讲解完了。后面的文章中，就开始学如何稍微高级一点的运用RxJava，比如map/flatmap操作符、lift的原理、线程控制的原理、各种运用场景等等，还有很长的路要走啊。

最后这是我第一次试着讲解源码，而且也在边学边分享，所以肯定有错误或不清楚的地方，欢迎大家指正与交流。


### 参考资料

[RxJava基本流程和lift源码分析](http://blog.csdn.net/lzyzsd/article/details/50110355)

[彻底搞懂 RxJava — 基础篇](http://diordna.sinaapp.com/?p=896)

# 大话RxJava：三、RxJava的中级使用方法

## 写在前面

前面两篇文章中介绍几乎全部都是基础，而且如果前面两篇吃透了的话，RxJava就算完全入门了。那入门之后就得学一些比较高级一点的用法及其原理了。

所以这篇文章来介绍一下RxJava中另一个核心内容—— **变换** 。本来准备连它的原理一起说明，但是变换的原理稍微复杂了点，如果连在一起写可能篇幅过长，看起来也就比较枯燥。所以暂时不讲源码，先来看看它的基本使用。当然，依然是通过小案例的形式来说明，这样就不会枯燥。

说完几个基本变换操作符之后，再了解一下RxJava中的FuncX与ActionX的作用与区别，最后补充一下上一次说线程控制Scheduler的时候故意省略的一部分内容，至于省略的原因后面再说。

所以全文的目录如下：

* 写在前面
* 神奇的变换
	* 回顾
	* Map
	* FuncX与ActionX
	* FlatMap
* 再话Scheduler
* 结语
	* 参考资料
	* 项目源码


## 神奇的变换

### 回顾

在说变换操作之前，先来回顾一下之前异步获取网络图片的小案例。

	//创建被观察者
	Observable.create(new Observable.OnSubscribe<Bitmap>() {
		/**
		* 复写call方法
		*
		* @param subscriber 观察者对象
		*/
		@Override
		public void call(Subscriber<? super Bitmap> subscriber) {
			//通过URL得到图片的Bitmap对象
			Bitmap bitmap = GetBitmapForURL.getBitmap(url);
			//回调观察者方法
			subscriber.onNext(bitmap);
			subscriber.onCompleted();
		}
	})
	.subscribeOn(Schedulers.io()) // 指定subscribe()发生在IO线程
	.observeOn(AndroidSchedulers.mainThread()) // 指定Subscriber的回调发生在UI线程
	.subscribe(new Observer<Bitmap>() {   //订阅观察者（其实是观察者订阅被观察者）

    	@Override
        public void onNext(Bitmap bitmap) {
        	mainImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onCompleted() {
        	mainProgressBar.setVisibility(View.GONE);
            Log.i(" onCompleted ---> ", "完成");
        }

        @Override
        public void onError(Throwable e) {
        	Log.e(" onError --->", e.toString());
        }
     });

案例本身是没有问题的，但是在这里需要注意一下，在Observable的create方法中就已经规定了发送的对象的类型是 **Bitmap** ，而这个Bitmap是通过图片的Url来获取得到的，得到后再发送给Subscriber（就是观察者Observer，再强调一下后面的文章一律用Subscriber代替Observer，原因上一篇文章已经强调过了，不再赘述）。

简单来说，就是一开始我们就规定好了要发送一个对象的类型。

那是否可以这样设想，我们一开始发送 **String** 类型的Url，然后通过某种方式再将得到的 **Bitmap** 发送出去呢?

用图来说就是这样：

![什么操作](http://www.iamxiarui.com/wp-content/uploads/2016/06/什么操作.png)

### Map

答案当然是有的，RxJava中提供了一种操作符： **Map** ，它的官方定义是这样的：

> **Map操作符对原始Observable发射的每一项数据应用一个你选择的函数，然后返回一个发射这些结果的Observable。**

这句话简单来说就是，它对Observable发送的每一项数据都应用一个函数，并在函数中执行变换操作。

如果还是不明白的话，那就画图去理解，这个图也是官方的图，只不过我重新画了一下：

![map流程图](http://www.iamxiarui.com/wp-content/uploads/2016/06/map.png)

从图中可以看到，这是一对一的转换，就是一个单独的数据转成另一个单独的数据，这一点需要跟后面的 **flatmap** 对比，所以需要留意一下这句话。

好了，了解了基本原理，现在就来给之前的代码进行一个改造：

	//先传递String类型的Url
	Observable.just(url)
		.map(new Func1<String, Bitmap>() {
	    	@Override
	        public Bitmap call(String s) {
	        	//通过Map转换成Bitmap类型发送出去
	            return GetBitmapForURL.getBitmap(s);
	        }
		})
		.subscribeOn(Schedulers.io()) // 指定subscribe()发生在IO线程
		.observeOn(AndroidSchedulers.mainThread()) // 指定Subscriber的回调发生在UI线程
		//可以看到，这里接受的类型是Bitmap，而不是String
		.subscribe(new Action1<Bitmap>() {
			@Override
			public void call(Bitmap bitmap) {
				mainImageView.setImageBitmap(bitmap);
				mainProgressBar.setVisibility(View.GONE);
	        }
	});

这里我们先用just操作符传递一个String类型的Url进去，然后在map操作符中，利用 **Func1** 类的call方法返回一个 **Bitmap** 出去，最后在 subscribe操作中的 **Action1** 类中接收一个 **Bitmap** 对象。

这样就成功的将初始Observable所发送参数的类型通过 **map** 转换成了其他的类型。这就是 **map** 操作符的妙用。

### FuncX与ActionX

在上面的代码中，出现了这两个类： **Func1** 与 **Action1** ，这是什么意思呢？

#### ActionX

先来解释Action1。点开它的源码：

	/**
	 * A one-argument action.
	 * @param <T> the first argument type
	 */
	public interface Action1<T> extends Action {
	    void call(T t);
	}

原来是有一个参数的接口，接口中有一个 **单参数无返回值的call方法** 。由于 **onNext(T obj)** 和 **onError(Throwable error)** 也是单参数无返回值的，因此 Action1 可以将 onNext(obj) 和 onError(error) 打包起来传入 subscribe() 以实现 **不完整定义的回调** 。

也就是说，这种回调只调用 **onNext** 与 **onError** 两个方法，并不是完整的回调(完整的是回调三个方法)。

而对于这种不完整的回调，**RxJava 会自动根据定义创建出 Subscriber** 。

另外，与Action1类似的是 Action0 ，这个也比较常用，依然点进去看一下源码：

	/**
	 * A zero-argument action.
	 */
	public interface Action0 extends Action {
	    void call();
	}

跟上面的一对比，一下就恍然大悟了，其实就是 **无参数无返回值的call方法** ，由于 **onCompleted()** 方法也是无参无返回值的，因此 Action0 可以被当成一个包装对象，将 onCompleted() 的内容打包起来，作为一个参数传入 subscribe() 以实现 **不完整定义的回调** 。

除此之外：

> **RxJava 是提供了多个 ActionX 形式的接口 (例如 Action2, Action3) 的，它们可以被用以包装不同的无返回值的方法。**


好吧，说得通俗一点：

* Action0 就是把 onCompleted() 作为参数传入 subscribe() 。
* Action1 就是把 onNext() 与 onError() 作为参数传入 subscribe() 。

#### FuncX

了解了ActionX之后，来看这个Func1。点进去看源码：

	/**
	 * Represents a function with one argument.
	 * @param <T> the first argument type
	 * @param <R> the result type
	 */
	public interface Func1<T, R> extends Function {
	    R call(T t);
	}

一对比，很容易发现，它跟Action1很相似，也是RxJava的一个接口。但是有一个明显的区别在于，Func1包装的是 **有返回值** 的方法。

而且与ActionX一样，FuncX也有很多个，主要用于不同个数的参数的方法。我们只要记着一点：

> **FuncX 和 ActionX 的区别在 FuncX 包装的是有返回值的方法。**

### FlatMap

好了，至此就把Map说完了。还记得之前强调的那句话么，Map是一对一的转换，那么有没有一对多的转换呢？当然有，就是现在要说的 **FlatMap** 。

依然先看官方定义：

> **FlatMap操作符使用一个指定的函数对原始Observable发射的每一项数据执行变换操作，这个函数返回一个本身也发射数据的Observable，然后FlatMap合并这些Observables发射的数据，最后将合并后的结果当做它自己的数据序列发射。**

好吧，定义总是很迷糊。没关系，现在尝试用图的形式来说明：

![FlatMap流程图](http://www.iamxiarui.com/wp-content/uploads/2016/06/flatmap_1.png)

简单来说就是分别将一组数据中的每个数据进行转换，转换后再把转换后的数据合并到一条序列上进行发送。

不过需要注意的是，转换后的每个数据本身其实也是一个可以发送数据的 **Observable** ，所以将上面图简化一下就是如下图所示：

![FlatMap流程简图](http://www.iamxiarui.com/wp-content/uploads/2016/06/flatmap_2.png)

从简图可以看出，与Map相比较，FlatMap是能进行一对多的转换。

好了，闲话不多说，我们来看具体的案例。案例就是一个GridView异步加载多张网络图片。

对于这个项目我事先说明两点：

* 不去说明GridView如何使用
* 这个项目并不是对FlatMap特性介绍的最佳案例，但是为了与前面的案例做对比，暂时就用这个案例。

好了，为了回顾前文from操作符的使用，我们将四个图片的Url加到一个数据中去，也就是一组Url数据：

	private final String url1 = "http://www.iamxiarui.com/wp-content/uploads/2016/06/套路.png";
    private final String url2 = "http://www.iamxiarui.com/wp-content/uploads/2016/06/为什么我的流量又没了.png";
    private final String url3 = "http://www.iamxiarui.com/wp-content/uploads/2016/05/cropped-iamxiarui.com_2016-05-05_14-42-31.jpg";
    private final String url4 = "http://www.iamxiarui.com/wp-content/uploads/2016/05/微信.png";

	//一组Url数据
    private final String[] urls = new String[]{url1, url2, url3, url4};

然后来看flatmap如何处理：

	//先传递String类型的Url
	Observable.from(urls)
		.flatMap(new Func1<String, Observable<String>>() {
			@Override
			public Observable<String> call(String s) {
				return Observable.just(s);
			}
		})

可以看到，它是将 **一组String类型的Urls** 转换成一个 **发送单独的String类型Url的Observable** 。

既然转换成了能够发送单独数据的Observable，那么就简单多了，就用刚刚学的map操作符吧：

		.map(new Func1<String, Bitmap>() {
	    	@Override
	        public Bitmap call(String s) {
	        	//通过Map转换成Bitmap类型发送出去
	            return GetBitmapForURL.getBitmap(s);
	        }
		})
		.subscribeOn(Schedulers.io()) // 指定subscribe()发生在IO线程
		.observeOn(AndroidSchedulers.mainThread()) // 指定Subscriber的回调发生在UI线程
		//可以看到，这里接受的类型是Bitmap，而不是String
		.subscribe(new Action1<Bitmap>() {
			@Override
			public void call(Bitmap bitmap) {
				mainImageView.setImageBitmap(bitmap);
				mainProgressBar.setVisibility(View.GONE);
	        }
	});

现在我们来看看运行后的动态图：

![加载多张图片](http://www.iamxiarui.com/wp-content/uploads/2016/06/加载多张图片.gif)


可以看到，它是依次加载各张图片。

还记得我之前说这个并不是最好的案例么，为什么呢？因为Flatmap有一个特性：

> **FlatMap对这些Observables发射的数据做的合并操作可能是交错的。**

什么意思呢？也就是这一组数据转换成单独数据后可能顺序会发生改变，从我这个案例来看，并没有出现这种情况，所以我说这并不是一个最完美的案例。

那么有人就问了，如何让它不产生交错呢？

RxJava还给我们提供了一个 **concatMap** 操作符，它类似于最简单版本的flatMap，但是它 **按次序连接** 而不是合并那些生成的Observables，然后产生自己的数据序列。

这个比较简单，我就不写案例演示了。

好了至此我们就将 **常用且非常重要的** 变换操作符讲完了。后面的文章会具体分析它的原理。

## 再话Scheduler

最后呢，想对Scheduler做一些补充。

还记得之前说Scheduler的时候介绍的两个操作符么：

* subscribeOn(): 指定subscribe() 订阅所发生的线程，即 call() 执行的线程。或者叫做事件产生的线程。

* observeOn(): 指定Observer所运行在的线程，即onNext()执行的线程。或者叫做事件消费的线程。

现在我们多介绍两个操作符。

### doOnSubscribe 

之前在说Subscriber与Observer的不同的时候，提到过Subscriber多了两个方法。其中 **onStart()** 方法发生在 **subscribe() 方法调用后且事件发送之前** 是一个进行初始化操作的方法。但是这个初始化操作并不能指定线程。

就那我这个案例来说，里面有一个进度条，如果要显示进度条的话必须在主线程中执行。但是我们事先并不知道subscribeOn()方法会指定什么样的线程。所以在onStart方法中执行一些初始化操作是比较有风险的。

那该怎么办呢？

RxJava中给我们提供了另外一种操作符： **doOnSubscribe** ，这个操作符跟onStart方法一样，都是在 **subscribe() 方法调用后且事件发送之前** 执行，所以我们一样可以在这里面进行初始化的操作。而区别在于它可以指定线程。

> **默认情况下， doOnSubscribe() 执行在 subscribe() 发生的线程；而如果在 doOnSubscribe() 之后有 subscribeOn() 的话，它将执行在离它最近的 subscribeOn() 所指定的线程。**

关于这句话我有两点疑问：

* 默认情况下执行的线程是不是subscribe()发生的线程？
* 什么叫做离它最近的subscribeOn()指定的线程？

先撇开疑问，来看一下用法：

	Observable.just(url)    //IO线程
		.map(new Func1<String, Bitmap>() {
			@Override
			public Bitmap call(String s) {
				Log.i(" map ---> ", "执行");
				Log.i(" map ---> ", Thread.currentThread().getName());
				return GetBitmapForURL.getBitmap(s);
			}
		})
		.subscribeOn(Schedulers.io()) // 指定subscribe()发生在IO线程
		.doOnSubscribe(new Action0() { //需要在主线程中执行
			@Override
			public void call() {
				mainProgressBar.setVisibility(View.VISIBLE);
				Log.i(" doOnSubscribe ---> ", "执行");
				Log.i(" doOnSubscribe ---> ", Thread.currentThread().getName());
			}
		})
		.subscribeOn(AndroidSchedulers.mainThread()) // 指定subscribe()发生在主线程
		.observeOn(AndroidSchedulers.mainThread()) // 指定Subscriber的回调发生在主线程
		.subscribe(new Action1<Bitmap>() {
			@Override
			public void call(Bitmap bitmap) {
				mainImageView.setImageBitmap(bitmap);
				mainProgressBar.setVisibility(View.GONE);
				Log.i(" subscribe ---> ", "执行");
				Log.i(" subscribe ---> ", Thread.currentThread().getName());
			}
	});


下面这是执行的Log日志：

![Log日志](http://www.iamxiarui.com/wp-content/uploads/2016/06/log1.png)

可以看到，从 **onClick()** 触发后，先执行了 **doOnSubscribe()** 然后执行 **map()** ，最后执行绑定操作 **subscribe()** 。也就是说，它确实是在数据发送之前调用的，完全可以做初始化操作。

好了，现在我们来解决疑问，先解决第二点：**什么是最近的？** 将代码改成这样：

	...	
	.subscribeOn(Schedulers.newThread()) // 指定subscribe()发生在新线程
	.doOnSubscribe(new Action0() { //需要在主线程中执行
		@Override
		public void call() {
			Log.i(" doOnSubscribe ---> ", "执行");
			Log.i(" doOnSubscribe ---> ", Thread.currentThread().getName());
		}
	})
	.subscribeOn(Schedulers.io()) // 指定subscribe()发生在IO线程
	.subscribeOn(AndroidSchedulers.mainThread()) // 指定subscribe()发生在主线程
	...

我故意将 doOnSubscribe 写在两个 subscribeOn 之间，且后面有两个subscribeOn ，现在来看日志：

![Log3](http://www.iamxiarui.com/wp-content/uploads/2016/06/log3.png)

从日志明显可以看出，doOnSubscribe() 执行在IO线程，所以结论是：

> * **如果在doOnSubscribe()之后指定了subscribeOn()，它决定了doOnSubscribe()在哪种线程中执行。** 
     * **（1）doOnSubscribe()之前的subscribeOn()不会影响它。** 
     * **（2）doOnSubscribe()之后的subscribeOn()，且是最近的才会影响它。** 


再来看第二个疑问：**默认线程在哪里？** 将代码改成这样：

	...
	.subscribeOn(Schedulers.io()) // 指定subscribe()发生在IO线程
	.doOnSubscribe(new Action0() { //需要在主线程中执行
		@Override
		public void call() {
			Log.i(" doOnSubscribe ---> ", "执行");
			Log.i(" doOnSubscribe ---> ", Thread.currentThread().getName());
		}
	})
	.observeOn(Schedulers.io()) // 指定Subscriber的回调发生在io线程
	...

来看Log：

![Log4](http://www.iamxiarui.com/wp-content/uploads/2016/06/log4.png)

大家看到这个日志肯定会有疑问，我当时也非常有疑问，为什么subscribeOn() 与 observeOn() 都指定了IO线程，且 doOnSubscribe() 之后并没有 subscribeOn() ，这个时候它应该默认执行在 subscribe() 所在线程。

而 subscribe() 所在线程已经被 observeOn() 指定在了IO线程，所以此时它应该执行在IO线程才对啊，为什么还是 main 线程呢？

我找了翻看了WiKi，找了很多资料，甚至看了源码都没有找到是什么原因。

> **如果有人知道，请告诉我，谢谢！**

### doOnNext

由于from与flatmap操作符能发送多个数据，假设有这样的需求，需要在每个数据发送的时候提示一下，告诉我们又发了一个数据，那该如何做呢？

RxJava中给我们提供了一个操作符： **doOnNext()** ，这个操作符允许我们在每次输出一个元素之前做一些其他的事情，比如提示啊保存啊之类的操作。

具体用法很简单，如下图所示，这个代码也就是上面flatmap案例的完整代码：

	Observable.from(urls)
		.flatMap(new Func1<String, Observable<String>>() {
			@Override
			public Observable<String> call(String s) {
				return Observable.just(s);
			}
	    })
		.map(new Func1<String, Bitmap>() {
			@Override
			public Bitmap call(String s) {
				return GetBitmapForURL.getBitmap(s);
			}
		})
		.subscribeOn(Schedulers.io()) // 指定subscribe()发生在IO线程
		.observeOn(AndroidSchedulers.mainThread()) // 指定后面所发生的回调发生在主线程
		.doOnNext(new Action1<Bitmap>() {    //每运行一次所要执行的操作
			@Override
			public void call(Bitmap bitmap) {
				Toast.makeText(OtherActivity.this, "图片增加", Toast.LENGTH_SHORT).show();
			}
		})
		.subscribe(new Action1<Bitmap>() {
			@Override
			public void call(Bitmap bitmap) {
				//将获取到的Bitmap对象添加到集合中
				list.add(bitmap);
				//设置图片
				gvOther.setAdapter(new GridViewAdapter(OtherActivity.this, list));
				pbOther.setVisibility(View.GONE);
			}
		});
	}

来看运行的动态图：

![加载多张图片2](http://www.iamxiarui.com/wp-content/uploads/2016/06/加载多张图片2.gif)

可以看到，在每张图片的加载过程中都有弹窗提示图片增加，这就是doOnNext操作符的作用。

## 结语

好了，今天的全部内容都讲解完毕了。大部分都是用法，而这些用法与基础用法相比较起来都或多或少复杂了一点，所以我就将它称为中级运用。

跟前面的基础一样，用法讲完了就需要了解其原理了。所以后面的文章将会讲解一下 **变换** 的原理，仍然是通过图文的形式轻轻松松地去学。

而每次写文章过程中，都能发现自己学习过程中的理解不当或错误的地方，现在分享出来。但是肯定还会有不对的地方，所以希望大家如果有不同意见给予指正或与我交流，谢谢！

[大话RxJava：一、初识RxJava与基本运用](http://www.jianshu.com/p/856297523728)

[大话RxJava：二、轻松学源码之基础篇](http://www.jianshu.com/p/288a52370e4c)

### 参考资料

[给 Android 开发者的 RxJava 详解](http://gank.io/post/560e15be2dca930e00da1083#toc_14)

[RxJava文档和教程](https://mcxiaoke.gitbooks.io/rxdocs/content/Topics.html)

[深入浅出RxJava(二：操作符)](http://blog.csdn.net/lzyzsd/article/details/44094895)
