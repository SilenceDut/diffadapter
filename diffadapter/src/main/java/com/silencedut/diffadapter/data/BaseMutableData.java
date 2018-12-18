package com.silencedut.diffadapter.data;


import android.support.annotation.NonNull;

import com.silencedut.diffadapter.IProvideItemId;

/**
 *
 * @author SilenceDut
 * @date 2018/9/6
 * 直接实现BaseMutableData的数据在显示期间内容可能会变化，如异步读取头像等，需自行实现copy接口
 */

public interface BaseMutableData<T extends BaseMutableData> extends IProvideItemId {



    /**
     * 提供一个独一无二特征
     * 比如uid，或者消息Id等
     * 这个方法很重要，用来处理DiffUtil里的areItemsTheSame方法，可以减少很多不必要的updateItem调用，不能简单的根据view类型来判断

     * @return 用来区别列表里不同数据的特征
     */
    @NonNull
    Object uniqueFeature();

    /**
     * 判断新旧数据对UI是否影响
     * @param newData 新数据
     * @return 是否需要跟新UI
     */
    boolean areUISame(@NonNull T newData);

    /**
     * 创建一个新对象，拷贝当前的数据，用于DiffUtil的拷贝
     * @return 新的对象
     */
    T copyData();
}
