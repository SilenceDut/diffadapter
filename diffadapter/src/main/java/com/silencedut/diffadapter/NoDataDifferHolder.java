package com.silencedut.diffadapter;

import android.view.View;

/**
 * Created by liushuai on  2018/9/6
 */

public class NoDataDifferHolder extends BaseDiffViewHolder {
    public NoDataDifferHolder(View itemView, DiffAdapter baseRecyclerAdapter) {
        super(itemView, baseRecyclerAdapter);
    }

    @Override
    public void updateItem(BaseMutableData data, int position) {

    }

    @Override
    public int getItemViewId() {
        return 0;
    }


}
