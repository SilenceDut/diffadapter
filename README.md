## Diffadapter [![](https://jitpack.io/v/silencedut/diffadapter.svg)](https://jitpack.io/#silencedut/diffadapter)
一款针对RecyclerView高效刷新，多类型列表，异步数据更新，崩溃等各种复杂难处理场景的高性能易用的列表库

### Demo
[diffadapter.apk](https://github.com/SilenceDut/diffadapter/blob/master/media/diffadapter.apk)

图像url,名称，价格都是异步或者通知变化的数据

![image](https://github.com/SilenceDut/diffadapter/blob/master/media/demo.gif)

## Introduce
[如何实现一个高效、高性能的、异步数据实时刷新的列表](http://www.silencedut.com/2019/01/24/%E5%A6%82%E4%BD%95%E4%BC%98%E9%9B%85%E7%9A%84%E5%AE%9E%E7%8E%B0%E4%B8%80%E4%B8%AA%E9%AB%98%E6%95%88%E3%80%81%E9%AB%98%E6%80%A7%E8%83%BD%E3%80%81%E5%BC%82%E6%AD%A5%E6%95%B0%E6%8D%AE%E5%AE%9E%E6%97%B6%E5%88%B7%E6%96%B0%E7%9A%84%E5%88%97%E8%A1%A8/)

diffadapter就是根据实际项目中各种复杂的列表需求，同时为了解决DiffUtil使用不方便，容易出错而实现的一个**高效，高性能的列表库
，侵入性低，方便接入**，致力于将列表需求的开发精力用于具体的Item Holder上，而不用花时间在一些能通用的和业务无关的地方。
使用DiffUtil作为来做最小更新，屏蔽外部调用**DiffUtil**的接口。无需自行实现DiffUtil,只用实现简单的数据接口和展示数据的Holder，
不用自己去实现Adapter来管理数据和Holder之间的关系，不用考虑DiffUtil的实现细节,就能快速的开发出一个高性能的复杂列表需求。

## Feature

* 无需自己实现Adapter,简单配置就可实现没有各种if-else判断类型的多Type视图列表
* 使用DiffUtil来找出最小需要更新的Item集合，使用者无需做任何DiffUtil的配置即可实现高效的列表
* 提供方便，稳定的更新、删、插入、查询方法，适用于各种非常频繁，复杂的场景（如因为异步或通知的原因同时出现插入，删除，全量设置的情况）
* 更友好方便的异步数据更新方案

## Using

### 基本用法

**Step 1：继承`BaseMutableData`，主要实现`areUISame(newData: AnyViewData)` 和 `uniqueItemFeature()`**

```kotlin
class AnyViewData(var id : Long ,var any : String) : BaseMutableData<AnyViewData>() {

    companion object {
         //数据展示的layout，也是和Holder一一对应的唯一特征
         const val VIEW_ID = R.layout.holder_skins
    }

    override fun getItemViewId(): Int {
       
        return VIEW_ID
    }
    
    override fun areUISame(newData: AnyViewData): Boolean {
        // 判断新旧数据是否展示相同的UI,如果返回True,则表示UI不需要改变，不会updateItem
       
        return this.any == newData.any
    }

    override fun uniqueItemFeature(): Any {
        // 返回可以标识这个Item的特征，比如uid,id等,用来做UI差分已经可以动态
        return this.id
    }
    
}
```

**Step 2：继承`BaseDiffViewHolder<T extends BaseMutableData>`，泛型类型传入上面定义的`AnyViewData`**

```kotlin
class AnyHolder(itemView: View, recyclerAdapter: DiffAdapter): BaseDiffViewHolder<AnyViewData>( itemView,  recyclerAdapter){
    
    override fun getItemViewId(): Int {
        return AnyViewData.VIEW_ID
    }


    override fun updateItem(data: AnyViewData, position: Int) {
        根据AnyViewData.VIEW_ID对应的layout来更新Item
        Log.d(TAG,"updateItem $data")
    }
}
```

**Step 3：注册，显示到界面**

```kotlin
val diffAdapter = DiffAdapter(this)

//注册类型，不分先后顺序
diffAdapter.registerHolder(AnyHolder::class.java, AnyViewData.VIEW_ID)
diffAdapter.registerHolder(AnyHolder2::class.java, AnyViewData2.VIEW_ID)
diffAdapter.registerHolder(AnyHolder3::class.java, AnyViewData3.VIEW_ID)

val linearLayoutManager = LinearLayoutManager(this)
recyclerView.layoutManager = linearLayoutManager
recyclerView.adapter = diffAdapter

//监听数据变化

fun onDatached(datas : List<BaseMutableData<*>>) {
    diffAdapter.datas = adapterListData
}

```

只需要上面几步，就可以完成如类似下图的多type列表,其中数据源里的每个BaseMutableData的getItemViewId()决定着用哪个Holder展示UI。
(以上均用`kotlin`实现，`Java`使用不受任何限制)

![](https://ws3.sinaimg.cn/large/006tNc79gy1fzbiv9fcc5j30gq19ywq2.jpg)

### 增、插入、删除、修改（更新）

```java
public <T extends BaseMutableData> void addData(T data) 

public void deleteData(BaseMutableData data)

public void deleteData(int startPosition, int size)

void insertData(int startPosition ,List<? extends BaseMutableData> datas)

public void updateData(BaseMutableData newData)
```
上述接口在调用的时机，频率都很复杂的场景下也不会引起崩溃

使用updateData(BaseMutableData newData)时，newData可以是新new的对象，也可以是修改后的原对象，不会出现[使用DiffUtil更新单个数据无效](http://www.silencedut.com/2019/01/24/%E5%A6%82%E4%BD%95%E4%BC%98%E9%9B%85%E7%9A%84%E5%AE%9E%E7%8E%B0%E4%B8%80%E4%B8%AA%E9%AB%98%E6%95%88%E3%80%81%E9%AB%98%E6%80%A7%E8%83%BD%E3%80%81%E5%BC%82%E6%AD%A5%E6%95%B0%E6%8D%AE%E5%AE%9E%E6%97%B6%E5%88%B7%E6%96%B0%E7%9A%84%E5%88%97%E8%A1%A8/)
的问题

基本上就提供了上述很少的几个接口，主要是为了功能更清晰，侵入性更低，你可以根据自己的需要组合更多的功能，像下拉刷新，动画等。

### 高阶用法

基本用法中**Data和Holder绑定的模式并没什么特殊之处，早在两年前的项目[KnowWeather](https://github.com/SilenceDut/KnowWeather)就已经用上这种思想，现在只是结合DiffUtil以及其他的疑难问题解决方案将其开源，diffadapter最核心的地方在于高性能和异步获取数据或者通知数据变化时列表的更新上**


#### 多数据源异步更新

![](https://ws3.sinaimg.cn/large/006tNc79gy1fz73pvxepdj30qc046ac1.jpg)
以一个类似的Item为例，这里认为服务器返回的数据列表只包含uid，也就是`List<Long> uids`，个人资料，等级，贵族等都属于不同的协议。下面展示的是异步获取个人资料展示的头像和昵称的情况，其他的可以类比。

**Step 1:定义ViewData**

```kotlin
data class ItemViewData(var uid:Long, var userInfo: UserInfo?, var anyOtherData: Any ...) : BaseMutableData<ItemViewData>() {

    companion object {
        const val VIEW_ID = R.layout....
    }

    override fun getItemViewId(): Int {
        return VIEW_ID
    }

    override fun areUISame(newData: UserInfo): Boolean {
        return this.userInfo?.portrait == newData.userInfo?.portrait && this.userInfo?.nickName == newData.userInfo?.nickName && this.anyOtherData == newData.anyOtherData
    }

    override fun uniqueItemFeature(): Any {
       return this.uid
    }

}
```
数据类ItemViewData包含所有需要显示到Item上的信息，这里只处理和个人资料相关的数据，`anyOtherData: Any ...`表示Item所需的其他数据内容

BaseMutableData里有个默认的方法`allMatchFeatures(@NonNull Set<Object> allMatchFeatures)`，不需要显示调用，这里当外部有异步数据变化时，提供当前BaseMutableData用来匹配变化的异步数据的对象

```java
public void appendMatchFeature(@NonNull Set<Object> allMatchFeatures) {
    allMatchFeatures.add(uniqueItemFeature());
}
```

默认添加了uniqueItemFeature(),allMatchFeatures是个Set，可以重写方法添加多个用来匹配的特征。


**Step 2:定义View Holder，同基本用法**

**Step 3:监听数据变化，更新列表**

```kotlin
//用于监听请求的异步数据，userInfoData变化时与此相关的数据
private val userInfoData = MutableLiveData<UserInfo>()

//在adapter里监听数据变化
diffAdapter.addUpdateMediator(userInfoData, object : UpdateFunction<UserInfo, ItemViewData> {
    override fun providerMatchFeature(input: UserInfo): Any {
        return input.uid
    }

    override fun applyChange(input: UserInfo, originalData: ItemViewData): ItemViewData {
        
       return originalData.userInfo = input
        
    }
})

// 任何通知数据获取到的通知
fun asyncDataFetch(userInfo : UserInfo) {
    userInfoData.value = userInfo
}

```
这样当asyncDataFetch接收到数据变化的通知的时候，改变userInfoData的值，adapter里对应的Item就会更新。其中找到adapter中需要更新的Item是关键部分，主要由实现`UpdateFunction`来完成，实现`UpdateFunction`也很简单。

```java
interface UpdateFunction<I,R extends BaseMutableData> {

    /**
     * 匹配所有数据，及返回类型为R的所有数据
     */
    Object MATCH_ALL = new Object();

    /**
     * 提供一个特征，用来查找列表数据中和此特征相同的数据
     * @param input 用来提供查找数据和最终改变列表的数据
     * @return 用来查找列表中的数据的特征项
     */
    Object providerMatchFeature(@NonNull I input);

    /**
     * 匹配到对应的数据，如果符合条件的数据有很多个，可能会被回调多次
     * @param input 是数据改变的部分数据源
     * @param originalData 需要改变的数据项
     * @return 改变后的数据项
     */
    R applyChange(@NonNull I input,@NonNull R originalData);

}
```
`UpdateFunction`用来提供异步数据获取到后数据用来和列表中的数据匹配的规则和根据规则找到需要更改的对象后如果改变原对象，剩下的更新都由`diffadapter`来处理。如果符合条件的数据有很多个，`applyChange(@NonNull I input,@NonNull R originalData)`会被回调多次。如下时：

```java
Object providerMatchFeature(@NonNull I input) {
    return UpdateFunction.MATCH_ALL
}
```
`applyChange`回调的次数就和列表中的数据量一样多。

如果同一种匹配规则`providerMatchFeature`对应多种Holder类型，`UpdateFunction<I,R>`的返回数据类型R就可以直接设为基类的`BaseMutableData`，然后再applyChange里在具体根据类型来处理不同的UI。

### 最高效的Item局部更新方式 —— payload

DiffUtil 能让一个列表中只更新部分变化的Item,payload能让同一个Item只更新需要变化的View,这种方式非常适合同一个Item有多个异步数据源的,同时又对性能有更高要求的列表。

**Step 1:重写BaseMutableData的appendDiffPayload**

```kotlin
data class ItemViewData(var uid:Long, var userInfo: UserInfo?, var anyOtherData: Any ...) : BaseMutableData<ItemViewData>() {

    companion object {
        const val KEY_BASE_INFO = "KEY_BASE_INFO"
        const val KEY_ANY = "KEY_ANY"
    }
    
    ...
    
    /**
     * 最高效的更新方式，如果不是频繁更新的可以不实现这个方法
     */
    override fun appendDiffPayload(newData: ItemViewData, diffPayloadBundle: Bundle) {
        super.appendDiffPayload(newData, diffPayloadBundle)
        if(this.userInfo!= newData.userInfo) {
            diffPayloadBundle.putString(KEY_BASE_INFO, KEY_BASE_INFO)
        }
        if(this.anyData != newData.anyData) {
            diffPayloadBundle.putString(KEY_ANY, KEY_ANY)
        }
        ...
    }
}
```

默认用Bundle存取变化，无需存具体的数据，只需类似设置标志位，表明Item的哪部分数据发生了变化。

**Step 2 :需要重写BaseDiffViewHolder里的`updatePartWithPayload`**

```kotlin
class ItemViewHolder(itemViewRoot: View, recyclerAdapter: DiffAdapter): BaseDiffViewHolder<ItemViewData>( itemViewRoot,  recyclerAdapter){
     
    override fun updatePartWithPayload(data: ItemViewData, payload: Bundle, position: Int) {

    if(payload.getString(ItemViewData.KEY_BASE_INFO)!=null) {
        updateBaseInfo(data)
    }

    if(payload.getString(ItemViewData.KEY_ANY)!=null) {
        updateAnyView(data)
    }
}
```
根据变化的标志位，更新Item中需要变化部分的View

## More

一些探讨：

1. 为什么没有提供类似onItemClickLisener用来处理点击事件的接口

     不是因为不好实现，其实现实起来非常简单。首先尝试去理解为什么RecyclerView.Adapter 没有提供像listview那样的点击事件的listener，我的理解是大而全的公用点击监听不是一个好的设计方式，尤其对于多类型的view来说，因为点击的是不同的holder，要在回调里根据类型来处理不同的逻辑，少不了各种`if-else`的代码块，不同holder相关的数据，逻辑耦合到一块，试想如果有四五种类型，处理统一点击回调的地方是多大的一块代码，后期的维护又是一个问题。我认为好的方式应该是在各自的holder的构造函数里来各自处理，每个holder都有自己的数据和类型，很好的隔离开不同类型数据的耦合，每个holder各司其职：显示数据，监听点击，维护方便。

2. 为什么没有下拉刷新、加载更多、动画、分割线等更多的功能

    首先diffadapter主要就是为了提供高性能刷新，异步数据更新，高效的配置多类型列表的功能，这也是绝大多数列表最常见的功能，像上面说的那些功能以及onItemClickLisener都是一些额外的添加项，不想做一个为了看起来更多功能但没有任何难度，堆积代码的开源库，不想为了看起来大而全来吸引别人使用。就是职责很单一，目的很明确，diffadapter侵入性很低，不影响任何其他功能的引入，包括不限于上面提到的那些。而且上面提到的那些都有很多很好的开源库，你可以根据任何自己的需要来定制。
    

**更详细，多样的使用方式和细节见[diffadapter demo](https://github.com/SilenceDut/diffadapter)**，有详细的demo和使用说明，demo用kotlin实现，使用了**mvvm**和**模块化**的框架方式。

这种方式也是目前能想到的比较好的异步数据更新列表的方式，非常欢迎一起探讨更多的实现方式。

## 引入

**Step1.Add it in your root add build.gradle at the end of repositories:**

```java
allprojects {
	repositories {
		..
		maven { url 'https://jitpack.io' }
	}
}
```


**Step2. Add the dependency:**

```java
dependencies {
    implementation 'com.github.silencedut.diffadapter:latestVersion'
}
```

**ProGuard**

```java
-keep class * extends com.silencedut.diffadapter.holder.BaseDiffViewHolder {*;}
-keep class * extends com.silencedut.diffadapter.data.BaseMutableData {*;}
```

## License


```
Copyright 2017-2018 SilenceDut

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
