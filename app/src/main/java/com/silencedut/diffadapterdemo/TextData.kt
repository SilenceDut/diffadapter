package com.silencedut.diffadapterdemo

import android.graphics.Color
import android.os.Bundle
import com.silencedut.diffadapter.data.BaseMutableData

/**
 * @author SilenceDut
 * @date 2018/12/5
 */
class TextData(var uid:Long,var content:String,var backgroundColor:Int = Color.TRANSPARENT) : BaseMutableData<TextData>() {

    companion object {
        const val VIEW_ID = R.layout.holder_text
    }


    override fun getItemViewId(): Int {
        return VIEW_ID
    }


    override fun areUISame(newData: TextData): Boolean {
        return this.content == newData.content && this.backgroundColor == newData.backgroundColor
    }


    override fun appendDiffPayload(newData: TextData, diffPayloadBundle: Bundle) {
        super.appendDiffPayload(newData, diffPayloadBundle)
        if(this.content != newData.content) {
            diffPayloadBundle.putString("content",content)
        }
        if(this.backgroundColor != newData.backgroundColor) {
            diffPayloadBundle.putInt("backgroundColor",backgroundColor)
        }
    }

    override fun uniqueItemFeature(): Any {
       return this.uid
    }

}