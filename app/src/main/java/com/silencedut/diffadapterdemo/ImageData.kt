package com.silencedut.diffadapterdemo

import com.silencedut.diffadapter.data.BaseImmutableData

/**
 * @author SilenceDut
 * @date 2018/12/5
 */
class ImageData(var uid: Long, var sourceId: Int, var name: String) : BaseImmutableData<ImageData>() {
    override fun uniqueFeature(): Any {
        return uid
    }

    companion object {
         const val VIEW_ID = R.layout.holder_image
    }

    override fun getItemViewId(): Int {
        return VIEW_ID
    }

    override fun areUISame(newData: ImageData): Boolean {
        return this.sourceId == newData.sourceId
    }



}