package com.silencedut.diffadapter.rvhelper

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator

/**
 * @author SilenceDut
 * @date 2018/12/26
 */
class RvHelper {
    companion object {


        /**
         * 最后一个Item是否可见
         */
        fun isAlignToBottom(recyclerView: RecyclerView): Boolean {
            return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
        }

        fun scrollToBottom(recyclerView: RecyclerView,position:Int) {
            recyclerView.layoutManager?.let {
                if(it is LinearLayoutManager) {
                    it.scrollToPositionWithOffset(position, 0)
                }
            }
        }

        /**
         * 关闭默认局部刷新动画，对性能要求高的地方试用，比如一些公屏等刷新频繁的
         */
        fun closeDefaultAnimator(rv: RecyclerView) {
            rv.itemAnimator?.let {
                it.addDuration = 0
                it.changeDuration = 0
                it.moveDuration = 0
                it.removeDuration = 0
                if(it is SimpleItemAnimator) {
                    it.supportsChangeAnimations = false
                }
            }
        }
    }
}