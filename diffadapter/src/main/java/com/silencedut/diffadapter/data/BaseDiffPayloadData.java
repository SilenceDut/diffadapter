package com.silencedut.diffadapter.data;

/**
 * @author SilenceDut
 * @date 2018/9/6
 */
public abstract class BaseDiffPayloadData<T> extends BaseMutableData {
    /**
     * payload 方式处理数据变化
     * @param newData 新数据
     * @return 改变的部分
     */
    public abstract Object getPayload(T newData);
}
