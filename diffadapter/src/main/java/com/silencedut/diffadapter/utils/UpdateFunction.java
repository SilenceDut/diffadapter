package com.silencedut.diffadapter.utils;

import com.silencedut.diffadapter.data.BaseImmutableData;
import com.silencedut.diffadapter.data.BaseMutableData;

/**
 * @author SilenceDut
 */
public interface UpdateFunction<I,R extends BaseMutableData> {


    /**
     * 提供能在列表中找到需要改变的数据的独一无二的特征
     * @param input 改变的部分数据
     * @return 同 <p> {@link BaseMutableData # uniqueFeature}
     */
    Object providerUniqueFeature(I input);

    /**
     * 只需关心自己需要更改的部分
     * @param input 是数据改变的部分数据源
     * @param originalData 需要改变的数据项
     * @return 改变后的数据项，可以是新new的对象对应<p> {@link BaseImmutableData  ，也可以是在原对象进行修改后的对象<p> {@link BaseMutableData
     */
    R apply(I input,R originalData);

}
