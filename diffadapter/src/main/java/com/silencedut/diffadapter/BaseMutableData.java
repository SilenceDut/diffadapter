package com.silencedut.diffadapter;


/**
 *
 * @author SilenceDut
 * @date 2018/9/6
 * 直接实现BaseMutableData的数据在显示期间内容可能会变化，如异步读取头像等，需自行实现copy接口
 */

public interface BaseMutableData<T extends BaseMutableData> extends IProvideItemId {

    /**
     * 在业务逻辑上判断是不是同一个Item，比如uid，或者消息Id等，这个方法很重要
     * 这个方法很重要可以减少很多不必要的updateItem调用，不能简单的根据view类型来判断
     * @param newData 新数据
     * @return 是否是同一个Item
     */
    boolean areSameItem(T newData);

    /**
     * 判断新旧数据对UI是否影响
     * @param newData 新数据
     * @return 是否需要跟新UI
     */
    boolean areUISame(T newData);

    /**
     * 创建一个新对象，拷贝当前的数据，用于DiffUtil的拷贝
     * @return 新的对象
     */
    T copyData();
}
