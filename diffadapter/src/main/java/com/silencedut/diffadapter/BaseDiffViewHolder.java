package com.silencedut.diffadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by SilenceDut on 16/10/19.
 */

/**
 * implement BaseViewInit interface is convenient to generate views on ViewHolder

 */

public abstract class BaseDiffViewHolder<T extends BaseImmutableData> extends RecyclerView.ViewHolder implements IProvideItemId {

    protected DiffAdapter mBaseAdapter;
    private Context mContext;


    protected Context getContext() {
        return mContext;
    }


    public BaseDiffViewHolder(View itemView, DiffAdapter baseRecyclerAdapter) {
        super(itemView);
        mBaseAdapter = baseRecyclerAdapter;
        this.mContext = itemView.getContext();
    }


    public abstract void updateItem(T data, int position);

    protected void onHolderClicked(int position, T data) {
        if (mBaseAdapter.mHolderClickListener != null) {
            mBaseAdapter.mHolderClickListener.onHolderClicked(position, data);
        }
    }

}
