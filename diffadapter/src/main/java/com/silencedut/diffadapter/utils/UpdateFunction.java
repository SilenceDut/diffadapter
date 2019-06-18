package com.silencedut.diffadapter.utils;

import android.support.annotation.NonNull;

import com.silencedut.diffadapter.data.BaseMutableData;

/**
 * @author SilenceDut
 * use {@link UpdatePayloadFunction}
 */
@Deprecated
public interface UpdateFunction<I,R extends BaseMutableData> {

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
