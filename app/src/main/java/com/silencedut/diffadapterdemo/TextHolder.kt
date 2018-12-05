package com.silencedut.diffadapterdemo

import android.util.Log
import android.view.View
import android.widget.TextView
import com.silencedut.diffadapter.BaseDiffViewHolder
import com.silencedut.diffadapter.DiffAdapter

/**
 * @author SilenceDut
 * @date 2018/12/5
 */
class TextHolder(itemViewRoot: View, recyclerAdapter: DiffAdapter): BaseDiffViewHolder<TextData>( itemViewRoot,  recyclerAdapter){
    private var textView : TextView?=null

    companion object {
        const val TAG ="TextHolder"
    }

    override fun getItemViewId(): Int {
        return TextData.VIEW_ID
    }

    init {
        textView = itemViewRoot.findViewById(R.id.hd_tv)
    }

    override fun updateItem(data: TextData, position: Int) {
        Log.d(TAG,"updateItem")
        textView?.text = data.content
    }

}