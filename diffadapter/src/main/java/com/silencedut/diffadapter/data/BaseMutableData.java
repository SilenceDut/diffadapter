package com.silencedut.diffadapter.data;


import android.os.Bundle;
import android.support.annotation.NonNull;

import com.silencedut.diffadapter.IProvideItemId;
import com.silencedut.diffadapter.utils.UpdatePayloadFunction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author SilenceDut
 * @date 2018/9/6
 * 直接实现BaseMutableData的在显示期间内容可能会变化，如异步读取头像，昵称，等级等异步获取等，
 */

public abstract class BaseMutableData<T extends BaseMutableData> implements IProvideItemId {
    private Set<Object> mMathFeature = new HashSet<>();
    private Bundle mPayloadBundle = new Bundle();

    /**
     * 通过一个列表里的数据独一无二的特征来判断是不是同一个Item，如uid，消息id等
     * 这个方法很重要，用来处理DiffUtil里的areItemsTheSame方法，可以减少很多不必要的updateItem调用，不能简单的根据view类型来判断
     *
     * @return 是不是同一个item
     */
    @NonNull
    public abstract Object uniqueItemFeature();

    /**
     * 判断新旧数据对UI是否影响, 即使不同的数据但UI不需要更新也返回true
     * 当areUISame结果为true时，不刷新item.当为false时，如果复写了{@link #getDiffPayload(BaseMutableData)}
     * ,则{@link com.silencedut.diffadapter.holder.BaseDiffViewHolder#updatePartWithPayload}会被调用，否则
     * {@link com.silencedut.diffadapter.holder.BaseDiffViewHolder#updateItem(BaseMutableData, int)}被调用
     * @param data 需要对比的数据
     * @return 是否需要跟新UI
     */
    public abstract boolean areUISame(@NonNull T data);

    /**
     * payload 方式 更新item，可实现对Item的局部刷新
     * @param newData 新数据
     * @return 旧数据和新数据需要改变的部分，
     */
    public final @NonNull Bundle getDiffPayload(@NonNull T newData){
        mPayloadBundle.clear();
        appendDiffPayload(newData,mPayloadBundle);
        return mPayloadBundle;
    }

    /**
     * 用于全量数据对比时对Item进行局部更新
     * 如果在一个页面不会多次调用{@link com.silencedut.diffadapter.DiffAdapter#setDatas(List)}或者不使用payload更新方式，可不实现此方法
     * 单个数据局部更新的方式用use {@link UpdatePayloadFunction},
     */
    public void appendDiffPayload(@NonNull T newData,@NonNull Bundle diffPayloadBundle){

    }


    /**
     * 提供一个或多个用来匹配列表中所有的需要变化的数据的特征，也就是找出数据中包含该mMathFeature里的特征的数据，
     * 和{@link com.silencedut.diffadapter.utils.UpdateFunction()}结合使用
     * 一般情况下可用{@link #uniqueItemFeature()}
     * 但有些情况不同，即匹配的数据不止一条，如聊天里同一个uid不止一条数据，{@link #uniqueItemFeature()}就只能用msgId之类的， 这时可按需复写这个方法
     * @return 用来匹配变化的数据的特征，最常用的比如uid
     */
    @NonNull
    public final Set<Object> matchChangeFeatures(){
        appendMatchFeature(mMathFeature);
        return mMathFeature;
    }

    public void appendMatchFeature(@NonNull Set<Object> allMatchFeatures) {
        allMatchFeatures.add(uniqueItemFeature());
    }


}
