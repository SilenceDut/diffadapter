package com.silencedut.diffadapter.data;

/**
 * @author SilenceDut
 * @date 2018/9/30
 * 继承BaseImmutableData的数据在显示期间当前数据对象不会改变，每次改变都会创建新的数据对象，比如从网络或者数据库直接解析创建的新的对象
 */
public abstract class BaseImmutableData<T extends BaseImmutableData>  extends BaseMutableData<T> {

    @Override
    public T copyData() {
        return (T) this;
    }

}
