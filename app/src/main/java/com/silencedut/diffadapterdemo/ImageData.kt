package com.silencedut.diffadapterdemo

import com.silencedut.diffadapter.BaseImmutableData

/**
 * @author SilenceDut
 * @date 2018/12/5
 */
class ImageData(var name:String,var sourceId:Int) : BaseImmutableData<ImageData>() {


    companion object {
         const val VIEW_ID = R.layout.holder_image
    }

    override fun getItemViewId(): Int {
        return VIEW_ID
    }

    override fun areSameItem(newData: ImageData?): Boolean {
        return this.name === newData?.name
    }

    override fun areUISame(newData: ImageData?): Boolean {
        return this.sourceId == newData?.sourceId
    }



}