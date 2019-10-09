package com.silencedut.diffadapterdemo

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


import android.util.Log

/**
 * Author: rigarsu
 * Date: 2018/12/26
 * Description:
 */
class LinearLayoutManagerWrapper(context: Context): LinearLayoutManager(context) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {
            Log.e("onLayoutChildren error", e.toString())
        }
    }
}