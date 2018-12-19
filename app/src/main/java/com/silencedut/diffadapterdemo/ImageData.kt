package com.silencedut.diffadapterdemo

import com.silencedut.diffadapter.data.BaseMutableData

/**
 * @author SilenceDut
 * @date 2018/12/5
 */
class ImageData(var uid: Long, var sourceId: Int, var name: String) : BaseMutableData<ImageData>() {

    companion object {
         const val VIEW_ID = R.layout.holder_image
    }

    override fun getItemViewId(): Int {
        return VIEW_ID
    }

    override fun matchChangeFeature(): Any {
        return uid
    }

    override fun areUISame(newData: ImageData): Boolean {
        return this.sourceId == newData.sourceId
    }

    override fun copyData(): ImageData {
        return ImageData(uid,sourceId,name)
    }

    override fun uniqueItemFeature(): Any {
        return this.name
    }


}