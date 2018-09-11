package com.silencedut.diffadapter;

import com.duowan.makefriends.annotation.Attribute;
import com.duowan.makefriends.annotation.DontProguard;
import com.duowan.makefriends.annotation.Extend;
import com.duowan.makefriends.framework.adapter.IProvideItemId;

/**
 *
 * @author SilenceDut
 * @date 16/10/19
 */


public interface BaseImmutableData<T extends BaseImmutableData> extends IProvideItemId {

    /**
     * 在业务逻辑上判断是不是同一个Item，比如uid，或者消息Id等
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
