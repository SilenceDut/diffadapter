package com.silencedut.diffadapter.holder;

import android.view.View;

import com.silencedut.diffadapter.DiffAdapter;
import com.silencedut.diffadapter.data.BaseMutableData;

/**
 * @author SilenceDut
 * @date 2018/9/6
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
