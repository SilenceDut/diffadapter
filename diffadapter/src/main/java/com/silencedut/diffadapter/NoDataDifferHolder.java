package com.silencedut.diffadapter;

import android.view.View;

/**
 * Created by SilenceDut on 16/10/19.
 */

public class NoDataDifferHolder extends BaseDiffViewHolder {
    public NoDataDifferHolder(View itemView, DiffAdapter baseRecyclerAdapter) {
        super(itemView, baseRecyclerAdapter);
    }

    @Override
    public void updateItem(BaseImmutableData data, int position) {

    }

    @Override
    public int getItemViewId() {
        return 0;
    }


}
