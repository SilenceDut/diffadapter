package com.silencedut.diffadapter.holder;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import com.silencedut.diffadapter.DiffAdapter;
import com.silencedut.diffadapter.IProvideItemId;
import com.silencedut.diffadapter.data.BaseMutableData;
import com.silencedut.diffadapter.utils.DiffModelProvider;

import java.util.Set;

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

    private DiffAdapter mBaseAdapter;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private T mData;

    protected LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    protected Context getContext() {
        return mContext;
    }

    protected DiffAdapter getAdapter() {
        return mBaseAdapter;
    }


    public BaseDiffViewHolder(View itemView, DiffAdapter baseRecyclerAdapter) {
        super(itemView);
        this.mBaseAdapter = baseRecyclerAdapter;
        this.mContext = baseRecyclerAdapter.mContext;
        this.mLayoutInflater = LayoutInflater.from(this.mContext);
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
     * @param newData
     * @param position
     */
    public void updatePartWithPayload(T newData, @NonNull Set<String> payloadKeys, int position){
        this.mData = newData;
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
