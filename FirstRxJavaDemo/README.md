#大话RxJava：一、初识RxJava与基本运用


##写在前面

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
* 项目源码


##初识RxJava

###什么是Rx

很多教程在讲解RxJava的时候，上来就介绍了什么是RxJava。这里我先说一下什么是Rx，Rx就是ReactiveX，官方定义是：

> **Rx是一个函数库，让开发者可以利用可观察序列和LINQ风格查询操作符来编写异步和基于事件的程序**

![一脸懵B](http://www.iamxiarui.com/wp-content/uploads/2016/06/一脸懵B-2.png)

看到这个定义我只能呵呵，稍微通俗点说是这样的：

> **Rx是微软.NET的一个响应式扩展。Rx借助可观测的序列提供一种简单的方式来创建异步的，基于事件驱动的程序。**

这个有点清晰了，至少看到我们熟悉的**异步**与**事件驱动**，所以简单点且不准确地来说：

> **Rx就是一种响应式编程，来创建基于事件的异步程序**

注意，这个定义是不准确的，但是对于初学者来说，已经可以有个基本的认知了。

另外还有一点就是Rx其实是一种编程思想，用很多语言都可以实现，比如RxJava、RxJS、RxPHP等等。而现在我们要说的就是RxJava。

###RxJava是什么

二话不说，先上定义：

> **RxJava就是一种用Java语言实现的响应式编程，来创建基于事件的异步程序**

有人问你这不是废话么，好吧那我上官方定义：

> **一个在 Java VM 上使用可观测的序列来组成异步的、基于事件的程序的库**

反正我刚看这句话的时候也呵呵了，当然现在有所领悟了。

除此之外，扔物线老师总结的就更精辟了：**异步**，它就是一个实现异步操作的库。

###扩展的观察者模式

对于普通的观察者模式，这里我就不细说了。简单概括就是，**观察者（Observer）**需要在**被观察者(Observable)**变化的一瞬间做出反应。

而两者通过**注册（Register）**或者**订阅(Subscribe)**的方式进行绑定。

就拿抛物线老师给的例子来说，我丰富了一下如图所示：

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

另外，关于RxJava的回调事件，抛物线老师总结的很好，我就不班门弄斧了：

* onNext()：基本事件。
* onCompleted(): 事件队列完结。RxJava 不仅把每个事件单独处理，还会把它们看做一个队列。RxJava 规定，当不会再有新的 onNext() 发出时，需要触发 onCompleted() 方法作为标志。
* onError(): 事件队列异常。在事件处理过程中出异常时，onError() 会被触发，同时队列自动终止，不允许再有事件发出。

值得注意的是在一个正确运行的事件序列中, onCompleted() 和 onError() 有且只有一个，并且是事件序列中的最后一个。如果在队列中调用了其中一个，就不应该再调用另一个。

好了，那我们也附一张图对比一下吧：

![RxJava观察者模式](http://www.iamxiarui.com/wp-content/uploads/2016/06/RxJava观察者模式.png)

##如何实现RxJava

关于实现RxJava的步骤，扔物线老师已经说的非常好了，这里我就大体总结概括一下。

###创建Observer

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


###创建Observable

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

###订阅（Subscribe）

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


##线程控制——Scheduler

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

##第一个RxJava案例

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


##总结

好了，由于本文是一个RxJava的基础，所以篇幅稍微过长了点。即使这样，很多细节性问题都没有交代清楚。但所幸的是，本文已经将RxJava必要的基础入门知识讲解完了。

在后期的文章中，主要是对RxJava的源码进行必要的分析，以及RxJava中各种操作符（比如常用的map、from、just、take、flatmap等等）的使用方式进行讲解。

跟现有的博客或者教程不一样的是，我不打算直接用理论知识来讲解，而是会用一个具体的项目来完成一系列的说明。

其实在写博客的时候，对自己所学也是一个复习的过程，能发现之前学习过程中所疏忽的问题。但由于技术水平有限，文中难免会有错误或者疏忽之处，欢迎大家指正与交流。

最后感谢扔物线老师的文章，写的真的是太好了。

##参考资料

[给 Android 开发者的 RxJava 详解——扔物线](http://gank.io/post/560e15be2dca930e00da1083)

[深入浅出RxJava（一：基础篇）](http://blog.csdn.net/lzyzsd/article/details/41833541)

[ReactiveX 的理念和特点](http://www.open-open.com/lib/view/open1440166491833.html)
