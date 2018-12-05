package com.silencedut.diffadapterdemo

import com.silencedut.diffadapter.BaseImmutableData

/**
 * @author SilenceDut
 * @date 2018/12/5
 */
class TextData(var id:Int,var content:String) : BaseImmutableData<TextData>() {

    companion object {
        const val VIEW_ID = R.layout.holder_text
    }


    override fun getItemViewId(): Int {
        return VIEW_ID
    }

    override fun areSameItem(newData: TextData?): Boolean {
        return this.id == newData?.id
    }

    override fun areUISame(newData: TextData?): Boolean {
        return this.content === newData?.content

    }

}