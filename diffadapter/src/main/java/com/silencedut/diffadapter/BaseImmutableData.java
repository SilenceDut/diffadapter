package com.silencedut.diffadapter;

/**
 * @author SilenceDut
 * @date 2018/9/30
 * 继承BaseImmutableData的数据在显示期间内容不会发生变化，每次显示的都是从网络或者数据库直接解析创建的新的对象
 */
public abstract class BaseImmutableData<T extends BaseImmutableData>  implements BaseMutableData<T> {


    @Override
    public T copyData() {
        return (T) this;
    }
}
