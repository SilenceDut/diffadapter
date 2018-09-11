package com.silencedut.diffadapter;

/**
 * @author SilenceDut
 * @date 2018/9/6
 */
public interface BaseDiffPayloadData<T> extends BaseImmutableData {
    /**
     * payload 方式处理数据变化
     * @param newData 新数据
     * @return 改变的部分
     */
    Object getPayload(T newData);
}
