# Android：学会超好用的图表控件 HelloCharts

## 写在前面

很早以前就想学习一些图表控件的实现，但一直被耽搁。现在毕设项目里有一个统计模块，所以不得不学了。找控件没花多大力气，不过确实在 **MPAndroidChart** 和 **HelloCharts** 之间犹豫了一会。不过简单对比来看，还是 **HelloCharts** 比较容易实现，也有很多可定义的模块/属性/效果。所以就选择了学习 **HelloCharts** 。

[开源库：hellocharts-android](https://github.com/lecho/hellocharts-android)

[开发者：lecho](https://github.com/lecho)

开源库开发者 **lecho** 给我们提供了一个 **Sample** 代码，同时也提供了一个 **Google Play** 的 **APP** ，也可在[豌豆荚](http://www.wandoujia.com/search?key=HelloCharts)进行下载。

我下载使用了一下，作者功能写的很丰富，但是好像还有很多新功能或效果没有加上。而且作者已经很久很久没有维护了，示例代码写的比较杂，注释也比较少，学起来比较费劲。

因为学习这种开源控件类的东西，最好的方式就是动手实现每一个属性/效果/功能。所以我就仿照他的 **Demo** ，自己也写了一个类似的 **Demo** ，刚刚写完了基本和高级的使用，也学会了如何使用它。

> Github 动图有问题 看效果 [点击这里](http://www.jianshu.com/p/c39dd0f74e8b)

为了方便还有其他人想学这个，我几乎全部重写了代码以及详细注释，如果还有想学习使用这个好用的图表控件的人，建议用我这个示例代码去学习，而且项目还在不断维护，实战应用场景也在添加中，一起去学如何在真正的项目中使用它。

此外，如果有同学学会并自己写了一个实战场景，那么

> **Welcome to Pull Requests !**

## 必读说明

* 本项目仅供参考学习使用；
* 本项目参考源码开发者示例 **Demo** ，有点中文版的意味；
* 除新增功能/应用场景外，其余案例均在源示例代码上改写；
* 重构原示例代码中 80% 内容，更简洁更清晰；
* 添加大量新属性/效果/功能/应用场景；
* 暂时删减部分不常用的控件/功能；
* 修正源示例代码中的部分错误；
* 添加详细规范中文注释；
* 项目会持续维护，如添加不同应用场景或新功能等；
* 软件运行若有异常问题，欢迎指正；
* 如有建议或意见，欢迎交流。

[项目 Github 地址](https://github.com/IamXiaRui/Android_5.0_ViewDemo/tree/master/HelloChartsDemo)

[项体验 APP 下载地址（至少 Android 5.0 以上）](http://www.iamxiarui.com/?dl_id=2)

### 一些图

![](http://www.iamxiarui.com/wp-content/uploads/2016/09/pic_main.jpg)

![](http://www.iamxiarui.com/wp-content/uploads/2016/09/pic_basic.jpg)

![](http://www.iamxiarui.com/wp-content/uploads/2016/09/pic_line.jpg)

![](http://www.iamxiarui.com/wp-content/uploads/2016/09/pic_high.jpg)

![](http://www.iamxiarui.com/wp-content/uploads/2016/09/pic_combo.jpg)

![](http://www.iamxiarui.com/wp-content/uploads/2016/09/pic_pre_column.jpg)

![](http://www.iamxiarui.com/wp-content/uploads/2016/09/pic_use.jpg)
