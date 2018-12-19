package com.silencedut.diffadapter.utils;

import com.silencedut.diffadapter.data.BaseImmutableData;
import com.silencedut.diffadapter.data.BaseMutableData;

/**
 * @author SilenceDut
 */
public interface UpdateFunction<I,R extends BaseMutableData> {


    /**
     * 提供一个特征，用来查找列表数据中和此特征相同的数据
     * @param input 用来提供查找数据和最终改变列表的数据
     * @return 同 <p> {@link BaseMutableData # matchChangeFeature() }
     */
    Object providerMatchFeature(I input);

    /**
     * 只需关心自己需要更改的部分
     * @param input 是数据改变的部分数据源
     * @param originalCopyData 需要改变的数据项
     * @return 改变后的数据项，可以是新new的对象对应<p> {@link BaseImmutableData  ，也可以是在原对象进行修改后的对象<p> {@link BaseMutableData
     */
    R applyChange(I input, R originalCopyData);

}
