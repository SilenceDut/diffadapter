package com.silencedut.diffadapter.utils;


import android.os.Bundle;
import android.support.annotation.NonNull;

import com.silencedut.diffadapter.data.BaseMutableData;

import java.util.Set;

/**
 * @author SilenceDut
 * @date 2019-06-18
 * 用来动态跟新单个数据项
 */
public abstract class UpdatePayloadFunction<I, R extends BaseMutableData> implements UpdateFunction<I,R > {

    /**
     * 匹配到对应的数据，如果符合条件的数据有很多个，可能会被回调多次，不需要新建对象，主需要根据Input把originalData改变相应的值就行了
     * @param input 是数据改变的部分数据源
     * @param originalData 需要改变的数据项
     * @param payloadKeys 用来标识改变后的数据哪些部分发生了改变，if payloadKeys is not empty  ,
     * {@link com.silencedut.diffadapter.holder.BaseDiffViewHolder#updatePartWithPayload(BaseMutableData, Bundle, int)}
     *                    will be call rather than
     * {@link com.silencedut.diffadapter.holder.BaseDiffViewHolder#updateItem(BaseMutableData, int)}
     * @return 改变后的数据项,
     *
     */

     public abstract R applyChange(@NonNull I input, @NonNull R originalData, @NonNull Set<String> payloadKeys);

    @Override
    @Deprecated
    public R applyChange(@NonNull I input, @NonNull R originalData){
         return null;
    }

}
