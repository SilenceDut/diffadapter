package com.silencedut.diffadapter;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

/**
 *
 * @author SilenceDut
 * @date 2018/9/6
 *
 * implement BaseViewInit interface is convenient to generate views on ViewHolder
 *
 * ViewHolder的子类不要用Notification 接收通知，用LiveData
 */

public abstract class BaseDiffViewHolder<T extends BaseMutableData> extends RecyclerView.ViewHolder implements IProvideItemId {

    protected DiffAdapter mBaseAdapter;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

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
        setUIContext(itemView.getContext());
    }


    public abstract void updateItem(@NonNull T data, int position);

    protected void onHolderClicked(int position, T data) {
        if (mBaseAdapter.mHolderClickListener != null) {
            mBaseAdapter.mHolderClickListener.onHolderClicked(position, data);
        }
    }


    /**
     * 获取当前处理逻辑的ViewModel,如果supportFragment的状态已经错误，将ViewModel attach到跟activity
     */
    public <V extends ViewModel> V getViewModel(Class<V> modelType){
        if(mBaseAdapter.attachedFragment == null || mBaseAdapter.attachedFragment.isDetached()) {
            return  ModelProvider.getModel(itemView.getContext(),modelType);
        }
        return ModelProvider.getModel(getAttachedFragment(),modelType);
    }

    @Nullable
    public Fragment getAttachedFragment() {
        return mBaseAdapter.attachedFragment;
    }

}
