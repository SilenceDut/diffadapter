package com.silencedut.diffadapterdemo

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.silencedut.diffadapter.BaseDiffViewHolder
import com.silencedut.diffadapter.DiffAdapter

/**
 * @author SilenceDut
 * @date 2018/12/5
 */
class ImageHolder( itemView: View, recyclerAdapter: DiffAdapter): BaseDiffViewHolder<ImageData>( itemView,  recyclerAdapter){
    private var imageView :ImageView?=null

    companion object {
        const val TAG = "ImageHolder"
    }

    override fun getItemViewId(): Int {
        return ImageData.VIEW_ID
    }

    init {
        imageView = itemView.findViewById(R.id.hd_iv)
    }

    override fun updateItem(data: ImageData, position: Int) {
        Log.d(TAG,"updateItem")

        imageView?.setImageResource(data.sourceId)
    }

}