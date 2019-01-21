package com.silencedut.diffadapter.data;


import android.os.Bundle;
import android.support.annotation.NonNull;

import com.silencedut.diffadapter.IProvideItemId;

import java.util.HashSet;
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
     * 仅当新旧数据的uniqueItemFeature相同时才会进行 {@link #areUISame( T)}的判断
     * 这个方法很重要，用来处理DiffUtil里的areItemsTheSame方法，可以减少很多不必要的updateItem调用，不能简单的根据view类型来判断
     * @return 是不是同一个item
     */
    @NonNull
    public abstract Object uniqueItemFeature();

    /**
     * 判断新旧数据对UI是否影响, 仅当新旧数据的uniqueItemFeature相同时才会进行 {@link #areUISame( T)}的判断
     * @param data 需要对比的数据
     * @return 是否需要跟新UI
     */
    public abstract boolean areUISame(@NonNull T data);

    /**
     * payload 方式 更新item，可实现对单个Item的局部刷新
     * @param newData 新数据
     * @return 旧数据和新数据需要改变的部分，
     */
    public final @NonNull Bundle getDiffPayload(@NonNull T newData){
        mPayloadBundle.clear();
        appendDiffPayload(newData,mPayloadBundle);
        return mPayloadBundle;
    }

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
