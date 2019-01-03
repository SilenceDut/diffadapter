package com.silencedut.diffadapterdemo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.silencedut.diffadapter.DiffAdapter
import com.silencedut.diffadapter.holder.BaseDiffViewHolder

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
        Log.d(TAG,"updatePartWithPayload")
        textView?.text = data.content
        textView?.setBackgroundColor(data.backgroundColor)
    }

    override fun updatePartWithPayload(data: TextData, payload: Bundle, position: Int) {

        Log.d(TAG,"position :"+position+"updatePartWithPayload payload content :"+payload.getString("content")+",backgroundColor : "+payload.getString("backgroundColor"))
        if(payload.getString("content")!=null) {
            textView?.text = payload.getString("content")
        }
        if(payload.getString("backgroundColor")!=null) {
            textView?.setBackgroundColor(Color.parseColor(payload.getString("backgroundColor")))
        }
    }

}