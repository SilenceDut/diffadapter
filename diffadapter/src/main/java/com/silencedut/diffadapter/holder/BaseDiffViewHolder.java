package com.silencedut.diffadapter.holder;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.silencedut.diffadapter.DiffAdapter;
import com.silencedut.diffadapter.IProvideItemId;
import com.silencedut.diffadapter.data.BaseMutableData;
import com.silencedut.diffadapter.utils.DiffModelProvider;

/**
 *
 * @author SilenceDut
 * @date 2018/9/6
 *
 * implement BaseViewInit interface is convenient to generate views on ViewHolder
 *
 * 建议逻辑写到对应的ViewModel来处理
 */

public abstract class BaseDiffViewHolder<T extends BaseMutableData> extends RecyclerView.ViewHolder implements IProvideItemId {

    protected DiffAdapter mBaseAdapter;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private T mData;

    private void setUIContext(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    protected LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    protected Context getContext() {
        return mContext;
    }


    public BaseDiffViewHolder(View itemView, DiffAdapter baseRecyclerAdapter) {
        super(itemView);
        mBaseAdapter = baseRecyclerAdapter;
        setUIContext(mBaseAdapter.mContext);
    }

    public final void update(@NonNull T data, int position) {
        this.mData = data;
        updateItem(data,position);
    }

    /**
     * default update item way , which payload is empty
     * @param data
     * @param position
     */
    public abstract void updateItem(@NonNull T data, int position);

    /**
     * if payload is not empty  , this method will be call rather than {@link #updateItem(T, int)}
     * @param data
     * @param position
     */
    public void updatePartWithPayload(T data, @NonNull Bundle payload, int position){

    }

    protected final T getData() {
        return mData;
    }


    /**
     * 获取当前处理逻辑的ViewModel,如果supportFragment的状态已经错误，将ViewModel attach到跟activity
     */
    public <V extends ViewModel> V getViewModel(Class<V> modelType){
        if(mBaseAdapter.attachedFragment == null || mBaseAdapter.attachedFragment.isDetached()) {
            return  DiffModelProvider.getModel(mContext,modelType);
        }
        return DiffModelProvider.getModel(getAttachedFragment(),modelType);
    }

    @Nullable
    public Fragment getAttachedFragment() {
        return mBaseAdapter.attachedFragment;
    }

}
